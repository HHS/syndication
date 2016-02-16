package com.ctacorp.syndication.mq

import com.budjb.rabbitmq.publisher.RabbitMessagePublisher
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.jobs.DelayedNotificationJob
import com.ctacorp.syndication.media.MediaItem
import grails.util.Holders
import com.ctacorp.syndication.GenericQueueService

import javax.annotation.PostConstruct

class QueueService implements GenericQueueService{
    static transactional = false

    def remoteCacheService
    RabbitMessagePublisher rabbitMessagePublisher
    def grailsApplication

    String updateExchangeName

    @PostConstruct
    def init(){
        updateExchangeName = Holders.config.syndication.mq.updateExchangeName
    }

    void sendMessage(Message msg) {
        def message = msg.toJsonString()

        log.info("Sending message: ${message}")

        try {
            rabbitMessagePublisher.send {
                exchange = updateExchangeName
                body = message
            }
        } catch(e){
            log.error "A rabbitMQ error occurred on message ${msg.toJsonString()}"
            e.printStackTrace()
        }
    }

    void sendDelayedMessage(Message msg){
        log.info "Sending update message in 10 sec"
        DelayedNotificationJob.schedule(new Date(System.currentTimeMillis() + 10000), [msg: msg])
    }

    void flushCache(){
        remoteCacheService.flushRemoteCache()
    }

    void flushCacheForMediaItemUpdate(Long mediaItemId){
        remoteCacheService.flushCacheForMediaItemUpdate(mediaItemId)
    }
}