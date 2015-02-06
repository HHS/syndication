/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.controller
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.*
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscriptionController)
@Build([Subscriber, RestSubscriber, Subscription, RestSubscription])
class RestSubscriptionControllerSpec extends Specification {

    def contentExtractionService = Mock(ContentExtractionService)
    def queueService = Mock(QueueService)
    def loggingService = Mock(LoggingService)
    def subscriptionService = Mock(SubscriptionService)

    Subscriber subscriber
    Subscription subscription
    RestSubscriber restSubscriber
    RestSubscription restSubscription

    def setup() {

        subscriber = Subscriber.build()
        subscription = Subscription.build()
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "htatp://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", restSubscriber: restSubscriber, subscription: subscription)

        controller.loggingService = loggingService
        controller.contentExtractionService = contentExtractionService
        controller.queueService = queueService
        controller.subscriptionService = subscriptionService
    }

    void "show action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        controller.show(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "show action correctly handles a valid instance"() {

        when: "calling the action with a valid instance"

        controller.show(restSubscription)

        then: "respond with the show view"

        view == 'show'

        and: "the rest subscriber"

        model.restSubscriptionInstance ?: model.restSubscription == restSubscription
    }

    void "create action responds with a new instance"() {

        when: "calling the action with a null instance param"

        controller.create()

        then: "respond with the create view"

        view == 'create'

        and: "a new rest subscriber instance"

        model.restSubscriptionInstance ?: model.restSubscription != null
    }

    void "save action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        request.method = "POST"
        controller.save(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "save action correctly handles a null source url"() {

        given: "an unsaved instance with a null source url"

        def subscription = RestSubscription.buildWithoutSave(sourceUrl: null)

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(subscription)

        then: "respond with the create view with errors"

        view == "create"
        subscription.errors.errorCount == 1
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "save action correctly handles when content extraction service throws a service exception"() {

        given: "the excpetion to throw"

        def serviceException = new ServiceException("damn man, that was just wrong!")

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "throw an exception from the content extraction service"

        contentExtractionService.getMediaId(restSubscription.sourceUrl) >> {
            throw serviceException
        }

        and: "log the exception"

        1 * loggingService.logError("Error occurred when fetching the media item for sourceUrl=http://double.stuffed.com/good/cookies.html", serviceException) >> {
            return "whats the word homie!"
        }

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["whats the word homie!"]
    }

    void "save action correctly handles when content extraction service returns a null mediaId"() {

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "return a null mediaId from the content extraction service"

        contentExtractionService.getMediaId(restSubscription.sourceUrl) >> null

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["restSubscription.sourceUrl.not.syndicated"]
    }

    void "save action successfully creates a non-existent subscription and new rest subscription"() {

        given: "an unsaved rest subscription"

        def restSubscription = RestSubscription.buildWithoutSave(sourceUrl: "http://manburger.com/extra/cheese.html")

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "return a null mediaId from the content extraction service"

        contentExtractionService.getMediaId(restSubscription.sourceUrl) >> 123456

        and: "extract the content"

        contentExtractionService.extractSyndicatedContent("123456") >> {
            return new SyndicatedMediaItem(name:"The greatest media item")
        }

        and: "create a new subscription for the media id"

        Subscription.count == 2

        and: "save the rest subscription"

        RestSubscription.count == 2

        and: "put an import message on the rest update queue"

        1 * queueService.sendToRestUpdateQueue(MessageType.IMPORT, 2)

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/show/2"
        flash.message == "default.created.message"
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "save action successfully creates a existing subscription and new rest subscription"() {

        given: "an unsaved rest subscription"

        def restSubscription = RestSubscription.buildWithoutSave(sourceUrl: "http://manburger.com/extra/cheese.html")

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "return a mediaId from the content extraction service that matches an existing subscription"

        contentExtractionService.getMediaId(restSubscription.sourceUrl) >> subscription.mediaId

        and: "extract the content"

        1 * contentExtractionService.extractSyndicatedContent(subscription.mediaId) >> {
            return new SyndicatedMediaItem(name:"The greatest media item")
        }

        and: "create a new subscription for the media id"

        Subscription.count == 1

        and: "save the rest subscription"

        RestSubscription.count == 2

        and: "put an import message on the rest update queue"

        1 * queueService.sendToRestUpdateQueue(MessageType.IMPORT, 2)

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/show/2"
        flash.message == "default.created.message"
    }

    void "delete action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        request.method = "DELETE"
        controller.delete(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "delete action correctly deletes the instance and all rest subscriptions"() {

        when: "calling the action with a valid instance"

        request.method = "DELETE"
        controller.delete(restSubscription)

        then: "delete the rest subscription"

        1 * subscriptionService.deleteChildSubscription(restSubscription)

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/restSubscription/index"
        flash.message == "default.deleted.message"
    }
}
