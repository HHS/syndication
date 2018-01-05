package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.commons.mq.MessageType

class RabbitDelayJob {

    def queueService

    @SuppressWarnings("GroovyAssignabilityCheck")
    def execute(context) {
        def subscription = context.mergedJobDataMap.get('subscription')
        def type = context.mergedJobDataMap.get('messageType')
        
        if(type == MessageType.IMPORT){
            switch(subscription){
                case EmailSubscription:
                    queueService.sendToEmailUpdateQueue(type, subscription.id)
                    break
                case RestSubscription:
                    queueService.sendToRestUpdateQueue(type, subscription.id)
                    break
                case RhythmyxSubscription:
                    queueService.sendToRhythmyxUpdateQueue(type, subscription.id)
                    break
            }
        }

        if(type == MessageType.DELETE){
            switch(subscription){
                case RestSubscription:
                    queueService.sendToRestErrorQueue(type, subscription.id, null, 0)
                    break
                case RhythmyxSubscription:
                    queueService.sendToRhythmyxErrorQueue(type, subscription.id, null, 0)
                    break
            }
        }
    }
}
