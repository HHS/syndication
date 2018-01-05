package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Infographic

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Infographic)
class InfographicConstraintsSpec extends Specification {

    void "valid instance should be valid"(){
        given: "a valid Infographic instance"
            def instance_Infographic = Infographic.build()

        when: "instance is validated"
            instance_Infographic.validate()

        then: "there should be no errors"
            instance_Infographic.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid Infographic instance"
            def instance_Infographic = Infographic.build()

        when: "when format, width, height or altText fields are set to null"
            instance_Infographic.height = null
            instance_Infographic.width = null
            instance_Infographic.imageFormat = null
            instance_Infographic.altText = null

        then: "the instance should not validate"
            instance_Infographic.validate() == false

        and: "there should be 4 errors"
            instance_Infographic.errors.errorCount == 4

        and: "the errors should be nullable"
            instance_Infographic.errors["height"].code     == "nullable"
            instance_Infographic.errors["width"] .code     == "nullable"
            instance_Infographic.errors["imageFormat"] .code    == "nullable"
            instance_Infographic.errors["altText"].code    == "nullable"
    }

    void "blank fields should not be allowed"(){
        given: "a valid Infographic instance"
            def instance_Infographic = Infographic.build()

        when: "format or altext fields are blank"
            instance_Infographic.altText = ""
            instance_Infographic.imageFormat = ""

        then: "the instance should not validate"
            instance_Infographic.validate() == false

        and: "there should be 2 errors"
            instance_Infographic.errors.errorCount == 2

        and: "the errors should be blank"
            instance_Infographic.errors["altText"].code == "blank"
            instance_Infographic.errors["imageFormat"].code == "blank"
    }

    void "too-small values should not be allowed"(){
        given: "a valid Infographic instance"
            def instance_Infographic = Infographic.build()

        when: "width or height are set to 0"
            instance_Infographic.width = 0
            instance_Infographic.height = 0

        then: "the instance should not validate"
            instance_Infographic.validate() == false

        and: "there should be 2 errors"
            instance_Infographic.errors.errorCount == 2

        and: "the errors should be min"
            instance_Infographic.errors["width"].code == "min.notmet"
            instance_Infographic.errors["height"].code == "min.notmet"
    }

    void "too-large values should not be allowed"(){
        given: "a valid Infographic instance"
            def instance_Infographic = Infographic.build()

        when: "width and height fields are set to max+1"
            instance_Infographic.width = Integer.MAX_VALUE
            instance_Infographic.height = Integer.MAX_VALUE

        then: "the instance should not validate"
            instance_Infographic.validate() == false

        and: "there should be 2 errors"
            instance_Infographic.errors.errorCount == 2

        and: "the errors should be max"
            instance_Infographic.errors["width"].code == "max.exceeded"
            instance_Infographic.errors["height"].code == "max.exceeded"

    }

    void "boundary values should be allowed"(){
        given: "a valid Infographic instance"
            def instance_Infographic1 = Infographic.build()
            def instance_Infographic2 = Infographic.build()

        when: "width and height fields are set to min and max"
            instance_Infographic1.width = 1
            instance_Infographic1.height = 1
            instance_Infographic2.width = Integer.MAX_VALUE -1
            instance_Infographic2.height = Integer.MAX_VALUE -1

        then: "the instance should still validate"
            instance_Infographic1.validate() == true
            instance_Infographic2.validate() == true

        and: "There should be no errors"
            instance_Infographic1.errors.errorCount == 0
            instance_Infographic2.errors.errorCount == 0
    }
}
