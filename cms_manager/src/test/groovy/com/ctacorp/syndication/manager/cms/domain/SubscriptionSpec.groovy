/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.domain
import com.ctacorp.syndication.manager.cms.Subscription
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Subscription)
@Build(Subscription)
class SubscriptionSpec extends Specification {

    Subscription subscription

    def setup() {
        Subscription.syndicationMediaUri = "http://spamandham.gov/api/v500/media/{id}.json"
        subscription = Subscription.buildWithoutSave(mediaId: 2)
    }

    void "valid constraints"() {

        given: "a valid instance"

        def subscription = subscription

        when: "the instance is validated"

        subscription.save(flush: true)

        then: "it will not have errors"

        0 == subscription.errors.errorCount
    }

    void "mediaId is required"() {

        given: "a valid instance"

        def subscription = subscription

        when: "the mediaId is set to null"

        subscription.mediaId = null

        and: "the instance is validated"

        subscription.validate()

        then: "it will have errors"

        1 == subscription.errors.errorCount
    }

    void "mediaId cannot be blank"() {

        given: "a valid instance"

        def subscription = subscription

        when: "the mediaId is set to an empty string"

        subscription.mediaId = ""

        and: "the instance is validated"

        subscription.validate()

        then: "it will have errors"

        1 == subscription.errors.errorCount
    }

    void "mediaUri is transient"() {

        given: "a valid instance"

        def subscription = subscription

        when: "getting the mediaUri from a subscription instance"

        def mediaUri = subscription.mediaUri

        then: "the mediaUri will be set to the value from the config"

        "http://spamandham.gov/api/v500/media/2.json" == mediaUri
    }

    void "mediaId must be unique"() {

        given: "a valid saved instance"

        def subscription1 = subscription
        assert subscription1.save(flush: true)

        when: "creating an instance with the same hostname"

        def subscription2 = Subscription.buildWithoutSave(mediaId: 2)

        and: "the instance is validated"

        subscription2.save(flush: true)

        then: "subscriber2 will have errors"

        1 == subscription2.errors.errorCount
    }
}
