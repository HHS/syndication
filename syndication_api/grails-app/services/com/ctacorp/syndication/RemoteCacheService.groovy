package com.ctacorp.syndication

import grails.util.Holders

class RemoteCacheService {
    static transactional = false

    def authorizationService
    def config = Holders.config

    def flushRemoteCacheByNameAndKey(String cacheName, String key){

        try{
            log.info "Flushing remote cache: ${cacheName} : ${key} :" + config?.API_SERVER_URL + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}"
            authorizationService.getRest(config?.API_SERVER_URL + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}")
        } catch(e){
           log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCacheByName(String cacheName){
        try{
            log.info "Flushing remote cache: ${config?.API_SERVER_URL}/cacheAccess/flushCacheByName?cacheName=${cacheName}"
            authorizationService.getRest(config?.API_SERVER_URL + "/cacheAccess/flushCacheByName?cacheName=${cacheName}")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCache() {
        try{
            log.info "Flushing remote cache: " + config?.API_SERVER_URL + "/cacheAccess/flushCache"
            authorizationService.getRest(config?.API_SERVER_URL + "/cacheAccess/flushCache")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushCacheForMediaItemUpdate(Long mediaItemId) {
        try{
            log.info "Flushing remote cache for a MediaItem update"
            authorizationService.getRest(config?.API_SERVER_URL + "/cacheAccess/flushCacheForMediaItemUpdate?mediaItemId=${mediaItemId}")
        } catch(e){
            log.error("Could not flush all caches for the media item update! ${e}")
        }
    }
}
