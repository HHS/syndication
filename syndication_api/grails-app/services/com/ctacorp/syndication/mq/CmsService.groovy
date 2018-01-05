package com.ctacorp.syndication.mq

import com.ctacorp.syndication.GenericQueueService
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.jobs.DelayedNotificationJob
import grails.converters.JSON
import grails.transaction.Transactional
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
//        authorizationService.post(([messageType:msg.messageType, mediaId:msg.mediaId] as JSON).toString(), config?.CMSMANAGER_SERVER_URL + "/updateExchange/emailUpdate", true)
//        authorizationService.post(([messageType:msg.messageType, mediaId:msg.mediaId] as JSON).toString(), config?.CMSMANAGER_SERVER_URL + "/updateExchange/rhythmyxUpdate", true)
        authorizationService.post(([messageType:[enumType:msg.messageType.class,name:msg.messageType.prettyName()], mediaId:msg.mediaId] as JSON).toString(), config?.CMSMANAGER_SERVER_URL + "/updateExchange/restUpdate")
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
