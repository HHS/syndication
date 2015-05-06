package com.ctacorp.syndication

import grails.util.Holders

class RemoteCacheService {
    static transactional = false

    def authorizationService

    def flushRemoteCacheByNameAndKey(String cacheName, String key){
        try{
            log.info "Flushing remote cache: ${cacheName} : ${key}"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCacheByName(String cacheName){
        try{
            log.info "Flushing remote cache"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByName?cacheName=${cacheName}")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }

    def flushRemoteCache() {
        try{
            log.info "Flushing remote cache"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByName")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }
}
