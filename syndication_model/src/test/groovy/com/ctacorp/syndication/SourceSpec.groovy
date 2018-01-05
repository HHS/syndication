package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.Source

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
//@TestFor(Source)
@Build(Source)
class SourceSpec extends Specification {

    void "a valid instance should be valid"() {
        given: "a valid Source instance"
            def instance_Source = Source.build()

        when: "the instance is validated"
            instance_Source.validate()

        then: "the instance should have no errors"
            instance_Source.errors.errorCount == 0
    }

    void "non-null fields should not be allowed"() {
        given: "a valid Source instance"
            def instance_Source = Source.build()

        when: "the name and acronym fields are set to null"
            instance_Source.name = null
            instance_Source.acronym = null

        then: "the instance should not validate"
            instance_Source.validate() == false

        and: "there should be 2 errors"
            instance_Source.errors.errorCount == 2

        and: "the errors should be nullable"
            instance_Source.errors["name"].code == "nullable"
            instance_Source.errors["acronym"].code == "nullable"
    }

    void "nullable fields should be allowed"(){
        given: "a valid Source instance"
            def instance_Source = Source.build()

        when: "the largeLogoUrl and smallLogoUrl fields are set to null"
            instance_Source.largeLogoUrl = null
            instance_Source.smallLogoUrl = null

        then: "the instance should still be valid"
            instance_Source.validate() == true

        and: "there should be 0 errors"
            instance_Source.errors.errorCount == 0
    }

    void "blank fields should not be allowed"() {
        given: "a valid Source instance"
            def instance_Source = Source.build()

        when: "the name and acronym fields are set to blank"
            instance_Source.name = ""
            instance_Source.acronym = ""

        then: "the instance should not validate"
            instance_Source.validate() == false

        and: "there should be 2 errors"
            instance_Source.errors.errorCount == 2

        and: "the errors should be blank"
            instance_Source.errors["name"].code == "blank"
            instance_Source.errors["acronym"].code == "blank"
    }

    void "overly sized fields should not be allowed"() {
        given: "a valid Source instance"
            def instance_Source = Source.build()

        when: "the name and acronym fields are larger than 255"
            instance_Source.acronym = 'a' * 256
            instance_Source.name = 'a' * 256

        then: "the instance should not validate"
            instance_Source.validate() == false

        and: "there should be 2 errors"
            instance_Source.errors.errorCount == 2

        and: "the errors should be maxSize"
            instance_Source.errors["name"].code == "maxSize.exceeded"
            instance_Source.errors["acronym"].code == "maxSize.exceeded"
    }

    void "boundary sized fields should be allowed"() {
        given: "a valid Source instance"
            def instance_Source1 = Source.build()
            def instance_Source2 = Source.build()

        when: "name and acronym are set to boundary values"
            instance_Source1.name = "a" * 255
            instance_Source1.acronym = "a" * 255
            instance_Source2.name = "a"
            instance_Source2.acronym = "a"

        then: "the instance should validate"
            instance_Source1.validate() == true
            instance_Source2.validate() == true

        and: "There should be no errors"
            instance_Source1.errors.errorCount == 0
            instance_Source2.errors.errorCount == 0
    }
}
