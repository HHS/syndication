package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(MediaItemSubscriber)
class MediaItemSubscriberSpec extends Specification {

    def setup() {
        mockForConstraintsTests(MediaItemSubscriber)
    }

    void "test a valid instance"() {
        given: "A valid MediaItemSubscriber instance"
            def mediaItemSubscriber = MediaItemSubscriber.build()
        
        when: "MediaItemSubscriber is validated"
            mediaItemSubscriber.validate()
        
        then: "The instance should have no errors"
            mediaItemSubscriber.errors.errorCount == 0
    }
    
    def "nulls are not allowed"(){
        given: "A valid MediaItemSubscriber instance"
            def mediaItemSubscriber = MediaItemSubscriber.build()

        when: "Non-nullable values are set to null"
            mediaItemSubscriber.subscriberId = null
            mediaItemSubscriber.mediaItem = null
        
        then: "Then the instance should not validate"
            !mediaItemSubscriber.validate()
        
        and: "The instance should have two errors"
            mediaItemSubscriber.errors.errorCount == 2
        
        and: "The errors should be because of nullable"
            mediaItemSubscriber.errors["subscriberId"] == "nullable"
            mediaItemSubscriber.errors["mediaItem"] == "nullable"
    }
}
