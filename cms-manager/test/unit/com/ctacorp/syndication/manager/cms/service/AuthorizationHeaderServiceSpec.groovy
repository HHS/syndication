/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import com.ctacorp.syndication.manager.cms.AuthorizationHeaderService
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AuthorizationHeaderService)
class AuthorizationHeaderServiceSpec extends Specification {

    def dateHeader = Mock(AuthorizationHeaderService.DateHeader)
    def authorizationHeaderGenerator

    def setup() {

        service.serverUrl = "http://hostwiththemost:9999"
        service.dateHeader = dateHeader
        service.headerName = "Authorization"

        AuthorizationHeaderGenerator.KeyAgreement keyAgreement = keyAgreememnt()
        authorizationHeaderGenerator = new AuthorizationHeaderGenerator("api_key", keyAgreement)
    }

    private static AuthorizationHeaderGenerator.KeyAgreement keyAgreememnt() {
        def keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()
        keyAgreement.secret = "xjY3i4AnsZ9wWuDKboD1XbAdtX1hgOh2tYMnwCWnXhweO94IKrbVJuPZIQsyO5Sa40CjAMF9tG5ciI+cXITjVw=="
        keyAgreement.publicKey = "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bg=="
        keyAgreement
    }

    @SuppressWarnings(["GroovyAssignabilityCheck", "SpellCheckingInspection"])
    void "create authorization headers correctly adds the authorization header"() {

        given: "the expected header values"

        def deliveryEndpoint = "http://delivery.endpoint.gov/publish/here.html"
        def content = "only the best content"

        def refererHeaderValue = "http://hostwiththemost:9999"
        def dateHeaderValue = "the day the world ended"

        and: "the expected headers map"

        def computedHeaders = [:]
        computedHeaders["Date"] = dateHeaderValue
        computedHeaders["Content-Type"] = "application/json"
        computedHeaders["Content-Length"] = content.bytes.length

        when: "creating the authorization headers"

        def headers = service.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content)

        then: "generate the dateHeaderValue header"

        1 * dateHeader.dateHeader >> dateHeaderValue

        and: "the authorization header should be set to the computed value"

        headers["Authorization"] == "api_key 9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bg==:DTHG/alJXi3109RVhJZxRA=="

        and: "the referrer header should be set to the grails server url"

        headers["Referer"] == refererHeaderValue

        and: "the dateHeaderValue header should be set"

        headers["Date"] == dateHeaderValue

        and: "the content type header should be removed"

        !headers["Content-Type"]

        and: "the content length header should be removed"

        !headers["Content-Length"]
    }

    @SuppressWarnings(["GroovyAssignabilityCheck", "SpellCheckingInspection"])
    void "create authorization headers correctly adds the authorization header when content is null or empty"() {

        given: "the expected header values"

        def deliveryEndpoint = "http://delivery.endpoint.gov/publish/here.html"

        def refererHeaderValue = "http://hostwiththemost:9999"
        def dateHeaderValue = "the day the world ended"

        and: "the expected headers map"

        def computedHeaders = [:]
        computedHeaders["Date"] = dateHeaderValue

        when: "creating the authorization headers"

        def headers = service.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint)

        then: "generate the dateHeaderValue header"

        1 * dateHeader.dateHeader >> dateHeaderValue

        and: "the authorization header should be set to the computed value"

        headers["Authorization"] == "api_key 9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bg==:NkRS1QgJevllJV81SzFZJQ=="

        and: "the referrer header should be set to the grails server url"

        headers["Referer"] == refererHeaderValue

        and: "the dateHeaderValue header should be set"

        headers["Date"] == dateHeaderValue

        and: "the content type header should be removed"

        !headers["Content-Type"]

        and: "the content length header should be removed"

        !headers["Content-Length"]
    }
}
