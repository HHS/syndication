/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import grails.transaction.Transactional

@Transactional
class RestSubscriptionUpdateService {

    def contentExtractionService
    def restSubscriptionDeliveryService
    def subscriptionService

    List updateSubscriptions(String mediaId) {

        log.info("Updating rest subscriptions for mediaId '${mediaId}'")

        def failedUpdates = []

        def subscription = Subscription.findByMediaId(mediaId)
        if (!subscription) {
            log.info("No subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        def restSubscriptions = RestSubscription.findAllBySubscription(subscription)
        if (!restSubscriptions) {
            log.info("No rest subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        def content
        def syndicatedMediaItem
        try {
            syndicatedMediaItem = contentExtractionService.extractSyndicatedContent(mediaId)
            content = syndicatedMediaItem?.content
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
            return restSubscriptions
        }

        if(!content) {
            log.error("The content for mediaId '${mediaId}' was null")
            return restSubscriptions
        }

        def sourceUrl
        try {
            sourceUrl = contentExtractionService.getMediaItem(mediaId)?.sourceUrl
        } catch (e) {
            log.error("Error occurred when getting the sourceUrl for mediaId '${mediaId}'", e)
            return restSubscriptions
        }

        if(sourceUrl==null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return restSubscriptions
        }

        restSubscriptions.each { RestSubscription restSubscription ->
            if (content) {
                try {
                    restSubscriptionDeliveryService.deliver(restSubscription, content as String)
                    restSubscription.title = syndicatedMediaItem.name
                    restSubscription.sourceUrl = sourceUrl
                    restSubscription.deliveryFailureLogId = null
                    restSubscription.isPending = false
                    restSubscription.save(flush:true)
                } catch (e) {
                    log.error("Error occurred when sending the subscription update for mediaId '${mediaId}' to '${restSubscription.restSubscriber.deliveryEndpoint}'", e)
                    failedUpdates.add(restSubscription)
                }

                //set logErrorId to null
            }
        }

        return failedUpdates
    }

    boolean importSubscription(long subscriptionId) {

        log.info("Importing restSubscription '${subscriptionId}'")

        def restSubscription = RestSubscription.findById(subscriptionId)

        if(!restSubscription) {
            log.warn("Could not find restSubscription '${subscriptionId}'")
            return false
        }

        def mediaId = restSubscription.subscription.mediaId

        def content
        try {
            content = contentExtractionService.extractSyndicatedContent(mediaId)?.content
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
        }

        if (content) {
            try {
                restSubscriptionDeliveryService.deliver(restSubscription, content as String)
                return true
            } catch (e) {
                log.error("Error occurred when sending the subscription create for mediaId '${mediaId}' to '${restSubscription.restSubscriber.deliveryEndpoint}'", e)
            }
        }

        return false
    }

    List deleteSubscriptions(String mediaId) {

        log.info("Deleteing rest subscriptions for mediaId '${mediaId}'")

        def failedUpdates = []

        def subscription = Subscription.findByMediaId(mediaId)
        if (!subscription) {
            log.info("No subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        def restSubscriptions = RestSubscription.findAllBySubscription(subscription)
        if (!restSubscriptions) {
            log.info("No rest subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        restSubscriptions.each { RestSubscription restSubscription ->

            try {

                String content = "<div>MediaItem has been removed from storefront mediaId:${subscription.mediaId}</div>"

                restSubscription.notificationOnly = true
                restSubscription.save(flush:true)
                restSubscriptionDeliveryService.deliverDelete(restSubscription, content, true)
                subscriptionService.deleteChildSubscription(restSubscription)

            } catch (e) {
                log.error("Error occurred when sending the subscription delete for mediaId '${mediaId}' to '${restSubscription.restSubscriber.deliveryEndpoint}'", e)
                failedUpdates.add(restSubscription)
            }
        }

        return failedUpdates
    }
}
