/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.cache

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.media.MediaItem
import com.google.common.cache.CacheBuilder
import com.google.common.cache.Cache
import grails.util.Holders
import java.util.concurrent.Callable

import java.util.concurrent.TimeUnit

class GuavaCacheService {
    static transactional = false
    private final int apiResponseCacheSize = Holders.config.disableGuavaCache ? 0 : 5000
    private final int apiResponseCacheTimeout = 30 //minutes

    private final int extractedContentCacheSize = Holders.config.disableGuavaCache ? 0 : 1000
    private final int extractedContentCacheTimeout = 12 //hours

    private final int imageCacheSize = Holders.config.disableGuavaCache ? 0 : 500
    private final int imageCacheTimeout = 24 // hours

    private final int embedCacheSize = Holders.config.disableGuavaCache ? 0 : 500
    private final int embedCacheTimeout = 1 // hours

    def caches = [
            apiResponseCache: CacheBuilder.newBuilder().
                    expireAfterWrite(apiResponseCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(apiResponseCacheSize).
                    build(),
            extractedContentCache: CacheBuilder.newBuilder().
                    expireAfterWrite(extractedContentCacheTimeout, TimeUnit.HOURS).
                    maximumSize(extractedContentCacheSize).
                    build(),
            imageCache: CacheBuilder.newBuilder().
                    expireAfterWrite(imageCacheTimeout, TimeUnit.HOURS).
                    maximumSize(imageCacheSize).
                    build(),
            embedCache: CacheBuilder.newBuilder().
                    expireAfterWrite(embedCacheTimeout, TimeUnit.HOURS).
                    maximumSize(embedCacheSize).
                    build(),
    ]

    Cache getApiResponseCache(){//all
        caches.apiResponseCache
    }

    Cache getExtractedContentCache(){//by name
        caches.extractedContentCache
    }
    //new mediaitemupdateflushmethod
    Cache getImageCache(){//by name
        caches.imageCache
    }

    Cache getEmbedCache(){//by name
        caches.embedCache
    }

    def getExtractedContentCachesForId(def id, String url, Closure c) {
        def key = Hash.md5("${id}")

        Map map = extractedContentCache.get(key, new Callable<Map>() {
            @Override
            public Map call(){
                // "regenerating full map cache"
                return [:]
            }
        });
        return getIndividualCacheForId(id, url, map, c, "content")
    }

    def getImageCachesForId(long id, String url, Closure c) {
        def key = Hash.md5("${id}")

        Map map = imageCache.get(key, new Callable<Map>() {
            @Override
            public Map call(){
                // "regenerating full map cache"
                return [:]
            }
        });
        return getIndividualCacheForId(id, url, map, c, "image")
    }

    def getEmbedCachesForId(long id, String url, Closure c) {
        def key = Hash.md5("${id}")

        Map map = embedCache.get(key, new Callable<Map>() {
            @Override
            public Map call(){
                // "regenerating full map cache"
                return [:]
            }
        });
        return getIndividualCacheForId(id, url, map, c, "embed")
    }

    def getIndividualCacheForId(def id, String url, def map, Closure c, String cacheName) {
        def individualKey = Hash.md5(url)
        if(map."$individualKey" == null) {
            //execute closure to put the value inside the cached map
            map."$individualKey" = c()
            def key  = Hash.md5("${id}")
            putCache(cacheName, key, map)
        }
        return map?."$individualKey"
    }

    def putCache(String cacheName, def key, def map) {
        switch(cacheName) {
            case "response" : apiResponseCache.put(key,map)
                break;
            case "content" : extractedContentCache.put(key,map)
                break;
            case "image" : imageCache.put(key,map)
                break;
            case "embed" : embedCache.put(key,map)
                break;
            default: log.error("cache for ${cacheName} did not perform a put() correctly")
                break;
        }

    }

    boolean flushItem(String cacheName, String key){
        log.info "FLUSHING ITEM CACHE: ${key}"
        try{
            caches."$cacheName".invalidate(key)
            return true
        } catch(e){
            log.error(e)
            return false
        }
    }

    boolean flushCache(String cacheName){
        log.info "FLUSHING ENTIRE CACHE: ${cacheName}"
        try{
            caches."$cacheName".invalidateAll()
            return true
        }catch(e){
            log.error(e)
            return false
        }
        true
    }

    //Careful, this flushes **ALL** caches
    boolean flushAllCaches(){
        log.info "FLUSHING ALL CACHES"
        try {
            caches.each{ String name, Cache cache ->
                cache.invalidateAll()
            }
            return true
        }catch(e){
            log.error(e)
            return false
        }
        true
    }

    boolean flushCacheForMediaItemUpdate(Long mediaItemId) {
        boolean flushImages = flushItem("imageCache",Hash.md5("${mediaItemId}"))
        boolean flushExtractedContent = true
        boolean flushApiResponses = flushCache("apiResponseCache")
        if(MediaItem.get(mediaItemId)?.getClass()?.toString() == "class com.ctacorp.syndication.media.Html") {
            flushExtractedContent = flushItem("extractedContentCache",Hash.md5("${mediaItemId}"))
        }

        if(flushApiResponses && flushExtractedContent && flushImages) {
            return true
        }
        return false
    }
}