package com.ctacorp.syndication.mq
import com.budjb.rabbitmq.RabbitMessageBuilder
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.jobs.DelayedNotificationJob
import grails.util.Holders
import com.ctacorp.syndication.GenericQueueService

import javax.annotation.PostConstruct

class QueueService implements GenericQueueService{
    static transactional = false
    String updateExchangeName
    def remoteCacheService

    @PostConstruct
    def init(){
        updateExchangeName = Holders.config.syndication.mq.updateExchangeName
        log.info "MQ Exchange inited"
    }

    void sendMessage(Message msg) {
        def message = msg.toJsonString()

        log.info("Sending message: ${message}")

        try {
            new RabbitMessageBuilder().send {
                exchange = updateExchangeName
                body = message
            }
        } catch(e){
            //Something is wrong with the initial MQ connection, and it barfs on the first
            //connection... but then works fine after that.
            log.error "A rabbitMQ error occurred on message ${msg.toJsonString()}"
        }
    }

    void sendDelayedMessage(Message msg){
        log.info "Sending update message in 10 sec: ${msg}"
        DelayedNotificationJob.schedule(new Date(System.currentTimeMillis() + 10000), [msg: msg])
    }

    void flushCache(){
        remoteCacheService.flushRemoteCache()
    }
}