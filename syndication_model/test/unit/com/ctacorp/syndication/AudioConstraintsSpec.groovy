package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Audio)
class AudioConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests Audio
    }

    def "test a valid instance"(){
        given: "A mocked audio instance"
            def audio = Audio.build()

        when: "Audio instance is validated"
            audio.validate()

        then: "The instance should have no errors"
            audio.errors.errorCount == 0
    }

    def "valid nulls are allowed"(){
        given: "a valid instance with duration set to null"
            def audio = Audio.build()

        when: "the duration is null"
            audio.duration = null

        then: "the instance will still validate"
            audio.validate() == true

        and: "the instance should have 0 errors"
            audio.errors.errorCount == 0
    }

    def "over-size durations are not allowed"(){
        given: "a valid audio instance"
            def audio = Audio.build()

        when: "the duration is greater than max"
            audio.duration = Integer.MAX_VALUE

        then: "the instance shouldn't validate"
            audio.validate() == false

        and: "there should be 1 error"
            audio.errors.errorCount == 1

        and: "the error should be max"
            audio.errors["duration"] == "max"
    }

    def "under-size durations are not allowed"(){
        given: "a valid audio instance"
            def audio = Audio.build()

        when: "the duration is negative"
            audio.duration = -1

        then: "the instance shouldn't validate"
            audio.validate() == false

        and: "there should be 1 error"
            audio.errors.errorCount == 1

        and: "the error should be max"
            audio.errors["duration"] == "min"
    }

    def "valid duration values should be allowed"(){
        given: "two valid audio instances"
            def audio1 = Audio.build()
            def audio2 = Audio.build()

        when: "the durations are set to the max and the min"
            audio1.duration = 0
            audio2.duration = Integer.MAX_VALUE -1

        then: "both instances should validate"
            audio1.validate() == true
            audio2.validate() == true

        and: "there should be no errors"
            audio1.errors.errorCount == 0
            audio2.errors.errorCount == 0
    }
}