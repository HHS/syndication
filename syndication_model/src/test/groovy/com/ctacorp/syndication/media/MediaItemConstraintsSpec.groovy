package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.MediaItem

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(MediaItem)
class MediaItemConstraintsSpec extends Specification {

    void "a valid instance should be valid"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "the instance is validated"
            def valid = instance_MediaItem.validate()

        then: "it should be valid"
            valid

        and: "There should be no errors"
            !instance_MediaItem.hasErrors()
    }

    void "null fields should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "name, sourceUrl, dateAuthored, dateUpdated, language, or source are null"
            instance_MediaItem.name                    = null
            instance_MediaItem.sourceUrl               = null
            instance_MediaItem.dateSyndicationCaptured = null
            instance_MediaItem.dateSyndicationUpdated  = null
            instance_MediaItem.language                = null
            instance_MediaItem.source                  = null

        then: "the instance should not validate"
            instance_MediaItem.validate() == false

        and: "there should be 7 errors"
            instance_MediaItem.errors.errorCount == 6

        and: "the errors should be nullable"
            instance_MediaItem.errors["name"].code                       == "nullable"
            instance_MediaItem.errors["sourceUrl"].code                  == "nullable"
            instance_MediaItem.errors["dateSyndicationCaptured"].code    == "nullable"
            instance_MediaItem.errors["dateSyndicationUpdated"].code     == "nullable"
            instance_MediaItem.errors["language"].code                   == "nullable"
            instance_MediaItem.errors["source"].code                     == "nullable"
    }

    void "blank fields should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "name, description, licenseInfo, sourceUrl, hash or are blank"
            instance_MediaItem.name               = ""
            instance_MediaItem.description        = ""
            instance_MediaItem.sourceUrl          = ""
            instance_MediaItem.targetUrl          = ""
            instance_MediaItem.customThumbnailUrl = ""
            instance_MediaItem.customPreviewUrl   = ""
            instance_MediaItem.hash               = ""

        then: "the instance should not validate"
            instance_MediaItem.validate() == false

        and: "there should be 6 errors"
            instance_MediaItem.errors.errorCount == 7

        and: "the errors should be blank"
            instance_MediaItem.errors["name"].code               == "blank"
            instance_MediaItem.errors["description"].code        == "blank"
            instance_MediaItem.errors["sourceUrl"].code          == "blank"
            instance_MediaItem.errors["targetUrl"].code          == "blank"
            instance_MediaItem.errors["customThumbnailUrl"].code == "blank"
            instance_MediaItem.errors["customPreviewUrl"].code   == "blank"
            instance_MediaItem.errors["hash"].code               == "blank"
    }
    
    void "fields that are oversized should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()
        
        when: "the fields that can be oversized are set to be oversized"
            instance_MediaItem.name = 'a' * 256
            instance_MediaItem.description = 'a' * 2001
            instance_MediaItem.sourceUrl = 'http://ex'  +'a'*1983 + 'ample.com'
            instance_MediaItem.targetUrl = 'http://ex'  +'a'*1983 + 'ample.com'
            instance_MediaItem.customThumbnailUrl = 'http://ex'  +'a'*1983 + 'ample.com'
            instance_MediaItem.customPreviewUrl = 'http://ex'  +'a'*1983 + 'ample.com'
            instance_MediaItem.externalGuid = 'a' * 256
            instance_MediaItem.hash = 'a' * 256

        then: "the instance should not validate"
            !instance_MediaItem.validate()

        and: "there should be 8 errors"
            instance_MediaItem.errors.errorCount == 8

        and: "the errors should be maxSize"
            instance_MediaItem.errors["name"].code == "maxSize.exceeded"
            instance_MediaItem.errors["description"].code == "maxSize.exceeded"
            instance_MediaItem.errors["sourceUrl"].code == "maxSize.exceeded"
            instance_MediaItem.errors["targetUrl"].code == "maxSize.exceeded"
            instance_MediaItem.errors["customThumbnailUrl"].code == "maxSize.exceeded"
            instance_MediaItem.errors["customPreviewUrl"].code == "maxSize.exceeded"
            instance_MediaItem.errors["externalGuid"].code == "maxSize.exceeded"
            instance_MediaItem.errors["hash"].code == "maxSize.exceeded"
    }

    void "boundary sized values should be valid"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "size limits are set to boundary values"
        instance_MediaItem.name = 'a' * 255
            instance_MediaItem.description = 'a'*2000
        instance_MediaItem.sourceUrl = 'http://ex'  +'a'*1982 + 'ample.com'
        instance_MediaItem.targetUrl = 'http://ex'  +'a'*1982 + 'ample.com'
        instance_MediaItem.customThumbnailUrl = 'http://ex'  +'a'*1982 + 'ample.com'
        instance_MediaItem.customPreviewUrl = 'http://ex'  +'a'*1982 + 'ample.com'
        instance_MediaItem.externalGuid = 'a' * 255
        instance_MediaItem.hash = 'a' * 255

        then: "the instance should validate"
            instance_MediaItem.validate() == true

        and: "There should be no errors"
            instance_MediaItem.errors.errorCount == 0
    }
    
}
