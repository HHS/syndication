/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class TinyUrlService {
    def grailsApplication
    private String apiUrl
    private RestBuilder rest = new RestBuilder()

    @PostConstruct
    void init() {
        apiUrl = grailsApplication.config.tinyUrl.serverAddress + grailsApplication.config.tinyUrl.mappingBase
        //This still needed?
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
    }

    def updateItemsWithoutMappings(){
        def mediaItems = MediaItem.list().collect{
            [id:it.id, url:it.sourceUrl]
        }

        def itemIds = rest.post("${apiUrl}/missingTinyUrls.json"){
            accept "application/json;charset=UTF-8"
            body mediaItems as JSON
        }.json.collect{ it as Long }

        if(itemIds){
            def mediaItemsWithoutTinyUrls = MediaItem.withCriteria {
                'in'('id', itemIds)
            }

            def bulkData = mediaItemsWithoutTinyUrls.collect{
                [url:it.sourceUrl, id:it.id, guid:it.externalGuid]
            }

            def resp = rest.post(apiUrl + "/bulkAdd.json") {
                header 'Date', new Date().toString()
                header 'Authorization', grailsApplication.config.syndication.internalAuthHeader ?: ""
                header 'Content-Type', "application/json;charset=UTF-8"
                accept 'application/json'

                json (bulkData as JSON)
            }

            return resp.json
        }

        []
    }

    @NotTransactional
    def listTinyUrlMappings(Map params = null) {
        rest.get("${apiUrl}.json", params){ accept "application/json" }.json
    }

    @NotTransactional
    def getMappingByTargetUrl(String targetUrl){
        rest.get("${apiUrl}/getByTargetUrl", ["targetUrl":targetUrl]){ accept "application/json" }.json
    }

    @NotTransactional
    def getMappingByTinyUrl(String tinyUrl){
        rest.get("${apiUrl}/getFromTinyUrl",[tinyUrl:tinyUrl]){ accept "application/json" }.json
    }

    @NotTransactional
    def getMappingByToken(String token){
        rest.get("${apiUrl}/getByToken", [token:token]){ accept "application/json" }.json
    }

    @NotTransactional
    def getMappingByMediaItemId(Long id){
        rest.get("${apiUrl}/getBySyndicationId/${id}"){ accept "application/json" }.json
    }

    @NotTransactional
    def getMapping(Long id){
        rest.get("${apiUrl}/${id}.json"){ accept "application/json" }.json
    }

    @NotTransactional
    def createMapping(String targetUrl, Long syndicationId, String guid){
        def resp = rest.post(apiUrl) {
            header 'Date', new Date().toString()
            header 'Authorization', grailsApplication.config.syndication.internalAuthHeader ?: ""
            header 'Content-Type', "application/json;charset=UTF-8"
            accept 'application/json'

            json ([targetUrl:targetUrl, syndicationId:syndicationId, guid:guid]as JSON)
        }
        return resp.json
    }

    @NotTransactional
    def updateMapping(String targetUrl, Long syndicationId, String guid, Long mappingId){
        def resp = rest.put("${apiUrl}/${mappingId}"){
            accept "application/json;charset=UTF-8"
            json targetUrl:targetUrl, syndicationId:syndicationId, guid:guid
        }
        resp.json
    }

    @NotTransactional
    def deleteMapping(Long id){
        rest.delete("${apiUrl}/${id}").status
    }
}
