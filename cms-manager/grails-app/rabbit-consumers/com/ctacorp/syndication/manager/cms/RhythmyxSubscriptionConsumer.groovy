package com.ctacorp.syndication.manager.cms
import com.budjb.rabbitmq.MessageContext
import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumer
import org.apache.commons.logging.LogFactory

@SuppressWarnings("GroovyUnusedDeclaration")
class RhythmyxSubscriptionConsumer implements RabbitMqConsumer {

    static transacted = true

    static log = LogFactory.getLog(this)

    static rabbitConfig = [
            queue: "rhythmyxUpdateQueue"
    ]

    def rhythmyxSubscriptionConsumerService

    @Override
    void handleMessage(String message, MessageContext messageContext) {
        MqUtils.handleMessage(message, rabbitConfig.queue, rhythmyxSubscriptionConsumerService)
    }
}
