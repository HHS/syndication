
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.tinyurl

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import grails.util.Holders

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class TinyUrlService {
    def grailsApplication
    def authorizationService
    String tServer
    def RestBuilder rest = new RestBuilder(accept: "application/json")

//    LoadingCache cache = CacheBuilder.newBuilder().
//        expireAfterWrite(60, TimeUnit.MINUTES).
//        maximumSize(500).
//        build(new CacheLoader() {
//            @Override
//            Object load(Object targetUrl) throws Exception {
//                return getJSON(serverAddy, mappingBase + "/getByTargetUrl", [targetUrl:targetUrl])
//            }
//        })

    @PostConstruct
    void init() {
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        tServer = Holders.config.TINYURL_SERVER_URL + Holders.config.TINYURL_MAPPINGBASE
    }

    def status() {
        try {
            def code = rest.get("${Holders.config.TINYURL_SERVER_URL}/statusCheck").status
            switch (code) {
                case 200: return true;
                default: return false;
            }
        } catch (e) {
            return false;
        }
    }

    def listTinyUrlMappings() {
        restGet("${tServer}.json")
    }

    def listTinyUrlMappings(Map params) {
        restGet("${tServer}.json", params)
    }

    def getMappingByTargetUrl(String targetUrl) {
        restGet("${tServer}/getByTargetUrl", ["targetUrl": targetUrl])
        // cache.get(targetUrl)
    }

    def getMappingByTinyUrl(String tinyUrl) {
        restGet("${tServer}/getFromTinyUrl", [tinyUrl: tinyUrl])
    }

    def getMappingByToken(String token) {
        restGet("${tServer}/getByToken", [token: token])
    }

    def getMappingByMediaItemId(Long id) {
        restGet("${tServer}/getBySyndicationId/${id}")
    }

    def getMapping(Long id) {
        restGet("${tServer}/${id}.json")
    }

    def createMapping(String targetUrl, Long syndicationId, String guid) {
        try {
            String body = ([targetUrl: targetUrl, syndicationId: syndicationId, guid: guid] as JSON).toString()
            def resp = rest.post(tServer) {
                header 'Date', new Date().toString()
                header 'Authorization', Holders.config.SYNDICATION_INTERNALAUTHHEADER ?: ""
                header 'Content-Type', "application/json;charset=UTF-8"
                accept 'application/json'

                json body
            }
            return resp.json
        } catch (e) {
            log.error e.getMessage()
        }
        return null
    }

    def updateMapping(String targetUrl, Long syndicationId, String guid, Long mappingId) {
        def resp = rest.put("${tServer}/${mappingId}") {
            accept "application/json"
            json targetUrl: targetUrl, syndicationId: syndicationId, guid: guid
        }
        resp.json
    }

    def deleteMapping(Long id) {
        rest.delete("${tServer}/${id}").status
    }

    private restGet(String url, Map params = null) {
        try {
            return rest.get(url, params) { accept "application/json" }.json
        } catch (e) {
            log.warn "TinyURL Service could not be reached."
        }
    }
}
