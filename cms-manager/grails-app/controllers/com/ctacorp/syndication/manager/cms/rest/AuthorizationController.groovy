/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.rest

import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationRequest
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResponseFormatter
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResult
import grails.converters.JSON
import grails.util.Holders

import javax.annotation.PostConstruct

class AuthorizationController {

    def authorizationService
    def loggingService

    String serverUrl

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @PostConstruct
    void init() {
        def config = Holders.config
        serverUrl = config.grails.serverURL as String
    }

    def authorize(AuthorizationRequest authorizationRequest) {

        if (!authorizationRequest.validate()) {

            loggingService.logDomainErrors(authorizationRequest)
            badRequest()

        } else {

            try {
                AuthorizationResult authResult = authorizationService.authorize(authorizationRequest)
                doAuthorization(authResult)
            } catch (Throwable t) {
                serverError(t)
            }
        }
    }

    def getSubscriber() {

        def subscriberId = params.id as String
        def publicKey = params.publicKey as String

        def subscriber = {

            if(subscriberId) {
                return Subscriber.findBySubscriberId(subscriberId)
            }

            if(publicKey) {
                def keyAgreement = KeyAgreement.findByEntity2PublicKey(publicKey)
                if(keyAgreement) {
                    return Subscriber.findByKeyAgreement(keyAgreement)
                }
            }

            return null
        }()

        if(!subscriber) {
            def json = ([message: 'Not Found'] as JSON).toString(true)
            render status: 404, text: json, encoding: "UTF-8", contentType: "application/json"
            return
        }

        render status: 200, text: (subscriber as JSON), encoding: "UTF-8", contentType: "application/json"
    }

    def getSubscribers() {
        def subscriberInfos = []

        Subscriber.list(params).each { subscriber ->
            subscriberInfos.add(subscriber)
        }

        render status: 200, text: (subscriberInfos as JSON), encoding: "UTF-8", contentType: "application/json"
    }

    void doAuthorization(AuthorizationResult authResult) {

        if (!authResult.isAuthorized) {
            def text = AuthorizationResponseFormatter.formatResponse(authResult)
            render status: authResult.httpStatus, text: text, encoding: "UTF-8", contentType: "application/json"
        } else {
            render status: authResult.httpStatus, encoding: "UTF-8", contentType: "application/json"
        }
    }

    def serverError(t) {
        def errorMessage = loggingService.logError("an unexpected error occurred when processing an authorization request", t as Throwable)
        def json = ([message: errorMessage] as JSON).toString(true)
        render status: 500, text: json, encoding: "UTF-8", contentType: "application/json"
    }

    def badRequest() {
        def json = ([message: 'Bad Request'] as JSON).toString(true)
        render status: 400, text: json, encoding: "UTF-8", contentType: "application/json"
    }
}
