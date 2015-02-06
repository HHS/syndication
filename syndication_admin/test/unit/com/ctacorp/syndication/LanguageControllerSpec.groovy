package com.ctacorp.syndication

import com.ctacorp.syndication.crud.LanguageController
import grails.test.mixin.*
import spock.lang.*

@TestFor(LanguageController)
@Mock(Language)
class LanguageControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.languageInstanceList
            model.languageInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.languageInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
            def language = new Language()
            language.validate()
            controller.save(language)

        then: "The create view is rendered again with the correct model"
            model.languageInstance != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            language = new Language(params)

            controller.save(language)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/language/show/1'
            controller.flash.message != null
            Language.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def language = new Language(params)
            controller.show(language)

        then: "A model is populated containing the domain instance"
            model.languageInstance == language
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def language = new Language(params)
            controller.edit(language)

        then: "A model is populated containing the domain instance"
            model.languageInstance == language
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/language/index'
            flash.message != null


        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def language = new Language()
            language.validate()
            controller.update(language)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.languageInstance == language

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            language = new Language(params).save(flush: true)
            controller.update(language)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/language/show/$language.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/language/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def language = new Language(params).save(flush: true)

        then: "It exists"
            Language.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(language)

        then: "The instance is deleted"
            Language.count() == 0
            response.redirectedUrl == '/language/index'
            flash.message != null
    }
}
