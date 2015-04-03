/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.transaction.Transactional

@Transactional
class RhythmyxSubscriptionUpdateService {

    def contentExtractionService
    def rhythmyxIngestionService
    def queueService

    boolean updateSubscription(RhythmyxSubscription rhythmyxSubscription) {

        def mediaId = rhythmyxSubscription.subscription.mediaId

        def sourceUrl
        try {
            sourceUrl = contentExtractionService.getMediaItem(mediaId)?.sourceUrl
        } catch (e) {
            log.error("Error occurred when getting the sourceUrl for mediaId '${mediaId}'", e)
            return false
        }

        if (sourceUrl == null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return false
        }

        SyndicatedMediaItem syndicatedMediaItem

        try {
            syndicatedMediaItem = contentExtractionService.getMediaSyndicate(mediaId)
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
            return false
        }

        if (syndicatedMediaItem?.content == null) {
            log.error("The content for mediaId '${mediaId}' was null")
            return false
        }

        try {
            def success = rhythmyxIngestionService.updateContentItem(rhythmyxSubscription, syndicatedMediaItem)
            if (success) {
                rhythmyxSubscription.systemTitle = syndicatedMediaItem.name
                rhythmyxSubscription.sourceUrl = sourceUrl
                rhythmyxSubscription.deliveryFailureLogId = null
                rhythmyxSubscription.save(flush: true)
                log.info("Successfully updated rhythmyxSubscription '${rhythmyxSubscription.id}'")
                return true
            }
        } catch (e) {
            log.error("Error occurred when updating rhythmyxSubscription '${rhythmyxSubscription.id}'", e)
        }

        false
    }
}
