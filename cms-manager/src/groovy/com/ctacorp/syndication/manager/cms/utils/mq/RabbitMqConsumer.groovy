package com.ctacorp.syndication.manager.cms.utils.mq

import com.budjb.rabbitmq.consumer.MessageContext

interface RabbitMqConsumer {
    void handleMessage(def message, MessageContext messageContext)
}