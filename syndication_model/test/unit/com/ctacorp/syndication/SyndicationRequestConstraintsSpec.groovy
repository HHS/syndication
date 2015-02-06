package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.SyndicationRequest

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(SyndicationRequest)
class SyndicationRequestConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests(SyndicationRequest)
    }

    void "a valid instance should be valid"(){
        given: "a valid SyndicationRequest instance"
            def instance_SyndicationRequest = SyndicationRequest.build()
        
        when: "the instance is validated"
            instance_SyndicationRequest.validate()
        
        then: "there should be no errors"
            instance_SyndicationRequest.errors.errorCount == 0
    }

    void "null fields should not be allowed"(){
        given: "a valid SyndicationRequest instance"
            def instance_SyndicationRequest = SyndicationRequest.build()

        when: "the fields requestedUrl, contactEmail, and status are set to null"
            instance_SyndicationRequest.requestedUrl = null
            instance_SyndicationRequest.contactEmail = null
            instance_SyndicationRequest.status = null

        then: "the instance should not validate"
            instance_SyndicationRequest.validate() == false

        and: "there should be 3 errors"
            instance_SyndicationRequest.errors.errorCount == 3

        and: "the errors should be nullable"
            instance_SyndicationRequest.errors["requestedUrl"] == "nullable"
            instance_SyndicationRequest.errors["contactEmail"] == "nullable"
            instance_SyndicationRequest.errors["status"] == "nullable"
    }

    void "blank fields should not be allowed"(){
        given: "a valid SyndicationRequest instance"
            def instance_SyndicationRequest = SyndicationRequest.build()

        when: "the fields requestedUrl, requesterNote, adminNote, contactEmail, and status are set to blank"
            instance_SyndicationRequest.requestedUrl = ""
            instance_SyndicationRequest.requesterNote = ""
            instance_SyndicationRequest.adminNote = ""
            instance_SyndicationRequest.contactEmail = ""
            instance_SyndicationRequest.status = ""

        then: "the instance should not validate"
            instance_SyndicationRequest.validate() == false

        and: "there should be 3 errors"
            instance_SyndicationRequest.errors.errorCount == 5

        and: "the errors should be blank"
            instance_SyndicationRequest.errors["requestedUrl"] == "blank"
            instance_SyndicationRequest.errors["requesterNote"] == "blank"
            instance_SyndicationRequest.errors["adminNote"] == "blank"
            instance_SyndicationRequest.errors["contactEmail"] == "blank"
            instance_SyndicationRequest.errors["status"] == "blank"
    }

    void "oversized fields should not be allowed"(){
        given: "a valid SyndicationRequest instance"
            def instance_SyndicationRequest = SyndicationRequest.build()

        when: "fields are 1 bigger than the allowed max"
            instance_SyndicationRequest.requestedUrl = 'a' * 5001
            instance_SyndicationRequest.contactEmail = 'a' * 256
            instance_SyndicationRequest.requesterNote = 'a' * 2001
            instance_SyndicationRequest.adminNote = 'a' * 2001
            instance_SyndicationRequest.status = 'a' * 256

        then: "the instance should not validate"
            instance_SyndicationRequest.validate() == false

        and: "there should be 5 errors"
            instance_SyndicationRequest.errors.errorCount == 5

        and: "the errors should be maxSize"
            instance_SyndicationRequest.errors["requestedUrl"] == "maxSize"
            instance_SyndicationRequest.errors["requesterNote"] == "maxSize"
            instance_SyndicationRequest.errors["adminNote"] == "maxSize"
            instance_SyndicationRequest.errors["contactEmail"] == "maxSize"
            instance_SyndicationRequest.errors["status"] == "maxSize"
    }

    void "boundary sized fields should validate"(){
        given: "a valid SyndicationRequest instance"
            def instance_SyndicationRequest = SyndicationRequest.build()

        when: "the fields are set to the max size value"
            instance_SyndicationRequest.requestedUrl = 'a' * 5000
            instance_SyndicationRequest.contactEmail = 'a' * 255
            instance_SyndicationRequest.requesterNote = 'a' * 2000
            instance_SyndicationRequest.adminNote = 'a' * 2000
            instance_SyndicationRequest.status = 'a' * 255

        then: "the instance should validate"
            instance_SyndicationRequest.validate() == true

        and: "There should be no errors"
            instance_SyndicationRequest.errors.errorCount == 0
    }
}
