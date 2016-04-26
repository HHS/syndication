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
class RestSubscriptionController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def contentExtractionService
    def queueService
    def loggingService
    def subscriptionService
    def rabbitDelayJobScheduler = new RabbitDelayJobScheduler()

    static allowedMethods = [save: "POST", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RestSubscription.list(params), model: [instanceCount: RestSubscription.count()], view: 'index'
    }

    def show(RestSubscription restSubscription) {
        if (!restSubscription) {
            return notFound()
        }
        respond restSubscription, view: 'show'
    }

    def create() {
        respond new RestSubscription(params), view: 'create'
    }

    @Transactional
    def save(RestSubscription restSubscription) {
        def sourceUrl = restSubscription.sourceUrl
        if (!sourceUrl) {
            restSubscription.validate()
            respond restSubscription.errors, view: 'create'
            return
        }

        //noinspection GroovyUnusedAssignment
        def mediaItem = null

        try {
            mediaItem = contentExtractionService.getMediaItemBySourceUrl(sourceUrl)
            if (!mediaItem) {
                flash.errors = [message(code: 'restSubscription.sourceUrl.not.syndicated', args: [sourceUrl])]
                redirect action: "index", method: "GET"
                return
            }
        } catch (e) {
            flash.errors = [loggingService.logError("An error occurred when trying to get the media item associated with sourceUrl '${sourceUrl}'", e)]
            redirect action: "index", method: "GET"
            return
        }

        def existingSubscription = Subscription.findByMediaId(mediaItem.id as String)

        if (!existingSubscription) {

            def subscription = new Subscription(mediaId: mediaItem.id)

            if (!subscription.validate()) {
                respond subscription.errors, view: 'create'
                return
            }

            subscription.save(flush: true)
            restSubscription.subscription = subscription

        } else {

            def restSubscriber = restSubscription.restSubscriber
            def existingRestSubscription = RestSubscription.findBySubscriptionAndRestSubscriber(existingSubscription, restSubscriber)

            if (existingRestSubscription) {
                flash.errors = [message(code: 'restSubscription.already.subscribed.message', args: [restSubscriber.deliveryEndpoint, mediaItem.id])]
                redirect action: "index", method: "GET"
                return
            }

            restSubscription.subscription = existingSubscription
        }

        restSubscription.title = mediaItem.name
        restSubscription.isPending = true
        restSubscription.save(flush: true)

        if (restSubscription.hasErrors()) {
            respond restSubscription.errors, view: 'create'
        } else {
            rabbitDelayJobScheduler.schedule(MessageType.IMPORT, restSubscription)
            showInstance(restSubscription, 'default.created.message')
        }
    }

    def deliver(RestSubscription restSubscription) {
        try {
            if(restSubscription.deliveryFailureLogId && restSubscription.isPending){
                flash.message = "Please wait to redeliver until the subscription is no longer pending."
            } else {
                flash.message = "The Rest Subscription id:${restSubscription.id} has been delivered."
                queueService.sendToRestUpdateQueue(MessageType.UPDATE, restSubscription.id)
            }
        } catch (ignore) {
        }
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def delete(RestSubscription restSubscription) {

        if (restSubscription == null) {
            return notFound()
        }

        def restSubscriptionId = restSubscription.id

        try {
            rabbitDelayJobScheduler.schedule(MessageType.DELETE, restSubscription)
            flash.message = message(code: 'restSubscription.delete.queued.message', args: [restSubscriptionId])
        } catch (e) {
            flash.errors = [loggingService.logError("Error occurred when queuing a delete message for restSubscription '${restSubscriptionId}'", e)]
            flash.contentTypes = flash.contentTypes
        }

        redirect action: "index", method: "GET"
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'restSubscription.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    def showInstance(RestSubscription restSubscription, code) {
        flash.message = message(code: code, args: [message(code: 'restSubscription.label'), restSubscription.subscription.mediaUri])
        redirect(action: "show", id: restSubscription.id)
    }
}
