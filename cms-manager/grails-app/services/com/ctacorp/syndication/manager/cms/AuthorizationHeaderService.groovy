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
import grails.transaction.Transactional
import grails.util.Holders

import javax.annotation.PostConstruct

@Transactional
class AuthorizationHeaderService {

    DateHeader dateHeader = new DateHeader()
    def serverUrl
    def headerName

    @PostConstruct
    void init() {
        def config = Holders.config
        def apiKey = config.apiKey
        serverUrl = config.grails.serverURL
        headerName = apiKey.headerName
    }

    def createAuthorizationHeaders(AuthorizationHeaderGenerator authHeaderGenerator, String deliveryEndpoint) {
        createAuthorizationHeaders(authHeaderGenerator, deliveryEndpoint, null)
    }

    def createAuthorizationHeaders(AuthorizationHeaderGenerator authHeaderGenerator, String deliveryEndpoint, String content, String httpMethod = "POST") {

        def computedHeaders = [:]

        //noinspection SpellCheckingInspection
        computedHeaders['Date'] = dateHeader.dateHeader

        if(content) {
            computedHeaders['Content-Type'] = 'application/json'
            computedHeaders['Content-Length'] = content.bytes.length as String
        }

        def apiKeyValue = authHeaderGenerator.getApiKeyHeaderValue(computedHeaders, deliveryEndpoint, httpMethod, content)

        def returnedHeaders = [:]
        returnedHeaders.putAll(computedHeaders)

        returnedHeaders[headerName] = apiKeyValue
        log.info("Authorization header value is '${apiKeyValue}'")

        returnedHeaders['Referer'] = serverUrl

        returnedHeaders.remove("Content-Type")
        returnedHeaders.remove("Content-Length")

        return returnedHeaders
    }

    static class DateHeader {
        @SuppressWarnings("GrMethodMayBeStatic")
        String getDateHeader() {
            return (new Date() as String)
        }
    }
}
