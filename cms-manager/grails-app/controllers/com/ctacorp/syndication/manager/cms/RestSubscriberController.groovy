/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class RestSubscriberController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RestSubscriber.list(params), model: [instanceCount: RestSubscriber.count()], view: 'index'
    }

    def show(RestSubscriber restSubscriber) {
        if (!restSubscriber) {
            return notFound()
        }
        respond restSubscriber, view: 'show'
    }

    def create() {
        respond new RestSubscriber(params), view: 'create'
    }

    def edit(RestSubscriber restSubscriber) {
        if (!restSubscriber) {
            return notFound()
        }
        respond restSubscriber, view: 'edit'
    }

    @Transactional
    def update(RestSubscriber restSubscriber) {

        if (!restSubscriber) {
            return notFound()
        }

        restSubscriber.save(flush: true)

        if (restSubscriber.hasErrors()) {
            respond restSubscriber.errors, view: 'edit'
            return
        }

        showInstance(restSubscriber, 'default.updated.message')
    }

    @Transactional
    def save(RestSubscriber restSubscriber) {

        if (!restSubscriber) {
            return notFound()
        }

        def subscriberId = params.subscriber?.id as Long
        def subscriber = Subscriber.findById(subscriberId)

        if(subscriber) {
            def existingRestSubscriber = RestSubscriber.findBySubscriberAndDeliveryEndpoint(subscriber, restSubscriber.deliveryEndpoint)
            if(existingRestSubscriber) {
                flash.errors = [message(code: 'default.exists.message', args: [message(code: 'restSubscriber.label'), existingRestSubscriber.subscriber.email])]
                redirect(action: "index", method: "GET")
                return
            }
        }

        restSubscriber.subscriber = subscriber

        restSubscriber.save(flush: true)
        if (restSubscriber.hasErrors()) {
            respond restSubscriber.errors, view: 'create'
            return
        }

        showInstance(restSubscriber, 'default.created.message')
    }

    @Transactional
    def delete(RestSubscriber restSubscriber) {

        if (!restSubscriber) {
            return notFound()
        }

        def subscriptions = RestSubscription.findByRestSubscriber(restSubscriber)
        subscriptions.each {
            it.delete(flush:true)
        }

        restSubscriber.delete(flush: true)

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'restSubscriber.label'), restSubscriber.subscriber.name])
        redirect(action: "index", method: "GET")
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'restSubscriber.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    def showInstance(restSubscriber, code) {
        flash.message = message(code: code, args: [message(code: 'restSubscriber.label'), restSubscriber.subscriber.name])
        redirect(action: "show", id: restSubscriber.id)
    }
}
