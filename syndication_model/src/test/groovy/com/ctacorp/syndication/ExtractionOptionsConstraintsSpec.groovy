package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.ExtractionOptions

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(ExtractionOptions)
class ExtractionOptionsConstraintsSpec extends Specification {

    void "valid instance should be valid"() {
        given: "A valid instance"
            def extractionOptionsInstance = ExtractionOptions.build()

        when: "the instance is validated"
            extractionOptionsInstance.validate()

        then: "there should be no errors"
            extractionOptionsInstance.errors.errorCount == 0
    }

    void "invalid nulls should not be allowed"(){
        given: "A valid instance"
            def extractionOptionsInstance = ExtractionOptions.build()

        when: "the html field is set to null"
            extractionOptionsInstance.html = null

        then: "the validation should fail"
            extractionOptionsInstance.validate() == false

        and: "there should be 1 error"
            extractionOptionsInstance.errors.errorCount == 1

        and: "the error should be nullable"
            extractionOptionsInstance.errors["html"].code == "nullable"
    }

    void "valid nulls should validate"(){
        given: "A valid instance"
            def extractionOptionsInstance = ExtractionOptions.build()

        when: "the namespace, field is set to null"
            extractionOptionsInstance.namespace = null

        then: "the instance should validate"
            extractionOptionsInstance.validate() == true

        and: "there should be no errors"
            extractionOptionsInstance.errors.errorCount == 0
    }

    void "invalid blanks should not be allowed"(){
        given: "A valid instance"
            def extractionOptionsInstance = ExtractionOptions.build()

        when: "the namespace is made blank"
            extractionOptionsInstance.namespace = ""

        then: "the instance should not validate"
            extractionOptionsInstance.validate() == false

        and: "there should be 1 error"
            extractionOptionsInstance.errors.errorCount

        and: "the error should be blank"
            extractionOptionsInstance.errors["namespace"].code == "blank"
    }
}
