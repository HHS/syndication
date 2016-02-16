package com.ctacorp.syndication.tools

import grails.plugin.springsecurity.annotation.Secured
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PUBLISHER"])
class MediaTestPreviewController {
    def contentRetrievalService

    def index(String sourceUrl) {
        def extractedContent = null
        if(params.sourceUrl) {
            try {
                extractedContent = contentRetrievalService.extractSyndicatedContent(sourceUrl, params)
                extractedContent = Jsoup.clean(extractedContent, Whitelist.relaxed().addAttributes(":all", "class"))
                if(!extractedContent){
                    flash.error = "The system could not find syndication markup at the provided URL. Please verify the media source to ensure it contains at least one <div> element containing the 'syndicate' class."
                }
            } catch(e){
                StringWriter sw = new StringWriter()
                PrintWriter pw = new PrintWriter(sw)
                e.printStackTrace(pw)
                log.error sw.toString()
                flash.error = "The provided URL doesn't appear to be accessible, perhaps it's invalid? Please make " +
                        "sure it is fully qualified and correct."
            }
        }

        [extractedContent:extractedContent, sourceUrl:sourceUrl]
    }
    
    def urlTestModal(String sourceUrl){
        def extractedContent = null
        if(params.sourceUrl) {
            try {
                extractedContent = contentRetrievalService.extractSyndicatedContent(sourceUrl, params)
                extractedContent = Jsoup.clean(extractedContent, Whitelist.relaxed().addAttributes(":all", "class"))
                if(!extractedContent){
                    flash.error = "The system could not find syndication markup at the provided URL. Please verify the media source to ensure it contains at least one <div> element containing the 'syndicate' class."
                }
            } catch(e){
                flash.error = "The provided URL doesn't appear to be accessible, perhaps it's invalid? Please make " +
                        "sure it is fully qualified and correct."
            }
        }
       render template:'testModal', model:[extractedContent:extractedContent, sourceUrl:sourceUrl]
    }
}
