/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationRequest
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResult
import com.ctacorp.syndication.manager.cms.rest.security.GrailsHttpServletRequestConverter
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import grails.util.Holders
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.client.ResourceAccessException

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Transactional
class AuthorizationService {

    def httpRequestConverter = new GrailsHttpServletRequestConverter()
    def apiKeyUtils = new ApiKeyUtils()
    private AuthorizationHeaderGenerator generator
    private AuthorizationHeaderGenerator.KeyAgreement keyAgreement
    def config = Holders.config

    @PostConstruct
    void init() {
        String privateKey = config?.CMSMANAGER_PRIVATEKEY
        String publicKey = config?.CMSMANAGER_PUBLICKEY
        String secret = config?.CMSMANAGER_SECRET
        if (privateKey && publicKey && secret) {
            RestBuilder rest = new RestBuilder()
            rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
            keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()

            keyAgreement.setPublicKey(publicKey)
            keyAgreement.setSecret(secret)

            generator = new AuthorizationHeaderGenerator(config?.apiKey_keyName ?: "syndication_api_key", keyAgreement)
            generator.printToConsole = false
        }
    }

    AuthorizationResult authorize(AuthorizationRequest authorizationRequest) {
        return apiKeyUtils.buildAuthorizationResult(authorizationRequest)
    }

    AuthorizationResult authorize(HttpServletRequest httpServletRequest) {
        def authorizationRequest = httpRequestConverter.convert(httpServletRequest)
        return apiKeyUtils.buildAuthorizationResult(authorizationRequest)
    }

    private sendAuthorizedRequest(String url, String body, String method){
        def requestHeaders = [
                'Date': new Date().toString(),
                'Accept': 'application/json',
                'Content-Type': "application/json;charset=UTF-8"
        ]

        if (body) {
            requestHeaders['Content-Length'] = body.bytes.size() as String
        } else {

            def headerContentLength = config?.hasProperty('CMSMANAGER_HEADERCONTENTLENGTH') ? config.CMSMANAGER_HEADERCONTENTLENGTH : true

            if(!headerContentLength) {
                requestHeaders['Content-Length'] = '0'
            }
        }

        def apiKeyHeaderValue = generator.getApiKeyHeaderValue(requestHeaders, url, method, body)
        def resp

        RestBuilder rest = new RestBuilder()
        try {
            switch (method) {
                case "POST":
                    resp = rest.post(url) {
                        header 'Date', requestHeaders.Date
                        header 'Authorization', apiKeyHeaderValue
                        header 'Content-Type', requestHeaders.'Content-Type'
                        accept "application/json"

                        json body
                    }
                    break
                case "DELETE":
                    resp = rest.delete(url) {
                        header 'Date', requestHeaders.Date
                        header 'Authorization', apiKeyHeaderValue
                        header 'Content-Type', requestHeaders.'Content-Type'
                    }
                    break
                case "GET":
                    resp = rest.get(url) {
                        header 'Date', requestHeaders.Date
                        header 'Authorization', apiKeyHeaderValue
                        header 'Content-Type', requestHeaders.'Content-Type'
                        accept "application/json"
                    }
                    break
                default: break; //do nothing
            }
        } catch(ResourceAccessException e){
            log.error "Couldn't reach remote server, it might be down"
        } catch(e){
            log.error "Unexpected connection error occured trying to communicated with: ${url}"
        }

        if (resp?.status == 403) {
            String responseDetails = "Status code:${resp.status}\nJsonBody: ${resp.json}"
            log.error(responseDetails)
            throw new AuthorizationServiceException("Access Denied - Your authorization keys have been denied.")
        }

        if(!resp || !resp.json){
            log.error "No json response from server ${url}\nResponse: ${resp?.status}"
            return null
        }

        resp.json
    }

    def post(String url, params = [:]){
        sendAuthorizedRequest(url, (params as JSON).toString(), "POST")
    }

    String logError(logId, message) {
        log.error("(${logId}) ${message}")
        message
    }
}
