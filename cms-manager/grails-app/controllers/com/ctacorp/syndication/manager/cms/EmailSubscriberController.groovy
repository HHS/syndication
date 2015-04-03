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
class EmailSubscriberController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EmailSubscriber.list(params), model: [instanceCount: EmailSubscriber.count()], view: 'index'
    }

    def show(EmailSubscriber emailSubscriber) {
        if (!emailSubscriber) {
            return notFound()
        }
        respond emailSubscriber, view: 'show'
    }

    def create() {
        respond new EmailSubscriber(params), view: 'create'
    }

    def edit(EmailSubscriber emailSubscriber) {
        if (!emailSubscriber) {
            return notFound()
        }
        respond emailSubscriber, view: 'edit'
    }

    @Transactional
    def update(EmailSubscriber emailSubscriber) {

        if (!emailSubscriber) {
            return notFound()
        }

        emailSubscriber.save(flush: true)

        if (emailSubscriber.hasErrors()) {
            respond emailSubscriber.errors, view: 'edit'
            return
        }

        showInstance(emailSubscriber, 'emailSubscriber.updated.message')
    }

    @Transactional
    def save(EmailSubscriber emailSubscriber) {

        if (!emailSubscriber) {
            return notFound()
        }

        def subscriberId = params.subscriber?.id as Long
        def subscriber = Subscriber.findById(subscriberId)
        emailSubscriber.subscriber = subscriber

        if (emailSubscriber.hasErrors()) {
            respond emailSubscriber.errors, view: 'create'
            return
        }

        emailSubscriber.save(flush: true)

        showInstance(emailSubscriber, 'emailSubscriber.created.message')
    }

    @Transactional
    def delete(EmailSubscriber emailSubscriber) {

        if (!emailSubscriber) {
            return notFound()
        }

        EmailSubscription.findAllByEmailSubscriber(emailSubscriber).each { emailSubscription ->
            emailSubscription.delete(flush: true)
        }

        emailSubscriber.delete(flush: true)

        flash.message = message(code: 'emailSubscriber.deleted.message', args: [message(code: 'emailSubscriber.label'), emailSubscriber.email])
        redirect(action: "index", method: "GET")
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'emailSubscriber.label'), params.id])]
        redirect(action: "index", method: "GET")
    }

    def showInstance(emailSubscriber, code) {
        flash.message = message(code: code, args: [message(code: 'emailSubscriber.label'), emailSubscriber.email])
        redirect(action: "show", id: emailSubscriber.id)
    }
}
