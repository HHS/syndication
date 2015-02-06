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
import com.ctacorp.syndication.manager.cms.RestSubscriber
import com.ctacorp.syndication.manager.cms.RestSubscription
import com.ctacorp.syndication.manager.cms.RestSubscriptionDeliveryService
import com.ctacorp.syndication.manager.cms.RestSubscriptionUpdateService
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.Subscription
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscriptionUpdateService)
@Build([Subscriber, RestSubscriber, Subscription, RestSubscription])
class RestSubscriptionUpdateServiceSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def restSubscriptionDeliveryService = Mock(RestSubscriptionDeliveryService)
    def syndicatedMediaItem = new SyndicatedMediaItem(content: "Only the best mon")

    RestSubscription restSubscription
    RestSubscription restSubscription2
    RestSubscriber restSubscriber
    Subscription subscription
    Subscriber subscriber

    def setup() {

        subscriber = Subscriber.build()
        subscription = Subscription.build()
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", deliveryFailureLogId: "AEDASDASD", restSubscriber: restSubscriber, subscription: subscription)
        restSubscription2 = RestSubscription.build(sourceUrl: "http://heartless.hangovers.gov/bad/morning.do", deliveryFailureLogId: "AEDASDASD", restSubscriber: restSubscriber, subscription: subscription)

        service.contentExtractionService = contentExtractionService
        service.restSubscriptionDeliveryService = restSubscriptionDeliveryService
    }

    void "update subscriptions correctly handles when the media id doesn't match any subscriptions"() {

        when: "updating subscriptions for an unknown media id"

        def failedUpdates = service.updateSubscriptions("9999")

        then: "return an empty list of failed updates"

        failedUpdates.size() == 0
    }

    void "update subscriptions correctly handles when the media id doesn't match any rest subscriptions"() {

        given: "a subscription that has no existing rest subscriptions"

        Subscription.build(mediaId:9999)

        when: "updating subscriptions for an unknown media id"

        def failedUpdates = service.updateSubscriptions("9999")

        then: "return an empty list of failed updates"

        failedUpdates.size() == 0
    }

    void "update subscriptions correctly handles when the extraction service throws an exception"() {

        given: "the exception to throw"

        def exception = new ServiceException("WTF!")

        when: "updating subscriptions"

        def failedUpdates = service.updateSubscriptions(subscription.mediaId as String)

        then: "throw an exception from the extraction service"

        1 * contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> {
            throw exception
        }

        and: "return the list of failed updates"

        failedUpdates.size() == 2
        failedUpdates.get(0) == restSubscription
    }

    void "update subscriptions correctly handles when the extraction service returns null"() {

        when: "updating subscriptions"

        def failedUpdates = service.updateSubscriptions(subscription.mediaId as String)

        then: "return null from the extraction service"

        1 * contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> null

        and: "return the list of failed updates"

        failedUpdates.size() == 2
        failedUpdates.get(0) == restSubscription
    }

    void "update subscriptions correctly handles when the rest delivery service throws an exception"() {

        given: "the exception to throw"

        def exception = new RuntimeException("WTF!")

        when: "updating subscriptions"

        def failedUpdates = service.updateSubscriptions(subscription.mediaId as String)

        then: "get the media item from the extraction service"

        1 * contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> syndicatedMediaItem

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId as String) >> [sourceUrl: "http://hamAndCheese.gov/please.html"]

        and: "throw an exception from the rest delivery service"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content) >> {
            throw exception
        }

        and: "return the list of failed updates"

        failedUpdates.size() == 1
        failedUpdates.get(0) == restSubscription
    }

    void "import subscriptions correctly handles an unknown subscription id"() {

        when: "importing a subscription"

        def success = service.importSubscription(9999)

        then: "the import should fail"

        !success
    }

    void "import subscriptions correctly handles when the content extraction service throws an exception"() {

        given: "the exception to throw"

        def exception = new RuntimeException("WTF!")

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "throw an exception when extracting the content"

        contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> {
            throw exception
        }

        and: "the import should fail"

        !success
    }

    void "import subscriptions correctly handles when the content extraction service fails to return any content"() {

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "return null from the extraction service"

        contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> null

        and: "the import should fail"

        !success
    }

    void "import subscriptions successfully imports a rest subscription"() {

        when: "importing a subscription"

        def success = service.importSubscription(restSubscription.id)

        then: "return null from the extraction service"

        contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> syndicatedMediaItem

        and: "deliver the rest subscription"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content)

        and: "the import should succeed"

        success
    }

    void "update subscriptions successfully updates multiple rest subscriptions"() {

        when: "updating subscriptions"

        def failedUpdates = service.updateSubscriptions(subscription.mediaId as String)

        then: "return the syndication media item from the extraction service"

        1 * contentExtractionService.extractSyndicatedContent(subscription.mediaId as String) >> syndicatedMediaItem

        then: "get the source url from the extraction service"

        1 * contentExtractionService.getMediaItem(subscription.mediaId as String) >> [sourceUrl: "http://hamAndCheese.gov/please.html"]

        and: "deliver the first rest subscription"

        1 * restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content)

        and: "deliver the first rest subscription"

        1 * restSubscriptionDeliveryService.deliver(restSubscription2, syndicatedMediaItem.content)

        and: "return the list of failed updates"

        failedUpdates.size() == 0

        and: "the source url has been updated"

        restSubscription.sourceUrl == "http://hamAndCheese.gov/please.html"
        restSubscription2.sourceUrl == "http://hamAndCheese.gov/please.html"

        and: "the delivery failure log id is null"

        !restSubscription.deliveryFailureLogId
        !restSubscription2.deliveryFailureLogId

        and: "is pending is false"

        !restSubscription.isPending
        !restSubscription2.isPending
    }
}
