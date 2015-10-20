package com.ctacorp.syndication.manager.cms
import com.budjb.rabbitmq.consumer.MessageContext
import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumer
import org.apache.commons.logging.LogFactory

class EmailSubscriptionConsumer implements RabbitMqConsumer {

    static transacted = true

    static log = LogFactory.getLog(this)

    static rabbitConfig = [
            queue: "emailUpdateQueue"
    ]

    def emailSubscriptionConsumerService

    @Override
    void handleMessage(def message, MessageContext messageContext) {
        MqUtils.handleMessage(message, rabbitConfig.queue, emailSubscriptionConsumerService)
    }
}
