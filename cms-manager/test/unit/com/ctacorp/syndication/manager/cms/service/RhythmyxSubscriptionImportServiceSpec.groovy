
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
import com.ctacorp.syndication.manager.cms.*
import com.ctacorp.syndication.manager.cms.utils.exception.RhythmyxIngestionException
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(RhythmyxSubscriptionImportService)
@Build([RhythmyxSubscription,Subscription,RhythmyxSubscriber,Subscriber])
class RhythmyxSubscriptionImportServiceSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def rhythmyxIngestionService = Mock(RhythmyxIngestionService)
    def queueService = Mock(QueueService)
    def loggingService = Mock(LoggingService)

    String sourceUrl = 'http://buttered.nail.clippers.com/extra/butter.asp'
    String systemTitle = 'Buttered Nail Clippers'
    String content = 'Why nail clippers in butter? Because why not good sir!'
    Subscription subscription
    RhythmyxSubscription rhythmyxSubscription
    SyndicatedMediaItem syndicatedMediaItem
    RhythmyxSubscriber rhythmyxSubscriber
    Subscriber subscriber
    def mediaContentUrl = 'http://syndication.api.gov/{mediaId}'

    def setup() {

        syndicatedMediaItem = new SyndicatedMediaItem()
        syndicatedMediaItem.setContent(content)
        syndicatedMediaItem.setId(9876)
        syndicatedMediaItem.setName(systemTitle)

        service.messageSource = null
        service.contentExtractionService = contentExtractionService
        service.rhythmyxIngestionService = rhythmyxIngestionService
        service.queueService = queueService
        service.loggingService = loggingService

        subscriber = Subscriber.build()
        rhythmyxSubscriber = RhythmyxSubscriber.build(subscriber:subscriber)
        subscription = Subscription.build(mediaId:9876)
        rhythmyxSubscription = RhythmyxSubscription.build(sourceUrl:sourceUrl,rhythmyxSubscriber:rhythmyxSubscriber)

        service.mediaContentUrl = mediaContentUrl

        assert Subscription.count == 1
    }

    void "import subscription correctly handles when the subscription is not found"() {

        when: "importing a subscription"

        service.importRhythmyxSubscription(9999)

        then: "requeue the subscription on the error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, rhythmyxSubscription.id, null, 0)

        and: "don't throw any any exceptions"

        notThrown(RhythmyxIngestionException)
    }

    void "import subscription correctly handles an unexpected exception"() {

        when: "importing a subscription"

        service.importRhythmyxSubscription(9999)

        then: "requeue the subscription on the error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, rhythmyxSubscription.id, null) >> {
            throw new RuntimeException('Obviously your not a golfer.')
        }

        and: "don't throw any any exceptions"

        notThrown(RhythmyxIngestionException)
    }

    void "import subscription correctly handles when syndication can't find any media for the source url"() {

        when: "importing the subscription"

        service.importRhythmyxSubscription(rhythmyxSubscription.id)

        then: "try to fetch the media item"

        contentExtractionService.getMediaItemBySourceUrl(sourceUrl) >> null

        and: "don't throw any any exceptions"

        notThrown(RhythmyxIngestionException)
    }

    void "import subscription correctly handles when rhythmyx ingestion service doesn't return a content id"() {

        when: "importing the subscription"

        service.importRhythmyxSubscription(rhythmyxSubscription.id)

        then: "fetch the media item"

        contentExtractionService.getMediaItemBySourceUrl(sourceUrl) >> 9876

        and: "extract the content"

        contentExtractionService.getMediaSyndicate('9876') >> syndicatedMediaItem

        and: "try to ingest the item into rhythmyx"

        rhythmyxIngestionService.importMediaItem(rhythmyxSubscription, content, systemTitle) >> null

        then: "requeue the subscription on the error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, rhythmyxSubscription.id, null, 0)

        and: "don't throw any any exceptions"

        notThrown(RhythmyxIngestionException)
    }

    void "import subscription for existing subscription"() {

        when: "importing the subscription"

        service.importRhythmyxSubscription(rhythmyxSubscription.id)

        then: "check that the media item exists"

        contentExtractionService.getMediaItemBySourceUrl(sourceUrl) >> new MediaItem(id: 9876, name: 'Buttered Nail Clippers')

        and: "extract the content"

        contentExtractionService.getMediaSyndicate('9876') >> syndicatedMediaItem

        and: "import the content into the rhythmyx instance"

        rhythmyxIngestionService.importMediaItem(rhythmyxSubscription, content, 'Buttered Nail Clippers') >> 1234

        and: "attach the base subscription to the rhythmyx subscription"

        RhythmyxSubscription.count == 1
    }
}
