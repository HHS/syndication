/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.tagcloud.controller

import tagcloud.ContentItemController
import tagcloud.domain.ContentItem
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(ContentItemController)
@Mock(ContentItem)
class ContentItemControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["url"] = "http://www.example.com"
        params["syndicationId"] = 1
    }

    void "Test the index action returns the correct model"() {
        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.contentItemList
            model.contentItemInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.contentItem != null
    }

    void "Test the save action correctly persists an instance"() {
        when: "The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def contentItem = new ContentItem()
            contentItem.validate()
            controller.save(contentItem)

        then: "The create view is rendered again with the correct model"
            model.contentItem != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            contentItem = new ContentItem(params)

            controller.save(contentItem)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/contentItem/show/1'
            controller.flash.message != null
            ContentItem.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def contentItem = new ContentItem(params)
            controller.show(contentItem)

        then: "A model is populated containing the domain instance"
            model.contentItem == contentItem
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def contentItem = new ContentItem(params)
            controller.edit(contentItem)

        then: "A model is populated containing the domain instance"
            model.contentItem == contentItem
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/contentItem/index'
            flash.message != null

        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def contentItem = new ContentItem()
            contentItem.validate()
            controller.update(contentItem)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.contentItem == contentItem

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            contentItem = new ContentItem(params).save(flush: true)
            controller.update(contentItem)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/contentItem/show/$contentItem.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/contentItem/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def contentItem = new ContentItem(params).save(flush: true)

        then: "It exists"
            ContentItem.count() == 1

        when: "The domain instance is passed to the delete action"
            //Simple mock for the delete service method
            controller.contentItemService = [delete:{ContentItem ci -> ci.delete(flush:true)}]
            controller.delete(contentItem)

        then: "The instance is deleted"
            ContentItem.count() == 0
            response.redirectedUrl == '/contentItem/index'
            flash.message != null
    }
}