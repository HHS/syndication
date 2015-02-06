/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.ContentExtractionService
import com.ctacorp.syndication.manager.cms.QueueService
import com.ctacorp.syndication.manager.cms.RhythmyxIngestionService
import com.ctacorp.syndication.manager.cms.RhythmyxSubscription
import com.ctacorp.syndication.manager.cms.RhythmyxSubscriptionUpdateService
import com.ctacorp.syndication.manager.cms.Subscription
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RhythmyxSubscriptionUpdateService)
@Build([Subscription, RhythmyxSubscription])
class RhythmyxSubscriptionUpdateServiceSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def rhythmyxIngestionService = Mock(RhythmyxIngestionService)
    def queueService = Mock(QueueService)

    def content = '<html><body><div class="syndicate"><h1>What is the deal with turtles?</h1><br><p>How to tame them, <b>Volume 1.</b></p></div></body></html>'
    def systemTitle = 'The System Title!'

    Subscription subscription
    SyndicatedMediaItem syndicatedMediaItem

    def setup() {

        syndicatedMediaItem = new SyndicatedMediaItem()
        syndicatedMediaItem.setContent(content)
        syndicatedMediaItem.setId(9876)
        syndicatedMediaItem.setName(systemTitle)

        service.contentExtractionService = contentExtractionService
        service.queueService = queueService
        service.rhythmyxIngestionService = rhythmyxIngestionService

        subscription = Subscription.build(mediaId: 9876)
    }

    void "update rhythmyx subscriptions correctly handles a non existing subscription"() {

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def updatedSubscriptions = service.updateSubscriptions('9876')

        then: "the udpated rhythmyx subscription list will be empty"

        updatedSubscriptions == [failedUpdates: [], successfulUpdates: []]
    }

    void "update rhythmyx subscriptions correctly handles an unexpected exception"() {

        given: "the rhythmyx subscriptions to update"

        def rhythymyxSubscription = RhythmyxSubscription.build(subscription: subscription)

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def updatedSubscriptions = service.updateSubscriptions('9876')

        then: "extract the content for the media id"

        contentExtractionService.extractSyndicatedContent('9876') >> syndicatedMediaItem

        and: "update the rhythmyx content item"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription, syndicatedMediaItem) >> {
            throw new RuntimeException("Whats a pederass Walter?")
        }

        and: "the udpated rhythmyx subscription list will be empty"

        updatedSubscriptions == [failedUpdates: [], successfulUpdates: []]
    }

    void "update rhythmyx subscriptions using a bad mediaId"() {

        given: "the rhythmyx subscriptions to update"

        RhythmyxSubscription.build(subscription: subscription)

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        service.updateSubscriptions('9875')

        then: "the udpated rhythmyx subscription list will be empty"

        [failedUpdates: [], successfulUpdates: []]
    }

    void "update rhythmyx subscriptions successfully processes two existing subscriptions"() {

        given: "the rhythmyx subscriptions to update"

        def rhythymyxSubscription1 = RhythmyxSubscription.build(subscription: subscription)
        def rhythymyxSubscription2 = RhythmyxSubscription.build(subscription: subscription)

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def updatedSubscriptions = service.updateSubscriptions('9876')

        then: "extract the content for the media id"

        contentExtractionService.extractSyndicatedContent('9876') >> syndicatedMediaItem

        then: "get the source url for the media id"

        contentExtractionService.getMediaItem('9876') >> [sourceUrl: "http://hambone.gov/makes/a/good/bean/soup.asp"]

        and: "update the rhythmyx content items"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription1, syndicatedMediaItem) >> true
        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription2, syndicatedMediaItem) >> true

        and: "the udpated rhythmyx subscription list will contain both subscriptions"

        updatedSubscriptions == [failedUpdates: [], successfulUpdates: [rhythymyxSubscription1, rhythymyxSubscription2]]

        and: "the updated subscriptions have the correct sourceUrl"

        rhythymyxSubscription1.sourceUrl == "http://hambone.gov/makes/a/good/bean/soup.asp"
        rhythymyxSubscription2.sourceUrl == "http://hambone.gov/makes/a/good/bean/soup.asp"
    }

    void "update rhythmyx subscriptions correctly handles a failed update successfully processes another"() {

        given: "the rhythmyx subscriptions to update"

        def rhythymyxSubscription1 = RhythmyxSubscription.build(subscription: subscription)
        def rhythymyxSubscription2 = RhythmyxSubscription.build(subscription: subscription)

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def updatedSubscriptions = service.updateSubscriptions('9876')

        then: "update the rhythmyx content items"

        contentExtractionService.extractSyndicatedContent('9876') >> syndicatedMediaItem

        then: "get the source url for the media id"

        contentExtractionService.getMediaItem('9876') >> [sourceUrl: "http://hambone.gov/makes/a/good/bean/soup.asp"]

        and: "extract the content for the media id"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription1, syndicatedMediaItem) >> false

        and: "throw an exception on second update"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription2, syndicatedMediaItem) >> true

        and: "expect an update error is pushed to the error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.UPDATE, rhythymyxSubscription2.id, '9876', 0)

        then: "the udpated rhythmyx subscription list will contain the successfully updated rhythmyx subscription"

        updatedSubscriptions == [failedUpdates: [rhythymyxSubscription1], successfulUpdates: [rhythymyxSubscription2]]
    }
}
