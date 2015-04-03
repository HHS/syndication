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
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitDelayJobScheduler
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RhythmyxSubscriptionController)
@Build([RhythmyxSubscription, RhythmyxSubscriber, Subscriber, Subscription, RhythmyxWorkflow])
class RhythmyxSubscriptionControllerSpec extends Specification {

    def ingestionService = Mock(RhythmyxIngestionService)
    def queueService = Mock(QueueService)
    def loggingService = Mock(LoggingService)
    def rhythmyxSubscriptionTransitionService = Mock(RhythmyxSubscriptionTransitionService)
    def contentExtractionService = Mock(ContentExtractionService)
    def subscriptionService = Mock(SubscriptionService)
    def rabbitDelayJobScheduler = Mock(RabbitDelayJobScheduler)
    def userSubscriberService = Mock(UserSubscriberService)

    Subscriber subscriber
    Subscription subscription
    RhythmyxSubscription rhythmyxSubscription
    RhythmyxSubscriber rhythmyxSubscriber
    RhythmyxWorkflow rhythmyxWorkflow

    def setup() {

        subscriber = Subscriber.build()
        subscription = Subscription.build(mediaId: "1")
        rhythmyxSubscriber = RhythmyxSubscriber.build(instanceName: 'manburger.gov', subscriber: subscriber)
        rhythmyxSubscription = RhythmyxSubscription.build(sourceUrl: "http://hamsandwiches.for.all.com/no/mayo", rhythmyxSubscriber: rhythmyxSubscriber, subscription: subscription)
        rhythmyxWorkflow = RhythmyxWorkflow.build()

        // For some reason, taking this out causes tests to fail due to a timing issue?
        Subscriber.count == 1
        Subscription.count == 1
        RhythmyxSubscriber.count == 1
        RhythmyxSubscription.count == 1

        controller.rhythmyxIngestionService = ingestionService
        controller.queueService = queueService
        controller.loggingService = loggingService
        controller.rhythmyxSubscriptionTransitionService = rhythmyxSubscriptionTransitionService
        controller.subscriptionService = subscriptionService
        controller.contentExtractionService = contentExtractionService
        controller.rabbitDelayJobScheduler = rabbitDelayJobScheduler
        controller.userSubscriberService = userSubscriberService
    }

    void "show action correctly handles a valid instance"() {

        when: "calling the show action with a valid instance"

        controller.show(rhythmyxSubscription)

        then: "respond with the show view and the rhythmyx subscription"

        view == 'show'
        model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription == rhythmyxSubscription
    }

