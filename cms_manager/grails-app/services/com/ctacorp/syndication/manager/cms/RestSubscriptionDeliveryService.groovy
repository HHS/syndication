/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import com.ctacorp.syndication.manager.cms.utils.exception.RestDeliveryException
import grails.transaction.Transactional
import grails.util.Holders
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

import javax.annotation.PostConstruct

@Transactional
class RestSubscriptionDeliveryService {

    def authorizationHeaderService
    def subscriptionService

    RESTClientFactory restClientFactory = new RESTClientFactory()
    AuthorizationHeaderGeneratorFactory authorizationHeaderGeneratorFactory

    @PostConstruct
    void init() {
        def config = Holders.config
        def apiKey = config.apiKey
        authorizationHeaderGeneratorFactory = new AuthorizationHeaderGeneratorFactory(apiKey.keyName as String)
    }

    @SuppressWarnings("GrUnresolvedAccess")
    void deliver(RestSubscription restSubscription, String content) {

        if (!restSubscription) {
            throw new RestDeliveryException("Rest subscription was null")
        }

        if (!content) {
            throw new RestDeliveryException("Content was null or empty")
        }

        def deliveryEndpoint = restSubscription.restSubscriber.deliveryEndpoint
        def mediaId = restSubscription.subscription.mediaId
        def keyAgreement = restSubscription.restSubscriber.subscriber.keyAgreement

        def authorizationHeaderGenerator = authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement)
        def headers = authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, content)

        def args = {
            if (restSubscription.notificationOnly) {
                [headers: headers, query: [media_id: mediaId]]
            } else {
                [headers: headers, body: content, query: [media_id: mediaId], requestContentType: 'text/html']
            }
        }()

        def status = {
            def client = restClientFactory.newRestClient(deliveryEndpoint)
            try {
                client.post(args)?.status
            } catch (HttpResponseException e) {
                e.statusCode
            } catch (e) {
                throw new RestDeliveryException("Error occured when trying to POST to '${deliveryEndpoint}' for rest subscription '${mediaId}'", e)
            }
        }()

        if (status != 200) {

            log.debug("Content was: \n${content}")

            if (status == 410) {
                log.warn("Deleting the rest subscription for media id '${mediaId}' belonging to rest subscriber '${deliveryEndpoint}'")
                subscriptionService.deleteChildSubscription(restSubscription)
            } else {
                throw new RestDeliveryException("Received a status code of '${status}' when trying to POST to '${deliveryEndpoint}'")
            }
        } else {
            log.info("Successfully delivered the rest subscription for media id '${mediaId}' belonging to rest subscriber '${deliveryEndpoint}'")
        }
    }

    void deliverDelete(RestSubscription restSubscription){
        if (!restSubscription) {
            throw new RestDeliveryException("Rest subscription was null")
        }

        def deliveryEndpoint = restSubscription.restSubscriber.deliveryEndpoint
        def mediaId = restSubscription.subscription.mediaId
        def keyAgreement = restSubscription.restSubscriber.subscriber.keyAgreement

        def authorizationHeaderGenerator = authorizationHeaderGeneratorFactory.newAuthorizationHeaderGenerator(keyAgreement)
        def headers = authorizationHeaderService.createAuthorizationHeaders(authorizationHeaderGenerator, deliveryEndpoint, null, "DELETE")

        def args = [headers: headers, query: [media_id: mediaId, subscriptionId: restSubscription.id]]

        def status = {
            def client = restClientFactory.newRestClient(deliveryEndpoint)
            try {
                client.delete(args)?.status
            } catch (HttpResponseException e) {
                e.statusCode
            } catch (e) {
                throw new RestDeliveryException("Error occured when trying to DELETE to '${deliveryEndpoint}' for rest subscription '${mediaId}'", e)
            }
        }()

        if (status != 200) {

            if (status == 410) {
                log.warn("Deleting the rest subscription for media id '${mediaId}' belonging to rest subscriber '${deliveryEndpoint}'")
                subscriptionService.deleteChildSubscription(restSubscription)
            } else {
                throw new RestDeliveryException("Received a status code of '${status}' when trying to DELETE to '${deliveryEndpoint}'")
            }
        } else {
            log.info("Successfully deleted the rest subscription for media id '${mediaId}' belonging to rest subscriber '${deliveryEndpoint}'")
        }
    }

    static class AuthorizationHeaderGeneratorFactory {

        String apiKeyName

        AuthorizationHeaderGeneratorFactory(String apiKeyName) {
            this.apiKeyName = apiKeyName
        }

        AuthorizationHeaderGenerator newAuthorizationHeaderGenerator(KeyAgreement keyAgreement) {

            if (!keyAgreement) {
                return null
            }

            AuthorizationHeaderGenerator.KeyAgreement _keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()
            _keyAgreement.secret = keyAgreement.secret
            _keyAgreement.publicKey = keyAgreement.entity1PublicKey
            _keyAgreement.privateKey = keyAgreement.entity1PrivateKey

            return new AuthorizationHeaderGenerator(apiKeyName, _keyAgreement)
        }
    }

    static class RESTClientFactory {
        @SuppressWarnings("GrMethodMayBeStatic")
        RESTClient newRestClient(String deliveryEndpoint) {
            return new RESTClient(deliveryEndpoint)
        }
    }
}
