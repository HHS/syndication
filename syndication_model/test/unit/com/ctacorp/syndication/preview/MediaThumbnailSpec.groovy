package com.ctacorp.syndication.preview

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(MediaThumbnail)
@TestMixin(GrailsUnitTestMixin)
@Build(MediaThumbnail)
class MediaThumbnailSpec extends Specification {

    def setup() {
        mockForConstraintsTests(MediaThumbnail)
    }

    void "a valid instance should be valid"(){
        given: "a valid MediaThumbnail instance"
        def instance_MediaThumbnail = MediaThumbnail.build()

        when: "the instance is validated"
        instance_MediaThumbnail.validate()

        then: "there should be no errors"
        instance_MediaThumbnail.validate()

        and: "There should be no errors"
        instance_MediaThumbnail.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid MediaThumbnail instance"
        def instance_MediaThumbnail = MediaThumbnail.build()

        when: "imageData are null"
        instance_MediaThumbnail.imageData                 = null

        then: "the instance should not validate"
        !instance_MediaThumbnail.validate()

        and: "there should be 3 errors"
        instance_MediaThumbnail.errors.errorCount == 1

        and: "the errors should be nullable"
        instance_MediaThumbnail.errors["imageData"]       == "nullable"
    }

    void "fields that are oversized should not be allowed"(){
        given: "a valid MediaThumbnail instance"
        def instance_MediaThumbnail = MediaThumbnail.build()

        when: "the fields that can be oversized are set to be oversized"
        def tmp = []
        (1*1024*1024/10+1).times{
            tmp.addAll([1,2,3,4,5,6,7,8,9,0])
        }
        instance_MediaThumbnail.imageData = tmp as byte[]

        then: "the instance should not validate"
        !instance_MediaThumbnail.validate()

        and: "there should be 1 errors"
        instance_MediaThumbnail.errors.errorCount == 1

        and: "the errors should be maxSize"
        instance_MediaThumbnail.errors["imageData"] == "maxSize"
    }

    void "boundary sized values should be valid"(){
        given: "a valid MediaThumbnail instance"
        def instance_MediaThumbnail = MediaThumbnail.build()

        when: "size limits are set to boundary values"
        def tmp = []
        (1*1024*1024/10).times{
            tmp.addAll([1,2,3,4,5,6,7,8,9,0])
        }
        instance_MediaThumbnail.imageData = tmp as byte[]

        then: "the instance should validate"
        instance_MediaThumbnail.validate()

        and: "There should be no errors"
        instance_MediaThumbnail.errors.errorCount == 0
    }
}