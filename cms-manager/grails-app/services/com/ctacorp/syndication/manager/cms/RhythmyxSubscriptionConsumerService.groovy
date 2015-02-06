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

@SuppressWarnings("GroovyUnusedDeclaration")
class RhythmyxSubscriptionConsumerService implements RabbitMqConsumerService {

    def rhythmyxSubscriptionUpdateService
    def rhythmyxSubscriptionImportService
    def rhythmyxSubscriptionTransitionService
    def queueService
    def subscriptionService

    void handleMessage(Message message, String rabbitQueue) {

        int attempts = message.meta?.attempts ?: 0

        String mediaId = message.mediaId
        MessageType messageType = message.messageType
        Long rhythmyxSubscriptionId = MqUtils.getSubscriptionId(message)

        if (rhythmyxSubscriptionId && messageType == MessageType.IMPORT) {

            log.info("Received an import message on the '${rabbitQueue}' for rhythmyxSubscription '${rhythmyxSubscriptionId}'")

            def success = rhythmyxSubscriptionImportService.importRhythmyxSubscription(rhythmyxSubscriptionId)
            if (!success) {

                log.error("Import of rhythmyxSubscription '${rhythmyxSubscriptionId}' was unsuccessful")
                queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, rhythmyxSubscriptionId, mediaId, attempts)

            } else {

                def rhythmyxSubscription = RhythmyxSubscription.findById(rhythmyxSubscriptionId)
                if(rhythmyxSubscription) {
                    rhythmyxSubscriptionTransitionService.doImportTransitions(rhythmyxSubscription)
                }
            }

        } else if (mediaId && messageType == MessageType.UPDATE) {

            log.info("Received an update message on the '${rabbitQueue}' for mediaId '${mediaId}'")

            def subscriptions = rhythmyxSubscriptionUpdateService.updateRhythmyxSubscriptions(mediaId)

            List<RhythmyxSubscription> successfulUpdates = subscriptions.successfullUpdates
            List<RhythmyxSubscription> failedUpdates = subscriptions.failedUpdates

            successfulUpdates.each { rhythmyxSubscription ->
                rhythmyxSubscription.deliveryFailureLogId = null
                rhythmyxSubscription.save(flush: true)
            }

            rhythmyxSubscriptionTransitionService.doUpdateTransitions(successfulUpdates)

            failedUpdates.each { RhythmyxSubscription rhythmyxSubscription ->
                queueService.sendToRhythmyxErrorQueue(MessageType.UPDATE, rhythmyxSubscription.id, mediaId, attempts)
            }

        } else if (mediaId && messageType == MessageType.DELETE) {

            log.info("Received a delete message on the '${rabbitQueue}' for mediaId '${mediaId}'")

            def subscription = Subscription.findByMediaId(mediaId)
            def existingSubscriptions = RhythmyxSubscription.findAllBySubscription(subscription)

            rhythmyxSubscriptionTransitionService.doDeleteTransitions(existingSubscriptions)

            existingSubscriptions.each {
                subscriptionService.deleteChildSubscription(it)
            }

        } else {
            log.warn("Received an malformed message '${message.toJsonString()}' on the '${rabbitQueue}' queue")
        }
    }
}