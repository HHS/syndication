
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.domain

import com.ctacorp.syndication.manager.cms.RhythmyxSubscriber
import com.ctacorp.syndication.manager.cms.Subscriber
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RhythmyxSubscriber)
@Build(Subscriber)
class RhythmyxSubscriberSpec extends Specification {

    RhythmyxSubscriber newRhythmyxSubscriberInstance() {
        def subscriber = Subscriber.build()
        return new RhythmyxSubscriber(
                subscriber: subscriber,
                instanceName: "tobaccoBurgers",
                rhythmyxHost: "www.spamburger.gov",
                rhythmyxPort: "9999",
                rhythmyxUser: "adminBadman",
                rhythmyxPassword: "passwordBadword",
                rhythmyxCommunity: "spamFanclub"
        )
    }

    void "valid constraints"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the instance is validated"

            rhythmyxSubscriber.save(flush:true)

        then: "it will not have errors"

            0 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxHostname is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxHostname is set to null"

            rhythmyxSubscriber.rhythmyxHost = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxHostname cannot be blank"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxHostname is set to an empty string"

            rhythmyxSubscriber.rhythmyxHost = ""

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "instanceName is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxHostname is set to null"

            rhythmyxSubscriber.instanceName = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "instanceName cannot be blank"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxHostname is set to an empty string"

            rhythmyxSubscriber.instanceName = ""

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "instanceName must be unique"() {

        given: "a valid saved instance"

            def subscriber1 = newRhythmyxSubscriberInstance()
            assert subscriber1.save(flush:true)

        when: "creating an instance with the same hostname"

            def subscriber2 = new RhythmyxSubscriber(
                subscriber: subscriber1.subscriber,
                instanceName: "tobaccoBurgers",
                rhythmyxHost: "sadasdasdasd",
                rhythmyxPort: "9999",
                rhythmyxUser: "asdasdasdas",
                rhythmyxPassword: "dsfsdfsdfdsfs",
                rhythmyxCommunity: "dsfdsfsdfsdfsdf"
            )

        and: "the instance is validated"

            subscriber2.save(flush:true)

        then: "subscriber2 will have errors"

            1 == subscriber2.errors.errorCount
    }

    void "rhythmyxPort is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxPort is set to null"

            rhythmyxSubscriber.rhythmyxPort = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxPort must be a number"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxPort is set to an alpha string"

            rhythmyxSubscriber.rhythmyxPort = "abcd"

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxUser is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxUser is set to null"

            rhythmyxSubscriber.rhythmyxUser = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxUser cannot be blank"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxUser is set to an empty string"

            rhythmyxSubscriber.rhythmyxUser = ""

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxPassword is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxPassword is set to null"

            rhythmyxSubscriber.rhythmyxPassword = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxPassword cannot be blank"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxPassword is set to an empty string"

            rhythmyxSubscriber.rhythmyxPassword = ""

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxCommunity is required"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxCommunity is set to null"

            rhythmyxSubscriber.rhythmyxCommunity = null

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }

    void "rhythmyxCommunity cannot be blank"() {

        given: "a valid instance"

            def rhythmyxSubscriber = newRhythmyxSubscriberInstance()

        when: "the rhythmyxCommunity is set to an empty string"

            rhythmyxSubscriber.rhythmyxCommunity = ""

        and: "the instance is validated"

            rhythmyxSubscriber.validate()

        then: "it will have errors"

            1 == rhythmyxSubscriber.errors.errorCount
    }
}
