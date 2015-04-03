/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.budjb.rabbitmq.RabbitMessageBuilder
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.utils.mq.SubscriptionType
import grails.transaction.Transactional
import grails.util.Holders
import org.apache.commons.lang.RandomStringUtils

import javax.annotation.PostConstruct

@Transactional
class QueueService {

    Integer maxAttempts

    def rabbitSender = new RabbitSender()

    @PostConstruct
    def init() {
        maxAttempts = Holders.config.errorQueueRetryPolicy?.maxAttempts ?: 1
    }

    void sendToRhythmyxErrorQueue(MessageType messageType, long subscriptionId, String mediaId, int attempts) {
        sendToErrorQueue(messageType, subscriptionId, mediaId, 'rhythmyxErrorDelayQueue', attempts, SubscriptionType.RHYTHMYX)
    }

    void sendToEmailErrorQueue(MessageType messageType, long subscriptionId, String mediaId, int attempts) {
        sendToErrorQueue(messageType, subscriptionId, mediaId, 'emailErrorDelayQueue', attempts, SubscriptionType.EMAIL)
    }

    void sendToRestErrorQueue(MessageType messageType, long subscriptionId, String mediaId, int attempts) {
        sendToErrorQueue(messageType, subscriptionId, mediaId, 'restErrorDelayQueue', attempts, SubscriptionType.REST)
    }

    void sendToRhythmyxUpdateQueue(MessageType messageType, long subscriptionId) {
        sendToUpdateQueue(messageType, subscriptionId, 'rhythmyxUpdateQueue')
    }

    void sendToEmailUpdateQueue(MessageType messageType, long subscriptionId) {
        sendToUpdateQueue(messageType, subscriptionId, 'emailUpdateQueue')
    }

    void sendToRestUpdateQueue(MessageType messageType, long subscriptionId) {
        sendToUpdateQueue(messageType, subscriptionId, 'restUpdateQueue')
    }

    void sendToUpdateQueue(MessageType messageType, long subscriptionId, String queueName) {
        sendToQueue(queueName, new Message(messageType: messageType, subscriptionId: subscriptionId).toJsonString())
    }

    void sendToErrorQueue(MessageType messageType, long subscriptionId, String mediaId, String queueName, int attempts, SubscriptionType subscriptionType) {

        def message = new Message(messageType: messageType, subscriptionId: subscriptionId, mediaId: mediaId, meta: [attempts: ++attempts])
        def jsonString = message.toJsonString()

        if(attempts > maxAttempts) {

            def errorCode = RandomStringUtils.randomAlphabetic(10).toUpperCase()

            log.error("Gave up requeuing the message: \n${jsonString} on the '${queueName}'")
            log.error("Failure log ID: ${errorCode}")

            def cleanup = { domainInstance ->
                handleOtherWiseHorribleErrors(messageType, domainInstance, subscriptionType, attempts, errorCode)
            }

            switch (subscriptionType) {

                case SubscriptionType.RHYTHMYX:
                    def rhythmyxSubscription = RhythmyxSubscription.findById(subscriptionId)
                    cleanup(rhythmyxSubscription)
                    break

                case SubscriptionType.REST:
                    def restSubscription = RestSubscription.findById(subscriptionId)
                    if(restSubscription) {
                        cleanup(restSubscription)
                    }
                    break

                case SubscriptionType.EMAIL:
                    def emailSubscription = EmailSubscription.findById(subscriptionId)
                    if(emailSubscription) {
                        cleanup(emailSubscription)
                    }
                    break

                default:
                    break
            }

        } else {
            if(attempts > 1) {
                log.warn("Requeuing the message: \n${jsonString} on the '${queueName}'")
            }
            sendToQueue(queueName, jsonString)
        }
    }

    void handleOtherWiseHorribleErrors(MessageType messageType, domainInstance, SubscriptionType subscriptionType, int attempts, String errorCode) {

        try {
            if (messageType == messageType.DELETE) {
                log.warn("Forcefully deleting '${subscriptionType}' subscription '${domainInstance?.id}' because ${attempts - 1} attempts have been made to delete it normally.")
                domainInstance.delete(flush: true)
            } else {
                domainInstance.deliveryFailureLogId = errorCode
                domainInstance.save(flush: true)
            }
        } catch (ignored) {
            log.warn("Could not save '${subscriptionType}' subscription '${domainInstance?.id}'. Perhaps it was deleted.")
        }
    }

    void sendToQueue(String queueName, String message) {

        def exception = rabbitSender.sendMessage(queueName, message)

        if(exception) {
            log.error("Error occured when trying to send a message to the '${queueName}'", exception)
        }
    }

    static class RabbitSender {

        @SuppressWarnings("GrMethodMayBeStatic")
        def sendMessage(String queueName, String message) {
            try {
                new RabbitMessageBuilder().send {
                    routingKey = queueName
                    body = message
                }
                null
            } catch (t) {
                return t
            }
        }
    }
}
