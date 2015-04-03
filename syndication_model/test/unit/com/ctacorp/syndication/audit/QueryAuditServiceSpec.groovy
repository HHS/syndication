package com.ctacorp.syndication.audit

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QueryAuditService)
class QueryAuditServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def "testSomething"(){
        expect:
        1+1 == 2
    }
}
