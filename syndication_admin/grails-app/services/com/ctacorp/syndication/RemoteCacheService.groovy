package com.ctacorp.syndication

import grails.util.Holders

class RemoteCacheService {
    static transactional = false

    def authorizationService

    def flushRemoteCacheByNameAndKey(String cacheName, String key){
        try{
            log.info "Flushing remote cache: ${cacheName} : ${key} :" + Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCacheByName(String cacheName){
        try{
            log.info "Flushing remote cache: ${Holders.config.syndication.serverUrl}/cacheAccess/flushCacheByName?cacheName=${cacheName}"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByName?cacheName=${cacheName}")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCache() {
        try{
            log.info "Flushing remote cache: " + Holders.config.syndication.serverUrl + "/cacheAccess/flushCache"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCache")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushCacheForMediaItemUpdate(Long mediaItemId) {
        try{
            log.info "Flushing remote cache for a MediaItem update"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheForMediaItemUpdate?mediaItemId=${mediaItemId}")
        } catch(e){
            log.error("Could not flush all caches for the media item update! ${e}")
        }
    }
}
