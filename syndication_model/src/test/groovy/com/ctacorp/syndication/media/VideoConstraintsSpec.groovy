package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Video

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Video)
class VideoConstraintsSpec extends Specification {

    void "a valid instance should be valid"(){
        given: "a valid Video instance"
            def instance_Video = Video.build()

        when: "the instance is validated"
            instance_Video.validate()

        then: "there should be no errors"
            instance_Video.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid Video instance"
            def instance_Video = Video.build()

        when: "the duration field is set to null"
            instance_Video.duration = null

        then: "the instance should not validate"
            instance_Video.validate() == false

        and: "there should be 1 errors"
            instance_Video.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_Video.errors["duration"].code == "nullable"
    }

    void "durations that are too small should not be allowed"(){
        given: "a valid Video instance"
            def instance_Video = Video.build()

        when: "the duration is set to -1"
            instance_Video.duration = 0

        then: "the instance should not validate"
            instance_Video.validate() == false

        and: "there should be 1 errors"
            instance_Video.errors.errorCount == 1

        and: "the errors should be min"
            instance_Video.errors["duration"].code == "min.notmet"
    }

    void "durations that are too big should not be allowed"(){
        given: "a valid Video instance"
            def instance_Video = Video.build()

        when: "the duration is set to 1 more than the max"
            instance_Video.duration = Integer.MAX_VALUE

        then: "the instance should not validate"
            instance_Video.validate() == false

        and: "there should be 1 errors"
            instance_Video.errors.errorCount == 1

        and: "the errors should be max"
            instance_Video.errors["duration"].code == "max.exceeded"
    }

    void "boundary value durations should be allowed"(){
        given: "a valid Video instance"
            def instance_Video1 = Video.build()
            def instance_Video2 = Video.build()

        when: "duration is set to 1 less and 1 more than the min and max"
            instance_Video1.duration = 1
            instance_Video2.duration = Integer.MAX_VALUE-1

        then: "the instance should validate"
            instance_Video1.validate() == true
            instance_Video2.validate() == true

        and: "There should be no errors"
            instance_Video1.errors.errorCount == 0
            instance_Video2.errors.errorCount == 0
    }
}
