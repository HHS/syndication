package com.ctacorp.syndication.authentication

import com.ctacorp.syndication.CmsManagerKeyService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(UserController)
@Mock([User, Role, User, UserRole])
class UserControllerSpec extends Specification {

    def userService = Mock(UserService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)

    void setup() {
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1").save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        controller.springSecurityService = [currentUser:User.get(1)]

        def userMockService = mockFor(UserService, true)
        userMockService.demand.saveUserAndRole {User userInstance, String authority ->
            userInstance.save flush:true
        }
        userMockService.demand.deleteUser {User userInstance ->
            userInstance.delete flush: true
        }
        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.userService = userService
        User.metaClass.encodePassword = { -> }
    }

    def populateValidParams(params) {
        assert params != null
        params["id"] = 1
        params["name"] = "admin"
        params["username"] = "admin@example.com"
        params["password"] = "password"
        params["authority"] = "ROLE_ADMIN"

    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            1 * userService.indexResponse(params)
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            model.userInstance != null
    }

    void "Test the save action correctly persists an instance"() {
        setup:
        populateValidParams(params)
        def user1 = new User(params).save()

        when: "The save action is executed with an invalid instance"
            request.method = 'POST'
            params.authority = 1
            request.contentType = FORM_CONTENT_TYPE
            def user = new User([username:"admin@example.com"])
            user.validate()
            controller.save(user)

        then: "The create view is rendered again with the correct model"
            view == 'create'
            model.userInstance != null


        when: "The save action is executed with a valid instance"
            response.reset()
            params.authority = 1
            params.password = "match"
            params.passwordVerify = "match"
            controller.save(user1)

        then: "A redirect is issued to the show action"
            response.redirectedUrl == "/user/show/$user1.id"
            1 * controller.userService.saveUserAndRole(user1, 1)
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the show action"
            populateValidParams(params)
            def user = new User(params)
            controller.show(user)

        then: "A model is populated containing the domain instance"
            model.userInstance == user
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def user = new User(params)
            controller.edit(user)

        then: "A model is populated containing the domain instance"
            model.userInstance == user
    }

    void "Test the update action performs an update on a valid domain instance"() {
        setup:
            populateValidParams(params)
            def user1 = new User(params).save(flush: true)

        when: "Update is called for a domain instance that doesn't exist"
            request.method = 'PUT'
            params.authority = 1
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/user/index'
            flash.message != null


        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def user = new User()
            user.validate()
            controller.update(user)

        then: "The edit view is rendered again with the invalid instance"
            response.redirectedUrl == '/user/edit'

        when: "A valid domain instance is passed to the update action"
            response.reset()
            params.password = "match"
            params.passwordVerify = "match"
            controller.update(user1)

        then: "A redirect is issues to the show action"
            response.redirectedUrl == "/user/show/$user1.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/user/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def user = new User(params)

        then: "It exists"
            User.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(user)

        then: "The userService delete method is called"
            1 * controller.userService.deleteUser(user)
            response.redirectedUrl == '/user/index'
            flash.message != null
    }
}
