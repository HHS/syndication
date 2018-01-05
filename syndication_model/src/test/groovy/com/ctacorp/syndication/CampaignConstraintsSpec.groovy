package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.Campaign

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Campaign)
class CampaignConstraintsSpec extends Specification {

    void "valid instance is valid"() {
        given: "A valid campaign instance"
            def campaign = Campaign.build()

        when: "Instance is validated"
            campaign.validate()

        then: "There should be no errors"
            campaign.errors.errorCount == 0
    }

    void "non-nullable fields do not validate"(){
        given: "A valid campaign instance"
            def campaign = Campaign.build()

        when: "name and startDate are set to null"
            campaign.name = null
            campaign.startDate = null

        then: "instance should not validate"
            campaign.validate() == false

        and: "there should be 2 errors"
            campaign.errors.errorCount == 2

        and: "the errors should be nullable"
            campaign.errors["name"].code == "nullable"
            campaign.errors["startDate"].code == "nullable"
    }

    void "descriptions bigger than 2000 characters should not validate"(){
        given: "A valid campaign instance"
            def campaign = Campaign.build()

        when: "the duration is set to 1 larger than the max allowed"
            campaign.description = "a" * 2001

        then: "instance should not validate"
            campaign.validate() == false

        and: "there should be 1 error"
            campaign.errors.errorCount == 1

        and: "the error should be max"
            campaign.errors["description"].code == "maxSize.exceeded"
    }

    void "descriptions 2000 chars or smaller should validate"(){
        given: "A valid campaign instance"
            def campaign = Campaign.build()

        when: "the duration is set to the max allowed"
            campaign.description = "a" * 2000

        then: "instance should validate"
            campaign.validate() == true

        and: "there should be 0 errors"
            campaign.errors.errorCount == 0
    }

    void "verify blank values do not validate"(){
        given: "A valid campaign instance"
            def campaign = Campaign.build()

        when: "the name and description are set to a space"
            campaign.name = ""
            campaign.description = ""

        then: "the instance should not validate"
            campaign.validate() == false

        and: "there should be 2 errors"
            campaign.errors.errorCount == 2

        and: "the errors should be blank"
            campaign.errors["name"].code == "blank"
            campaign.errors["description"].code == "blank"
    }
}