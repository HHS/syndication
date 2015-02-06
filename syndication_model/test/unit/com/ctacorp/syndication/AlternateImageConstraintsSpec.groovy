package com.ctacorp.syndication

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import grails.buildtestdata.mixin.Build

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/2/14
 * Time: 12:15 PM
 */
@TestMixin(GrailsUnitTestMixin)
@Build(AlternateImage)
class AlternateImageConstraintsSpec extends Specification {
    def setup(){
        mockForConstraintsTests AlternateImage
    }

    void "a valid instance should be valid"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "the instance is validated"
            instance_AlternateImage.validate()

        then: "there should be no errors"
            instance_AlternateImage.errors.errorCount == 0
    }

    void "valid nulls are allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "the width, and height are null"
            instance_AlternateImage.width = null
            instance_AlternateImage.height = null

        then: "the instance will still validate"
            instance_AlternateImage.validate() == true

        and: "There should be no errors"
            instance_AlternateImage.errors.errorCount == 0
    }

    void "invalid nulls shouldn't be allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "url is set to null"
            instance_AlternateImage.url = null

        then: "The instance shouldn't validate"
            instance_AlternateImage.validate() == false

        and: "there should be 1 errors"
            instance_AlternateImage.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_AlternateImage.errors["url"] == "nullable"
    }

    void "names that are too long shouldn't be allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "a name is grater than 255 chars"
            instance_AlternateImage.name = "a"*256

        then: "the instance shouldn't validate"
            instance_AlternateImage.validate() == false

        and: "there should be 1 errors"
            instance_AlternateImage.errors.errorCount == 1

        and: "the errors should be maxSize"
            instance_AlternateImage.errors["name"] == "maxSize"
    }

    void "names and urls that are blank shouldn't be allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "name or url is blank"
            instance_AlternateImage.name = ""
            instance_AlternateImage.url = ""

        then: "the instance shouldn't validate"
            instance_AlternateImage.validate() == false

        and: "there should be 2 errors"
            instance_AlternateImage.errors.errorCount == 2

        and: "the errors should be blank"
            instance_AlternateImage.errors["name"] == "blank"
            instance_AlternateImage.errors["url"] == "blank"
    }

    void "names that are correctly sized should work"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "name is the correct size"
            instance_AlternateImage.name = "a" * 255

        then: "the instance should be valid"
            instance_AlternateImage.validate() == true

        and: "There should be no errors"
            instance_AlternateImage.errors.errorCount == 0
    }

    void "invalid urls should not be allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "the url is set to something bogus"
            instance_AlternateImage.url = "ASDFGHJKLQWERTYUIOPZXCVBNM"

        then: "the instance should not validate"
            instance_AlternateImage.validate() == false

        and: "there should be 1 errors"
            instance_AlternateImage.errors.errorCount == 1

        and: "the errors should be url"
            instance_AlternateImage.errors["url"] == "url"
    }

    void "valid urls should be allowed"(){
        given: "a valid AlternateImage instance"
            def instance_AlternateImage = AlternateImage.build()

        when: "a good url us supplied"
            instance_AlternateImage.url = "http://www.example.com"

        then: "the instance should validate"
            instance_AlternateImage.validate() == true

        and: "There should be no errors"
            instance_AlternateImage.errors.errorCount == 0
    }
}