/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.util.Holders

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class SubscriberController {

    static entityName = Holders.config.apiKey.entities.cmsManager as String

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def subscriberMailService
    def loggingService
    def subscriptionService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Subscriber.list(params), model: [instanceCount: Subscriber.count()], view: 'index'
    }

    def show(Subscriber subscriber) {

        if (!subscriber) {
            return notFound()
        }
        respond subscriber, view: 'show'
    }

    def create() {
        respond new Subscriber(params), view: 'create'
    }

    def edit(Subscriber subscriber) {
        subscriber.sendKeyAgreement = false
        respond subscriber, view: 'edit'
    }

    @Transactional
    def update(Subscriber subscriber) {

        flash.clear()

        if (!subscriber) {
            return notFound()
        }

        if (!subscriber.validate()) {
            respond subscriber.errors, view: 'edit'
            return
        }

        if (!subscriber.keyAgreement) {
            KeyAgreement keyAgreement = newKeyAgreement(subscriber)
            subscriber.keyAgreement = keyAgreement
        }

        makeOnlyPrivilegedSubscriber(subscriber)

        subscriber.save(flush: true)

        if (subscriber.sendKeyAgreement) {
            try {
                subscriberMailService.sendSubscriberKeyAgreement(subscriber)
            } catch (Exception e) {
                return emailError(subscriber, 'keyAgreement.email.send.error', e, false)
            }
        }

        showInstance(subscriber, 'default.updated.message')
    }

    @Transactional
    def delete(Subscriber subscriber) {

        flash.clear()

        if (!subscriber) {
            return notFound()
        }

        RhythmyxSubscriber.findAllBySubscriber(subscriber).each { rhythmyxSubscriber ->
            RhythmyxSubscription.findAllByRhythmyxSubscriber(rhythmyxSubscriber).each { rhythmyxSubscription ->
                subscriptionService.deleteChildSubscription(rhythmyxSubscription)
            }
            rhythmyxSubscriber.delete(flush: true)
        }

        EmailSubscriber.findAllBySubscriber(subscriber).each { emailSubscriber ->
            EmailSubscription.findAllByEmailSubscriber(emailSubscriber).each { emailSubscription ->
                subscriptionService.deleteChildSubscription(emailSubscription)
            }
            emailSubscriber.delete(flush: true)
        }

        RestSubscriber.findAllBySubscriber(subscriber).each { restSubscriber ->
            RestSubscription.findAllByRestSubscriber(restSubscriber).each { restSubscription ->
                subscriptionService.deleteChildSubscription(restSubscription)
            }
            restSubscriber.delete(flush: true)
        }

        def name = subscriber.name

        def keyAgreement = subscriber.keyAgreement
        subscriber.delete(flush: true)
        keyAgreement?.delete(flush: true)

        try {
            subscriberMailService.sendSubscriberDelete(subscriber)
        } catch (e) {
            return emailError(subscriber, 'subscriber.delete.email.send.error', e, true)
        }

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'subscriber.label'), name])
        redirect(action: "index", method: "GET")
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'subscriber.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def save() {

        flash.clear()

        def subscriber = new Subscriber(params)

        if (!subscriber.validate()) {
            respond subscriber.errors, view: 'create'
            return
        }

        KeyAgreement keyAgreement = newKeyAgreement(subscriber)
        subscriber.keyAgreement = keyAgreement

        makeOnlyPrivilegedSubscriber(subscriber)

        subscriber.save(flush: true)

        if (subscriber.sendKeyAgreement) {
            try {
                subscriberMailService.sendSubscriberKeyAgreement(subscriber)
            } catch (e) {
                return emailError(subscriber, "keyAgreement.email.send.error", e, false)
            }
        }

        showInstance(subscriber, 'default.created.message')
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void makeOnlyPrivilegedSubscriber(Subscriber subscriber) {
        def isPrivileged = subscriber.isPrivileged
        if (isPrivileged) {
            Subscriber.list().each {
                it.isPrivileged = false
                it.save(flush: true)
            }
        }
        subscriber.setIsPrivileged(isPrivileged)
    }

    private static KeyAgreement newKeyAgreement(Subscriber subscriber) {
        def agreement = ApiKeyUtils.newKeyAgreement(entityName, subscriber.name)
        def keyAgreement = new KeyAgreement(agreement).save(failOnError: true)
        keyAgreement
    }

    def emailError(Subscriber subscriber, messageKey, e, boolean isDelete) {
        def errorMessage = message(code: messageKey)
        //noinspection GroovyAssignabilityCheck
        flash.errors = [loggingService.logError(errorMessage, e), errorMessage]
        if(isDelete) {
            redirect(action: "index", method: "GET")
        } else {
            redirect(action: "show", id: subscriber.id, method: "GET")
        }
    }

    def showInstance(Subscriber subscriber, code) {
        flash.message = message(code: code, args: [message(code: 'subscriber.label'), subscriber.name])
        redirect(action: "show", id: subscriber.id)
    }
}
