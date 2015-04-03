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
class RestSubscriptionUpdateService {

    def contentExtractionService
    def restSubscriptionDeliveryService
    def subscriptionService

    boolean updateSubscription(RestSubscription restSubscription) {

        def mediaId = restSubscription.subscription.mediaId

        def sourceUrl = getSourceUrl(mediaId)
        if (sourceUrl == null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return false
        }

        SyndicatedMediaItem syndicatedMediaItem = getSyndicatedMediaItem(mediaId)
        if (syndicatedMediaItem == null) {
            log.error("The syndicatedMediaItem for mediaId '${mediaId}' was null")
            return false
        }

        try {
            deliver(restSubscription, syndicatedMediaItem, sourceUrl)
            log.info("Successfully updated restSubscription '${restSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when updating restSubscription '${restSubscription.id}'", e)
            return false
        }
    }

    String getSourceUrl(String mediaId) {
        try {
            return contentExtractionService.getMediaItem(mediaId)?.sourceUrl
        } catch (e) {
            log.error("Error occurred when getting the sourceUrl for mediaId '${mediaId}'", e)
            return null
        }
    }

    SyndicatedMediaItem getSyndicatedMediaItem(String mediaId) {
        try {
            return contentExtractionService.getMediaSyndicate(mediaId)
        } catch (e) {
            log.error("Error occurred when getting the syndicated content for mediaId '${mediaId}'", e)
            return null
        }
    }

    boolean importSubscription(long subscriptionId) {

        def restSubscription = RestSubscription.findById(subscriptionId)

        if (!restSubscription) {
            log.warn("Could not find restSubscription '${subscriptionId}'")
            return false
        }

        def mediaId = restSubscription.subscription.mediaId

        def sourceUrl = getSourceUrl(mediaId)
        if (sourceUrl == null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return false
        }

        SyndicatedMediaItem syndicatedMediaItem = getSyndicatedMediaItem(mediaId)
        if (syndicatedMediaItem == null) {
            log.error("The syndicatedMediaItem for mediaId '${mediaId}' was null")
            return false
        }

        try {
            deliver(restSubscription, syndicatedMediaItem, sourceUrl)
            log.info("Successfully imported restSubscription '${restSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when importing restSubscription '${restSubscription.id}'", e)
            return false
        }
    }

    private void deliver(RestSubscription restSubscription, SyndicatedMediaItem syndicatedMediaItem, String sourceUrl) {

        restSubscriptionDeliveryService.deliver(restSubscription, syndicatedMediaItem.content)

        restSubscription.title = syndicatedMediaItem.name
        restSubscription.sourceUrl = sourceUrl
        restSubscription.deliveryFailureLogId = null
        restSubscription.isPending = false
        restSubscription.save(flush: true)
    }

    boolean deleteSubscription(RestSubscription restSubscription) {

        try {
            restSubscriptionDeliveryService.deliverDelete(restSubscription)
            subscriptionService.deleteChildSubscription(restSubscription)
            log.info("Successfully deleted restSubscription '${restSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when importing restSubscription '${restSubscription.id}'", e)
            return false
        }
    }
}
