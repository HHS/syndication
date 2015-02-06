package com.ctacorp.syndication.manager.cms.utils.mq

import com.ctacorp.syndication.commons.mq.Message

interface RabbitMqConsumerService {
    void handleMessage(Message message, String queueName)
}
