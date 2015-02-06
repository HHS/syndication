package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.Language

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Language)
class LanguageConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Language)
    }

    void "a valid instance should be valid"(){
        given: "a valid Language instance"
            def instance_Language = Language.build()

        when: "the instance is validated"
            instance_Language.validate()

        then: "there should be no errors"
            instance_Language.errors.errorCount == 0
    }

    void "null name and isoCode fields should not be allowed"(){
        given: "a valid Language instance"
            def instance_Language = Language.build()

        when: "name and isoCode fields are set to null"
            instance_Language.name = null
            instance_Language.isoCode = null

        then: "the instance should not validate"
            instance_Language.validate() == false

        and: "there should be 2 errors"
            instance_Language.errors.errorCount == 2

        and: "the errors should be nullable"
            instance_Language.errors["name"] == "nullable"
            instance_Language.errors["isoCode"] == "nullable"
    }

    void "blank name and isoCode fields should not be allowed"(){
        given: "a valid Language instance"
            def instance_Language = Language.build()

        when: "the name and isoCode fields are set to blank"
            instance_Language.name = ""
            instance_Language.isoCode = ""

        then: "the instance should not validate"
            instance_Language.validate() == false

        and: "there should be 2 errors"
            instance_Language.errors.errorCount == 2

        and: "the errors should be blank"
            instance_Language.errors["name"] == "blank"
            instance_Language.errors["isoCode"] == "blank"
    }

}
