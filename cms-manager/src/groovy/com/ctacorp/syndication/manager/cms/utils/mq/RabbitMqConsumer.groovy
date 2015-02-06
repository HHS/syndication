package com.ctacorp.syndication.manager.cms.utils.mq

import com.budjb.rabbitmq.MessageContext

interface RabbitMqConsumer {
    void handleMessage(String message, MessageContext messageContext)
}