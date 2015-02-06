/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.manager.cms.ContentExtractionService
import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.LoggingService
import com.ctacorp.syndication.manager.cms.QueueService
import com.ctacorp.syndication.manager.cms.RestSubscriber
import com.ctacorp.syndication.manager.cms.RestSubscription
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.Subscription
import com.ctacorp.syndication.manager.cms.RestSubscriptionManagementService
import com.ctacorp.syndication.manager.cms.SubscriptionService
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
@TestFor(RestSubscriptionManagementService)
@Build([KeyAgreement, Subscriber, RestSubscriber, Subscription, RestSubscription])
class RestSubscriptionManagementServiceSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def loggingService = Mock(LoggingService)
    def queueService = Mock(QueueService)
    def subscriptionService = Mock(SubscriptionService)

    RestSubscription restSubscription
    RestSubscriber restSubscriber
    Subscription subscription
    Subscriber subscriber
    KeyAgreement keyAgreement

    def setup() {

        keyAgreement = KeyAgreement.build(entity2PublicKey: "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bg==")
        subscriber = Subscriber.build(keyAgreement: keyAgreement)
        subscription = Subscription.build(mediaId: "11111")
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", notificationOnly: true, restSubscriber: restSubscriber, subscription: subscription)

        service.contentExtractionService = contentExtractionService
        service.subscriptionService = subscriptionService
        service.loggingService = loggingService
        service.queueService = queueService
    }

    void "create subscription correctly handles a missing sender public key"() {

        when: "creating a subscription with a missing sender public key"

        def result = service.createSubscription("12345", null, true, null)

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "create subscription correctly handles an unknown public key"() {

        when: "creating a subscription with an unknown public key"

        def result = service.createSubscription("12345", null, true, "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bh==")

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "create subscription correctly handles both a missing media id and source url"() {

        when: "creating a subscription with a null media id and source url"

        def result = service.createSubscription(null, null, true, keyAgreement.entity2PublicKey)

        then: "return a 400 status with bad request message"

        result.status == 400
        result.message == "{\"message\": \"Bad Request: 'media_id' or 'source_url' query parameter is required\"}"
    }

    void "create subscription correctly handles a non-long media id"() {

        when: "creating a subscription with a alphanumeric media id"

        def result = service.createSubscription("asd21312", null, true, keyAgreement.entity2PublicKey)

        then: "return a 400 status with bad request message"

        result.status == 400
        result.message == "{\"message\": \"Bad Request: could not find a media item identified by media_id 'asd21312'\"}"
    }

    void "create subscription correctly handles an unknown source url"() {

        given: "the source url to subscribe to"

        def sourceUrl = "http://ham.and.cheese.gov/on/a/six/inch/role.html"

        when: "creating a subscription with an unknown source url"

        def result = service.createSubscription(null, sourceUrl, true, keyAgreement.entity2PublicKey)

        then: "return a null media id from the content extraction service"

        1 * contentExtractionService.getMediaId(sourceUrl) >> null

        and: "return a 400 status with bad request message"

        result.status == 400
        result.message == "{\"message\": \"Bad Request: could not find a media item associated with source_url 'http://ham.and.cheese.gov/on/a/six/inch/role.html'\"}"
    }

    void "create subscription correctly handles when the content extraction service throws an exception trying to get the mediaId for a source url"() {

        given: "the source url to subscribe to"

        def sourceUrl = "http://ham.and.cheese.gov/on/a/six/inch/role.html"

        and: "the exception to throw"

        def exception = new ServiceException("a white pizza! ugh!")

        when: "creating a subscription with an unknown source url"

        def result = service.createSubscription(null, sourceUrl, true, keyAgreement.entity2PublicKey)

        then: "throw and exception when trying to get the media id"

        1 * contentExtractionService.getMediaId(sourceUrl) >> {
            throw exception
        }

        and: "log the error and return a sanitized message"

        loggingService.logError("Error occurred when trying to fetch the media item associated with source_url '${sourceUrl}'", exception) >> "a better message"

        and: "return a 500 status with server error message"

        result.status == 500
        result.message == "{\"message\": \"a better message\"}"
    }

    void "create subscription correctly handles when the content extraction service throws an exception when trying to get the media item"() {

        given: "the exception to throw"

        def exception = new ServiceException("a white pizza! ugh!")

        when: "creating a subscription"

        def result = service.createSubscription("123456", null, true, keyAgreement.entity2PublicKey)

        then: "throw and exception when trying to get the media item"

        1 * contentExtractionService.getMediaItem("123456") >> {
            throw exception
        }

        and: "log the error and return a sanitized message"

        loggingService.logError("Error occurred when trying to fetch the media item for media_id '${123456}'", exception) >> "a better message"

        and: "return a 500 status with server error message"

        result.status == 500
        result.message == "{\"message\": \"a better message\"}"
    }

    void "create subscription saves a new rest subscription for an existing subscription"() {

        given: "an existing subscription"

        def subscription = Subscription.build(mediaId: "12345")

        and: "the media item to be returned from the content extraction service"

        def mediaItem = new MediaItem(sourceUrl: "http://bros.in.the.know.gov", name: "Bros in the Know")

        when: "creating a subscription"

        def result = service.createSubscription(subscription.mediaId, null, true, keyAgreement.entity2PublicKey)

        then: "get media item from the content extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> mediaItem

        and: "create a new rest subscription"

        RestSubscription.count == 2

        and: "set the rest subscription's source url, title and notification option"

        def restSubscription = RestSubscription.findByTitle(mediaItem.name)
        restSubscription.sourceUrl == mediaItem.sourceUrl
        restSubscription.notificationOnly

        and: "return a 200 status with success message"

        result.status == 201
        result.message.startsWith("{\n" + "   \"id\": ${restSubscription.id},")
    }

    void "create subscription saves a new rest subscription for a new subscription"() {

        given: "the media item to be returned from the content extraction service"

        def mediaItem = new MediaItem(sourceUrl: "http://bros.in.the.know.gov", name: "Bros in the Know")

        when: "creating a subscription"

        def result = service.createSubscription("123456", null, true, keyAgreement.entity2PublicKey)

        then: "get media item from the content extraction service"

        1 * contentExtractionService.getMediaItem("123456") >> mediaItem

        and: "create a new subscription"

        Subscription.count == 2

        and: "create a new rest subscription"

        RestSubscription.count == 2

        and: "set the rest subscription's source url, title and notification option"

        def restSubscription = RestSubscription.findByTitle(mediaItem.name)
        restSubscription.sourceUrl == mediaItem.sourceUrl
        restSubscription.notificationOnly

        and: "return a 200 status with the created subscriptions"

        result.status == 201
        result.message.startsWith("{\n" + "   \"id\": ${restSubscription.id},")
    }

    void "create subscription skips an existing rest subscription"() {

        given: "the media item to be returned from the content extraction service"

        def mediaItem = new MediaItem(sourceUrl: "http://bros.in.the.know.gov", name: "Bros in the Know")

        when: "creating a subscription"

        def result = service.createSubscription(subscription.mediaId, null, true, keyAgreement.entity2PublicKey)

        then: "get media item from the content extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> mediaItem

        and: "skip creation of the rest subscription"

        RestSubscription.count == 1

        and: "return a 200 status with the created subscriptions"

        result.status == 201
        result.message.startsWith("{\n" + "   \"id\": ${restSubscription.id},")
    }

    void "create subscription skips creation when no rest subscribers are associated with the subscriber"() {

        given: "a key agreement"

        def keyAgreement = KeyAgreement.build(entity2PublicKey: "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bh==")

        and: "a subscriber with no associated rest subscribers"

        Subscriber.build(keyAgreement: keyAgreement)

        when: "creating a subscription"

        def result = service.createSubscription(subscription.mediaId, null, true, keyAgreement.entity2PublicKey)

        then: "return a 400 status with bad request message"

        result.status == 400
        result.message == "{\"message\": \"Bad Request: no rest subscribers (delivery endpoints) exist for this subscriber\"}"
    }

    void "get all subscriptions correctly handles a missing sender public key"() {

        when: "getting all subscriptions with a missing sender public key"

        def result = service.getAllSubscriptions(null)

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "get all subscriptions correctly handles an unknown public key"() {

        when: "getting all subscriptions with an unknown public key"

        def result = service.getAllSubscriptions("9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bh==")

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "get all subscriptions success"() {

        when: "getting all subscriptions"

        def result = service.getAllSubscriptions(keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 200
        result.message.startsWith("[{\n" + "   \"id\": ${restSubscription.id},")
    }

    void "get all subscriptions correctly handles when no rest subscribers exist"() {

        setup: "delete the rest subscriber"

        restSubscription.delete(flush: true)
        restSubscriber.delete(flush: true)

        when: "getting all subscriptions"

        def result = service.getAllSubscriptions(keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 200
        result.message == "[]"
    }

    void "get all subscriptions correctly handles when no rest subscriptions exist"() {

        setup: "delete all rest subscriptions"

        restSubscription.delete(flush: true)

        when: "getting all subscriptions"

        def result = service.getAllSubscriptions(keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 200
        result.message == "[]"
    }

    void "get subscription correctly handles a missing sender public key"() {

        when: "getting a subscription with a missing sender public key"

        def result = service.getSubscription(restSubscription.id, null)

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "get subscription correctly handles an unknown public key"() {

        when: "getting a subscription"

        def result = service.getSubscription(restSubscription.id, "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bh==")

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "get subscription success"() {

        when: "getting a subscription"

        def result = service.getSubscription(restSubscription.id, keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 200
        result.message.startsWith("{\n" + "   \"id\": ${restSubscription.id},")
    }

    void "get subscription correctly handles when no rest subscribers exist"() {

        setup: "delete the rest subscriber"

        restSubscription.delete(flush: true)
        restSubscriber.delete(flush: true)

        when: "getting a subscription"

        def result = service.getSubscription(12345, keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 404
        result.message == "{\"message\": \"Not Found: the rest subscription identified by id '12345' does not exist\"}"
    }

    void "get subscription correctly handles when no rest subscriptions exist"() {

        setup: "delete the rest subscriber"

        restSubscription.delete(flush: true)

        when: "getting a subscription"

        def result = service.getSubscription(12345, keyAgreement.entity2PublicKey)

        then: "return a 200 status with all subscriptions"

        result.status == 404
        result.message == "{\"message\": \"Not Found: the rest subscription identified by id '12345' does not exist\"}"
    }

    void "delete subscription correctly handles a missing sender public key"() {

        when: "deleting a subscription with a missing sender public key"

        def result = service.deleteSubscription(restSubscription.id, null)

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "delete subscription correctly handles an unknown public key"() {

        when: "deleting a subscription with an unknown public key"

        def result = service.deleteSubscription(restSubscription.id, "9k+x8vDQJBcEYcEb/y/iipg8kXMU7sFfk1klV3PqMZkUBuJ/rDgVZtHmJGBydEKfnGKPAf6y9DBb7a+1tAz9Bh==")

        then: "return a 403 status with unauthorized response message"

        result.status == 403
        result.message == "{\"message\": \"Unauthorized: the sender's public key is not associated with a known subscriber\"}"
    }

    void "delete subscription success"() {

        when: "deleting a subscription"

        def result = service.deleteSubscription(restSubscription.id, keyAgreement.entity2PublicKey)

        then: "delete the subscription"

        subscriptionService.deleteChildSubscription(restSubscription)

        and: "return a 204 status with no response text"

        result.status == 204
        result.message == null
    }

    void "delete subscription correctly handles when no rest subscribers exist"() {

        setup: "delete the rest subscriber"

        restSubscription.delete(flush: true)
        restSubscriber.delete(flush: true)

        when: "deleting a subscription"

        def result = service.deleteSubscription(12345, keyAgreement.entity2PublicKey)

        then: "return a 204 status with no response text"

        result.status == 204
        result.message == null
    }

    void "delete subscription correctly handles when no rest subscriptions exist"() {

        setup: "delete the rest subscriber"

        restSubscription.delete(flush: true)

        when: "deleting a subscription"

        def result = service.deleteSubscription(12345, keyAgreement.entity2PublicKey)

        then: "return a 204 status with no response text"

        result.status == 204
        result.message == null
    }
}
