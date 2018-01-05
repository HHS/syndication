/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.manager.cms.LoggingService
//import grails.test.GrailsUnitTestCase
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import org.apache.commons.logging.Log
import spock.lang.Specification

@TestFor(LoggingService)
//@TestMixin(GrailsUnitTestCase)
class LoggingServiceSpec extends Specification {

    def log = Mock(Log)
    def errorCodeGenerator = Mock(LoggingService.ErrorCodeGenerator)

    def setup() {
        //noinspection GrFinalVariableAccess
        //service.log = log
        service.errorCodeGenerator = errorCodeGenerator
    }

    void "log a message with a null exception"() {

        when: "logging a message only"

            def message = service.logError("I want all the ham!",null,"12345")

        then: "log the error code"

            log.error("Application Error: 12345")

        then: "log the message only"

            log.error("I want all the ham!")

        and: "return a sanitized message"

            "Application Error: Please contact an administrator and reference this error code: 12345" == message
    }

    void "log a message with an exception"() {

        given: "an exception to log"

            def exception = new Exception("huh?")

        when: "logging a message and an exception"

            def message = service.logError("I want all the ham!", exception, "12345")

        then: "log the error code"

            log.error("Application Error: 12345")

        then: "log the message and the exception"

            log.error("I want all the ham!", exception) >> { _message, _exception ->
                assertEquals("I want all the ham!", _message)
                assertEquals(exception, _exception)
            }

        and: "return a sanitized message"

            "Application Error: Please contact an administrator and reference this error code: 12345" == message
    }

    void "log domain errors"() {

        given: "a domain object"

            def domain = [errors:[someError:"someError"],class:[simpleName:"HamBiscuits"]]

        when: "logging the domain errors"

            def message = service.logDomainErrors(domain)

        then: "generate the error code"

            errorCodeGenerator.generateErrorCode() >> 'CUIVTZHNVBYWQYTGPTDB'

        and: "log the error code"

            log.error('Application Error: CUIVTZHNVBYWQYTGPTDB')

        and: "log the message only"

            log.error("Could not create domain object of type HamBiscuits due to errors [someError:someError]")

        and: "return a sanitized message"

            'Application Error: Please contact an administrator and reference this error code: CUIVTZHNVBYWQYTGPTDB' == message
    }
}
