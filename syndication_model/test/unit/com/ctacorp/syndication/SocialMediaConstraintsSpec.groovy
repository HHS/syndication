package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.SocialMedia

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(SocialMedia)
class SocialMediaConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(SocialMedia)
    }

    void "a valid instance should be valid"(){
        given: "a valid SocialMedia instance"
            def instance_SocialMedia = SocialMedia.build()
        
        when: "the instance is validated"
            instance_SocialMedia.validate()
        
        then: "there are no errors"
            instance_SocialMedia.errors.errorCount == 0
    }

    void "blank fields are not allowed"(){
        given: "a valid SocialMedia instance"
            def instance_SocialMedia = SocialMedia.build()

        when: "socialMediaType field is set to blank"
            instance_SocialMedia.socialMediaType = ""

        then: "the instance does not validate"
            instance_SocialMedia.validate() == false

        and: "there should be 1 errors"
            instance_SocialMedia.errors.errorCount == 1

        and: "the errors should be blank"
            instance_SocialMedia.errors["socialMediaType"] == "blank"
    }

    void "null fields are not allowed"(){
        given: "a valid SocialMedia instance"
            def instance_SocialMedia = SocialMedia.build()

        when: "socialMediaType field is set to null"
            instance_SocialMedia.socialMediaType = null

        then: "the instance does not validate"
            instance_SocialMedia.validate() == false

        and: "there should be 1 errors"
            instance_SocialMedia.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_SocialMedia.errors["socialMediaType"] == "nullable"
    }
}
