
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import com.ctacorp.syndication.Language
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(LanguagesController)
@Build(Language)
class LanguagesControllerSpec extends Specification {

    def setup() {
        def ls = [listLanguages:{params->[]}, getTotal:{params->0}]
        controller.languagesService = ls
    }

    def cleanup() {
    }

    void "show action should 400 on an invalid lookup"() {
        when: "The show action is executed with a null id"
            controller.show(null)
        then: "A 400 error is returned"
            response.status == 400
        and: "An error payload describing the error is returned"
            response.json != null
        and: "the error should have the required fields"
            response.json.meta != null
            response.json.meta.messages != null
            response.json.meta.messages.errorMessage != null
        and: "the status should be 400"
            response.json.meta.status == 400
    }

    void "show action should return the correct record when requested"(){
        when: "The show action is called with a valid id"
            controller.show(Language.build())
        then: "a valid response should be returned"
            response.status == 200
        and: "there should be a data payload"
            response.json != null
        and: "the status should also be 200"
            response.json.meta.status == 200
        and: "the response body should have data with the correct id"
            response.json.results != null
            response.json.results[0] != null
            response.json.results[0].id == 1
    }

    void "list action should always return a json payload"(){
        when: "the list action is called"
            controller.list()
        then: "there should be a json response"
            response.json != null
    }
}
