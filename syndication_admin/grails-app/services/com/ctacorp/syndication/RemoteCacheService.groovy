package com.ctacorp.syndication

import grails.transaction.Transactional
import grails.util.Holders

@Transactional
class RemoteCacheService {
    def authorizationService

    def flushRemoteCache() {
        try{
            log.info "Flushing remote cache"
            authorizationService.getRest(Holders.config.syndication.serverUrl + "/cacheAccess/flushCache")
        } catch(e){
            log.error("Could not flush API cache! ${e}")
        }
    }
}
