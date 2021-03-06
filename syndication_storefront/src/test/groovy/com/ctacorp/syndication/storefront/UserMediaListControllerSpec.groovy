package com.ctacorp.syndication.storefront

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.media.MediaItem

@TestFor(UserMediaListController)
@Mock([UserMediaList, MediaItem, User])
class UserMediaListControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["name"] = 'someValidName'
        params["description"] = 'someValidDescription'
        params["dateCreated"] = new Date()
        params["lastUpdated"] = new Date()
        params["mediaItemIds"] = "1,2,3"
        params["user"] = controller.springSecurityService.getCurrentUser()
    }

    def setup() {
        controller.springSecurityService = [getCurrentUser: { User.get(1) }]
        controller.mediaService = [getFeaturedMedia: { max -> [] }]

        new MediaItem(name: "Item 1", sourceUrl: "http://www.example.com/a", source: new Source(), language: new Language()).save()
        new MediaItem(name: "Item 2", sourceUrl: "http://www.example.com/b", source: new Source(), language: new Language()).save()
        new MediaItem(name: "Item 3", sourceUrl: "http://www.example.com/c", source: new Source(), language: new Language()).save()

        new User(username: "person@example.com", password: "ABC123def").save()
    }

    def "Test the index action returns the correct model"() {
        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            !model.userMediaListList
            model.userMediaListInstanceCount == 0
    }

    def "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.userMediaList != null
    }

    def "Test the save action correctly persists an instance"() {
        when: "The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def userMediaList = new UserMediaList()
            userMediaList.validate()
            controller.save(userMediaList)

        then: "The create view is rendered again with the correct model"
            model.userMediaList != null
            view == 'create'

        when: "The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            userMediaList = new UserMediaList(params)
            userMediaList.user = User.load(1)
            controller.save(userMediaList)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == '/userMediaList/show/1'
            controller.flash.message != null
            UserMediaList.count() == 1
    }

    def "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def userMediaList = new UserMediaList(params)
            controller.show(userMediaList)

        then: "A model is populated containing the domain instance"
            model.userMediaList == userMediaList
    }

    def "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def userMediaList = new UserMediaList(params)
            controller.edit(userMediaList)

        then: "A model is populated containing the domain instance"
            model.userMediaList == userMediaList
    }

    def "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/userMediaList/index'
            flash.message != null

        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def userMediaList = new UserMediaList()
            userMediaList.validate()
            controller.update(userMediaList)

        then: "The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.userMediaList == userMediaList

        when: "A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            userMediaList = new UserMediaList(params)
            userMediaList.user = controller.springSecurityService.getCurrentUser()
            userMediaList.save(flush: true)
            controller.update(userMediaList)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/userMediaList/show/${userMediaList?.id}"
            flash.message != null
    }

    def "delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "DELETE"

            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/userMediaList/index'
            flash.message != null

        when: "Two media lists are created"
            response.reset()
            populateValidParams(params)
            def userMediaList = new UserMediaList(params)
            userMediaList.save(flush: true)
            def userMediaList2 = new UserMediaList(params)
            userMediaList2.save(flush: true)

        then: "They exist"
            UserMediaList.count() == 2

        when: "The domain instance is passed to the delete action"
            controller.delete(userMediaList2)

        then: "The instance is deleted"
            UserMediaList.count() == 1
            response.redirectedUrl == '/userMediaList/index'
            flash.message != null
    }

    def "a user should not be able to delete their last list"() {
        given: "the user's last list"
            populateValidParams(params)
            def userMediaList = new UserMediaList(params)
            userMediaList.save(flush: true)
        when: "the delete action is called"
            request.contentType = FORM_CONTENT_TYPE
            request.method = "DELETE"
            controller.delete(userMediaList)
        then: "the item should still exist"
            UserMediaList.count() == 1
    }
}