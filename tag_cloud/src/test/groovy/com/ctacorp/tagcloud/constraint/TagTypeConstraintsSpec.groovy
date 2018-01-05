package com.ctacorp.tagcloud.constraint

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import tagcloud.domain.TagType


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/8/14
 * Time: 11:43 PM
 */
@Mock(TagType)
@Build(TagType)
@TestFor(TagType)
class TagTypeConstraintsSpec extends Specification {

    void "a valid instance should be valid"() {
        given: "a valid TagType instance"
            def instance_TagType = TagType.build()

        when: "the instance is validated"
            instance_TagType.validate()

        then: "there should be no errors"
            instance_TagType.errors.errorCount == 0
    }

    void "valid nulls should be allowed"() {
        given: "a valid TagType instance"
            def instance_TagType = TagType.build()

        when: "description is set to null"
            instance_TagType.description = null

        then: "instance should validate"
            instance_TagType.validate() == true

        and: "There should be no errors"
            instance_TagType.errors.errorCount == 0
    }

    void "invalid nulls should not be allowed"() {
        given: "a valid TagType instance"
            def instance_TagType = TagType.build()

        when: "name is set to null"
            instance_TagType.name = null

        then: "the instance shouldn't validate"
            instance_TagType.validate() == false

        and: "there should be 1 errors"
            instance_TagType.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_TagType.errors["name"].code == "nullable"
    }

    void "blanks should not be allowed"() {
        given: "a valid TagType instance"
            def instance_TagType = TagType.build()

        when: "name is blank"
            instance_TagType.name = ""

        then: "the instance should not validate"
            instance_TagType.validate() == false

        and: "there should be 1 errors"
            instance_TagType.errors.errorCount == 1

        and: "the errors should be blank"
            instance_TagType.errors["name"].code == "blank"
    }

    void "names should be unique"() {
        given: "a valid TagType instance"
            def instance_TagType1 = TagType.build()
            def instance_TagType2 = TagType.build()

        when: "two tag instances have the same name"
            instance_TagType1.name = "a"
            instance_TagType1.save(flush: true)
            instance_TagType2.name = "a"

        then: "the second instance shouldn't validate"
            instance_TagType2.validate() == false

        and: "there should be 1 errors"
            instance_TagType2.errors.errorCount == 1

        and: "the errors should be unique"
            instance_TagType2.errors["name"].code == "unique"
    }
}