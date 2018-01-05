
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.service


import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.KeyAgreementService
import com.ctacorp.syndication.manager.cms.Subscriber
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import grails.util.Holders
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
@TestFor(KeyAgreementService)
@Build([Subscriber,KeyAgreement])
class KeyAgreementServiceSpec extends Specification {

    def cmsManagerEntityName

    def setup() {
        cmsManagerEntityName = Holders.config.apiKey.entities.cmsManager as String
    }

	void "create a new key agreement for a Subscriber"() {

        given: "An existing Subscriber instance"

            def subscriber = Subscriber.build([name:"TheWorldsGreatestCms"])

        when: "creating a new key agreement"

            service.addKeyAgreementToSubscriber(subscriber)

        then: "a key agreement will be added to the Subscriber domain instance"

            def keyAgreement = subscriber.keyAgreement
            keyAgreement != null

        and: "entity1 of the key agreement will be value of the 'cmsManagerEntityName' from the config"

            keyAgreement.entity1 == cmsManagerEntityName

        and: "entity2 of the key agreement will be the subscriber instance's name"

            keyAgreement.entity2 == subscriber.name
    }
}
