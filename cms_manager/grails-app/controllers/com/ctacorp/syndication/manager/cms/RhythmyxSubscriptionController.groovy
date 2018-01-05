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
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_RHYTHMYX_USER', 'ROLE_ADMIN'])
@Transactional(readOnly = true)
class RhythmyxSubscriptionController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def rhythmyxIngestionService
    def contentExtractionService
    def loggingService
    def queueService
    def subscriptionService
    def rhythmyxSubscriptionTransitionService
    def userSubscriberService
    def rabbitDelayJobScheduler = new RabbitDelayJobScheduler()

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

        def rhythmyxSubscriptions = userSubscriberService.listRhythmyxSubscriptions(params)
        def count = userSubscriberService.rhythmyxSubscriptionCount()

        respond rhythmyxSubscriptions, model: [instanceCount: count], view: 'index'
    }

    def show(RhythmyxSubscription rhythmyxSubscription) {
        if (!rhythmyxSubscription) {
            return notFound()
        }

        respond rhythmyxSubscription, view: 'show'
    }

    def create() {

        def rhythmyxSubscribers = userSubscriberService.listRhythmyxSubscribers()
        if(!rhythmyxSubscribers) {
            flash.errors = [message(code: 'rhythmyxSubscription.noAssociatedRhythmyxSubscribers')]
            redirect(action: "index", method: "GET")
            return
        }

        def targetFolder = params.sys_folderid
        def instanceName = params.instance as String

        def rhythmyxSubscriber = rhythmyxSubscribers.find { instanceName == it.instanceName}
        if (rhythmyxSubscriber) {
            try {
                flash.contentTypes = rhythmyxIngestionService.getContentTypes(rhythmyxSubscriber)
            } catch (e) {
                flash.errors = [loggingService.logError("Error occurred when fetching the content types for rhythmyxSubscriber '${rhythmyxSubscriber.instanceName}'", e)]
                redirect action: "index", method: "GET"
                return
            }
        }

        flash.rhythmyxSubscribers = rhythmyxSubscribers

        def rhythmyxWorkflow = rhythmyxSubscriber?.rhythmyxWorkflow ?: new RhythmyxWorkflow()

        respond new RhythmyxSubscription(targetFolder: targetFolder, rhythmyxSubscriber: rhythmyxSubscriber, rhythmyxWorkflow: rhythmyxWorkflow, subscription: new Subscription()), view:'create'
    }

    @Transactional
    def save(RhythmyxSubscription rhythmyxSubscription) {
        if (!rhythmyxSubscription) {
            return notFound()
        }

        def sourceUrl = rhythmyxSubscription.sourceUrl
        def rhythmyxSubscriber = rhythmyxSubscription.rhythmyxSubscriber

        if(!sourceUrl || !rhythmyxSubscriber || !rhythmyxSubscription.validate()) {
            flash.rhythmyxSubscribers = userSubscriberService.listRhythmyxSubscribers()
            respond rhythmyxSubscription.errors, view: 'create'
            return
        }

        if(!userSubscriberService.hasAccess(rhythmyxSubscriber)) {
            flash.errors = [message(code: 'rhythmyxSubscription.accessDeniedToRhythmyxSubscriber')]
            redirect(action: "index", method: "GET")
            return
        }

        //noinspection GroovyUnusedAssignment
        def mediaItem = null

        try {
            mediaItem = contentExtractionService.getMediaItemBySourceUrl(sourceUrl)
            if (!mediaItem) {
                flash.errors = [message(code: 'rhythmyxSubscription.sourceUrl.not.syndicated', args: [sourceUrl])]
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
            subscription.save(flush: true)
            rhythmyxSubscription.subscription = subscription

        } else {

            def existingRhythmyxSubscription = RhythmyxSubscription.findBySubscriptionAndRhythmyxSubscriber(existingSubscription, rhythmyxSubscriber)

            if (existingRhythmyxSubscription) {
                flash.errors = [message(code: 'rhythmyxSubscription.already.subscribed.message', args: [rhythmyxSubscriber.instanceName, mediaItem.id])]
                redirect action: "index", method: "GET"
                return
            }

            rhythmyxSubscription.subscription = existingSubscription
        }

        if (params.useAsDefaultWorkflow) {
            rhythmyxSubscriber.rhythmyxWorkflow = rhythmyxSubscription.rhythmyxWorkflow
            rhythmyxSubscriber.save(flush: true)
        }

        rhythmyxSubscription.systemTitle = mediaItem.name
        rhythmyxSubscription.save(flush: true)


        if (rhythmyxSubscription.hasErrors()) {
            respond rhythmyxSubscription.errors, view: 'create'
        } else {
            rabbitDelayJobScheduler.schedule(MessageType.IMPORT, rhythmyxSubscription)
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

        if (!rhythmyxSubscription) {
            return notFound()
        }

        rhythmyxSubscription.save flush: true

        if (rhythmyxSubscription.hasErrors()) {
            respond rhythmyxSubscription.errors, view: 'edit'
            return
        }

        showInstance(rhythmyxSubscription, 'default.updated.message')
    }

    def deliver(RhythmyxSubscription rhythmyxSubscription) {

        try {
            if( !rhythmyxSubscription.deliveryFailureLogId && rhythmyxSubscription.contentId){
            flash.message = "Please wait to redeliver until the subscription is no longer pending."
        }
            queueService.sendToRhythmyxUpdateQueue(MessageType.UPDATE, rhythmyxSubscription.id)
        } catch (ignore) {
        }
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def delete(RhythmyxSubscription rhythmyxSubscription) {

        if (!rhythmyxSubscription) {
            return notFound()
        }

        def rhythmyxSubscriber = rhythmyxSubscription.rhythmyxSubscriber
        if(!userSubscriberService.hasAccess(rhythmyxSubscriber)) {
            flash.errors = [message(code: 'rhythmyxSubscription.accessDeniedToRhythmyxSubscriber')]
            redirect(action: "index", method: "GET")
            return
        }

        def rhythmyxSubscriptionId = rhythmyxSubscription.id

        try {
            rabbitDelayJobScheduler.schedule(MessageType.DELETE, rhythmyxSubscription)
            flash.message = message(code: 'rhythmyxSubscription.delete.queued.message', args: [rhythmyxSubscriptionId])
        } catch (e) {
            flash.errors = [loggingService.logError("Error occurred when queuing a delete message for rhythmyxSubscription '${rhythmyxSubscriptionId}'", e)]
        }

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
