package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by sgates on 12/29/15.
 */
@TestFor(FAQ)
@Build([FAQ])
@Mock([FAQ])
class FaqConstraintsSpec extends Specification {

    def "A valid instance should be valid"() {
        given: "an instance of a FAQ"
            def faqInstance = FAQ.build()
        when: "the instance is validated"
            def valid = faqInstance.validate()
        then: "the instance should be valid"
            valid
    }
}