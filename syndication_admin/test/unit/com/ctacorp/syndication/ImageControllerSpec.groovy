package com.ctacorp.syndication

import com.ctacorp.syndication.crud.ImageController
import com.ctacorp.syndication.media.Image
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(ImageController)
@Mock(Image)
class ImageControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.imageInstanceList
            model.imageInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.imageInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            def image = new Image()
            image.validate()
            controller.save(image)

        then:"The create view is rendered again with the correct model"
            model.imageInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            image = new Image(params)

            controller.save(image)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/image/show/1'
            controller.flash.message != null
            Image.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def image = new Image(params)
            controller.show(image)

        then:"A model is populated containing the domain instance"
            model.imageInstance == image
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def image = new Image(params)
            controller.edit(image)

        then:"A model is populated containing the domain instance"
            model.imageInstance == image
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/image/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def image = new Image()
            image.validate()
            controller.update(image)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.imageInstance == image

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            image = new Image(params).save(flush: true)
            controller.update(image)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/image/show/$image.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/image/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def image = new Image(params).save(flush: true)

        then:"It exists"
            Image.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(image)

        then:"The instance is deleted"
            Image.count() == 0
            response.redirectedUrl == '/image/index'
            flash.message != null
    }
}
