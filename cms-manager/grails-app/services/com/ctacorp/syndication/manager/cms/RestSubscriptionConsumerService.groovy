/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumerService

class RestSubscriptionConsumerService implements RabbitMqConsumerService {

    def restSubscriptionUpdateService
    def queueService

    void handleMessage(Message message, String rabbitQueue) {

        int attempts = message.meta?.attempts ?: 0

        String mediaId = message.mediaId
        MessageType messageType = message.messageType
        Long subscriptionId = MqUtils.getSubscriptionId(message)

        log.info("'${messageType.prettyName()}' message received on the '${rabbitQueue}' for mediaId '${mediaId}' and subscription '${subscriptionId}'")

        if (subscriptionId && messageType == MessageType.IMPORT) {
            importSubscription(subscriptionId, attempts)
        } else {

            if (subscriptionId) {

                def subscription = RestSubscription.findById(subscriptionId)
                if (!subscription) {
                    log.warn("Could not find restSubscription '${subscriptionId}'")
                    return
                }

                if (messageType == MessageType.UPDATE) {
                    updateSubscription(subscription, attempts)
                } else if (messageType == MessageType.DELETE) {
                    deleteSubscription(subscription, attempts)
                }

            } else if (mediaId) {
                queueSubscriptions(mediaId, messageType, rabbitQueue)
            } else {
                log.warn("Received an malformed message '${message.toJsonString()}' on the '${rabbitQueue}' queue")
            }
        }
    }

    void deleteSubscription(RestSubscription restSubscription, int attempts) {

        def success = restSubscriptionUpdateService.deleteSubscription(restSubscription)
        if(!success) {
            queueService.sendToRestErrorQueue(MessageType.DELETE, restSubscription.id, null, attempts)
        }
    }

    void queueSubscriptions(String mediaId, MessageType messageType, String rabbitQueue) {

        def subscription = Subscription.findByMediaId(mediaId)
        if (!subscription) {
            log.info("No subscriptions found for mediaId '${mediaId}'")
            return
        }

        def restSubscriptions = RestSubscription.findAllBySubscription(subscription)
        if (!restSubscriptions) {
            log.info("No restSubscriptions found for mediaId '${mediaId}'")
            return
        }

        restSubscriptions.each { restSubscription ->
            log.info("Queuing a message of type '${messageType}' on the '${rabbitQueue}' for restSubscription '${restSubscription.id}'")
            queueService.sendToRestUpdateQueue(messageType, restSubscription.id)
        }
    }

    void updateSubscription(RestSubscription restSubscription, int attempts) {

        def success = restSubscriptionUpdateService.updateSubscription(restSubscription)
        if (!success) {
            log.error("Update of restSubscription '${restSubscription.id}' was unsuccessful")
            queueService.sendToRestErrorQueue(MessageType.UPDATE, restSubscription.id, null, attempts)
        }
    }

    void importSubscription(long restSubscriptionId, int attempts) {

        def success = restSubscriptionUpdateService.importSubscription(restSubscriptionId)

        if (!success) {
            queueService.sendToRestErrorQueue(MessageType.IMPORT, restSubscriptionId, null, attempts)
        }
    }
}
