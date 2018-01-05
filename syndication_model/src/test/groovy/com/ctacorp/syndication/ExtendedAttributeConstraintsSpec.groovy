package com.ctacorp.syndication

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import grails.buildtestdata.mixin.Build

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 2/28/14
 * Time: 1:16 PM
 */

@TestMixin(GrailsUnitTestMixin)
@Build(ExtendedAttribute)
class ExtendedAttributeConstraintsSpec extends Specification {

    void "a valid instance should be valid"(){
        given: "a valid ExtendedAttribute instance"
            def instance_ExtendedAttribute = ExtendedAttribute.build()

        when: "the instance is validated"
            instance_ExtendedAttribute.validate()

        then: "there should be no errors"
            instance_ExtendedAttribute.errors.errorCount == 0
    }

    void "null name and value fields should not be allowed"(){
        given: "a valid ExtendedAttribute instance"
            def instance_ExtendedAttribute = ExtendedAttribute.build()

        when: "name and value fields are set to null"
            instance_ExtendedAttribute.name = null
            instance_ExtendedAttribute.value = null

        then: "the instance should not validate"
            instance_ExtendedAttribute.validate() == false

        and: "there should be 2 errors"
            instance_ExtendedAttribute.errors.errorCount == 2

        and: "the errors should be nullable"
            instance_ExtendedAttribute.errors["name"].code == "nullable"
            instance_ExtendedAttribute.errors["value"].code == "nullable"
    }

    void "blank name and value fields should not be allowed"(){
        given: "a valid ExtendedAttribute instance"
            def instance_ExtendedAttribute = ExtendedAttribute.build()

        when: "the name and value fields are set to blank"
            instance_ExtendedAttribute.name = ""
            instance_ExtendedAttribute.value = ""

        then: "the instance should not validate"
            instance_ExtendedAttribute.validate() == false

        and: "there should be 2 errors"
            instance_ExtendedAttribute.errors.errorCount == 2

        and: "the errors should be blank"
            instance_ExtendedAttribute.errors["name"].code == "blank"
            instance_ExtendedAttribute.errors["value"].code == "blank"
    }

    void "names and values that are too long should not be allowed"(){
        given: "a valid ExtendedAttribute instance"
            def instance_ExtendedAttribute = ExtendedAttribute.build()

        when: "the value is more than 3 characters"
            instance_ExtendedAttribute.name =  'a' * 256
            instance_ExtendedAttribute.value = 'a' * 256

        then: "the instance should not validate"
            instance_ExtendedAttribute.validate() == false

        and: "there should be 1 errors"
            instance_ExtendedAttribute.errors.errorCount == 2

        and: "the errors should be maxSize"
            instance_ExtendedAttribute.errors["name"].code == "maxSize.exceeded"
            instance_ExtendedAttribute.errors["value"].code == "maxSize.exceeded"
    }

    void "names and values that are the right size should be allowed"(){
        given: "a valid ExtendedAttribute instance"
            def instance_ExtendedAttribute = ExtendedAttribute.build()

        when: "the value is more than 3 characters"
            instance_ExtendedAttribute.value = 'a' * 255
            instance_ExtendedAttribute.name = 'a' * 255

        then: "the instance should not validate"
            instance_ExtendedAttribute.validate() == true

        and: "there should be 1 errors"
            instance_ExtendedAttribute.errors.errorCount == 0
    }
}