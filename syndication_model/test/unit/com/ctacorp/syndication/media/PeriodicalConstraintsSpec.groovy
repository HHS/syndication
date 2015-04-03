package com.ctacorp.syndication.media

import com.ctacorp.syndication.media.Periodical
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Periodical)
class PeriodicalConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Periodical)
    }

    void "test a valid instance"() {
        given: "A mocked Periodical Instance"
            def periodical = Periodical.build()
        
        when: "Periodical Instance is validated"
            periodical.validate()
        
        then: "Then instance should have no errors"
            periodical.errors.errorCount == 0
    }
    
    def "nulls are not allowed"() {
        given: "A valid periodical Instance"
            def periodical = Periodical.build()
        
        when: "The period is null"
            periodical.period = null
        
        then: "The instance shouldn't validate."
            !periodical.validate()
        
        and: "The instance should have one error"
            periodical.errors.errorCount == 1
        
        and: "The error should be null"
            periodical.errors["period"] == "nullable"

    }
    
    def "allowed enum values are allowed"() {
        given: "A valid periodical Instance"
            def periodical = Periodical.build()

        when: "given a valid period name"
            periodical.period = "ANNUALLY"
        
        then: "periodical instance should validate"
            periodical.validate()

        when: "given a valid period name"
        periodical.period = "MONTHLY"

        then: "periodical instance should validate"
        periodical.validate()

        when: "given a valid period name"
        periodical.period = Periodical.Period.ANNUALLY

        then: "periodical instance should validate"
        periodical.validate()

        when: "given a valid period name"
        periodical.period = "DAILY"

        then: "periodical instance should validate"
        periodical.validate()
    }
    
}
