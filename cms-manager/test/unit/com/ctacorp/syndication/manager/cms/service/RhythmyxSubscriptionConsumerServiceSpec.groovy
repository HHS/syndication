/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(RhythmyxSubscriptionConsumerService)
@Build([RhythmyxSubscription,Subscription])
class RhythmyxSubscriptionConsumerServiceSpec extends Specification {

    def rhythmyxSubscriptionUpdateService = Mock(RhythmyxSubscriptionUpdateService)
    def rhythmyxSubscriptionImportService = Mock(RhythmyxSubscriptionImportService)
    def rhythmyxSubscriptionTransitionService = Mock(RhythmyxSubscriptionTransitionService)
    def queueService = Mock(QueueService)
    def subscriptionService = Mock(SubscriptionService)

    def rhythmyxSubscription1
    def rhythmyxSubscription2
    def subscription

    def queueName = 'someQueueName'

    def setup() {

        service.rhythmyxSubscriptionUpdateService = rhythmyxSubscriptionUpdateService
        service.rhythmyxSubscriptionImportService = rhythmyxSubscriptionImportService
        service.rhythmyxSubscriptionTransitionService = rhythmyxSubscriptionTransitionService
        service.queueService = queueService
        service.subscriptionService = subscriptionService

        subscription = Subscription.build()
        rhythmyxSubscription1 = RhythmyxSubscription.build(subscription: subscription)
        rhythmyxSubscription2 = RhythmyxSubscription.build(subscription: subscription)
    }

    void "handle import message fails gracefully when missing the subscription id"() {

        when: "sending an import message to the consumer without the subscription id"

        service.handleMessage(new Message(messageType:MessageType.IMPORT), queueName)

        then: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle message fails gracefully when the message type is uninteresting"() {

        when: "sending a message to the consumer without the message type"

        service.handleMessage(new Message(messageType: MessageType.PUBLISH, subscriptionId:'123456'), queueName)

        then: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle import message successfully imports and transitions a rhythmyx subscription"() {

        given: "a valid rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build()

        when: "sending a valid import message to the consumer"

        service.handleMessage(new Message(subscriptionId:"${rhythmyxSubscription.id}", messageType:MessageType.IMPORT), queueName)

        then: "import the media item using the rhythmyx import service"

        1 * rhythmyxSubscriptionImportService.importRhythmyxSubscription(rhythmyxSubscription.id) >> rhythmyxSubscription

        and: "transition the rhythmyx subscription"

        1 * rhythmyxSubscriptionTransitionService.doImportTransitions(rhythmyxSubscription)
    }

    void "handle import message correctly handles a failed import message"() {

        when: "sending a valid import message to the consumer"

        service.handleMessage(new Message(subscriptionId:'123456', messageType:MessageType.IMPORT), queueName)

        then: "attempt to import the media item using the rhythmyx import service"

        1 * rhythmyxSubscriptionImportService.importRhythmyxSubscription(123456) >> null

        and: "send the message to the rhythmyx error queue"

        1 * queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, 123456, null, 0)

        and: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle import message fails gracefully when the subscription id is not a long"() {

        when: "sending a valid import message to the consumer"

        service.handleMessage(new Message(subscriptionId:'sdfdsfdf', messageType:MessageType.IMPORT), queueName)

        then: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle update message fails gracefully when missing the media id"() {

        when: "sending an update message to the consumer without the media id"

        service.handleMessage(new Message(messageType:MessageType.UPDATE), queueName)

        then: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle update message successfully updates and transitions a rhythmyx subscription"() {

        given: "a valid rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build(deliveryFailureLogId: "DSADASDASDDAS")

        when: "sending a valid update message to the consumer"

        service.handleMessage(new Message(mediaId:"${rhythmyxSubscription.id}", messageType:MessageType.UPDATE), queueName)

        then: "update the media item using the rhythmyx import service"

        1 * rhythmyxSubscriptionUpdateService.updateRhythmyxSubscriptions("${rhythmyxSubscription.id}") >> [successfullUpdates: [rhythmyxSubscription]]

        and: "transition the rhythmyx subscriptions"

        1 * rhythmyxSubscriptionTransitionService.doUpdateTransitions([rhythmyxSubscription])

        and: "the rhythmyx subscription's delivery failure log id should be set to null"

        rhythmyxSubscription.deliveryFailureLogId == null
    }

    void "handle update message correctly handles a failed update message"() {

        given: "a valid rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build()

        when: "sending a valid update message to the consumer"

        service.handleMessage(new Message(mediaId:"${rhythmyxSubscription.id}", messageType:MessageType.UPDATE), queueName)

        then: "attempt to update the media item using the rhythmyx import service"

        1 * rhythmyxSubscriptionUpdateService.updateRhythmyxSubscriptions("${rhythmyxSubscription.id}") >> [successfullUpdates: []]

        and: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle delete message fails gracefully when missing the media id"() {

        when: "sending a delete message to the consumer without the media id"

        service.handleMessage(new Message(messageType:MessageType.DELETE), queueName)

        then: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle delete message successfully transitions a rhythmyx subscription"() {

        given: "a valid rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build()

        when: "sending a valid delete message to the consumer"

        service.handleMessage(new Message(mediaId:'123456', messageType:MessageType.DELETE), queueName)

        then: "transition the rhythmyx subscriptions"

        1 * rhythmyxSubscriptionTransitionService.doDeleteTransitions([rhythmyxSubscription])

        then: "delete the subscription"

        1 * subscriptionService.deleteChildSubscription(rhythmyxSubscription)
    }
}
