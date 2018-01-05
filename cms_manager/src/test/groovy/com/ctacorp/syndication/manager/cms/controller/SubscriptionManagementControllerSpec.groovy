/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.controller

import com.ctacorp.syndication.manager.cms.RestSubscriptionManagementService
import com.ctacorp.syndication.manager.cms.rest.SubscriptionManagementController
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(SubscriptionManagementController)
class SubscriptionManagementControllerSpec extends Specification {

    def restSubscriptionManagementService = Mock(RestSubscriptionManagementService)

    def senderPublicKey = "some key value"
    def responseText = "{\"somekey\": \"someValue\"}"
    def responseTextAsJsonp = "callback({\"somekey\": \"someValue\"});"
    def mediaId = "12345"
    def subscriptionId = 23456
    def sourceUrl = "http://hamonaise.gov/ham/with/mayo/in/a/jar/sounds/disgusting.html"

    def setup() {
        request.setAttribute("senderPublicKey", "some key value")
        controller.restSubscriptionManagementService = restSubscriptionManagementService
    }

    void "delete subscription success"() {

        when: "calling the delete action"

        request.method = "DELETE"
        controller.deleteSubscription(subscriptionId)

        then: "delete the subscription using the subscription management service"

        1 * restSubscriptionManagementService.deleteSubscription(subscriptionId, senderPublicKey) >> {
            return [status: 204]
        }

        and: "the response status should match"

        response.status == 204

        and: "the response text should be empty"

        response.text == ""
    }

    void "get subscription success"() {

        when: "calling the get subscription action"

        controller.getSubscription(subscriptionId)

        then: "get the subscription using the subscription management service"

        1 * restSubscriptionManagementService.getSubscription(subscriptionId, senderPublicKey) >> {
            return [status: 200, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 200
        response.text == responseText

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }

    void "get subscription success as jsonp"() {

        when: "calling the get subscription action"

        controller.getSubscription(subscriptionId)

        then: "get the subscription using the subscription management service"

        1 * restSubscriptionManagementService.getSubscription(subscriptionId, senderPublicKey) >> {
            return [status: 200, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 200
        response.text == responseText

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }

    void "get all subscriptions success"() {

        given: "a jsonp callback parameter was provided"

        params['callback'] = 'callback'

        when: "calling the get all subscriptions action"

        controller.getAllSubscriptions()

        then: "get all the subscriptions using the subscription management service"

        1 * restSubscriptionManagementService.getAllSubscriptions(senderPublicKey) >> {
            return [status: 200, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 200
        response.text == responseTextAsJsonp

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }

    void "get all subscriptions success as jsonp"() {

        given: "a jsonp callback parameter was provided"

        params['callback'] = 'callback'

        when: "calling the get all subscriptions action"

        controller.getAllSubscriptions()

        then: "get all the subscriptions using the subscription management service"

        1 * restSubscriptionManagementService.getAllSubscriptions(senderPublicKey) >> {
            return [status: 200, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 200
        response.text == responseTextAsJsonp

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }

    void "subscribe success"() {

        given: "the required/optional params"

        params['media_id'] = mediaId
        params['source_url'] = sourceUrl
        params['notification_only'] = "true"

        when: "calling the subscribe action"

        request.method ="PUT"
        controller.subscribe()

        then: "create the subscription"

        1 * restSubscriptionManagementService.createSubscription(mediaId, sourceUrl, true, senderPublicKey) >> {
            return [status: 201, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 201
        response.text == responseText

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }

    void "subscribe success as jsonp"() {

        given: "the required/optional params"

        params['media_id'] = mediaId
        params['source_url'] = sourceUrl

        and: "a jsonp callback parameter was provided"

        params['callback'] = 'callback'

        when: "calling the subscribe action"

        request.method ="PUT"
        controller.subscribe()

        then: "create the subscription"

        1 * restSubscriptionManagementService.createSubscription(mediaId, sourceUrl, false, senderPublicKey) >> {
            return [status: 201, message: responseText]
        }

        and: "the response status and text should match"

        response.status == 201
        response.text == responseTextAsJsonp

        and: "the response should be of content type application/json with UTF-8 encoding"

        response.contentType == "application/json;charset=UTF-8"
    }
}
