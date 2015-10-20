/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.commons.util.Util
import com.ctacorp.syndication.exception.InaccessibleVideoException
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Video
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import groovyx.net.http.URIBuilder
import org.joda.time.format.*
import org.joda.time.*

@Transactional(readOnly = true)
class YoutubeService {
    private final String youtubeDateFormat = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
    private RestBuilder rest = new RestBuilder()

    def getMetaDataForVideo(Long id) {
        MediaItem mi = MediaItem.get(id)
        if (mi?.getClass().simpleName == "Video") {
            rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
            return rest.get(getVideoFeedUrl(mi.sourceUrl)).json
        }
    }

    def getMetaDataForVideoUrl(String url) {
        log.info getVideoFeedUrl(url)
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        rest.get(getVideoFeedUrl(url)).json
    }

    String thumbnailLinkForUrl(String url) {
        String id = getVideoId(url)
        def json = getMetaDataForVideoUrl(url)
        def thumbnails = json.items[0].snippet.thumbnails
        if(thumbnails.maxres){
            return "http://i.ytimg.com/vi/${id}/maxresdefault.jpg"
        } else if(thumbnails.high){
            return "http://i.ytimg.com/vi/${id}/hqdefault.jpg"
        } else if(thumbnails.medium){
            return "http://i.ytimg.com/vi/${id}/mqdefault.jpg"
        } else{
            return "http://i.ytimg.com/vi/${id}/default.jpg"
        }
    }

    String getIframeEmbedCode(Long id, params = [:]) {
        MediaItem mi = MediaItem.get(id)
        if (mi?.getClass()?.simpleName == "Video") {
            return embedSnippet(getVideoId(mi.sourceUrl), params)
        }
        null
    }

    String getIframeEmbedCode(String url, params = [:]) {
        embedSnippet(getVideoId(url), params)
    }

    private String embedSnippet(String videoId, params){
        boolean autoplay = Util.isTrue(params.autoplay, true)
        boolean rel = Util.isTrue(params.rel)

        "<iframe id=\"ytplayer\" type=\"text/html\" width=\"640\" height=\"390\" src=\"https://www.youtube.com/embed/${videoId}?autoplay=${autoplay ? '1':'0'}&rel=${rel ? '1':'0'}&origin=http://syndication.hhs.gov\" frameborder=\"0\"></iframe>"
    }

    @Transactional
    def importYoutubeVideo(String url, Language language = null, Source source = null) {
        language = language ?: Language.findByIsoCode("eng")
        source = source ?: Source.findByAcronym("HHS")

        def video = getVideoInstanceFromUrl(url, language, source)

        video.save(flush: true)
    }

    def getVideoInstanceFromUrl(String url, Language language = null, Source source = null){
        def jsonData = getMetaDataForVideoUrl(url)
        if(!jsonData || jsonData.error || jsonData.items.size() == 0){
            if(jsonData?.error?.message == "Private video"){
                throw new InaccessibleVideoException("${jsonData?.error?.errors}")
                return null
            }

            log.error "Error getting info about video: ${url}, details: ${jsonData}"
            return null
        }
        new Video(
                name: jsonData.items[0].snippet.localized.title,
                sourceUrl: url,
                dateAuthored: Date.parse(youtubeDateFormat, jsonData.items[0].snippet.publishedAt),
                language: language,
                externalGuid: jsonData.items[0].id,
                source: source,
                duration: parseTime(jsonData.items[0].contentDetails.duration),
                description: jsonData.items[0].snippet.localized.description
        )
    }

    private parseTime(String timecode){
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        Period p = formatter.parsePeriod(timecode);
        Seconds s = p.toStandardSeconds();
        s.seconds
    }

    String getVideoId(String sourceUrl) {
        try {
            def url = new URIBuilder(sourceUrl)
            if(url?.query?.v){
                return url.query.v
            }
            def matcher = sourceUrl =~ /embed\/([\w\W&&[^\/]]+)/
            if(matcher.count) {
                return matcher[0][1]
            }
            println "Could not extract a youtube video ID from ${sourceUrl}"
            return null
        } catch(e){
            println "An unhandeled error has occured trying to retrieve video location from the provided url\n${e}"
            return null
        }
    }

    def grailsApplication
    private String getVideoFeedUrl(String sourceUrl) {
        String videoId = getVideoId(sourceUrl)
        String youtubeFeedUrl = "https://www.googleapis.com/youtube/v3/videos?id=${videoId}&key=${grailsApplication.config.google.youtube.apiKey}&part=snippet,contentDetails"
        youtubeFeedUrl
    }
}

