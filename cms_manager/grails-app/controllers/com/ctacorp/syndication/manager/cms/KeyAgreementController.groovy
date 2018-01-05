
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
class KeyAgreementController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def keyAgreementService
    def loggingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.sort = params.sort ?: "entity2"
        params.max = Math.min(max ?: 10, 100)
        respond KeyAgreement.list(params), model: [instanceCount: KeyAgreement.count()], view: 'index'
    }

    def show(KeyAgreement keyAgreement) {
        if (!keyAgreement) {
            return notFound()
        }
        respond keyAgreement, view: 'show'
    }

    def create() {
        respond new KeyAgreement(params), view: 'create'
    }

    @Transactional
    def save() {

        //log.info(params)

        def entity2Name = params.entity2 as String
        def subscriber = Subscriber.findByName(entity2Name)
        if(subscriber && subscriber.keyAgreement?.entity2 == entity2Name) {
            flash.errors = [[message:message(code: 'keyAgreement.create.integrity.error.message', args: [subscriber.name])]]
            println flash.errors
            redirect action: "create", method: "GET"
            return
        }

        try {
            def keyAgreement = keyAgreementService.newKeyAgreement(entity2Name)
            if(keyAgreement.hasErrors()) {
                respond keyAgreement.errors, view: 'create'
            } else {

                subscriber.keyAgreement = keyAgreement
                subscriber.save(flush:true)

                flash.message = message(code: 'keyAgreement.created.message', args: [subscriber.name])
                redirect keyAgreement
            }
        } catch (Exception e) {
            flash.errors = [loggingService.logError("Could not generate a key agreement", e)]
            redirect action: "index", method: "GET"
        }
    }

    @Transactional
    def delete(KeyAgreement keyAgreementInstance) {

        if (!keyAgreementInstance) {
            notFound()
        } else {

            def subscriber = Subscriber.findByKeyAgreement(keyAgreementInstance)
            def subscriberName = keyAgreementInstance.entity2

            if (subscriber) {
                subscriber.keyAgreement = null
                subscriber.save(flush:true)
            }

            keyAgreementInstance.delete(flush:true)

            flash.message = message(code: 'keyAgreement.deleted.message', args: [subscriberName])
            redirect action: "index", method: "GET"
        }
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'keyAgreement.label'), params.id])]
        redirect action: "index", method: "GET"
    }
}
