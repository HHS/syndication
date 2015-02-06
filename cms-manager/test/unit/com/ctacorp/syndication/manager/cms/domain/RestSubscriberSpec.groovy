
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
import com.ctacorp.syndication.manager.cms.Subscriber
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RestSubscriber)
@Build([Subscriber, RestSubscriber])
class RestSubscriberSpec extends Specification {

    RestSubscriber newRestSubscriberInstance() {
        def subscriber = Subscriber.build()
        return RestSubscriber.buildWithoutSave(
            subscriber: subscriber,
            deliveryEndpoint: "http://bolognasandwiches.jp/international/sandwich/makers.json"
        )
    }

    void "a valid instance"() {

        given: "a valid rest subscriber"

        def restSubscriber = newRestSubscriberInstance()

        when: "the instance is validated"

        def validated = restSubscriber.validate()

        then: "validation should succeed"

        validated
    }

    void "delivery endpoint cannot be null"() {

        given: "a valid rest subscriber"

        def restSubscriber = newRestSubscriberInstance()

        when: "the delivery endpoint is set to null"

        restSubscriber.deliveryEndpoint = null

        then: "validation should fail"

        !restSubscriber.validate()
    }

    void "delivery endpoint cannot be blank"() {

        given: "a valid rest subscriber"

        def restSubscriber = newRestSubscriberInstance()

        when: "the delivery endpoint is set to blank"

        restSubscriber.deliveryEndpoint = ""

        then: "validation should fail"

        !restSubscriber.validate()
    }

    void "delivery endpoint must be a url"() {

        given: "a valid rest subscriber"

        def restSubscriber = newRestSubscriberInstance()

        when: "the delivery endpoint is set to blank"

        restSubscriber.deliveryEndpoint = "Bologna With Hamonaise On Marbled Rye, No Cheese, Extra Love"

        then: "validation should fail"

        !restSubscriber.validate()
    }

    void "delivery endpoint must be a unique"() {

        given: "a valid rest subscriber"

        newRestSubscriberInstance().save(flush:true)

        when: "attempting to validate a new instance with the same delivery endpoint"

        def restSubscriber2 = newRestSubscriberInstance()

        then: "validation should fail"

        !restSubscriber2.validate()
    }
}
