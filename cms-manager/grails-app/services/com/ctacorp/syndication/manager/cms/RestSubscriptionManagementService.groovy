/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.commons.mq.MessageType
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class RestSubscriptionManagementService {

    def contentExtractionService
    def loggingService
    def queueService
    def subscriptionService

    def getSubscription(Long subscriptionId, String senderPublicKey) {

        if(!senderPublicKey) {
            return unauthorized()
        }

        def keyAgreement = KeyAgreement.findByEntity2PublicKey(senderPublicKey)
        if(!keyAgreement) {
            return unauthorized()
        }

        def subscriber = Subscriber.findByKeyAgreement(keyAgreement)
        def restSubscriber = RestSubscriber.findBySubscriber(subscriber)
        def restSubscription = RestSubscription.findByRestSubscriberAndId(restSubscriber, subscriptionId)

        if(!restSubscription) {
            return notFound(subscriptionId)
        }

        def result = new SubscriptionManagementResult()
        result.status = 200
        result.message = (restSubscription as JSON).toString(true)
        return result
    }

    def deleteSubscription(Long subscriptionId, String senderPublicKey) {

        if(!senderPublicKey) {
            return unauthorized()
        }

        def keyAgreement = KeyAgreement.findByEntity2PublicKey(senderPublicKey)
        if(!keyAgreement) {
            return unauthorized()
        }

        def subscriber = Subscriber.findByKeyAgreement(keyAgreement)
        def restSubscriber = RestSubscriber.findBySubscriber(subscriber)
        def restSubscription = RestSubscription.findByRestSubscriberAndId(restSubscriber, subscriptionId)

        if(restSubscription) {
            subscriptionService.deleteChildSubscription(restSubscription)
        }

        def result = new SubscriptionManagementResult()
        result.status = 204
        return result
    }

    def getAllSubscriptions(String senderPublicKey) {

        if(!senderPublicKey) {
            return unauthorized()
        }

        def keyAgreement = KeyAgreement.findByEntity2PublicKey(senderPublicKey)
        if(!keyAgreement) {
            return unauthorized()
        }

        def subscriber = Subscriber.findByKeyAgreement(keyAgreement)
        def restSubscribers = RestSubscriber.findAllBySubscriber(subscriber)

        def restSubscriptions = []

        restSubscribers.each {
            restSubscriptions.addAll(RestSubscription.findAllByRestSubscriber(it))
        }

        def result = new SubscriptionManagementResult()
        result.status = 200
        result.message = (restSubscriptions as JSON).toString(true)
        return result
    }

    def createSubscription(String mediaId, String sourceUrl, boolean notificationOnly, String senderPublicKey) {

        if(!senderPublicKey) {
            return unauthorized()
        }

        def keyAgreement = KeyAgreement.findByEntity2PublicKey(senderPublicKey)
        if(!keyAgreement) {
            return unauthorized()
        }

        if(!mediaId && !sourceUrl) {
            return missingRequiredQueryParams()
        }

        if(mediaId) {
            try {
                Long.parseLong(mediaId)
            } catch (NumberFormatException ignored) {
                return invalidMediaId(mediaId)
            }
        } else {
            try {
                mediaId = contentExtractionService.getMediaId(sourceUrl)
                if(!mediaId) {
                    return invalidSourceUrl(sourceUrl)
                }
            } catch (e) {
                def errorMessage = loggingService.logError("Error occurred when trying to fetch the media item associated with source_url '${sourceUrl}'", e)
                return serverError(errorMessage)
            }
        }

        def subscriber = Subscriber.findByKeyAgreement(keyAgreement)
        def restSubscribers = RestSubscriber.findAllBySubscriber(subscriber)
        if(!restSubscribers) {
            return noRestSubscribersExist()
        }

        def mediaItem

        try {
            mediaItem = contentExtractionService.getMediaItem(mediaId)
        } catch (e) {
            def errorMessage = loggingService.logError("Error occurred when trying to fetch the media item for media_id '${mediaId}'", e)
            return serverError(errorMessage)
        }

        if (!mediaItem) {
            return invalidMediaId(mediaId)
        }

        def subscription = Subscription.findByMediaId(mediaId)
        if(!subscription) {
            subscription = new Subscription(mediaId: mediaId).save(flush: true)
        }

        def newSubscriptions = []
        def existingSubscriptions = []

        for(RestSubscriber restSubscriber : restSubscribers) {

            def existingSubscription = RestSubscription.findBySubscriptionAndRestSubscriber(subscription, restSubscriber)

            if(!existingSubscription) {
                def restSubscription = new RestSubscription(sourceUrl: mediaItem.sourceUrl, notificationOnly: notificationOnly, title: mediaItem.name, subscription: subscription, restSubscriber: restSubscriber)

                if (!restSubscription.validate()) {
                    def errorMessage = loggingService.logDomainErrors(restSubscription)
                    return serverError(errorMessage)
                } else {
                    newSubscriptions.add(restSubscription)
                }
            } else {
                existingSubscriptions.add(existingSubscription)
            }
        }

        if(newSubscriptions) {
            newSubscriptions.each { RestSubscription restSubscription ->
                restSubscription.save(flush: true)
                log.info("successfully created rest subscription '${restSubscription.sourceUrl}' for rest subscriber '${restSubscription.restSubscriber.deliveryEndpoint}'")
                existingSubscriptions.add(restSubscription)
                queueService.sendToRestUpdateQueue(MessageType.IMPORT, restSubscription.id)
            }
        }

        created(existingSubscriptions)
    }

    private static SubscriptionManagementResult serverError(errorMessage) {
        def result = new SubscriptionManagementResult()
        result.status = 500
        result.message = ([message: "${errorMessage}"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult unauthorized() {
        def result = new SubscriptionManagementResult()
        result.status = 403
        result.message = ([message: "Unauthorized: the sender's public key is not associated with a known subscriber"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult missingRequiredQueryParams() {
        def result = new SubscriptionManagementResult()
        result.status = 400
        result.message = ([message: "Bad Request: 'media_id' or 'source_url' query parameter is required"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult invalidMediaId(mediaId) {
        def result = new SubscriptionManagementResult()
        result.status = 400
        result.message = ([message: "Bad Request: could not find a media item identified by media_id '${mediaId}'"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult invalidSourceUrl(sourceUrl) {
        def result = new SubscriptionManagementResult()
        result.status = 400
        result.message = ([message: "Bad Request: could not find a media item associated with source_url '${sourceUrl}'"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult noRestSubscribersExist() {
        def result = new SubscriptionManagementResult()
        result.status = 400
        result.message = ([message: "Bad Request: no rest subscribers (delivery endpoints) exist for this subscriber"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult notFound(subscriptionId) {
        def result = new SubscriptionManagementResult()
        result.status = 404
        result.message = ([message: "Not Found: the rest subscription identified by id '${subscriptionId}' does not exist"] as JSON).toString(true)
        return result
    }

    private static SubscriptionManagementResult created(List newSubscriptions) {

        def result = new SubscriptionManagementResult()
        result.status = 201

        result.message = {
            if(newSubscriptions.size() == 1) {
                (newSubscriptions.get(0) as JSON).toString(true)
            } else {
                (newSubscriptions as JSON).toString(true)
            }
        }()

        return result
    }

    static class SubscriptionManagementResult {
        int status
        String message
    }
}
