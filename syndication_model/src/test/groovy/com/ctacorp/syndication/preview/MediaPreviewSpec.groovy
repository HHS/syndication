package com.ctacorp.syndication.preview

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(MediaPreview)
@TestMixin(GrailsUnitTestMixin)
@Build(MediaPreview)
class MediaPreviewSpec extends Specification {

    void "a valid instance should be valid"(){
        given: "a valid MediaPreview instance"
        def instance_MediaPreview = MediaPreview.build()

        when: "the instance is validated"
        instance_MediaPreview.validate()

        then: "there should be no errors"
        instance_MediaPreview.validate()

        and: "There should be no errors"
        instance_MediaPreview.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid MediaPreview instance"
        def instance_MediaPreview = MediaPreview.build()

        when: "imageData are null"
        instance_MediaPreview.imageData                 = null

        then: "the instance should not validate"
        !instance_MediaPreview.validate()

        and: "there should be 3 errors"
        instance_MediaPreview.errors.errorCount == 1

        and: "the errors should be nullable"
        instance_MediaPreview.errors["imageData"].code       == "nullable"
    }

    void "fields that are oversized should not be allowed"(){
        given: "a valid MediaPreview instance"
        def instance_MediaPreview = MediaPreview.build()

        when: "the fields that can be oversized are set to be oversized"
        def tmp = []
        (4*1024*1024/10+1).times{
            tmp.addAll([1,2,3,4,5,6,7,8,9,0])
        }
        instance_MediaPreview.imageData = tmp as byte[]

        then: "the instance should not validate"
        !instance_MediaPreview.validate()

        and: "there should be 1 errors"
        instance_MediaPreview.errors.errorCount == 1

        and: "the errors should be maxSize"
        instance_MediaPreview.errors["imageData"].code == "maxSize.exceeded"
    }

    void "boundary sized values should be valid"(){
        given: "a valid MediaPreview instance"
        def instance_MediaPreview = MediaPreview.build()

        when: "size limits are set to boundary values"
        def tmp = []
        (4*1024*1024/10).times{
            tmp.addAll([1,2,3,4,5,6,7,8,9,0])
        }
        instance_MediaPreview.imageData = tmp as byte[]

        then: "the instance should validate"
        instance_MediaPreview.validate()

        and: "There should be no errors"
        instance_MediaPreview.errors.errorCount == 0
    }
}