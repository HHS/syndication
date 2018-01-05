/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.controller
import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscriberController)
@Build([Subscriber, RestSubscriber, Subscription, RestSubscription])
class RestSubscriberControllerSpec extends Specification {

    Subscriber subscriber
    Subscription subscription
    RestSubscriber restSubscriber
    RestSubscriber restSubscriber2
    RestSubscription restSubscription

    def setup() {
        subscriber = Subscriber.build()
        subscription = Subscription.build()
        restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp", subscriber: subscriber)
        restSubscriber2 = RestSubscriber.build(deliveryEndpoint: "http://thebuttertoremovetheclutter.gov/signup/index.html", subscriber: subscriber)
        restSubscription = RestSubscription.build(sourceUrl: "http://double.stuffed.com/good/cookies.html", restSubscriber: restSubscriber, subscription: subscription)
    }

    void "show action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        controller.show(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.not.found.message"]
    }

    void "show action correctly handles a valid instance"() {

        when: "calling the action with a valid instance"

        controller.show(restSubscriber)

        then: "respond with the show view"

        view == 'show'

        and: "the rest subscriber"

        model.restSubscriberInstance ?: model.restSubscriber == restSubscriber
    }

    void "create action responds with a new instance"() {

        when: "calling the action with a null instance param"

        controller.create()

        then: "respond with the create view"

        view == 'create'

        and: "a new rest subscriber instance"

        model.restSubscriberInstance ?: model.restSubscriber != null
    }

    void "edit action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        controller.edit(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.not.found.message"]
    }

    void "edit action correctly handles a valid instance"() {

        when: "calling the edit action with the instance"

        controller.edit(restSubscriber)

        then: "respond with the create view"

        view == 'edit'

        and: "the rest subscriber instance"

        model.restSubscriberInstance ?: model.restSubscriber == restSubscriber
    }

    void "update action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        request.method = "PUT"
        controller.update(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.not.found.message"]
    }

    void "update action correctly handles an invalid instance"() {

        given: "an instance with a non-url delivery endpoint"

        restSubscriber.deliveryEndpoint = "hamAndCheese"

        when: "calling the action with an invalid instance"

        request.method = "PUT"
        controller.update(restSubscriber)

        then: "respond with the edit view with errors"

        view == "edit"
        restSubscriber.errors.errorCount == 1
    }

    void "update action correctly handles a valid instance"() {

        given: "an instance with an updated delivery endpoint"

        restSubscriber.deliveryEndpoint = "http://dude.man.gov/burgers.html"

        when: "calling the action with an invalid instance"

        request.method = "PUT"
        controller.update(restSubscriber)

        then: "respond with the show view an updated"

        response.redirectUrl == "/restSubscriber/show/1"
        restSubscriber.deliveryEndpoint == "http://dude.man.gov/burgers.html"
    }

    void "save action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        request.method = "POST"
        controller.save(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.not.found.message"]
    }

    void "save action correctly handles a duplicate rest subscriber"() {

        given: "an unsaved instance with a duplicate delivery endpoint"

        def restSubscriber = RestSubscriber.buildWithoutSave(
            deliveryEndpoint: "http://cookiesforthehomeless.gov/donate/now.asp"
        )

        params.subscriber = subscriber

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscriber)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.exists.message"]
    }

    void "save action correctly handles an invalid instance"() {

        given: "an unsaved instance with an invalid delivery endpoint"

        def restSubscriber = RestSubscriber.buildWithoutSave(deliveryEndpoint: "http://barfoo.organization")

        params.subscriber = subscriber

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscriber)

        then: "respond with the create view with errors"

        view == "create"
        restSubscriber.errors.errorCount == 1
    }

    void "save action correctly handles an valid instance"() {

        given: "a valid unsaved instance"

        def restSubscriber = RestSubscriber.buildWithoutSave(deliveryEndpoint: "http://whipped.cream.gov/good/on/pie.html")
        params.subscriber = subscriber

        when: "calling the action with the instance"

        request.method = "POST"
        controller.save(restSubscriber)

        then: "respond with the create view with a created message"

        response.redirectUrl == "/restSubscriber/show/3"
        flash.message == "default.created.message"
    }

    void "delete action correctly handles a null instance"() {

        when: "calling the action with a null instance"

        request.method = "DELETE"
        controller.delete(null)

        then: "redirect to the index view with a not found message"

        response.redirectUrl == "/restSubscriber/index"
        flash.errors == ["default.not.found.message"]
    }

    void "delete action correctly deletes the instance and all rest subscriptions"() {

        when: "calling the action with a valid instance"

        request.method = "DELETE"
        controller.delete(restSubscriber)

        then: "delete all rest subscriptions belonging to the instance"

        RestSubscription.count == 0

        and: "delete the instance"

        RestSubscriber.count == 1

        and: "redirect to the index view with a deleted message"

        response.redirectUrl == "/restSubscriber/index"
        flash.message == "default.deleted.message"
    }
}
