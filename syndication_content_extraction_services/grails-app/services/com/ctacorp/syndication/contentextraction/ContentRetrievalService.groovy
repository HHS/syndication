/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.exception.ContentUnretrievableException

import java.util.regex.Matcher
import java.util.regex.Pattern

class ContentRetrievalService {
    static transactional = false

    def webUtilService
    def jsoupWrapperService

    String extractSyndicatedContent(String url, params = [:]) throws ContentUnretrievableException{
        if(url.endsWith("/")){
            url = url[0..-2]
        }
        params.newUrlBase = removeSuffix(url)

        String content = null
        try{
            content = webUtilService.getPage(url)
            return wrap(jsoupWrapperService.extract(content, params), params)
        } catch(ContentUnretrievableException e){
            log.error("Tried to extract content from a bad URL: ${url}\nError: ${e}")
            throw e
        }
        null
    }

    Map getContentAndMd5Hashcode(String url, params = [:]){
        String extracted = extractSyndicatedContent(url, params)
        [content:extracted, hash:Hash.md5(extracted)]
    }

    String getDescriptionFromContent(String content, String sourceUrl){
        String desc = jsoupWrapperService.getMetaDescription(sourceUrl)
        if(!desc) {
            desc = jsoupWrapperService.getDescriptionFromContent(content, 3)
        }
        desc
    }

    String wrapWithSyndicateDiv(String content){
        "<div class='syndicate'>${content}</div>"
    }

    String addAttributionToExtractedContent(Long mediaId, String content){
        MediaItem mi = MediaItem.get(mediaId)
        Source src = mi.source

        if(!mi || !src){
            return content
        }

        String attr = "" +
            "<div class='syndicate'>" +
            "<span><Strong>Syndicated Content Details:</strong></span><br/>" +
            "<span>Source URL: <a href='${mi.sourceUrl}'>${mi.sourceUrl}</a></span><br/>" +
            "<span>Source Agency: <a href='${src.websiteUrl}'>${src.name} (${src.acronym})</a></span><br/>" +
            "<span>Captured Date: ${mi.dateSyndicationCaptured}</span><br/>" +
            "</div>"
        content + attr
    }

    private String wrap(String content, params){
        StringBuilder sb = new StringBuilder()
        if(params.jsonP){
            content.eachLine(){
                sb.append("document.write('" + it.trim().replace("'","’") + "');\n")
            }
            return sb.toString()
        }
        content
    }

    private String removeSuffix(String input) {
        Pattern p = Pattern.compile("http[s]*://.+/(.+\\..+)")
        Matcher m = p.matcher(input)
        if (m.find()) {
            input = input.substring(0, input.length() - m.group(1).length() - 1)
            return input
        } else{
            //root case (http://www.cdc.gov)
            if(input.count("/") == 2){
                return input
            }

            //the url doesn't end with an extension
            //input substring from beginning to end minus the length of the last word after the last /
            //so: http://www.example.com/one/two/three becomes http://www.example.com/one/two
            return input[0..-1-input.split("/")[-1].size()]
        }
    }
}
