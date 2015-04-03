
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
import com.ctacorp.syndication.manager.cms.QueueService
import com.ctacorp.syndication.manager.cms.RhythmyxSubscription
import com.ctacorp.syndication.manager.cms.Subscription
import com.ctacorp.syndication.manager.cms.utils.mq.SubscriptionType
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(QueueService)
@Build([RhythmyxSubscription, Subscription])
class QueueServiceSpec extends Specification {

    def rabbitSender = Mock(QueueService.RabbitSender)

    RhythmyxSubscription rhythmyxSubscription
    Subscription subscription

    def setup() {

        service.rabbitSender = rabbitSender

        service.maxAttempts = 2

        subscription = Subscription.build()
        rhythmyxSubscription = RhythmyxSubscription.build(subscription: subscription)
    }

    void "send to the rhythmyx error queue"() {

        given: "the message type, subscription id and media id"

        def messageType = MessageType.UPDATE
        def subscriptionId = 12345
        def mediaId = "2"

        and: "the expected json string to send to the queue"

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId, mediaId: mediaId).toJsonString()

        when: "pushing the message to the error queue"

        service.sendToRhythmyxErrorQueue(messageType, subscriptionId, mediaId, 0)

        then: "convert and send the message via rabbitmq"

        rabbitSender.sendMessage('rhythmyxErrorQueue', message) >> null
    }

    void "send to the email error queue"() {

        given: "the message type, subscription id and media id"

        def messageType = MessageType.UPDATE
        def subscriptionId = 12345
        def mediaId = "2"

        and: "the expected json string to send to the queue"

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId, mediaId: mediaId).toJsonString()

        when: "pushing the message to the error queue"

        service.sendToEmailErrorQueue(messageType, subscriptionId, mediaId, 0)

        then: "convert and send the message via rabbitmq"

        rabbitSender.sendMessage('emailErrorQueue', message) >> null
    }

    void "send to the rhythmyx update queue"() {

        given: "the message type and subscription id"

        def messageType = MessageType.IMPORT
        def subscriptionId = 12345

        and: "the expected json string to send to the queue"

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId).toJsonString()

        when: "pushing the message to the rhythmyx update queue"

        service.sendToRhythmyxUpdateQueue(messageType, subscriptionId)

        then: "convert and send the message via rabbitmq"

        rabbitSender.sendMessage('rhythmyxUpdateQueue', message) >> null
    }

    void "send to the email update queue"() {

        given: "the message type and subscription id"

        def messageType = MessageType.IMPORT
        def subscriptionId = 12345

        and: "the expected json string to send to the queue"

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId).toJsonString()

        when: "pushing the message to the rhythmyx update queue"

        service.sendToEmailUpdateQueue(messageType, subscriptionId)

        then: "convert and send the message via rabbitmq"

        rabbitSender.sendMessage('emailUpdateQueue', message) >> null
    }

    void "send to when the rabbit service throws an exception"() {

        given: "the message type and subscription id"

        def messageType = MessageType.IMPORT
        def subscriptionId = 12345

        and: "the expected json string to send to the queue"

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId).toJsonString()

        when: "pushing the message to the rhythmyx update queue"

        service.sendToErrorQueue(messageType, subscriptionId, null, 'nutjobQueue', 1, SubscriptionType.RHYTHMYX)

        then: "don't send the message to the error queue"

        rabbitSender.sendMessage('emailUpdateQueue', message) >> new Exception("bologna hammies")

        and: "no exception should be thrown"

        noExceptionThrown()
    }

    void "send to rhythmyx error queue aborts if max attempts has been reached"() {

        given: "the message type, subscription id, media id"

        def messageType = MessageType.IMPORT
        def mediaId = "98765"

        when: "sending a message to the rhythmyx error queue"

        service.sendToRhythmyxErrorQueue(messageType, rhythmyxSubscription.subscriptionId, mediaId, 2)

        then: "then save the delivery failure log id"

        rhythmyxSubscription.deliveryFailureLogId != null
    }
}
