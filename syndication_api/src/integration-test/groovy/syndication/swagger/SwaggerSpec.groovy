
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.swagger

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.Integration
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.transaction.Rollback
import swaggergrails3.SwaggerController
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 *
 */
@TestFor(SwaggerController)
@Integration
@Rollback
class SwaggerSpec extends Specification {
    def config = Holders.config

    def setup() {
    }

    def cleanup() {
    }

//    void "main swagger json data should be available"() {
//        given: "the api action is called"
//        controller.api()
//        def json = controller.response.json
//
//        expect: "that the action returns JSON data"
//        json != null
//    }
//
//    void "swagger json should contain correct entry points"(){
//        given:"The api action is called"
//        controller.api()
//
//        when: "the json response is parsed"
//        def json = controller.response.json
//
//        then: "there should be a specific set of entry points listed"
//        def actualEntryPoints = json.apis*.path
//        def expectedEntryPoints = [
//            "/campaigns",
//            "/languages",
//            "/media",
//            "/mediaTypes",
//            "/resources",
//            "/tags",
//            "/sources",
//            "/resources",
//            "/userMediaLists"
//        ]
//
//        actualEntryPoints.containsAll(expectedEntryPoints)
//
//        and: "there shouldn't be extra entry points we don't know about"
//        !(actualEntryPoints - expectedEntryPoints)
//    }
//
//    void "meta data should be correct as defined in swagger"(){
//        given:"The api action is called"
//        controller.api()
//
//        when: "the json response is parsed"
//        def json = controller.response.json
//
//        then: "the declared version should be what we expect"
//        json.apiVersion == "2"
//
//        and: "the swagger version should be what we expect"
//        json.swaggerVersion == "1.2"
//    }
}
