package com.ctacorp.syndication.media

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.buildtestdata.mixin.Build

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FeaturedMedia)
@Mock(MediaItem)
@Build([MediaItem, FeaturedMedia])
class FeaturedMediaConstraintsSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "a valid instance should be valid"(){
        given: "a valid FeaturedMedia instance"
            def instance_FeaturedMedia = FeaturedMedia.build()

        when: "the instance is validated"
            instance_FeaturedMedia.validate()

        then: "there should be no errors"
            instance_FeaturedMedia.errors.errorCount == 0
    }

    void "invalid nulls should not be allowed"(){
        given: "a valid FeaturedMedia instance"
            def instance_FeaturedMedia = FeaturedMedia.build()

        when: "the mediaItem field is null"
            instance_FeaturedMedia.mediaItem = null

        then: "the instance shouldn't validate"
            instance_FeaturedMedia.validate() == false

        and: "there should be 1 errors"
            instance_FeaturedMedia.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_FeaturedMedia.errors["mediaItem"].code == "nullable"
    }
}
