package com.ctacorp.tinyurl



import grails.test.mixin.*
import spock.lang.*
import grails.buildtestdata.mixin.Build

import static org.springframework.http.HttpStatus.NOT_FOUND

@TestFor(MediaMappingController)
@Build(MediaMapping)
class MediaMappingControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.mediaMappingList
            model.mediaMappingInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.mediaMapping!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def mediaMapping = new MediaMapping()
            mediaMapping.validate()
            controller.save(mediaMapping)

        then:"The create view is rendered again with the correct model"
            model.mediaMapping!= null
            view == 'create'

        when:"The save action is executed with a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            controller.save(null)

        then:"The create view is rendered again with the correct model"
            response.redirectedUrl == '/'
            flash.message != null


        when:"The save action is executed with a valid instance"
            response.reset()
            mediaMapping = MediaMapping.build()
            controller.save(mediaMapping)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/mediaMapping/show/1'
            controller.flash.message != null
            MediaMapping.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def mediaMapping = new MediaMapping(params)
            controller.show(mediaMapping)

        then:"A model is populated containing the domain instance"
            model.mediaMapping == mediaMapping
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def mediaMapping = new MediaMapping(params)
            controller.edit(mediaMapping)

        then:"A model is populated containing the domain instance"
            model.mediaMapping == mediaMapping
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            // Ideally it should return to mediaMapping/index but because of UrlMapping rule it returned to /
            response.redirectedUrl == '/'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def mediaMapping = new MediaMapping()
            mediaMapping.validate()
            controller.update(mediaMapping)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.mediaMapping == mediaMapping

        when:"A valid domain instance is passed to the update action"
            response.reset()
            mediaMapping = MediaMapping.build()
            controller.update(mediaMapping)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/mediaMapping/show/$mediaMapping.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            // Ideally it should return to /mediaMapping/index but because of UrlMapping rule it returned to /
            response.redirectedUrl == '/'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            def mediaMapping=MediaMapping.build()
            mediaMapping.save(flush: true)

        then:"It exists"
            MediaMapping.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(mediaMapping)

        then:"The instance is deleted"
            MediaMapping.count() == 0
            // Ideally it should return to /mediaMapping/index but because of UrlMapping rule it returned to /
            response.redirectedUrl == '/'
            flash.message != null
    }


}
