/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.tagcloud.controller

import com.ctacorp.syndication.Language
import tagcloud.TagController
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(TagController)
@Build([ContentItem, Tag, TagType])
@Mock([ContentItem, Tag, Language, TagType])
class TagControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params['name'] = "a tag"
        params['type'] = TagType.build()
        params['language'] = Language.load(1)
        params["syndicationId"] = 1
        params["url"] = "http://www.example.com"
    }

    def setup() {
        Language eng = new Language(name:"English", isoCode: "eng").save(flush:true)

        populateValidParams(params)
    }

    void "index action returns the correct model"() {
        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.tagInstanceList
            model.tagInstanceCount == 0
    }

    void "create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.tagInstance != null
    }

    void "save action correctly persists an instance"() {
        when: "The save action is executed with an invalid instance"
            controller.solrIndexingService = [inputTag:{a, b -> null}]
            request.contentType = FORM_CONTENT_TYPE
            request.method = "POST"
            def tag = new Tag()
            tag.validate()
            controller.save(tag)

        then: "The create view is rendered again with the correct model"
            model.tagInstance != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            tag = Tag.build()
            controller.save(tag)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/tag/show/1'
            controller.flash.message != null
            Tag.count() == 1
    }

    void "show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def tag = new Tag(params)
            controller.show(tag)

        then: "A model is populated containing the domain instance"
            model.tagInstance == tag
    }

    void "edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def tag = new Tag(params)
            controller.edit(tag)

        then: "A model is populated containing the domain instance"
            model.tagInstance == tag
    }


    void "update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            controller.solrIndexingService = [inputTag:{a, b -> null}]
            request.contentType = FORM_CONTENT_TYPE
            request.method = "PUT"
            controller.update(-1)

        then: "A 404 error is returned"
            response.redirectedUrl == '/tag/index'
            flash.message != null

        when: "An invalid domain instance is passed to the update action"
            response.reset()
            Tag tag = new Tag(name:"someTag", language: params.language, type:TagType.get(1)).save(flush:true)
            params.name = null
            controller.update(tag.id)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.tagInstance == tag

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            tag = new Tag(params).save(flush: true)
            controller.update(tag.id)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/tag/show/$tag.id"
            flash.message != null
    }

    void "delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "DELETE"
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/tag/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            def tag = Tag.build()

        then: "It exists"
            Tag.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(tag)

        then: "The instance is deleted"
            Tag.count() == 0
            response.redirectedUrl == '/tag/index'
            flash.message != null
    }
}