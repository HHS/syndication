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
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitDelayJobScheduler
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
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
    def rabbitDelayJobScheduler = Mock(RabbitDelayJobScheduler)

    Subscriber subscriber
    Subscription subscription
    RestSubscriber restSubscriber
    RestSubscription restSubscription

    def setup() {

        subscriber = Subscriber.build()
        subscription = Subscription.build(mediaId: "1")
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", restSubscriber: restSubscriber, subscription: subscription)

        controller.loggingService = loggingService
        controller.contentExtractionService = contentExtractionService
        controller.queueService = queueService
        controller.subscriptionService = subscriptionService
        controller.rabbitDelayJobScheduler = rabbitDelayJobScheduler
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

        then: "throw an exception when trying to get the media item for the source url"

        contentExtractionService.getMediaItemBySourceUrl(restSubscription.sourceUrl) >> {
            throw serviceException
        }

        and: "log the exception"

        1 * loggingService.logError("An error occurred when trying to get the media item associated with sourceUrl 'http://double.stuffed.com/good/cookies.html'", serviceException) >> {
            return "whats the word homie!"
        }

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/index"
        flash.errors == ["whats the word homie!"]
    }

    void "save action correctly handles when content extraction service returns a null mediaId"() {
        setup:
        MediaItem mediaItem = null

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "return a null mediaId from the content extraction service"

        contentExtractionService.getMediaItemBySourceUrl(restSubscription.sourceUrl) >> mediaItem

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

        then: "get the media item for the source url"

        contentExtractionService.getMediaItemBySourceUrl(restSubscription.sourceUrl) >> new MediaItem(id: 123456, name: "The Greatest Media Item in the World")

        and: "schedule a new rabbit import job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.IMPORT, restSubscription)

        and: "set the title"

        restSubscription.title == "The Greatest Media Item in the World"

        and: "set the status to pending"

        restSubscription.isPending

        and: "create a new subscription for the media id"

        Subscription.count == 2

        and: "set it on the rest subscription"

        restSubscription.subscription.mediaId == "123456"

        and: "save the rest subscription"

        RestSubscription.count == 2

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/show/2"
        flash.message == "default.created.message"
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void "save action successfully creates a new rest subscription"() {

        given: "an unsaved rest subscription"

        def restSubscription = RestSubscription.buildWithoutSave(sourceUrl: "http://manburger.com/extra/cheese.html")

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscription)

        then: "get the media item for the source url"

        contentExtractionService.getMediaItemBySourceUrl(restSubscription.sourceUrl) >> new MediaItem(id: new Long(subscription.mediaId), name: "The Best Media Item According to Someone")

        and: "set the title"

        restSubscription.title == "The Best Media Item According to Someone"

        and: "set the status to pending"

        restSubscription.isPending

        and: "create a new subscription for the media id"

        Subscription.count == 1

        and: "set it on the rest subscription"

        restSubscription.subscription == subscription

        and: "schedule a new rabbit import job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.IMPORT, restSubscription)

        and: "save the rest subscription"

        RestSubscription.count == 2

        and: "redirect to the index view with a nice error message"

        response.redirectUrl == "/restSubscription/show/2"
        flash.message == "default.created.message"
    }

    void "delete action queues a delete message"() {

        given: "a rest subscription instance"

        def restSubscription = RestSubscription.build().save(flush: true)
        RestSubscription.count == 1

        when: "calling the delete action with a valid instance"

        request.method = 'DELETE'
        controller.delete(restSubscription)

        then: "schedule a new rabbit delete job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.DELETE, restSubscription)

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/index" || response.redirectUrl == "/restSubscription/index"
        flash.message == "restSubscription.delete.queued.message"
    }

    void "delete action handles when the job scheduler throws"() {

        given: "a rest subscription instance"

        def restSubscription = RestSubscription.build().save(flush: true)
        RestSubscription.count == 1

        and: "the exception to throw"

        def exception = new RuntimeException()

        when: "calling the delete action with a valid instance"

        request.method = 'DELETE'
        controller.delete(restSubscription)

        then: "schedule a new rabbit delete job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.DELETE, restSubscription) >> {
            throw exception
        }

        and: "log the exception and create a clean message"

        1 * loggingService.logError("Error occurred when queuing a delete message for restSubscription '${restSubscription.id}'", exception) >> "A better message"

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/index" || response.redirectUrl == "/restSubscription/index"
        flash.errors == ["A better message"]
    }
}
