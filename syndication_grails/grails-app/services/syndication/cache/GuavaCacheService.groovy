/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.cache

import com.google.common.cache.CacheBuilder
import com.google.common.cache.Cache

import java.util.concurrent.TimeUnit

class GuavaCacheService {
    static transactional = false
    private final int apiResponseCacheSize = 10000
    private final int apiResponseCacheTimeout = 1

    private final int extractedContentCacheSize = 200
    private final int extractedContentCacheTimeout = 30

    def caches = [
            apiResponseCache: CacheBuilder.newBuilder().
                    expireAfterWrite(apiResponseCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(apiResponseCacheSize).
                    build(),
            extractedContentCache:CacheBuilder.newBuilder().
                    expireAfterWrite(extractedContentCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(extractedContentCacheSize).
                    build()
    ]

    Cache getApiResponseCache(){
        caches.apiResponseCache
    }

    Cache getExtractedContentCache(){
        caches.apiResponseCache
    }

    void flushCache(String cacheName){
        caches."$cacheName".invalidateAll()
    }

    //Careful, this flushes **ALL** caches
    boolean flushAllCaches(){
        try {
            caches.each{ String name, Cache cache ->
                cache.invalidateAll()
            }
        }catch(e){
            log.error(e)
            return false
        }
        true
    }
}
