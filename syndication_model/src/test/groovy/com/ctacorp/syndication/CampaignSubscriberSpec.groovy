package com.ctacorp.syndication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(CampaignSubscriber)
class CampaignSubscriberSpec extends Specification {

    void "test a valid instance"() {
        given: "A valid CampaignSubscriber instance"
        def campaignSubscriber = CampaignSubscriber.build()

        when: "CampaignSubscriber is validated"
            campaignSubscriber.validate()

        then: "The instance should have no errors"
            campaignSubscriber.errors.errorCount == 0
    }
    
    def "nulls are not allowed"(){
        given: "A valid CampaignSubscriber instance"
            def campaignSubscriber = CampaignSubscriber.build()

        when: "Non-nullable values are set to null"
            campaignSubscriber.subscriberId = null
            campaignSubscriber.campaign = null

        then: "Then the instance should not validate"
            !campaignSubscriber.validate()

        and: "The instance should have two errors"
            campaignSubscriber.errors.errorCount == 2

        and: "The errors should be because of nullable"
            campaignSubscriber.errors["subscriberId"].code == "nullable"
            campaignSubscriber.errors["campaign"].code == "nullable"
    }
    
}
