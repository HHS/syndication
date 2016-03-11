/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.exception.ContentUnretrievableException
import grails.converters.JSON
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import java.util.regex.Matcher
import java.util.regex.Pattern

@Transactional(readOnly = true)
class ContentRetrievalService {
    def webUtilService
    def jsoupWrapperService
    def tagsService

    @NotTransactional
    Map extractSyndicatedContent(String url, params = [:]) throws ContentUnretrievableException{
        if(url.endsWith("/")){
            url = url[0..-2]
        }
        params.newUrlBase = removeSuffix(url)

        String content = null
        try{
            content = webUtilService.getPage(url, params.disableFailFast)
            String extractedContent = jsoupWrapperService.extract(content, params)
            def jsonLD = jsoupWrapperService.getJsonLDMetaDataAsJSON(content)
            return [
                extractedContent:extractedContent,
                jsonLD:jsonLD
            ]
        } catch(ContentUnretrievableException e){
            log.error("Tried to extract content from a bad URL: ${url}\nError: ${e}")
            throw e
        }
    }

    String addJsonLDMetadata(Long mediaId, String extractedContent){
        MediaItem mi = MediaItem.read(mediaId)
        String jsonLdData = buildJsonLdDataString(mi)

        def updatedContent = jsoupWrapperService.updateJsonLD(extractedContent, jsonLdData)
        updatedContent
    }

    @NotTransactional
    String buildJsonLdDataString(MediaItem mi) {
        String schemaType = ""

        switch(mi){
            case Html:
                schemaType = mi.structuredContentType?.prettyName?.replace(" ", "") ?: "Article"
                break
            case Image:
                schemaType = "ImageObject"
                break
            case Infographic:
                schemaType = "ImageObject"
                break
            case PDF:
                schemaType = mi.structuredContentType?.prettyName?.replace(" ", "") ?: "Article"
                break
            case Tweet:
                schemaType = "SocialMediaPosting"
                break
            case Video:
                schemaType = "VideoObject"
                break
            case com.ctacorp.syndication.media.Collection:
                schemaType = "ItemList"
                break
        }

        String aboutField = ""
        String audienceField = ""

        def tags = tagsService.getTagsForMediaId(mi.id)
        println tags
        def generalTags = tags.findAll{ it.type.name == "General" && it.language.isoCode == "eng" }.collect{ it.name }
        def audienceTags = tags.findAll{ it.type.name == "Audience" && it.language.isoCode == "eng" }.collect{ it.name }

        aboutField = generalTags.join(", ")
        audienceField = audienceTags.join(", ")

        def jsonLdData = [
                "@context"          : "http://schema.org",
                "@type"             : schemaType,
                "headline"          : mi.name,
                "datePublished"     : mi.dateContentPublished?.format("YYYY-mm-dd'T'HH:mm:ss'Z'") ?: mi.dateSyndicationCaptured.format("YYYY-mm-dd'T'HH:mm:ss'Z'"),
                "description"       : mi.description,
                "about"             : aboutField,
                "audience"          : audienceField,
                "dateCreated"       : mi.dateSyndicationCaptured.format("YYYY-mm-dd'T'HH:mm:ss'Z'"),
                "dateModified"      : mi.dateSyndicationUpdated.format("YYYY-mm-dd'T'HH:mm:ss'Z'"),
                "sourceOrganization": mi.source.name
        ]
        (jsonLdData as JSON).toString()
    }

    @NotTransactional
    Map getContentAndMd5Hashcode(String url, params = [:]){
        def extractionResult = extractSyndicatedContent(url, params)
        String extracted = extractionResult.extractedContent
        [content:extracted, hash:Hash.md5(extracted), jsonLD:extractionResult.jsonLD]
    }

    @NotTransactional
    String getDescriptionFromContent(String content, String sourceUrl){
        String desc = jsoupWrapperService.getMetaDescription(sourceUrl)
        if(!desc) {
            desc = jsoupWrapperService.getDescriptionFromContent(content, 3)
        }
        desc
    }

    @NotTransactional
    String wrapWithSyndicateDiv(String content, MediaItem item = null){
        if(item && item.instanceOf(PDF)){
            return "<div class='syndicate' style='height: 350px;'>${content}</div>"
        } else {
            return "<div class='syndicate'>${content}</div>"
        }
    }


    String addAttributionToExtractedContent(Long mediaId, String content){
        MediaItem mi = MediaItem.read(mediaId)
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

    @NotTransactional
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
