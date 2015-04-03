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
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitDelayJobScheduler
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class EmailSubscriptionController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def contentExtractionService
    def loggingService
    def emailSubscriptionMailService
    def subscriptionService
    def queueService
    def rabbitDelayJobScheduler = new RabbitDelayJobScheduler()

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EmailSubscription.list(params), model: [instanceCount: EmailSubscription.count()], view: 'index'
    }

    def show(EmailSubscription emailSubscription) {
        if (!emailSubscription) {
            return notFound()
        }
        respond emailSubscription, view: 'show'
    }

    def create() {
        respond new EmailSubscription(params), view: 'create'
    }

    @Transactional
    def save(EmailSubscription emailSubscription) {

        if (!emailSubscription) {
            notFound()
            return
        }

        def sourceUrl = emailSubscription.sourceUrl
        if (!sourceUrl) {
            emailSubscription.validate()
            respond emailSubscription.errors, view: 'create'
            return
        }

        //noinspection GroovyUnusedAssignment

        def mediaItem = null

        try {
            mediaItem = contentExtractionService.getMediaItemBySourceUrl(sourceUrl)
            if (!mediaItem) {
                flash.errors = [message(code: 'emailSubscription.sourceUrl.not.syndicated', args: [sourceUrl])]
                redirect action: "index", method: "GET"
                return
            }
        } catch (e) {
            flash.errors = [loggingService.logError("An error occurred when trying to get the media item associated with sourceUrl '${sourceUrl}'", e)]
            redirect action: "index", method: "GET"
            return
        }

        def existingSubscription = Subscription.findByMediaId(mediaItem.id as String)
        def emailSubscriber = emailSubscription.emailSubscriber

        if (!existingSubscription) {

            def subscription = new Subscription(mediaId: mediaItem.id)

            if (!subscription.validate()) {
                respond subscription.errors, view: 'create'
                return
            }

            subscription.save(flush: true)
            emailSubscription.subscription = subscription

        } else {

            def existingEmailSubscription = EmailSubscription.findBySubscriptionAndEmailSubscriber(existingSubscription, emailSubscriber)

            if (existingEmailSubscription) {
                flash.errors = [message(code: 'emailSubscription.already.subscribed.message', args: [emailSubscriber.email, mediaItem.id])]
                redirect action: "index", method: "GET"
                return
            }

            emailSubscription.subscription = existingSubscription
        }

        emailSubscription.title = mediaItem.name
        emailSubscription.isPending = true
        emailSubscription.save(flush: true)

        if (emailSubscription.hasErrors()) {
            respond emailSubscription.errors, view: 'create'
        } else {
            rabbitDelayJobScheduler.schedule(MessageType.IMPORT, emailSubscription)
            showInstance(emailSubscription, 'default.created.message')
        }
    }

    def deliver(EmailSubscription emailSubscription) {
        try {
            if(!emailSubscription.deliveryFailureLogId && emailSubscription.isPending){
                flash.message = "Please wait to redeliver until the subscription is no longer pending."
            } else {
                flash.message = "The Email Subscription id:${emailSubscription.id} has been delivered."
                queueService.sendToEmailUpdateQueue(MessageType.UPDATE, emailSubscription.id)
            }
        } catch (ignore) {
        }
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def delete(EmailSubscription emailSubscription) {

        if (emailSubscription == null) {
            notFound()
            return
        }

        def emailSubscriptionId = emailSubscription.id

        try {
            emailSubscriptionMailService.sendSubscriptionDelete(emailSubscription)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'emailSubscription.label'), emailSubscriptionId])
        } catch (e) {
            flash.errors = [loggingService.logError("Error occurred when queuing a delete message for emailSubscription '${emailSubscriptionId}'", e)]
        }

        subscriptionService.deleteChildSubscription(emailSubscription)
        redirect action: "index", method: "GET"
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'emailSubscription.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    def showInstance(EmailSubscription emailSubscription, code) {
        flash.message = message(code: code, args: [message(code: 'emailSubscription.label'), emailSubscription.subscription.mediaUri])
        redirect(action: "show", id: emailSubscription.id)
    }
}
