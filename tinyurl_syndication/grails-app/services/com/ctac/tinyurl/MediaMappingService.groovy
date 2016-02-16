package com.ctac.tinyurl

import com.ctacorp.tinyurl.MediaMapping
import grails.transaction.Transactional

@Transactional
class MediaMappingService {

    MediaMapping saveMediaMapping(MediaMapping mediaMappingInstance) {
        if (mediaMappingInstance.hasErrors()) {
            log.error mediaMappingInstance.errors
            return null
        }

        //Find existing entries by syndication ID
        def existing = MediaMapping.findBySyndicationId(
                mediaMappingInstance.syndicationId
        )

        //If one exists, we should update it's mapping incase the URL changed
        if(existing){
            existing.properties = mediaMappingInstance.properties
            mediaMappingInstance = existing
        }

        mediaMappingInstance.save(flush: true)
    }

    def bulkMapping(mappingList){
        def saved = []
        mappingList.each{
            def mapping = new MediaMapping(syndicationId: it.id, guid: it.guid, targetUrl: it.url)
            def existing = MediaMapping.findBySyndicationId(
                    mapping.syndicationId
            )
            if(existing){
                existing.properties = mapping.properties
                mapping = existing
            }
            if(mapping.save()){
                saved << mapping
                log.info("saved bulk mapping: ${mapping}")
            } else{
                log.error "error saving bulk mapping: ${mapping.errors}"
            }
        }
        saved
    }
}
