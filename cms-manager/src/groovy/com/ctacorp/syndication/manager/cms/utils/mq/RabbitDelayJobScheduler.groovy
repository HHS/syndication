package com.ctacorp.syndication.manager.cms.utils.mq

import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.RabbitDelayJob

class RabbitDelayJobScheduler {

    @SuppressWarnings("GrMethodMayBeStatic")
    def schedule(MessageType messageType, subscription) {
        RabbitDelayJob.schedule(new Date(System.currentTimeMillis() + 5000), [messageType: messageType, subscription: subscription])
    }
}
