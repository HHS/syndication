package com.ctacorp.tagcloud

/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder

import javax.annotation.PostConstruct

class AuthorizationService {
    static transactional = false

    def grailsApplication

    private AuthorizationHeaderGenerator generator
    private AuthorizationHeaderGenerator.KeyAgreement keyAgreement
    private RestBuilder rest

    @PostConstruct
    void init() {
        String privateKey = grailsApplication.config.cmsManager.privateKey
        String publicKey = grailsApplication.config.cmsManager.publicKey
        String secret = grailsApplication.config.cmsManager.secret
        if (privateKey && publicKey && secret) {
            rest = new RestBuilder()
            keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()

            keyAgreement.setPrivateKey(privateKey)
            keyAgreement.setPublicKey(publicKey)
            keyAgreement.setSecret(secret)

            generator = new AuthorizationHeaderGenerator("syndication_api_key", keyAgreement)
        }
    }

    String hashBody(String body){
        generator.hashData(body)
    }

    boolean checkAuthorization(Map thirdPartyRequest) {
        def authorizationRequest = (thirdPartyRequest as JSON).toString()

        String date = new Date().toString()
        String requestUrl = grailsApplication.config.cmsManager.serverUrl + grailsApplication.config.cmsManager.verifyAuthPath
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([
                date: date,
                "content-type": "application/json",
                "content-length": authorizationRequest.bytes.size() as String
        ],
                requestUrl, "POST", authorizationRequest)

        def resp = rest.post(requestUrl) {
            header 'Date', date
            header 'Authorization', apiKeyHeaderValue

            json thirdPartyRequest
        }

        if(resp?.status == 204) {
            return true
        }

        log.error "The response status from the authentication service was ${resp.status}"
        log.error "${resp?.json}"
        false
    }

    def sendAuthorizedRequest(String body, String url){
        def requestHeaders = [
                'Date': new Date().toString(),
                'Content-Type': "application/json",
                'Content-Length': body.bytes.size() as String,
                'Accept': 'application/json'
        ]

        def apiKeyHeaderValue = generator.getApiKeyHeaderValue(requestHeaders, url, 'POST', body)

        def resp

        try {
            resp = rest.post(url) {
                header 'Date', requestHeaders.Date
                header 'Authorization', apiKeyHeaderValue
                accept "application/json"

                json body
            }

        }catch(e){
            log.error("Couldn't post tinyUrl - maybe the server isn't up?")
            return null
        }

        if(resp?.status == 204) {
            return resp.json
        }

        log.error "The response status from the authentication service was ${resp.status}"
        log resp.json
        return null
    }

    boolean amIAuthorized() {
        String date = new Date().toString()
        String requestUrl = grailsApplication.config.cmsManager.serverUrl + grailsApplication.config.cmsManager.selfAuthPath
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([date: date], requestUrl, "GET", null)
        def resp = rest.get(requestUrl) {
            header 'Date', date
            header 'Authorization', apiKeyHeaderValue
        }

        resp.status == 204
    }
}