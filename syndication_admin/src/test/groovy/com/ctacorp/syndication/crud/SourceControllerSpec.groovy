
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.Source
import com.ctacorp.syndication_elasticsearch_plugin.ElasticsearchService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(SourceController)
@Mock([Source, MediaItem])
class SourceControllerSpec extends Specification {

    def elasticsearchService = Mock(ElasticsearchService)

    void setup() {
        controller.elasticsearchService = elasticsearchService
    }

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params["name"] = 'National Institute on Aging'
        params["acronym"] = 'NIA'
        params["websiteUrl"] = 'https://www.nia.nih.gov'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.sourceList
            model.sourceInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.source != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "POST"
            def source = new Source()
            source.validate()
            controller.save(source)

        then: "The create view is rendered again with the correct model"
            model.source != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            source = new Source(params)

            controller.save(source)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/source/show/1'
            controller.flash.message != null
            Source.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def source = new Source(params)
            controller.show(source)

        then: "A model is populated containing the domain instance"
            model.source == source
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def source = new Source(params)
            controller.edit(source)

        then: "A model is populated containing the domain instance"
            model.source == source
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "PUT"
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/source/index'
            flash.message != null


        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def source = new Source()
            source.validate()
            controller.update(source)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.source == source

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            source = new Source(params).save(flush: true)
            controller.update(source)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/source/show/$source.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "DELETE"
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/source/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def source = new Source(params).save(flush: true)

        then: "It exists"
            Source.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(source)

        then: "The instance is deleted"
            Source.count() == 0
            response.redirectedUrl == '/source/index'
            flash.message != null
    }
}
