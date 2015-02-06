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

    Map updateRhythmyxSubscriptions(String mediaId) {

        log.info("Updating rhythmyx subscriptions for mediaId '${mediaId}'")

        try {
            return updateSubscriptions(mediaId)
        } catch(e) {
            log.error("An unexpected error occured when updating the rhythmyx subscriptions associated with mediaId '${mediaId}'", e)
            return [failedUpdates:[],successfulUpdates:[]]
        }
    }

    Map updateSubscriptions(String mediaId) {

        def failedUpdates = []
        def successfulUpdates = []

        def subscription = Subscription.findByMediaId(mediaId)

        if(!subscription) {
            log.info("No subscriptions found for mediaId '${mediaId}'")
            return updates(failedUpdates, successfulUpdates)
        }

        def existingSubscriptions = RhythmyxSubscription.findAllBySubscription(subscription)
        if(!existingSubscriptions) {
            log.info("No rhythmyx subscriptions found for subscription '${subscription.id}'")
            return updates(failedUpdates, successfulUpdates)
        }

        SyndicatedMediaItem syndicatedMediaItem

        try {
            syndicatedMediaItem = contentExtractionService.extractSyndicatedContent(mediaId)
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
            failedUpdates.addAll(existingSubscriptions)
            return updates(failedUpdates, successfulUpdates)
        }

        def sourceUrl
        try {
            sourceUrl = contentExtractionService.getMediaItem(mediaId)?.sourceUrl
        } catch (e) {
            log.error("Error occurred when getting the sourceUrl for mediaId '${mediaId}'", e)
            failedUpdates.addAll(existingSubscriptions)
            return updates(failedUpdates, successfulUpdates)
        }

        if(sourceUrl==null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return updates(failedUpdates, successfulUpdates)
        }

        existingSubscriptions.each { rhythmyxSubscription ->
            try {

                if(rhythmyxIngestionService.updateContentItem(rhythmyxSubscription, syndicatedMediaItem)) {

                    successfulUpdates.add(rhythmyxSubscription)
                    rhythmyxSubscription.systemTitle = syndicatedMediaItem.name
                    rhythmyxSubscription.sourceUrl = sourceUrl
                    rhythmyxSubscription.save(flush:true)

                    log.info("Successfully updated rhythmyxSubscription '${rhythmyxSubscription.id}'")

                } else {
                    failedUpdates.add(rhythmyxSubscription)
                }
            } catch (e) {
                failedUpdates.add(rhythmyxSubscription)
                log.error("Error occurred when updating rhythmyxSubscription '${rhythmyxSubscription.id}'", e)
            }
        }

        return updates(failedUpdates, successfulUpdates)
    }

    private static LinkedHashMap<String, ArrayList> updates(ArrayList failedUpdates, ArrayList successfulUpdates) {
        [failedUpdates: failedUpdates, successfulUpdates: successfulUpdates]
    }
}
