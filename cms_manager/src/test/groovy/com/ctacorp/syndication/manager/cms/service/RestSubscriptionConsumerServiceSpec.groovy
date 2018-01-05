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

@TestFor(RestSubscriptionConsumerService)
@Build([RestSubscription,Subscription])
class RestSubscriptionConsumerServiceSpec extends Specification {

    def restSubscriptionUpdateService = Mock(RestSubscriptionUpdateService)
    def queueService = Mock(QueueService)
    def queueName = 'someQueueName'

    def setup() {
        service.restSubscriptionUpdateService = restSubscriptionUpdateService
        service.queueService = queueService
    }

    void "correctly handle an uninteresting messageType"() {

        when: "handling a message with a missing messageType"

        service.handleMessage(new Message(messageType: MessageType.PUBLISH), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle an update message with a missing mediaId"() {

        when: "handling an update message with a missing mediaId"

        service.handleMessage(new Message(messageType: MessageType.UPDATE), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle a failed update"() {

        given: "a rest subscription"

        def restSubscription = RestSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, subscriptionId: '123', meta: [attempts: 2]), queueName)

        then: "fail the update"

        restSubscriptionUpdateService.updateSubscription(restSubscription) >> false

        and: "requeue the message"

        queueService.sendToRestErrorQueue(MessageType.UPDATE, restSubscription.id, null, 2)

        and: "don't throw the exception"

        noExceptionThrown()
    }

    void "correctly handle an import message with a missing subscriptionId"() {

        when: "handling an import message with a missing subscriptionId"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: null), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle an import message when the rest service fails"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123), queueName)

        then: "when an exception is thrown by the rest service"

        1 * restSubscriptionUpdateService.importSubscription(123) >> {
            throw new RuntimeException("boogers!")
        }

        and: "don't catch the exception"

        thrown(RuntimeException)
    }

    void "correctly handle an import message"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123), queueName)

        then: "when an exception is thrown by the rest service"

        1 * restSubscriptionUpdateService.importSubscription(123)

        and: "don't catch the exception"

        noExceptionThrown()
    }

    void "correctly handle a failed import"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123), queueName)

        then: "an exception is thrown by the rest service"

        1 * restSubscriptionUpdateService.importSubscription(123) >> false

        then: "send the message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, 123, null, 0)
    }

    void "correctly handle a second attempt failed import"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123, attempts: 1), queueName)

        then: "an exception is thrown by the rest service"

        1 * restSubscriptionUpdateService.importSubscription(123) >> false

        then: "send the message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, 123, null, 1)
    }

    void "correctly handle an update message"() {

        given: "a rest subscription"

        def restSubscription = RestSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, subscriptionId: '123', meta: [attempts: 2]), queueName)

        then: "update the rest subscription"

        restSubscriptionUpdateService.updateSubscription(restSubscription) >> false
    }

    void "correctly handle a delete"() {

        given: "a rest subscription"

        def restSubscription = RestSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.DELETE, subscriptionId: '123', meta: [attempts: 2]), queueName)

        then: "update the rest subscription"

        restSubscriptionUpdateService.deleteSubscription(restSubscription) >> false
    }

    void "correctly handle a failed delete"() {

        given: "a rest subscription"

        def restSubscription = RestSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.DELETE, subscriptionId: '123', meta: [attempts: 2]), queueName)

        then: "fail the update"

        restSubscriptionUpdateService.deleteSubscription(restSubscription) >> false

        and: "requeue the message"

        queueService.sendToRestErrorQueue(MessageType.DELETE, restSubscription.id, null, 2)

        and: "don't throw the exception"

        noExceptionThrown()
    }

    void "handle message creates requeues individual update subscriptions"() {

        given: "an email subscription instance"

        def subscription = Subscription.build(mediaId: '123456')

        def restSubscription1 = RestSubscription.build(subscription: subscription)
        def restSubscription2 = RestSubscription.build(subscription: subscription)

        when: "sending a message to the consumer with an update the message type a media id"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: '123456'), queueName)

        then: "requeue each individual subscription found"

        1 * queueService.sendToRestUpdateQueue(MessageType.UPDATE, restSubscription1.id)
        1 * queueService.sendToRestUpdateQueue(MessageType.UPDATE, restSubscription2.id)

        and: "expect no exceptions to be thrown"

        noExceptionThrown()
    }
}
