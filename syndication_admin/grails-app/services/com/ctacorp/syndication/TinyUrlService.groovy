
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class TinyUrlService {
    def grailsApplication
    private String tServer
    private RestBuilder rest = new RestBuilder()

    @PostConstruct
    void init() {
        tServer = grailsApplication.config.syndication.tinyUrl.serverAddress + grailsApplication.config.syndication.tinyUrl.mappingBase
    }

    def listTinyUrlMappings() {
        rest.get("${tServer}.json"){ accept "application/json" }.json
    }

    def listTinyUrlMappings(Map params) {
        rest.get("${tServer}.json", params){ accept "application/json" }.json
    }

    def getMappingByTargetUrl(String targetUrl){
        rest.get("${tServer}/getByTargetUrl", ["targetUrl":targetUrl]){ accept "application/json" }.json
    }

    def getMappingByTinyUrl(String tinyUrl){
        rest.get("${tServer}/getFromTinyUrl",[tinyUrl:tinyUrl]){ accept "application/json" }.json
    }

    def getMappingByToken(String token){
        rest.get("${tServer}/getByToken", [token:token]){ accept "application/json" }.json
    }

    def getMappingByMediaItemId(Long id){
        rest.get("${tServer}/getBySyndicationId/${id}"){ accept "application/json" }.json
    }

    def getMapping(Long id){
        rest.get("${tServer}/${id}.json"){ accept "application/json" }.json
    }

    @Transactional
    def createMapping(String targetUrl, Long syndicationId, String guid){
        def resp = rest.post(tServer, [targetUrl:targetUrl, syndicationId:syndicationId, guid:guid]){
            accept "application/json"
            json targetUrl:targetUrl, syndicationId:syndicationId, guid:guid
        }
        resp.json
    }

    @Transactional
    def updateMapping(String targetUrl, Long syndicationId, String guid, Long mappingId){
        def resp = rest.put("${tServer}/${mappingId}"){
            accept "application/json"
            json targetUrl:targetUrl, syndicationId:syndicationId, guid:guid
        }
        resp.json
    }

    @Transactional
    def deleteMapping(Long id){
        rest.delete("${tServer}/${id}").status
    }
}
