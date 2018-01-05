/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.controller
import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.rest.AuthorizationController
import com.ctacorp.syndication.manager.cms.utils.marshalling.KeyAgreementMarshaller
import com.ctacorp.syndication.manager.cms.utils.marshalling.SubscriberMarshaller
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AuthorizationController)
@Build([Subscriber, KeyAgreement])
class AuthorizationControllerSpec extends Specification {

    Subscriber subscriber1
    Subscriber subscriber2

    KeyAgreement keyAgreement1
    KeyAgreement keyAgreement2

    @SuppressWarnings("GroovyResultOfObjectAllocationIgnored")
    def setup() {

        keyAgreement1 = KeyAgreement.build(entity2PublicKey: "SUBSCRIBER_1_PUBLIC_KEY")
        keyAgreement2 = KeyAgreement.build(entity2PublicKey: "SUBSCRIBER_2_PUBLIC_KEY")

        subscriber1 = Subscriber.build(keyAgreement: keyAgreement1)
        subscriber2 = Subscriber.build(keyAgreement: keyAgreement2)

        new SubscriberMarshaller("http://donkey.time.gov")
        new KeyAgreementMarshaller()
    }

    @SuppressWarnings(["GrUnresolvedAccess", "GroovyAssignabilityCheck"])
    void "get subscribers renders all subscribers' information as json"() {

        when: "getting all subscriptions"

        controller.getSubscribers()

        then: "render a list of subscription information JSON objects"

        response.status == 200

        response.json[0].id == subscriber1.subscriberId
        response.json[0].name == subscriber1.name
        response.json[0].email == subscriber1.email
        response.json[0].uri == "http://donkey.time.gov/api/v1/subscriber.json?id=${subscriber1.subscriberId}"
        response.json[0].keyAgreement.publicKey == "SUBSCRIBER_1_PUBLIC_KEY"
        response.json[0].keyAgreement.dateCreated

        response.json[1].id == subscriber2.subscriberId
        response.json[1].name == subscriber2.name
        response.json[1].email == subscriber2.email
        response.json[1].uri == "http://donkey.time.gov/api/v1/subscriber.json?id=${subscriber2.subscriberId}"
        response.json[1].keyAgreement.publicKey == "SUBSCRIBER_2_PUBLIC_KEY"
        response.json[1].keyAgreement.dateCreated
    }

    @SuppressWarnings(["GrUnresolvedAccess", "GroovyAssignabilityCheck"])
    void "get subscriber by public key renders a subscriber's information as json"() {

        setup: "set the subscriber's public key as a request param"

        params.publicKey = keyAgreement1.entity2PublicKey

        when: "getting all subscriptions"

        controller.getSubscriber()

        then: "render a list of subscription information JSON objects"

        response.status == 200

        response.json.id == subscriber1.subscriberId
        response.json.name == subscriber1.name
        response.json.email == subscriber1.email
        response.json.uri == "http://donkey.time.gov/api/v1/subscriber.json?id=${subscriber1.subscriberId}"
        response.json.keyAgreement.publicKey == "SUBSCRIBER_1_PUBLIC_KEY"
        response.json.keyAgreement.dateCreated
    }

    @SuppressWarnings(["GrUnresolvedAccess", "GroovyAssignabilityCheck"])
    void "get subscriber by subscriber id renders a subscriber's information as json"() {

        setup: "set the subscriber's subscriberId as a request param"

        params.id = Subscriber.generateSubscriberId(subscriber1)

        when: "getting all subscriptions"

        controller.getSubscriber()

        then: "render a list of subscription information JSON objects"

        response.status == 200

        response.json.id == subscriber1.subscriberId
        response.json.name == subscriber1.name
        response.json.email == subscriber1.email
        response.json.uri == "http://donkey.time.gov/api/v1/subscriber.json?id=${subscriber1.subscriberId}"
        response.json.keyAgreement.publicKey == "SUBSCRIBER_1_PUBLIC_KEY"
        response.json.keyAgreement.dateCreated
    }

    @SuppressWarnings(["GrUnresolvedAccess", "GroovyAssignabilityCheck"])
    void "get subscriber correctly handles a 404"() {

        setup: "set a bogus subscriber's public key as a request param"

        params.publicKey = "BOGUS_PUBLIC_KEY"

        when: "getting all subscriptions"

        controller.getSubscriber()

        then: "render a list of subscription information JSON objects"

        response.status == 404
        response.json.message == "Not Found"
    }
}
