/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.cache.CachedContent
import grails.transaction.Transactional

@Transactional
class ContentCacheService {
    def contentRetrievalService

    CachedContent cache(MediaItem media){
        String content = contentRetrievalService.extractSyndicatedContent(media?.sourceUrl, [disableFailFast:true])
        cacheHelper(media, content)
    }

    CachedContent cache(MediaItem media, String content){
        cacheHelper(media, content)
    }

    CachedContent cacheHelper(MediaItem media, String content){
        CachedContent cached = CachedContent.findByMediaItem(media)
        if(!cached) {
            cached = new CachedContent(mediaItem: media, content: content)
        } else{
            cached.content = content
        }
        cached.save(flush:true)
        if(cached.hasErrors()){
            log.error(cached.errors)
        }
        cached
    }

    @Transactional(readOnly = true)
    CachedContent get(MediaItem media){
        CachedContent.findByMediaItem(media)
    }

    @Transactional(readOnly = true)
    CachedContent get(String url){
        CachedContent cached = CachedContent.where{
            mediaItem{
                sourceUrl == url
            }
        }.find()
        cached
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // The flush methods should be used very carefully - they will purge last known good states from items
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    void flush(MediaItem media){
        CachedContent cached = CachedContent.findByMediaItem(media)
        if(cached){
            CachedContent.where{
                id == cached.id
            }.deleteAll()
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // The flush methods should be used very carefully - they will purge last known good states from items
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    void flush(String url){
        def cached = CachedContent.where {
            mediaItem{
                sourceUrl == url
            }
        }.find()
        if(cached){
            CachedContent.where{
                id == cached.id
            }.deleteAll()
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // The flush methods should be used very carefully - they will purge last known good states from items
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    void flushAll(){
        CachedContent.where{
            id > 0L
        }.deleteAll()
    }
}
