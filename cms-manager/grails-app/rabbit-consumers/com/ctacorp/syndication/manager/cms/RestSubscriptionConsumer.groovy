package com.ctacorp.syndication.manager.cms
import com.budjb.rabbitmq.MessageContext
import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumer
import org.apache.commons.logging.LogFactory

@SuppressWarnings("GroovyUnusedDeclaration")
class RestSubscriptionConsumer implements RabbitMqConsumer {

    static transacted = true

    static log = LogFactory.getLog(this)

    static rabbitConfig = [
            queue: "restUpdateQueue"
    ]

    def restSubscriptionConsumerService

    @Override
    void handleMessage(String message, MessageContext messageContext) {
        MqUtils.handleMessage(message, rabbitConfig.queue, restSubscriptionConsumerService)
    }
}
