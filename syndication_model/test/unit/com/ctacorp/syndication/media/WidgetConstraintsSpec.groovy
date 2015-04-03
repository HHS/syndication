package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Widget

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Widget)
class WidgetConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(Widget)
    }

    void "a valid instance should be valid"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "the instance is validated"
            instance_Widget.validate()

        then: "there should be no errors"
            instance_Widget.errors.errorCount == 0
    }

    void "null values should not be allowed"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "the fields height, width, and code are set to null"
            instance_Widget.height = null
            instance_Widget.width = null
            instance_Widget.code = null

        then: "the instance should not validate"
            instance_Widget.validate() == false

        and: "there should be 3 errors"
            instance_Widget.errors.errorCount == 3

        and: "the errors should be nullable"
            instance_Widget.errors["height"] == "nullable"
            instance_Widget.errors["width"] == "nullable"
            instance_Widget.errors["code"] == "nullable"
    }

    void "blank values should not be allowed"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "the code field is set to blank"
            instance_Widget.code = ""

        then: "the instance should not validate"
            instance_Widget.validate() == false

        and: "there should be 1 errors"
            instance_Widget.errors.errorCount == 1

        and: "the errors should be blank"
            instance_Widget.errors["code"] == "blank"
    }

    void "oversize values should not be allowed"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "the fields height, width, or code are too big"
            instance_Widget.height = Integer.MAX_VALUE
            instance_Widget.width = Integer.MAX_VALUE
            instance_Widget.code = 'a'*5001

        then: "the instance should not validate"
            instance_Widget.validate() == false

        and: "there should be 3 errors"
            instance_Widget.errors.errorCount == 3

        and: "the errors should be max"
            instance_Widget.errors["width"] == "max"
            instance_Widget.errors["height"] == "max"

        and: "the errors should be maxSize"
            instance_Widget.errors["code"] == "maxSize"
    }

    void "undersize should not be allowed"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "height and width fields are too small"
            instance_Widget.height = 0
            instance_Widget.width = 0

        then: "the instance should not validate"
            instance_Widget.validate() == false

        and: "there should be 2 errors"
            instance_Widget.errors.errorCount == 2

        and: "the errors should be min"
            instance_Widget.errors["height"] == "min"
            instance_Widget.errors["width"] == "min"
    }

    void "boundary sizes should be allowed"(){
        given: "a valid Widget instance"
            def instance_Widget = Widget.build()

        when: "the height, width and code fields are 1 less than the max"
            instance_Widget.height = Integer.MAX_VALUE-1
            instance_Widget.width = Integer.MAX_VALUE-1
            instance_Widget.code = 'a'*5000

        then: "the instance should validate"
            instance_Widget.validate() == true

        and: "There should be no errors"
            instance_Widget.errors.errorCount == 0
    }
}
