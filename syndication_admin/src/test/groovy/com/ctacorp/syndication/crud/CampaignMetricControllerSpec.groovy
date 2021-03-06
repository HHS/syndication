
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.metric.CampaignMetric
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(CampaignMetricController)
@Mock([CampaignMetric, Campaign])
class CampaignMetricControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params["campaign"] = new Campaign()
//        params["name"] = 'Allergy'
//        params["startDate"] = new Date()
//        params["source"] = new Source()
        params["day"] = new Date()
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.campaignMetricList
            model.campaignMetricInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.campaignMetric != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
            request.method = 'POST'
            request.contentType = FORM_CONTENT_TYPE
            def campaignMetric = new CampaignMetric()
            campaignMetric.validate()
            controller.save(campaignMetric)

        then: "The create view is rendered again with the correct model"
            model.campaignMetric != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            campaignMetric = new CampaignMetric(params)
            controller.save(campaignMetric)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/campaignMetric/show/1'
            controller.flash.message != null
            CampaignMetric.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def campaignMetric = new CampaignMetric(params)
            controller.show(campaignMetric)

        then: "A model is populated containing the domain instance"
            model.campaignMetric == campaignMetric
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def campaignMetric = new CampaignMetric(params)
            controller.edit(campaignMetric)

        then: "A model is populated containing the domain instance"
            model.campaignMetric == campaignMetric
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/campaignMetric/index'
            flash.message != null


        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def campaignMetric = new CampaignMetric()
            campaignMetric.validate()
            controller.update(campaignMetric)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.campaignMetric == campaignMetric

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            campaignMetric = new CampaignMetric(params).save(flush: true)
            controller.update(campaignMetric)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/campaignMetric/show/$campaignMetric.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/campaignMetric/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def campaignMetric = new CampaignMetric(params).save(flush: true)

        then: "It exists"
            CampaignMetric.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(campaignMetric)

        then: "The instance is deleted"
            CampaignMetric.count() == 0
            response.redirectedUrl == '/campaignMetric/index'
            flash.message != null
    }
}
