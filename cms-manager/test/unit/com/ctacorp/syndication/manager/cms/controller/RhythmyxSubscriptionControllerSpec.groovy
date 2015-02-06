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
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RhythmyxSubscriptionController)
@Build([RhythmyxSubscription, RhythmyxSubscriber])
class RhythmyxSubscriptionControllerSpec extends Specification {

    def ingestionService = Mock(RhythmyxIngestionService)
    def queueService = Mock(QueueService)
    def loggingService = Mock(LoggingService)
    def rhythmyxSubscriptionTransitionService = Mock(RhythmyxSubscriptionTransitionService)
    def subscriptionService = Mock(SubscriptionService)

    def setup() {

        controller.rhythmyxIngestionService = ingestionService
        controller.queueService = queueService
        controller.loggingService = loggingService
        controller.rhythmyxSubscriptionTransitionService = rhythmyxSubscriptionTransitionService
        controller.subscriptionService = subscriptionService
    }

    void "show action correctly handles a valid instance"() {

        given: "a valid rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build()

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

        then: "expect an null list of content types on the flash object"

        flash.contentTypes == null
        view == "create"

        expect: "the model contains the correct values"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        !rhythmyxSubscription.rhythmyxSubscriber
        rhythmyxSubscription.subscription
    }

    void "create action correctly handles when the rhythmyx subscriber is not found"() {

        given: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = 'manburger.gov'

        when: "calling the create action with a null instance param"

        controller.create()

        then: "expect an null list of content types on the flash object"

        flash.contentTypes == null
        view == "create"

        expect: "the model contains the correct values"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        !rhythmyxSubscription.rhythmyxSubscriber
        rhythmyxSubscription.subscription
    }

    void "create action correctly handles when content types service throws and exception"() {

        given: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = 'manburger.gov'

        and: "a valid rhythmyx subscriber instance"

        def rhythmyxSubscriber = RhythmyxSubscriber.build(instanceName: 'manburger.gov').save(flush: true)

        and: "the exception to be thrown"

        def exception = new Exception("monkey farts")

        when: "calling the create action with a null instance param"

        controller.create()

        then: "fetch the content types"

        ingestionService.getContentTypes(rhythmyxSubscriber) >> {
            throw exception
        }

        and: "sanitize the error before rendering the response"

        loggingService.logError("Error occurred when fetching the content types for rhythmyxSubscriber=${rhythmyxSubscriber.instanceName}", exception) >> {
            "this is a much better error for our user. don't you agree sir?"
        }

        then: "redirect to the index view with a not found message"

        flash.errors == ["this is a much better error for our user. don't you agree sir?"]
        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
    }

    void "create action successfully loads the content types for a rhythmyx instance"() {

        given: "the sys_folderid and instance params are provided"

        params.sys_folderid = '12345'
        params.instance = 'spamburger.gov'

        and: "a two rhythmyx subscriber instances"

        RhythmyxSubscriber.build(instanceName: 'manburger.gov').save(flush: true)
        def rhythmyxSubscriber2 = RhythmyxSubscriber.build(instanceName: 'spamburger.gov').save(flush: true)

        and: "the content types to be returned"

        def contentTypes = ['MonkeyNewsFormat(.mnf)', 'ReturnOfTheGangsterNews(.rotgn)']

        when: "calling the create action with a null instance param"

        controller.create()

        then: "fetch the content types"

        ingestionService.getContentTypes(rhythmyxSubscriber2) >> contentTypes

        and: "the model contains the correct values"

        def rhythmyxSubscription = model.rhythmyxSubscriptionInstance ?: model.rhythmyxSubscription
        rhythmyxSubscription.targetFolder == 12345
        rhythmyxSubscription.rhythmyxSubscriber == rhythmyxSubscriber2
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

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(rhythmyxSubscriber: null)

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "respond with the create view and domain errors"

        rhythmyxSubscription.errors.errorCount == 1
        view == 'create'
    }

    void "save action correctly handles missing required field"() {

        given: "a rhythmyx subscription instance with a null sourceUrl"

        def rhythmyxSubscription = RhythmyxSubscription.buildWithoutSave(sourceUrl: null)

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "respond with the create view and domain errors"

        rhythmyxSubscription.errors.errorCount == 1
        view == 'create'
    }

    void "save action successfully sends an import message to tghe update queue"() {

        given: "a rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build()

        when: "calling the save action with a null instance"

        request.method = 'POST'
        controller.save(rhythmyxSubscription)

        then: "send an import message to the rhythmyx update queue"

        queueService.sendToRhythmyxUpdateQueue(MessageType.IMPORT, rhythmyxSubscription.id)

        expect: "redirect to the show view with the rhythmyx subscription"

        response.redirectUrl == "/show/1" || response.redirectUrl == "/rhythmyxSubscription/show/1"
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

    void "update action is successful"() {

        given: "a rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build(sourceUrl: "http://cardio.manburger.gov/getbig.html")
        rhythmyxSubscription.sourceUrl = "http://spamforham.us/GODBLESS"

        when: "calling the update action with a valid instance"

        request.method = 'PUT'
        controller.update(rhythmyxSubscription)

        then: "redirect to the show view with the updated instance and an updated message"

        flash.message == "default.updated.message"
        response.redirectUrl == "/show/1" || response.redirectUrl == "/rhythmyxSubscription/show/1"
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

    void "delete action handles an invalid instance correctly"() {

        when: "calling the delete action with an invalid instance"

        request.method = 'DELETE'
        controller.delete(RhythmyxSubscription.buildWithoutSave())

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.message == "default.deleted.message"
    }

    void "delete action handles a valid instance correctly"() {

        given: "a rhythmyx subscription instance"

        def rhythmyxSubscription = RhythmyxSubscription.build().save(flush: true)
        RhythmyxSubscription.count == 1

        when: "calling the delete action with a valid instance"

        request.method = 'DELETE'
        controller.delete(rhythmyxSubscription)

        then: "transition the item using delete transitions"

        1 * rhythmyxSubscriptionTransitionService.doDeleteTransitions([rhythmyxSubscription])

        then: "delete the rhythmyx subscription"

        1 * subscriptionService.deleteChildSubscription(rhythmyxSubscription)

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/index" || response.redirectUrl == "/rhythmyxSubscription/index"
        flash.message == "default.deleted.message"
    }
}
