package com.ctacorp.tinyurl

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class MediaMappingQueryController {

    static reponseFormats = ["json"]

    def getByTargetUrl(String targetUrl){
        MediaMapping mm = MediaMapping.findByTargetUrl(targetUrl)
        if(mm){
            respond mm
        } else{
            render text:[error:"$targetUrl does not have a tinyUrl mapping!"] as JSON, contentType: "application/json;charset=UTF-8"
        }
    }

    def getBySyndicationId(Long id){
        MediaMapping mm = MediaMapping.findBySyndicationId(id)
        if(mm){
            respond mm
        } else{
            render text:[error:"$id does not have a tinyUrl mapping!"] as JSON, contentType: "application/json;charset=UTF-8"
        }
    }

    def getByToken(String token){
        Long id = token.decodeBase32()
        MediaMapping mm = MediaMapping.get(id)
        if(mm){
            respond mm
        } else{
            render text:[error:"$token does not have a tinyUrl mapping!"] as JSON, contentType: "application/json;charset=UTF-8"
        }
    }

    def getByTinyUrl(String tinyUrl){
        if(tinyUrl.endsWith("/")){
            tinyUrl = tinyUrl[0..-1 -1]
        }
        String token = tinyUrl.split("/")[-1]
        getByToken(token)
    }

    def missingTinyUrls(){
        def data = request.getJSON()

        def missing = []
        MediaMapping.withSession {
            data.each{ entry ->
                def mediaMapping = MediaMapping.findByTargetUrl(entry.url)
                if(!mediaMapping){
                    missing << entry.id
                }
            }
        }

        render text: missing as JSON, contentType: "application/json;charset=UTF-8"
    }
}
