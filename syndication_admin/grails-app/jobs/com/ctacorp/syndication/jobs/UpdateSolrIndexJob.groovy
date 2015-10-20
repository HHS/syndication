package com.ctacorp.syndication.jobs

import grails.plugins.rest.client.RestBuilder
import com.ctacorp.syndication.media.MediaItem

class UpdateSolrIndexJob {
    def group = "Solr"
    def description = "Reindex Solr content for a specific media item after a delay"
    def grailsApplication
    def solrIndexingService
    RestBuilder rest = new RestBuilder()

    def execute(context) {
        def mediaId = context.mergedJobDataMap.get('mediaId')
        if(mediaId){
            MediaItem mi = MediaItem.get(mediaId)
            def mediaContent
            try {
                String apiUrl = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
                rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
                mediaContent = rest.get("${apiUrl}/resources/media/${mi.id}/syndicate.html").text
            }catch(org.springframework.web.client.ResourceAccessException e){
                log.error "Syndication api server could not be reached!\n${e}"
            }catch(e){
                log.error "An unhandeled error has occured trying to get content from syndication server\n${e}"
                e.printStackTrace()
            }
            solrIndexingService.inputMediaItem(mi, mediaContent)
        }
    }
}
