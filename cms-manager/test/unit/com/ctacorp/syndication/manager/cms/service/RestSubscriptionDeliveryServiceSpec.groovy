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
import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.RestSubscriber
import com.ctacorp.syndication.manager.cms.RestSubscription
import com.ctacorp.syndication.manager.cms.RestSubscriptionDeliveryService
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.Subscription
import com.ctacorp.syndication.manager.cms.SubscriptionService
import com.ctacorp.syndication.manager.cms.utils.exception.RestDeliveryException
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
@TestFor(RestSubscriptionDeliveryService)
@Build([Subscriber, RestSubscriber, Subscription, RestSubscription, KeyAgreement])
class RestSubscriptionDeliveryServiceSpec extends Specification {

    String content = "Greasy Whale Milk"
    String deliveryEndpoint = "http://cookiesforthehomeless.gov/donate/now.asp"
    def headers = [:]

    RestSubscription restSubscription
    RestSubscription restSubscription2
    RestSubscriber restSubscriber
    Subscription subscription
    Subscriber subscriber
    KeyAgreement keyAgreement

    RESTClient restClient = Mock(RESTClient)
    RestSubscriptionDeliveryService.RESTClientFactory restClientFactory = Mock(RestSubscriptionDeliveryService.RESTClientFactory)
    RestSubscriptionDeliveryService.AuthorizationHeaderGeneratorFactory authorizationHeaderGeneratorFactory = Mock(RestSubscriptionDeliveryService.AuthorizationHeaderGeneratorFactory)
    AuthorizationHeaderService authorizationHeaderService = Mock(AuthorizationHeaderService)
    AuthorizationHeaderGenerator authorizationHeaderGenerator = Mock(AuthorizationHeaderGenerator)
    def subscriptionService = Mock(SubscriptionService)

    def setup() {

        keyAgreement = KeyAgreement.build()
        subscriber = Subscriber.build(keyAgreement: keyAgreement)
        subscription = Subscription.build()
        restSubscriber = RestSubscriber.build(deliveryEndpoint: deliveryEndpoint, subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", restSubscriber: restSubscriber, subscription: subscription)
        restSubscription2 = RestSubscription.build(sourceUrl: "http://double.punched.com/good/left/hook.html", notificationOnly: true, restSubscriber: restSubscriber, subscription: subscription)

        service.restClientFactory = restClientFactory
        service.authorizationHeaderGeneratorFactory = authorizationHeaderGeneratorFactory
        service.authorizationHeaderService = authorizationHeaderService
        service.subscriptionService = subscriptionService
    }

    void "deliver correctly handles a null rest subscription"() {

        when: "delivering the rest subscription"

        service.deliver(null, content)

        then: "throw a rest delivery exception"

        thrown(RestDeliveryException)
    }

    void "deliver correctly handles a null content"() {

        when: "delivering the rest subscription"

        service.deliver(restSubscription, null)

        then: "throw a rest delivery exception"

        thrown(RestDeliveryException)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "deliver correctly handles when the rest client throws an exception"() {

        given: "the exception to throw"

        def exception = new RuntimeException("cafe leche por favor")

        when: "delivering the rest subscription"

        service.deliver(restSubscription, content)

        then: "create a new authorization header generator"

        1 * authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement) >> authorizationHeaderGenerator

        and: "create the required authorization headers"

        1 * authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content) >> headers

        and: "create the rest client"

        1 * restClientFactory.newRestClient(deliveryEndpoint) >> restClient

        and: "throw an exception when trying to post the content"

        1 * restClient.post([headers: headers, body: content, query: [media_id: restSubscription.subscription.mediaId], requestContentType: 'application/json']) >> {
            throw exception
        }

        and: "throw a rest delivery exception"

        thrown(RestDeliveryException)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "deliver correctly handles a non-200 and non-410 response"() {

        given: "the expected exception to throw"

        def exception = Mock(HttpResponseException)

        when: "delivering the rest subscription"

        service.deliver(restSubscription, content)

        then: "create a new authorization header generator"

        1 * authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement) >> authorizationHeaderGenerator

        and: "create the required authorization headers"

        1 * authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content) >> headers

        and: "create the rest client"

        1 * restClientFactory.newRestClient(deliveryEndpoint) >> restClient

        and: "throw a http response exception with a 410 status code"

        1 * restClient.post([headers: headers, body: content, query: [media_id: restSubscription.subscription.mediaId], requestContentType: 'application/json']) >> {
            throw exception
        }

        and: "get the status from the exception"

        1 * exception.statusCode >> 400

        and: "throw a rest delivery exception"

        thrown(RestDeliveryException)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "deliver correctly handles a 200 response"() {

        when: "delivering the rest subscription"

        service.deliver(restSubscription, content)

        then: "create a new authorization header generator"

        1 * authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement) >> authorizationHeaderGenerator

        and: "create the required authorization headers"

        1 * authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content) >> headers

        and: "create the rest client"

        1 * restClientFactory.newRestClient(deliveryEndpoint) >> restClient

        and: "throw an exception when trying to post the content"

        1 * restClient.post([headers: headers, body: content, query: [media_id: restSubscription.subscription.mediaId], requestContentType: 'application/json']) >> [status:200]

        and: "don't throw an exception"

        noExceptionThrown()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "deliver correctly handles a notification only 200 response"() {

        when: "delivering the rest subscription"

        service.deliver(restSubscription2, content)

        then: "create a new authorization header generator"

        1 * authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement) >> authorizationHeaderGenerator

        and: "create the required authorization headers"

        1 * authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content) >> headers

        and: "create the rest client"

        1 * restClientFactory.newRestClient(deliveryEndpoint) >> restClient

        and: "throw an exception when trying to post the content"

        1 * restClient.post([headers: headers, query: [media_id: restSubscription2.subscription.mediaId]]) >> [status:200]

        and: "don't throw an exception"

        noExceptionThrown()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "deliver correctly handles a 410 response"() {

        given: "the expected exception to throw"

        def exception = Mock(HttpResponseException)

        when: "delivering the rest subscription"

        service.deliver(restSubscription, content)

        then: "create a new authorization header generator"

        1 * authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement) >> authorizationHeaderGenerator

        and: "create the required authorization headers"

        1 * authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content) >> headers

        and: "create the rest client"

        1 * restClientFactory.newRestClient(deliveryEndpoint) >> restClient

        and: "throw a http response exception with a 410 status code"

        1 * restClient.post([headers: headers, body: content, query: [media_id: restSubscription.subscription.mediaId], requestContentType: 'application/json']) >> {
            throw exception
        }

        and: "get the status from the exception"

        1 * exception.statusCode >> 410

        and: "delete the subscription"

        1 * subscriptionService.deleteChildSubscription(restSubscription)
    }
}
