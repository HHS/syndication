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
import grails.util.Holders
import org.springframework.context.i18n.LocaleContextHolder

import javax.annotation.PostConstruct

@Transactional
class RhythmyxSubscriptionImportService {

    def mediaContentUrl
    def locale

    def messageSource
    def contentExtractionService
    def rhythmyxIngestionService
    def queueService
    def loggingService
    def subscriptionFactory = new SubscriptionFactory()

    @PostConstruct()
    def init() {
        def syndicationConfig = Holders.config.syndication
        mediaContentUrl = syndicationConfig.mediaContentUrl as String
        locale = LocaleContextHolder.locale
    }

    boolean importRhythmyxSubscription(long rhythmyxSubscriptionId) {

        log.info("Importing rhythmyxSubscription '${rhythmyxSubscriptionId}'")

        def rhythmyxSubscription = RhythmyxSubscription.findById(rhythmyxSubscriptionId)
        if(!rhythmyxSubscription) {
            log.warn("Could not find rhythmyxSubscription '${rhythmyxSubscription?.id}'")
            return false
        }

        try {
            return importRhythmyxSubscription(rhythmyxSubscription)
        } catch(e) {
            log.error("An unexpected error occured when importing the content item for rhythmyxSubscription '${rhythmyxSubscriptionId}'", e)
            return false
        }
    }

    boolean importRhythmyxSubscription(RhythmyxSubscription rhythmyxSubscription) {

        def sourceUrl = rhythmyxSubscription.sourceUrl
        def mediaId = getMediaId(sourceUrl)
        if(!mediaId) {
            log.error("Could not find a mediaItem associated with sourceUrl '${sourceUrl}' for rhythmyxSubscription '${rhythmyxSubscription.id}'")
            return false
        }

        def mediaItem = getMediaItem(mediaId)
        if(!mediaItem) {
            log.error("Could not find a mediaItem associated with sourceUrl '${sourceUrl}' for rhythmyxSubscription '${rhythmyxSubscription.id}'")
            return false
        }

        String systemTitle = mediaItem.name
        String content = mediaItem.content
        String contentId = importMediaItem(rhythmyxSubscription, content, mediaId, systemTitle)

        if (!contentId) {
            return false
        }

        rhythmyxSubscription.contentId = contentId
        rhythmyxSubscription.systemTitle = systemTitle

        def existingSubscription = Subscription.findByMediaId(mediaId)
        if(!existingSubscription) {

            def mediaUri = mediaContentUrl.replace("{mediaId}", mediaId)
            def subscription = subscriptionFactory.newSubscription(mediaId, mediaUri)
            subscription.save(flush:true)

            if(subscription.hasErrors()) {
                loggingService.logDomainErrors(subscription.errors)
                return false
            }

            rhythmyxSubscription.subscription = subscription

        } else {
            rhythmyxSubscription.subscription = existingSubscription
        }

        rhythmyxSubscription.save(flush:true, failOnError:true)
    }

    static class SubscriptionFactory {

        @SuppressWarnings("GrMethodMayBeStatic")
        Subscription newSubscription(mediaId, mediaUri) {
            return new Subscription(mediaId: mediaId, mediaUri: mediaUri)
        }
    }

    private String importMediaItem(RhythmyxSubscription rhythmyxSubscription, String content, String mediaId, String systemTitle) {
        log.info("Importing the content item for mediaId=${mediaId}")
        log.debug("Content is:\n ${content}")
        def contentId = rhythmyxIngestionService.importMediaItem(rhythmyxSubscription, content, systemTitle)
        if(contentId) {
            log.info("Successfully imported the content item (contentId = ${contentId}) for rhythmyxSubscription '${rhythmyxSubscription.id}'")
        } else {
            log.error("Error occurred when importing the content item for rhythmyxSubscription '${rhythmyxSubscription.id}'")
        }
        return contentId
    }

    private SyndicatedMediaItem getMediaItem(String mediaId) {
        log.info("Getting content for mediaId=${mediaId}")
        def content = contentExtractionService.extractSyndicatedContent(mediaId)
        log.debug("The content for mediaId=${mediaId} is:\n${content}")
        return content
    }

    private String getMediaId(String sourceUrl) {
        log.info("Getting mediaId for sourceUrl=${sourceUrl}")
        def mediaId = contentExtractionService.getMediaId(sourceUrl)
        log.debug("The mediaId for sourceUrl=${sourceUrl} is:\n${mediaId}")
        return mediaId
    }
}
