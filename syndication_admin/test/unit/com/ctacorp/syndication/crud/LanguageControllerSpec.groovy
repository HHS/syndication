
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.LanguageService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(LanguageController)
@Mock(Language)
class LanguageControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params["name"] = 'english'
        params["isoCode"] = 'eng'
    }

    void "Test the index action returns the correct model"() {
        setup: ""
            populateValidParams(params)
            params.isActive = true
            Language language = new Language(params).save(flush: true)

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            model.languageInstanceList == [language]
            model.activeTagLanguageInstanceList == [language]
    }

    void "Test the setActive fails properly with an invalid language"() {
        setup: ""
            request.contentType = MULTIPART_FORM_CONTENT_TYPE
            populateValidParams(params)
            Language language = new Language(params).save(flush: true)
            !language.isActive

        when:"The setActive is called with a null"
            controller.setActive(null)

        then:"The index is properly displayed"
            response.status == 302
            response.redirectedUrl == "/language/index"

        when:"The setActive is called with a invalid id"
            response.reset()
            controller.setActive(-1)

        then:"the index is properly displayed"
            response.status == 302
            response.redirectedUrl == "/language/index"

        when:"The setActive is called with a valid inactive language id"
            response.reset()
            controller.setActive(language.id)

        then:"the language is set to active"
            language.isActive
            response.redirectedUrl == "/language/index"
    }

    void "Test the setInactive fails properly with an invalid language"() {
        setup: ""
        controller.languageService = Mock(LanguageService)
        request.contentType = MULTIPART_FORM_CONTENT_TYPE
        populateValidParams(params)
        params.isActive = true
        Language language = new Language(params).save(flush: true)
        language.isActive

        when:"The setInactive is called with a null"
        controller.setActive(null)

        then:"The index is properly displayed"
        response.status == 302
        response.redirectedUrl == "/language/index"

        when:"The setInactive is called with a invalid id"
        response.reset()
        controller.setActive(-1)

        then:"the index is properly displayed"
        response.status == 302
        response.redirectedUrl == "/language/index"

        when:"The setInactive is called with a valid inactive language id"
        response.reset()
        println "languageId: " + language.id
        controller.setInactive(language.id)

        then:"the language is set to inactive"
        !language.isActive
        response.redirectedUrl == "/language/index"
    }
}
