
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.domain

import com.ctacorp.syndication.manager.cms.KeyAgreement
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import spock.lang.Specification


@TestMixin(ControllerUnitTestMixin)
@TestFor(KeyAgreement)
@Build(KeyAgreement)

class KeyAgreementSpec extends Specification {

    def keyAgreement

    void "test entity1 cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity1 to null"

            keyAgreement.entity1 = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test entity2 cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity1 to null"

            keyAgreement.entity2 = null

        then: "the key agreement should have errors"


            !keyAgreement.validate()
    }

    void "test entity1PrivateKey cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity1PrivateKey to null"

            keyAgreement.entity1PrivateKey = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test entity1PublicKey cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity1PublicKey to null"

            keyAgreement.entity1PublicKey = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test entity2PrivateKey cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity2PrivateKey to null"

            keyAgreement.entity2PrivateKey = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test entity2PublicKey cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting entity2PublicKey to null"

            keyAgreement.entity2PublicKey = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test secret cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting secret to null"

            keyAgreement.secret = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test prime cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting prime to null"

            keyAgreement.prime = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test generator cannot be null"() {

        given: "a valid KeyAgreement instance"

            def keyAgreement = KeyAgreement.build()

        when: "setting prime to null"

            keyAgreement.generator = null

        then: "the key agreement should have errors"

            !keyAgreement.validate()
    }

    void "test entity2 must be unique"() {

        given: "an existing KeyAgreement instance"

            def keyAgreement1 = KeyAgreement.build([entity1:"entity1",entity2:"entity2"])
            keyAgreement1.save(flush:true)

        when: "creating a new key agreement with the same entity2 name"

            def keyAgreement2 = KeyAgreement.buildWithoutSave([entity1:"entity1",entity2:"entity2"])

        then: "the new key agreement should have errors"

            !keyAgreement2.validate()
    }
}