    void "show action correctly handles a null instance"() {

        when: "calling the show action with a null instance"

        controller.show(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "create action correctly handles null instance name"() {

        given: "the sys_folderid param was provided"

        params.sys_folderid = '12345'

        when: "calling the create action with a null instance param"

        controller.create()

        then: "fetch the list of accessible rhythmyx subscribers"

        1 * userSubscriberService.listRhythmyxSubscribers() >> [rhythmyxSubscriber]

        and: "expect an null list of content types on the flash object"

        flash.contentTypes == null
        view == "create"

        expect: "respond with a new rhythmyx subscription"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        !rhythmyxSubscription.rhythmyxSubscriber
        rhythmyxSubscription.subscription
    }

    void "create action handles when user does not have access to any subscribers"() {

        given: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = 'spamburger.gov'

        when: "calling the create action with an unknown instance param"

        controller.create()

        then: "fetch the list of accessible rhythmyx subscribers"

        1 * userSubscriberService.listRhythmyxSubscribers() >> []

        and: "redirect to the index with an error message"

        response.redirectUrl == "/index"
        flash.errors == ['rhythmyxSubscription.noAssociatedRhythmyxSubscribers']
    }

    void "create action correctly handles when the rhythmyx subscriber is not found"() {

        given: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = 'spamburger.gov'

        when: "calling the create action with an unknown instance param"

        controller.create()

        then: "fetch the list of accessible rhythmyx subscribers"

        1 * userSubscriberService.listRhythmyxSubscribers() >> [rhythmyxSubscriber]

        and: "expect an null list of content types on the flash object"

        flash.contentTypes == null
        view == "create"

        expect: "the model contains the correct values"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        !rhythmyxSubscription.rhythmyxSubscriber
        rhythmyxSubscription.subscription
    }

    void "create action correctly handles when the ingestion service throws an exception"() {

        setup: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = rhythmyxSubscriber.instanceName

        and: "the exception to be thrown"

        def exception = new Exception("monkey farts")

        when: "calling the create action with a null instance param"

        controller.create()

        then: "fetch the list of accessible rhythmyx subscribers"

        1 * userSubscriberService.listRhythmyxSubscribers() >> [rhythmyxSubscriber]

        and: "fetch the content types"

        ingestionService.getContentTypes(this.rhythmyxSubscriber) >> {
            throw exception
        }

        and: "sanitize the error before rendering the response"

        loggingService.logError("Error occurred when fetching the content types for rhythmyxSubscriber 'manburger.gov'", exception) >> {
            "this is a much better error for our user. don't you agree sir?"
        }

        and: "redirect to the index view with a not found message"

        flash.errors == ["this is a much better error for our user. don't you agree sir?"]
        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
    }

    void "create action successfully loads the content types for a rhythmyx instance"() {

        given: "the sys_folderid and instance query params"

        params.sys_folderid = '12345'
        params.instance = rhythmyxSubscriber.instanceName

        and: "the content types to be returned"

        def contentTypes = ['MonkeyNewsFormat(.mnf)', 'ReturnOfTheGangsterNews(.rotgn)']

        when: "calling the create action with a null instance param"

        controller.create()

        then: "fetch the list of accessible rhythmyx subscribers"

        1 * userSubscriberService.listRhythmyxSubscribers() >> [rhythmyxSubscriber]

        and: "fetch the content types"

        ingestionService.getContentTypes(rhythmyxSubscriber) >> contentTypes

        and: "the model contains the correct values"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        rhythmyxSubscription.rhythmyxSubscriber == rhythmyxSubscriber
        rhythmyxSubscription.subscription
    }

    void "save action correctly handles a null instance"() {

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "save action correctly handles a null rhythmyx subscriber"() {

        given: "an unsaved instance with a null subscriber"

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(sourceUrl: "http://something.somewhere.com/sometime", rhythmyxSubscriber: null)

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "respond with the create view and domain errors"

        rhythmyxSubscription.errors.errorCount == 1
        view == 'create'
    }

    void "save action correctly handles an inaccessible rhythmyx subscriber"() {

        given: "an unsaved instance with a null subscriber"

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(sourceUrl: "http://something.somewhere.com/sometime", rhythmyxSubscriber: rhythmyxSubscriber)

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> false

        and: "redirect to the the index with an error message"

        response.redirectUrl == '/index'
        flash.errors == ['rhythmyxSubscription.accessDeniedToRhythmyxSubscriber']
    }

    void "save action correctly handles a null source url"() {

        given: "a rhythmyx subscription instance with a null source url"

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(sourceUrl: null, rhythmyxSubscriber: rhythmyxSubscriber)

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "respond with the create view and domain errors"

        rhythmyxSubscription.errors.errorCount == 1
        view == 'create'
    }

    void "save action successfully creates a non-existent subscription and new rhythmyx subscription"() {

        given: "a rhythmyx subscription instance"

        def rhythmyxSubscription2 = RhythmyxSubscription.buildWithoutSave(rhythmyxSubscriber: rhythmyxSubscriber, sourceUrl: "http://something.somewhere.com/sometime")

        when: "calling the save action"

        request.method = 'POST'
        controller.save(rhythmyxSubscription2)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "fetch the media item from syndication"

        contentExtractionService.getMediaItemBySourceUrl(rhythmyxSubscription2.sourceUrl) >> new MediaItem(id: 123456, name: "The Greatest Media Item in the World!")

        and: "create and save a new subscription"

        Subscription.count == 2

        and: "set the title"

        rhythmyxSubscription2.systemTitle == "The Greatest Media Item in the World!"

        and: "save the rhythmyx subscription"

        RhythmyxSubscription.count == 2

        and: "schedule a new rabbit import job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.IMPORT, rhythmyxSubscription2)

        and: "redirect to the show view with the rhythmyx subscription"

        response.redirectUrl == "/show/2" || response.redirectUrl == "/rhythmyxSubscription/show/2"
        flash.message == "default.created.message"
    }

    void "save action successfully creates a new rest subscription for an existing subscription"() {

        setup: "a rhythmyx subscription instance and an existing subscription"

        def subscription = Subscription.build(mediaId: "9999")

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(
                rhythmyxSubscriber: rhythmyxSubscriber,
                sourceUrl: "http://something.somewhere.com/sometime"
        )

        when: "calling the save action"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "fetch the media item from syndication"

        contentExtractionService.getMediaItemBySourceUrl(rhythmyxSubscription.sourceUrl) >> new MediaItem(id: new Long(subscription.mediaId), name: "The Greatest Media Item in the World!")

        and: "save the existing subscription on the rhythmyx subscription"

        Subscription.count == 2
        rhythmyxSubscription.subscription == subscription

        and: "set the title"

        rhythmyxSubscription.systemTitle == "The Greatest Media Item in the World!"

        and: "save the rhythmyx subscription"

        RhythmyxSubscription.count == 2

        then: "schedule a new rabbit import job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.IMPORT, rhythmyxSubscription)

        and: "redirect to the show view with the rhythmyx subscription"

        response.redirectUrl == "/show/2" || response.redirectUrl == "/rhythmyxSubscription/show/2"
        flash.message == "default.created.message"
    }

    void "save action successfully save the default workflow on the subscriber"() {

        setup: "a rhythmyx subscription instance and an existing subscription"

        def subscription = Subscription.build(mediaId: "9999")

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(
                rhythmyxSubscriber: rhythmyxSubscriber,
                sourceUrl: "http://something.somewhere.com/sometime",
                rhythmyxWorkflow: rhythmyxWorkflow
        )

        and: "the default workflow param is true"

        params.useAsDefaultWorkflow = true

        when: "calling the save action"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "fetch the media item from syndication"

        contentExtractionService.getMediaItemBySourceUrl(rhythmyxSubscription.sourceUrl) >> new MediaItem(id: new Long(subscription.mediaId), name: "The Greatest Media Item in the World!")

        and: "save the existing subscription on the rhythmyx subscription"

        Subscription.count == 2
        rhythmyxSubscription.subscription == subscription

        and: "set the title"

        rhythmyxSubscription.systemTitle == "The Greatest Media Item in the World!"

        and: "save the rhythmyx subscription"

        RhythmyxSubscription.count == 2

        and: "save the default workflow"

        rhythmyxSubscriber.rhythmyxWorkflow == rhythmyxWorkflow

        then: "schedule a new rabbit import job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.IMPORT, rhythmyxSubscription)

        and: "redirect to the show view with the rhythmyx subscription"

        response.redirectUrl == "/show/2" || response.redirectUrl == "/rhythmyxSubscription/show/2"
        flash.message == "default.created.message"
    }

    void "edit action handles null instance correctly"() {

        when: "calling the edit action with a null instance"

        request.method = 'POST'
        controller.edit(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "edit action handles valid instance correctly"() {

        given: "a rhythmyx subscription instance with a null sourceUrl"

        def rhythmyxSubscription = RhythmyxSubscription.build().save(flush: true)

        when: "calling the edit action with a valid instance"

        request.method = 'POST'
        controller.edit(rhythmyxSubscription)

        then: "respond with the show view and the rhythmyx subscription"

        view == 'edit'
        model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription == rhythmyxSubscription
    }

    void "update action handles null instance correctly"() {

        when: "calling the update action with a null instance"

        request.method = 'PUT'
        controller.update(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "update action handles invalid instance correctly"() {

        given: "a rhythmyx subscription instance with a null sourceUrl"

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(sourceUrl: null)

        when: "calling the update action with a null instance"

        request.method = 'PUT'
        controller.update(rhythmyxSubscription)

        then: "respond with the edit view and domain errors"

        rhythmyxSubscription.errors.errorCount == 1
        view == 'edit'
    }

    void "update action successfully updates a rhythmyx subscription"() {

        given: "a rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build(sourceUrl: "http://cardio.manburger.gov/getbig.html")
        rhythmyxSubscription.sourceUrl = "http://spamforham.us/GODBLESS"

        when: "calling the update action with a valid instance"

        request.method = 'PUT'
        controller.update(rhythmyxSubscription)

        then: "redirect to the show view with the updated instance and an updated message"

        flash.message == "default.updated.message"
        response.redirectUrl == "/show/2" || response.redirectUrl == "/rhythmyxSubscription/show/2"
        rhythmyxSubscription.sourceUrl == "http://spamforham.us/GODBLESS"
    }

    void "delete action handles null instance correctly"() {

        when: "calling the delete action with a null instance"

        request.method = 'DELETE'
        controller.delete(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["default.not.found.message"]
    }

    void "delete action handles an inaccessible rhythmyx subscriber"() {

        when: "calling the delete action with a null instance"

        request.method = 'DELETE'
        controller.delete(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> false

        and: "redirect to the index view with an error message"

        response.redirectUrl == "/index"
        flash.errors == ["rhythmyxSubscription.accessDeniedToRhythmyxSubscriber"]
    }

    void "delete action queues a delete message"() {

        when: "calling the delete action with a valid instance"

        request.method = 'DELETE'
        controller.delete(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "schedule a new rabbit delete job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.DELETE, rhythmyxSubscription)

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.message == "rhythmyxSubscription.delete.queued.message"
    }

    void "delete action handles when queue service throws"() {

        given: "the exception to throw"

        def exception = new RuntimeException()

        when: "calling the delete action with a valid instance"

        request.method = 'DELETE'
        controller.delete(rhythmyxSubscription)

        then: "check that the user has access to the rhythmyx subscriber"

        userSubscriberService.hasAccess(rhythmyxSubscriber) >> true

        and: "schedule a new rabbit delete job"

        1 * rabbitDelayJobScheduler.schedule(MessageType.DELETE, rhythmyxSubscription) >> {
            throw exception
        }

        then: "log the exception and create a clean message"

        1 * loggingService.logError("Error occurred when queuing a delete message for rhythmyxSubscription '${rhythmyxSubscription.id}'", exception) >> "A better message"

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.errors == ["A better message"]
    }
}
