
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
class RhythmyxSubscriberController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond RhythmyxSubscriber.list(params), model: [instanceCount: RhythmyxSubscriber.count()], view: 'index'
    }

    def show(RhythmyxSubscriber rhythmyxSubscriber) {
        if (!rhythmyxSubscriber) {
            return notFound()
        }
        respond rhythmyxSubscriber, view: 'show'
    }

    def create() {
        respond new RhythmyxSubscriber(params), view: 'create'
    }

    @Transactional
    def save(RhythmyxSubscriber rhythmyxSubscriber) {

        if (!rhythmyxSubscriber) {
            return notFound()
        }

        def subscriberId = params.subscriber as Long

        def subscriber = Subscriber.findById(subscriberId)
        rhythmyxSubscriber.subscriber = subscriber

        rhythmyxSubscriber.save(flush: true)
        if (rhythmyxSubscriber.hasErrors()) {
            respond rhythmyxSubscriber.errors, view: 'create'
            return
        }

        showInstance(rhythmyxSubscriber, 'default.created.message')
    }

    def edit(RhythmyxSubscriber rhythmyxSubscriber) {
        respond rhythmyxSubscriber, view: 'edit'
    }

    @Transactional
    def update(RhythmyxSubscriber rhythmyxSubscriber) {

        if (!rhythmyxSubscriber) {
            return notFound()
        }

        rhythmyxSubscriber.save(flush: true)

        if (rhythmyxSubscriber.hasErrors()) {
            respond rhythmyxSubscriber.errors, view: 'edit'
            return
        }

        showInstance(rhythmyxSubscriber, 'default.updated.message')
    }

    @Transactional
    def delete(RhythmyxSubscriber rhythmyxSubscriber) {

        if (!rhythmyxSubscriber) {
            return notFound()
        }

        def instanceName = rhythmyxSubscriber.instanceName

        RhythmyxSubscription.findAllByRhythmyxSubscriber(rhythmyxSubscriber).each { rhythmyxSubscription ->
            rhythmyxSubscription.delete(flush:true)
        }
        rhythmyxSubscriber.delete(flush: true)

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'rhythmyxSubscriber.label'), instanceName])
        redirect(action: "index", method: "GET")
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'rhythmyxSubscriber.label'), params.id])]
        redirect action: "index", method: "GET"
    }

    def showInstance(rhythmyxSubscriber, code) {
        flash.message = message(code: code, args: [message(code: 'rhythmyxSubscriber.label'), rhythmyxSubscriber.instanceName])
        redirect(action: "show", id: rhythmyxSubscriber.id)
    }

    def getEmailSubscribersWithSameSubscriber(){
        def emailSubscribers = RhythmyxSubscriber.get(params.rhythmyxSubscriberId as Long).getEmailSubscribersWithSameSubscriber()
        render template:"emailSubscribers", model:[emailSubscribers:emailSubscribers]
        
    }
}
