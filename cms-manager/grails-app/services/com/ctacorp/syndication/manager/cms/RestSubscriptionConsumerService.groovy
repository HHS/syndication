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

        def mediaId = message.mediaId as String
        def messageType = message.messageType
        def subscriptionId = MqUtils.getSubscriptionId(message)

        if(mediaId && messageType == MessageType.UPDATE) {

            log.info("Received an update message on the '${rabbitQueue}' queue for mediaId '${mediaId}'")

            def failedUpdates = restSubscriptionUpdateService.updateSubscriptions(mediaId)
            failedUpdates.each { restSubscription ->
                queueService.sendToRestErrorQueue(MessageType.UPDATE, restSubscription.id as Long, mediaId, attempts)
            }

        } else if (subscriptionId && messageType == MessageType.IMPORT) {

            log.info("Received a create message on the '${rabbitQueue}' queue for subscription '${subscriptionId}'")

            if(!restSubscriptionUpdateService.importSubscription(subscriptionId)) {
                queueService.sendToRestErrorQueue(MessageType.IMPORT, subscriptionId, mediaId, attempts)
            } else{
                def restSub = RestSubscription.get(subscriptionId)
                if(!restSub) {
                    log.warn("Could not clear 'is pending' status on rest subscription '${subscriptionId}' because it could not be found")
                } else {
                    restSub.isPending = false
                    restSub.save(flush: true)
                }
            }

        } else if (mediaId && messageType == MessageType.DELETE) {

            log.info("Received a delete message on the '${rabbitQueue}' for mediaId '${mediaId}'")

            def failedUpdates = restSubscriptionUpdateService.deleteSubscriptions(mediaId)
            failedUpdates.each { RestSubscription restSubscription ->
                queueService.sendToRestErrorQueue(MessageType.DELETE, restSubscription.id as Long, mediaId, attempts)
            }

        } else {
            log.warn("Received an malformed message '${message.toJsonString()}' on the '${rabbitQueue}' queue")
        }
    }
}
