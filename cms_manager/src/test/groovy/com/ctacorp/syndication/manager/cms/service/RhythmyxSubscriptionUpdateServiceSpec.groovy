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
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
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

        subscription = Subscription.build(mediaId: '9876')
    }

    void "update rhythmyx subscription correctly handles an unexpected exception"() {

        given: "a rhythmyx subscription"

        def rhythymyxSubscription = RhythmyxSubscription.build(subscription: subscription, deliveryFailureLogId: "3213123")

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def success = service.updateSubscription(rhythymyxSubscription)

        then: "get the source url for the media id"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(id: subscription.id, sourceUrl: "http://hamburgertownusa.gov/with/cheese")

        and: "extract the content for the media id"

        1* contentExtractionService.getMediaSyndicate(subscription.mediaId) >> syndicatedMediaItem

        and: "update the rhythmyx content item"

        1* rhythmyxIngestionService.updateContentItem(rhythymyxSubscription, syndicatedMediaItem) >> {
            throw new RuntimeException("Whats a pederass Walter?")
        }

        and: "don't clear the delivery failure status"

        rhythymyxSubscription.deliveryFailureLogId == "3213123"

        and: "return a failure status"

        !success
    }

    void "update rhythmyx subscription correctly handles a failed update"() {

        given: "the rhythmyx subscriptions to update"

        def rhythymyxSubscription = RhythmyxSubscription.build(subscription: subscription, deliveryFailureLogId: "fxfxzf")

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def success = service.updateSubscription(rhythymyxSubscription)

        then: "get the source url for the media id"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(id: subscription.id, sourceUrl: "http://hamburgertownusa.gov/with/cheese")

        and: "extract the content for the media id"

        1* contentExtractionService.getMediaSyndicate(subscription.mediaId) >> syndicatedMediaItem

        and: "extract the content for the media id"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription, syndicatedMediaItem) >> false

        and: "don't clear the delivery failure status"

        rhythymyxSubscription.deliveryFailureLogId == "fxfxzf"

        and: "return a failure status"

        !success
    }

    void "successfully update a rhythmyx subscription"() {

        given: "the rhythmyx subscriptions to update"

        def rhythymyxSubscription = RhythmyxSubscription.build(subscription: subscription, deliveryFailureLogId: "fxfxzf")

        and: "a changed syndicated media item name"

        syndicatedMediaItem.name = "Hamburger Town USA"

        when: "updating a subscription that is not associated with a rhythmyx subscription"

        def success = service.updateSubscription(rhythymyxSubscription)

        then: "get the source url for the media id"

        1 * contentExtractionService.getMediaItem(subscription.mediaId) >> new MediaItem(id: subscription.id, sourceUrl: "http://hamburgertownusa.gov/with/cheese")

        and: "extract the content for the media id"

        1 * contentExtractionService.getMediaSyndicate(subscription.mediaId) >> syndicatedMediaItem

        and: "extract the content for the media id"

        rhythmyxIngestionService.updateContentItem(rhythymyxSubscription, syndicatedMediaItem) >> true

        and: "clear the delivery failure status and update the source url and system title"

        !rhythymyxSubscription.deliveryFailureLogId
        rhythymyxSubscription.sourceUrl == "http://hamburgertownusa.gov/with/cheese"
        rhythymyxSubscription.systemTitle == "Hamburger Town USA"

        and: "return a success status"

        success
    }
}
