/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.manager.cms.*
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscriptionUpdateService)
@Build([Subscriber, RestSubscriber, Subscription, RestSubscription])
class RestSubscriptionUpdateServiceSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def subscriptionService = Mock(SubscriptionService)
    def restSubscriptionDeliveryService = Mock(RestSubscriptionDeliveryService)
    def syndicatedMediaItem = new SyndicatedMediaItem(content: "Only the best mon")

    RestSubscription restSubscription
    RestSubscription restSubscription2
    RestSubscriber restSubscriber
    Subscription subscription
    Subscriber subscriber

    def setup() {

        subscriber = Subscriber.build()
        subscription = Subscription.build(mediaId: '1234')
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", deliveryFailureLogId: "AEDASDASD", restSubscriber: restSubscriber, subscription: subscription)
        restSubscription2 = RestSubscription.build(sourceUrl: "http://heartless.hangovers.gov/bad/morning.do", deliveryFailureLogId: "AEDASDASD", restSubscriber: restSubscriber, subscription: subscription)

        service.subscriptionService = subscriptionService
        service.contentExtractionService = contentExtractionService
        service.restSubscriptionDeliveryService = restSubscriptionDeliveryService
    }

    void "update subscription correctly handles when the extraction service throws an exception"() {

        given: "the exception to throw"

        def exception = new ServiceException("WTF!")

        when: "updating subscriptions"

        boolean success = service.updateSubscription(restSubscription)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        and: "throw an exception from the extraction service"

        1 * contentExtractionService.getMediaSyndicate(subscription.mediaId) >> {
            throw exception
        }

        and: "return a failed status"

        !success
    }

    void "update subscription correctly handles when the extraction service returns null"() {

        when: "updating subscriptions"

        boolean success = service.updateSubscription(restSubscription)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        and: "return null from the extraction service"

        1 * contentExtractionService.getMediaSyndicate(subscription.mediaId) >> null

        and: "return a failed status"

        !success
    }

    void "update subscription correctly handles when the rest delivery service throws an exception"() {

        given: "the exception to throw"

        def exception = new RuntimeException("WTF!")

        when: "updating subscriptions"

        def success = service.updateSubscription(restSubscription)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        then: "get the media item from the extraction service"

        1 * contentExtractionService.getMediaSyndicate(subscription.mediaId) >> syndicatedMediaItem

        and: "throw an exception from the rest delivery service"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content) >> {
            throw exception
        }

        and: "return a failed status"

        !success
    }

    void "import subscription correctly handles an unknown subscription id"() {

        when: "importing a subscription"

        def success = service.importSubscription(9999)

        then: "return a failed status"

        !success
    }

    void "import subscription correctly handles when the content extraction service throws an exception"() {

        given: "the exception to throw"

        def exception = new RuntimeException("WTF!")

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        and: "throw an exception when extracting the content"

        contentExtractionService.getMediaSyndicate(subscription.mediaId as String) >> {
            throw exception
        }

        and: "return a failed status"

        !success
    }

    void "import subscription correctly handles when the content extraction service fails to return any content"() {

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        then: "return null from the extraction service"

        contentExtractionService.getMediaSyndicate(subscription.mediaId as String) >> null

        and: "return a failed status"

        !success
    }

    void "import subscription successfully imports a rest subscription"() {

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        and: "return null from the extraction service"

        contentExtractionService.getMediaSyndicate(subscription.mediaId as String) >> syndicatedMediaItem

        and: "deliver the rest subscription"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content)

        and: "clear the is pending and log failure id"

        !restSubscription.deliveryFailureLogId
        !restSubscription.isPending

        and: "return a success status"

        success
    }

    void "update subscription successfully updates a rest subscription"() {

        when: "updating a subscription"

        def success = service.updateSubscription(restSubscription)

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(sourceUrl: "http://hamAndCheese.gov/please.html")

        then: "return the syndication media item from the extraction service"

        1 * contentExtractionService.getMediaSyndicate(subscription.mediaId as String) >> syndicatedMediaItem

        and: "deliver the rest subscription"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content)

        and: "return a success status"

        success
    }

    void "delete subscription successfully updates a rest subscription"() {

        when: "updating a subscription"

        def success = service.deleteSubscription(restSubscription)

        then: "deliver the delete message"

        1 * restSubscriptionDeliveryService.deliverDelete(restSubscription)

        and: "delete the subscription"

        subscriptionService.deleteChildSubscription(restSubscription)

        and: "return a success status"

        success
    }
}
