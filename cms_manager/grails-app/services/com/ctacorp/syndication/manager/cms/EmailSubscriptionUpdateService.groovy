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
class EmailSubscriptionUpdateService {

    def contentExtractionService
    def emailSubscriptionMailService
    def queueService
    def subscriptionService

    boolean updateEmailSubscription(EmailSubscription emailSubscription) {

        def mediaId = emailSubscription.subscription.mediaId

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

        if (syndicatedMediaItem == null) {
            log.error("The syndicatedMediaItem for mediaId '${mediaId}' was null")
            return false
        }

        try {
            emailSubscriptionMailService.sendSubscriptionUpdate(emailSubscription, syndicatedMediaItem.content)
            emailSubscription.title = syndicatedMediaItem.name
            emailSubscription.sourceUrl = sourceUrl
            emailSubscription.deliveryFailureLogId = null
            emailSubscription.isPending = false
            emailSubscription.save(flush: true)
            log.info("Successfully updated emailSubscription '${emailSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when sending the subscription update for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
            return false
        }
    }

    boolean importEmailSubscription(Long subscriptionId) {

        def emailSubscription = EmailSubscription.findById(subscriptionId)

        if (!emailSubscription) {
            log.warn("Could not find emailSubscription '${subscriptionId}'")
            return false
        }

        def mediaId = emailSubscription.subscription.mediaId

        SyndicatedMediaItem syndicatedMediaItem

        try {
            syndicatedMediaItem = contentExtractionService.getMediaSyndicate(mediaId)
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
            return false
        }

        if (syndicatedMediaItem == null) {
            log.error("The syndicatedMediaItem for mediaId '${mediaId}' was null")
            return false
        }

        try {
            emailSubscriptionMailService.sendSubscriptionCreate(emailSubscription, syndicatedMediaItem.content)
            emailSubscription.deliveryFailureLogId = null
            emailSubscription.isPending = false
            emailSubscription.save(flush: true)
            log.info("Successfully imported emailSubscription '${emailSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when sending the subscription create for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
            false
        }
    }

    boolean deleteEmailSubscription(EmailSubscription emailSubscription) {

        try {
            emailSubscriptionMailService.sendStorefrontDelete(emailSubscription)
            subscriptionService.deleteChildSubscription(emailSubscription)
            log.info("Successfully deleted emailSubscription '${emailSubscription.id}'")
            return true
        } catch (e) {
            log.error("Error occurred when importing emailSubscription '${emailSubscription.id}'", e)
            return false
        }
    }

    def getMediaId(String sourceUrl) {
        log.info("looking up mediaId for sourceUrl=${sourceUrl}")
        def mediaId = contentExtractionService.getMediaItemBySourceUrl(sourceUrl)
        log.info("mediaId for sourceUrl=${sourceUrl} is ${mediaId}")
        return mediaId
    }
}
