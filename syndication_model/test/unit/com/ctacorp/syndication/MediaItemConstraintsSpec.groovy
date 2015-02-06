package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.MediaItem

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(MediaItem)
class MediaItemConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(MediaItem)
    }

    void "a valid instance should be valid"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "the instance is validated"
            instance_MediaItem.validate()

        then: "there should be no errors"
            instance_MediaItem.validate() == true

        and: "There should be no errors"
            instance_MediaItem.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "name, sourceUrl, dateAuthored, dateUpdated, language, or source are null"
            instance_MediaItem.name                    = null
            instance_MediaItem.sourceUrl               = null
            instance_MediaItem.dateSyndicationCaptured = null
            instance_MediaItem.dateSyndicationUpdated  = null
            instance_MediaItem.dateSyndicationVisible  = null
            instance_MediaItem.language                = null
            instance_MediaItem.source                  = null

        then: "the instance should not validate"
            instance_MediaItem.validate() == false

        and: "there should be 7 errors"
            instance_MediaItem.errors.errorCount == 7

        and: "the errors should be nullable"
            instance_MediaItem.errors["name"]                       == "nullable"
            instance_MediaItem.errors["sourceUrl"]                  == "nullable"
            instance_MediaItem.errors["dateSyndicationCaptured"]    == "nullable"
            instance_MediaItem.errors["dateSyndicationUpdated"]     == "nullable"
            instance_MediaItem.errors["dateSyndicationVisible"]     == "nullable"
            instance_MediaItem.errors["language"]                   == "nullable"
            instance_MediaItem.errors["source"]                     == "nullable"
    }

    void "blank fields should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "name, description, licenseInfo, sourceUrl, hash or are blank"
            instance_MediaItem.name             = ""
            instance_MediaItem.description      = ""
            instance_MediaItem.sourceUrl        = ""
            instance_MediaItem.hash             = ""

        then: "the instance should not validate"
            instance_MediaItem.validate() == false

        and: "there should be 6 errors"
            instance_MediaItem.errors.errorCount == 4

        and: "the errors should be blank"
            instance_MediaItem.errors["name"]               == "blank"
            instance_MediaItem.errors["description"]        == "blank"
            instance_MediaItem.errors["sourceUrl"]          == "blank"
            instance_MediaItem.errors["hash"]               == "blank"
    }
    
    void "fields that are oversized should not be allowed"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()
        
        when: "the description is too large"
            instance_MediaItem.description = 'a' * 2001

        then: "the instance should not validate"
            instance_MediaItem.validate() == false

        and: "there should be 2 errors"
            instance_MediaItem.errors.errorCount == 1

        and: "the errors should be maxSize"
            instance_MediaItem.errors["description"] == "maxSize"
    }

    void "boundary sized values should be valid"(){
        given: "a valid MediaItem instance"
            def instance_MediaItem = MediaItem.build()

        when: "description is set to boundary values"
            instance_MediaItem.description = 'a'*2000

        then: "the instance should validate"
            instance_MediaItem.validate() == true

        and: "There should be no errors"
            instance_MediaItem.errors.errorCount == 0
    }
}
