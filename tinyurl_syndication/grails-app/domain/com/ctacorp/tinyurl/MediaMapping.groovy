package com.ctacorp.tinyurl

import grails.rest.Resource
import grails.util.Holders

class MediaMapping {
    String targetUrl
    Long syndicationId
    String guid

    def config = Holders.config

    static constraints = {
        targetUrl       nullable: false, minSize:11, maxSize:2000
        syndicationId   nullable: true,  min:0L
        guid            nullable: true, unique:true
    }

    public String getToken(){
        id.encodeAsBase32() //See bootstrap for encodeAsBase32() definition
    }

    public String getTinyUrl(){
        "${config.TINYURL_SERVER_URL}/${getToken()}"
    }

    String toString(){
        "MediaMapping:  targetUrl: ${targetUrl}\nsyndicationId: ${syndicationId}\nguid: ${guid}"
    }
}