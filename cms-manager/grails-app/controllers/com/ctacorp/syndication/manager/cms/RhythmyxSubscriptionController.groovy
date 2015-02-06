/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.commons.mq.MessageType
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_RHYTHMYX_USER', 'ROLE_ADMIN'])
@Transactional(readOnly = true)
class RhythmyxSubscriptionController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def rhythmyxIngestionService
    def loggingService
    def queueService
    def subscriptionService
    def rhythmyxSubscriptionTransitionService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def home() {

        if (SpringSecurityUtils.ifNotGranted('ROLE_RHYTHMYX_USER')) {
            redirect controller: 'subscriber', action: 'index'
            return
        }

        redirect controller: 'rhythmyxSubscription', action: 'index'
    }

    def index(Integer max) {

        params.max = Math.min(max ?: 10, 100)

        def rhythmyxSubscriptions = RhythmyxSubscription.list(params)
        if(params.sort == 'subscription.mediaId') {

            def pendingOrFailedSubscriptions = RhythmyxSubscription.findAllBySubscriptionIsNull()

            if(params.order == 'asc') {
                rhythmyxSubscriptions.addAll(0, pendingOrFailedSubscriptions)
            } else {
                rhythmyxSubscriptions.addAll(pendingOrFailedSubscriptions)
            }
        }

        respond rhythmyxSubscriptions, model: [instanceCount: RhythmyxSubscription.count()], view: 'index'
    }

    def show(RhythmyxSubscription rhythmyxSubscription) {

        if (!rhythmyxSubscription) {
            return notFound()
        }

        respond rhythmyxSubscription, view: 'show'
    }

    def create() {

        def targetFolder = params.sys_folderid
        def instanceName = params.instance as String

        def rhythmyxSubscriber = RhythmyxSubscriber.findByInstanceName(instanceName)

        if (rhythmyxSubscriber) {
            try {
                flash.contentTypes = rhythmyxIngestionService.getContentTypes(rhythmyxSubscriber)
            } catch (e) {
                flash.errors = [loggingService.logError("Error occurred when fetching the content types for rhythmyxSubscriber=${rhythmyxSubscriber.instanceName}", e)]
                redirect action: "index", method: "GET"
                return
            }
        }

        def rhythmyxWorkflow = rhythmyxSubscriber?.rhythmyxWorkflow ?: new RhythmyxWorkflow()

        respond new RhythmyxSubscription(targetFolder: targetFolder, rhythmyxSubscriber: rhythmyxSubscriber, rhythmyxWorkflow: rhythmyxWorkflow, subscription: new Subscription()), view: 'create'
    }

    @Transactional
    def save(RhythmyxSubscription rhythmyxSubscription) {

        if (!rhythmyxSubscription) {
            return notFound()
        }

        def rhythmyxSubscriber = rhythmyxSubscription.rhythmyxSubscriber

        if(!rhythmyxSubscriber) {
            rhythmyxSubscription.validate()
            respond rhythmyxSubscription.errors, view: 'create'
            return
        }

        def useAsDefaultWorkflow = rhythmyxSubscription.useAsDefaultWorkflow

        rhythmyxSubscription.save(flush: true)

        if (params.useAsDefaultWorkflow) {
            rhythmyxSubscriber.rhythmyxWorkflow = rhythmyxSubscription.rhythmyxWorkflow
            rhythmyxSubscriber.save(flush: true)
        }

        if (rhythmyxSubscription.hasErrors()) {
            respond rhythmyxSubscription.errors, view: 'create'
        } else {
            try {
                queueService.sendToRhythmyxUpdateQueue(MessageType.IMPORT, rhythmyxSubscription.id)
            } catch (e) {
                flash.contentTypes = flash.contentTypes
                flash.errors = [loggingService.logError("Error occurred when saving the rhythmyxSubscription with sourceUrl=${rhythmyxSubscription.sourceUrl}", e)]
                respond rhythmyxSubscription, view: 'create'
                return
            }
            showInstance(rhythmyxSubscription, 'default.created.message')
        }
    }

    def edit(RhythmyxSubscription rhythmyxSubscription) {

        if (!rhythmyxSubscription) {
            return notFound()
        }

        respond rhythmyxSubscription, view: 'edit'
    }

    @Transactional
    def update(RhythmyxSubscription rhythmyxSubscription) {

        if (rhythmyxSubscription == null) {
            return notFound()
        }

        rhythmyxSubscription.save flush: true

        if (rhythmyxSubscription.hasErrors()) {
            respond rhythmyxSubscription.errors, view: 'edit'
            return
        }

        showInstance(rhythmyxSubscription, 'default.updated.message')
    }

    @Transactional
    def delete(RhythmyxSubscription rhythmyxSubscription) {

        if (!rhythmyxSubscription) {
            notFound()
            return
        }

        rhythmyxSubscriptionTransitionService.doDeleteTransitions([rhythmyxSubscription])

        def sourceUrl = rhythmyxSubscription.sourceUrl

        subscriptionService.deleteChildSubscription(rhythmyxSubscription)

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'rhythmyxSubscription.label'), sourceUrl])
        redirect(action: "index", method: "GET")
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'rhythmyxSubscription.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    def showInstance(RhythmyxSubscription rhythmyxSubscription, code) {
        flash.message = message(code: code, args: [message(code: 'rhythmyxSubscription.label'), rhythmyxSubscription.sourceUrl])
        redirect(action: "show", id: rhythmyxSubscription.id)
    }

    def getDefaultWorkflow(){
        def rhythmyxWorkflow = RhythmyxSubscriber.findById(params.instance as Long)
        render template:'defaultWorkflow', model:[instance: rhythmyxWorkflow]
    }
}
