package com.ctacorp.syndication.metric

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.metric.CampaignMetric

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(CampaignMetric)
class CampaignMetricConstraintsSpec extends Specification {

    def setup() {
        mockForConstraintsTests CampaignMetric
    }

    void "valid instance should be valid"() {
        given: "a valid campaignMetric instance"
            def campaignMetricInstance = CampaignMetric.build()

        when: "the instance is validated"
            campaignMetricInstance.validate()

        then: "there should be no errors"
            campaignMetricInstance.errors.errorCount == 0
    }

    void "invalid null values shouldn't be allowed"(){
        given: "a valid campaignMetric instance"
            def campaignMetricInstance = CampaignMetric.build()

        when: "the fields campaign and day are set to null"
            campaignMetricInstance.day = null
            campaignMetricInstance.campaign = null

        then: "the instance should not validate"
            campaignMetricInstance.validate() == false

        and: "there should be 2 errors"
            campaignMetricInstance.errors.errorCount == 2

        and: "the errors should be 'nullable' for both fields"
            campaignMetricInstance.errors["day"] == "nullable"
            campaignMetricInstance.errors["campaign"] == "nullable"
    }

    void "oversize viewCounts should not be allowed"(){
        given: "a valid campaignMetric instance"
            def campaignMetricInstance = CampaignMetric.build()

        when: "the fields are 1 larger than the max"
            campaignMetricInstance.storefrontViewCount = Long.MAX_VALUE
            campaignMetricInstance.apiViewCount = Long.MAX_VALUE

        then: "the instance should not validate"
            campaignMetricInstance.validate() == false

        and: "there should be 2 errors"
            campaignMetricInstance.errors.errorCount == 2

        and: "the errors should be 'max' for both fields"
            campaignMetricInstance.errors["storefrontViewCount"] == "max"
            campaignMetricInstance.errors["apiViewCount"] == "max"
    }

    void "undersize viewCounts should not be allowed"(){
        given: "a valid campaignMetric instance"
            def campaignMetricInstance = CampaignMetric.build()

        when: "the fields are 1 less than the min"
            campaignMetricInstance.storefrontViewCount = -1L
            campaignMetricInstance.apiViewCount = -1L

        then: "the instance should not validate"
            campaignMetricInstance.validate() == false

        and: "there should be 2 errors"
            campaignMetricInstance.errors.errorCount == 2

        and: "the errors should be 'max' for both fields"
            campaignMetricInstance.errors["storefrontViewCount"] == "min"
            campaignMetricInstance.errors["apiViewCount"] == "min"
    }

    void "edge values for viewCounts should be allowed"(){
        given: "two valid campaignMetric instances"
            def campaignMetricInstance1 = CampaignMetric.build()
            def campaignMetricInstance2 = CampaignMetric.build()

        when: "the fields are set to max and min"
            campaignMetricInstance1.storefrontViewCount = Long.MAX_VALUE -1
            campaignMetricInstance1.apiViewCount = Long.MAX_VALUE -1
            campaignMetricInstance2.storefrontViewCount = 0
            campaignMetricInstance2.apiViewCount = 0

        then: "the instance should validate"
            campaignMetricInstance1.validate() == true
            campaignMetricInstance2.validate() == true

        and: "there should be 0 errors"
            campaignMetricInstance1.errors.errorCount == 0
            campaignMetricInstance2.errors.errorCount == 0
    }
}
