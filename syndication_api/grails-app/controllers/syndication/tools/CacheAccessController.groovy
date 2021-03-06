/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.tools

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class CacheAccessController {
    def guavaCacheService

    def index(){

    }

    def flushCache(){
        log.info("Flushing Guava Cache by Request")
        boolean successful = guavaCacheService.flushAllCaches()
        if(successful) {
            render text: [msg: "Cache Flushed Successfully."] as JSON, status: 200, contentType: "application/json"
        } else{
            long time = System.nanoTime()
            log.error("Cache flush failed: ${time}")
            render text: [msg: "There was an error flushing the cache, error code ${time}"] as JSON, status: 500, contentType: "application/json"
        }
    }
    
    def flushCacheByName(){
        log.info("Flushing Guava Cache for " + params.cacheName)
        boolean successful = guavaCacheService.flushCache(params.cacheName)
        if(successful) {
            render text: [msg: "Cache Flushed Successfully."] as JSON, status: 200, contentType: "application/json"
        } else{
            long time = System.nanoTime()
            log.error("Cache flush failed: ${time}")
            render text: [msg: "There was an error flushing the cache, error code ${time}"] as JSON, status: 500, contentType: "application/json"
        }
    }

    def flushCacheByNameAndKey(){
        log.info("Flushing Guava Cache for " + params.cacheName + " : " + params.key)
        boolean successful = guavaCacheService.flushItem(params.cacheName, params.key)
        if(successful) {
            render text: [msg: "Cache Flushed Successfully."] as JSON, status: 200, contentType: "application/json"
        } else{
            long time = System.nanoTime()
            log.error("Cache flush failed: ${time}")
            render text: [msg: "There was an error flushing the cache, error code ${time}"] as JSON, status: 500, contentType: "application/json"
        }
    }

    def flushCacheForMediaItemUpdate(){
        log.info("Flushing Guava Cache by Request")
        boolean successful = guavaCacheService.flushCacheForMediaItemUpdate(params.long("mediaItemId"))
        if(successful) {
            render text: [msg: "Cache Flushed Successfully."] as JSON, status: 200, contentType: "application/json"
        } else{
            long time = System.nanoTime()
            log.error("Cache flush failed: ${time}")
            render text: [msg: "There was an error flushing the cache for a media item update, error code ${time}"] as JSON, status: 500, contentType: "application/json"
        }
    }

}