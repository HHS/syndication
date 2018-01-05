package com.ctacorp.syndication.mq

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.jobs.DelayedNotificationJob
import grails.transaction.Transactional
import com.ctacorp.syndication.GenericQueueService
import grails.util.Holders

import javax.annotation.PostConstruct

@Transactional
class CmsService implements GenericQueueService {

    static transactional = false

    def remoteCacheService
    def grailsApplication
    def authorizationService
    def config = Holders.config

    String updateExchangeName

    @PostConstruct
    def init(){
        updateExchangeName = config.SYNDICATION_MQ_UPDATE_EXCHANGENAME
    }

    void sendMessage(Message msg) {
        authorizationService.post(config?.CMSMANAGER_SERVER_URL + "/updateExchange/emailUpdate",[messageType:msg.messageType, mediaId:msg.mediaId])
        authorizationService.post(config?.CMSMANAGER_SERVER_URL + "/updateExchange/rhythmyxUpdate",[messageType:msg.messageType, mediaId:msg.mediaId])
        authorizationService.post(config?.CMSMANAGER_SERVER_URL + "/updateExchange/restUpdate",[messageType:msg.messageType, mediaId:msg.mediaId])
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
