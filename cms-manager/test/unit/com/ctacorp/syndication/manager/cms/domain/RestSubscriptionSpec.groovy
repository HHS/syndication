
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.domain

import com.ctacorp.syndication.manager.cms.RestSubscriber
import com.ctacorp.syndication.manager.cms.RestSubscription
import com.ctacorp.syndication.manager.cms.Subscription
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscription)
@Build([Subscription, RestSubscriber, RestSubscription])
class RestSubscriptionSpec extends Specification {

    RestSubscription newRestSubscriberInstance() {
        def subscription = Subscription.build()
        def restSubscriber = RestSubscriber.build(deliveryEndpoint: "http://bolognasandwiches.jp/international/sandwich/makers.json")
        return new RestSubscription(
            subscription: subscription,
            restSubscriber: restSubscriber,
            title: "Ham and Cheese Please!",
            sourceUrl: "http://hamandcheeseplease.gov/hamandcheese/foryourgoodhealth.html"
        )
    }

    void "valid instance"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its source url to null"

        def validated = restSubscription.validate()

        then: "validation should fail"

        validated
    }

    void "source url cannot be null"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its source url to null"

        restSubscription.sourceUrl = null

        then: "validation should fail"

        !restSubscription.validate()
    }

    void "source url cannot be blank"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its source url to blank"

        restSubscription.sourceUrl = ""

        then: "validation should fail"

        !restSubscription.validate()
    }

    void "source url must be a url"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its source url to blank"

        restSubscription.sourceUrl = "dude"

        then: "validation should fail"

        !restSubscription.validate()
    }

    void "title cannot be null"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its title to null"

        restSubscription.title = null

        then: "validation should fail"

        !restSubscription.validate()
    }

    void "title cannot be blank"() {

        given: "a valid rest subscription instance"

        def restSubscription = newRestSubscriberInstance()

        when: "setting its title to blank"

        restSubscription.title = ""

        then: "validation should fail"

        !restSubscription.validate()
    }
}
