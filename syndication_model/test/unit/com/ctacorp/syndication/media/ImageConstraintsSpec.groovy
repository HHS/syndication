package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Image

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Image)
class ImageConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Image)
    }

    void "a valid instance should be valid"(){
        given: "a valid Image instance"
            def instance_Image = Image.build()
        
        when: "instance is validated"
            instance_Image.validate()
        
        then: "there should be no errors"
            instance_Image.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid Image instance"
            def instance_Image = Image.build()
        
        when: "when format, width, height or altText fields are set to null"
            instance_Image.height = null
            instance_Image.width = null
            instance_Image.imageFormat = null
            instance_Image.altText = null
        
        then: "the instance should not validate"
            instance_Image.validate() == false

        and: "there should be 4 errors"
            instance_Image.errors.errorCount == 4

        and: "the errors should be nullable"
            instance_Image.errors["height"]     == "nullable"
            instance_Image.errors["width"]      == "nullable"
            instance_Image.errors["imageFormat"]     == "nullable"
            instance_Image.errors["altText"]    == "nullable"
    }

    void "blank fields should not be allowed"(){
        given: "a valid Image instance"
            def instance_Image = Image.build()

        when: "format or altText fields are blank"
            instance_Image.altText = ""
            instance_Image.imageFormat = ""

        then: "the instance should not validate"
            instance_Image.validate() == false

        and: "there should be 2 errors"
            instance_Image.errors.errorCount == 2

        and: "the errors should be blank"
            instance_Image.errors["altText"] == "blank"
            instance_Image.errors["imageFormat"] == "blank"
    }

    void "too-small values should not be allowed"(){
        given: "a valid Image instance"
            def instance_Image = Image.build()

        when: "width or height are set to -1"
            instance_Image.width = 0
            instance_Image.height = 0

        then: "the instance should not validate"
            instance_Image.validate() == false

        and: "there should be 2 errors"
            instance_Image.errors.errorCount == 2

        and: "the errors should be min"
            instance_Image.errors["width"] == "min"
            instance_Image.errors["height"] == "min"
    }

    void "too-large values should not be allowed"(){
        given: "a valid Image instance"
            def instance_Image = Image.build()

        when: "width and height fields are set to max+1"
            instance_Image.width = Integer.MAX_VALUE
            instance_Image.height = Integer.MAX_VALUE

        then: "the instance should not validate"
            instance_Image.validate() == false

        and: "there should be 2 errors"
            instance_Image.errors.errorCount == 2

        and: "the errors should be max"
            instance_Image.errors["width"] == "max"
            instance_Image.errors["height"] == "max"
    }

    void "boundary values should be allowed"(){
        given: "a valid Image instance"
            def instance_Image1 = Image.build()
            def instance_Image2 = Image.build()

        when: "width and height fields are set to min and max"
            instance_Image1.width = 1
            instance_Image1.height = 1
            instance_Image2.width = Integer.MAX_VALUE -1
            instance_Image2.height = Integer.MAX_VALUE -1

        then: "the instance should still validate"
            instance_Image1.validate() == true
            instance_Image2.validate() == true

        and: "There should be no errors"
            instance_Image1.errors.errorCount == 0
            instance_Image2.errors.errorCount == 0
    }
}