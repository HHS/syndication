package com.ctac.tinyurl

import com.ctacorp.tinyurl.MediaMapping
import grails.transaction.Transactional

@Transactional
class MediaMappingService {

    MediaMapping saveMediaMapping(mediaMappingInstance) {
        if (mediaMappingInstance.hasErrors()) {
            log.error mediaMappingInstance.errors
            return null
        }

        mediaMappingInstance.save flush:true
    }

    def bulkMapping(mappingList){
        def saved = []
        mappingList.each{
            def mapping = new MediaMapping(syndicationId: it.id, guid: it.guid, targetUrl: it.url)
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
