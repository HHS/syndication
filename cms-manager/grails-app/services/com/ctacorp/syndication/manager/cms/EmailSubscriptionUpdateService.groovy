
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

    List updateEmailSubscriptions(String mediaId) {

        log.info("Updating email subscriptions for mediaId '${mediaId}'")

        def failedUpdates = []

        def subscription = Subscription.findByMediaId(mediaId)
        if (!subscription) {
            log.info("No subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        def emailSubscriptions = EmailSubscription.findAllBySubscription(subscription)
        if (!emailSubscriptions) {
            log.info("No email subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        SyndicatedMediaItem syndicatedMediaItem

        try {
            syndicatedMediaItem = contentExtractionService.extractSyndicatedContent(mediaId)
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
            return emailSubscriptions
        }

        def sourceUrl
        try {
            sourceUrl = contentExtractionService.getMediaItem(mediaId)?.sourceUrl
        } catch (e) {
            log.error("Error occurred when getting the sourceUrl for mediaId '${mediaId}'", e)
            return emailSubscriptions
        }

        if(sourceUrl==null) {
            log.error("The sourceUrl for mediaId '${mediaId}' was null")
            return emailSubscriptions
        }

        emailSubscriptions.each { EmailSubscription emailSubscription ->
            if (syndicatedMediaItem) {
                try {

                    try{
                        emailSubscriptionMailService.sendSubscriptionUpdate(emailSubscription, syndicatedMediaItem.content)
                    } catch (e) {
                        log.error("Error occurred when sending the subscription update for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
                        failedUpdates.add(emailSubscription)
                    }

                    emailSubscription.title = syndicatedMediaItem.name
                    emailSubscription.sourceUrl = sourceUrl
                    emailSubscription.deliveryFailureLogId = null
                    emailSubscription.isPending = false
                    emailSubscription.save(flush:true)


                } catch (e) {
                    log.error("Error occurred when sending the subscription update for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
                    failedUpdates.add(emailSubscription)
                }
            }
        }

        return failedUpdates
    }

    boolean importEmailSubscription(Long subscriptionId) {

        log.info("Importing emailSubscription '${subscriptionId}'")

        def emailSubscription = EmailSubscription.findById(subscriptionId)

        if(!emailSubscription) {
            log.warn("Could not find emailSubscription '${subscriptionId}'")
            return false
        }

        def mediaId = emailSubscription.subscription.mediaId

        def content
        try {
            content = contentExtractionService.extractSyndicatedContent(mediaId)?.content
        } catch (e) {
            log.error("Error occurred when extracting the content for mediaId '${mediaId}'", e)
        }

         if (content) {
            try {
                emailSubscriptionMailService.sendSubscriptionCreate(emailSubscription, content)
                return true
            } catch (e) {
                log.error("Error occurred when sending the subscription create for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
            }
        }

        return false
    }

    List deleteEmailSubscription(String mediaId){

        log.info("Deleting email subscriptions for mediaId '${mediaId}'")

        def failedUpdates = []

        def subscription = Subscription.findByMediaId(mediaId)
        if (!subscription) {
            log.info("No subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        def emailSubscriptions = EmailSubscription.findAllBySubscription(subscription)
        if (!emailSubscriptions) {
            log.info("No email subscriptions found for media '${mediaId}'")
            return failedUpdates
        }

        emailSubscriptions.each { EmailSubscription emailSubscription ->

            try {
                emailSubscriptionMailService.sendStorefrontDelete(emailSubscription)
            } catch (e) {
                log.error("Error occurred when sending the subscription delete for mediaId '${mediaId}' to '${emailSubscription.emailSubscriber.email}'", e)
                failedUpdates.add(emailSubscription)
            }

            def emailSubscriptionId = emailSubscription.id

            try {
                subscriptionService.deleteChildSubscription(emailSubscription)
            } catch (e) {
                log.warn("Could not delete email subscription '${emailSubscriptionId}'", e)
                failedUpdates.add(emailSubscription)
            }

            log.info("Successfully deleted emailSubscription '${emailSubscription}'")
        }

        return failedUpdates
    }
}
