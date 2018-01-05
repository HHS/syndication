package com.ctacorp.syndication.manager.cms.controller
import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
@Build([User, Role, UserRole, Subscriber])
class UserControllerSpec extends Specification {

    User user
    User unsavedUser
    Role role1
    Role role2
    UserRole userRole
    Subscriber subscriber1
    Subscriber subscriber2

    def loggingService = Mock(LoggingService)
    def springSecurityService = Mock(SpringSecurityService)
    def userSubscriberService = Mock(UserSubscriberService)

    def setup() {

        role1 = Role.build()
        role2 = Role.build()
        user = User.build(username: "Al Kohol")

        subscriber1 = Subscriber.build()
        subscriber2 = Subscriber.build()
        unsavedUser = User.buildWithoutSave()

        controller.loggingService = loggingService
        controller.springSecurityService = springSecurityService
        controller.userSubscriberService = userSubscriberService
    }

    void "save action handles when the username already exists"() {

        given: "an existing username"

        unsavedUser.username = 'Al Kohol'

        when: "calling the action"

        request.method = 'POST'
        controller.save(unsavedUser)

        then: "redirect to the index with an existing user error message"

        response.redirectUrl == '/user/index'
        flash.errors == ['default.exists.message']
    }

    void "save action handles user validation errors"() {

        given: "the username is null"

        unsavedUser.username = null

        when: "calling the action"

        request.method = 'POST'
        controller.save(unsavedUser)

        then: "respond with the create view and the domain errors"

        view == "create"
        unsavedUser.errors.errorCount == 1
    }

    void "save action creates a user with associated subscribers"() {

        setup: "add the subscriber ids and the role to the params"

        params.subscribers = [subscriber1.id, subscriber2.id]
        params.role = role1.id

        when: "calling the action"

        request.method = 'POST'
        controller.save(unsavedUser)

        then: "create the user subscriber associations"

        1 * userSubscriberService.associateUserWithSubscribers([subscriber1, subscriber2], unsavedUser) >> [subscriber1, subscriber2]

        and: "create the user role"

        UserRole.count == 1

        and: "create the user"

        User.count == 2

        and: "redirect the user to show view with a created message"

        response.redirectUrl == "/user/show/2"
        flash.message == "default.created.message"
    }

    void "update action handles a null instance"() {

        when: "calling the action"

        request.method = 'PUT'
        controller.update(null)

        then: "redirect to the index with an existing user error message"

        response.redirectUrl == '/user/index'
        flash.errors == ['default.not.found.message']
    }

    void "update action handles validation errors"() {

        given: "an invalid user domain object"

        user.password = null

        when: "calling the action"

        request.method = 'PUT'
        controller.update(user)

        then: "respond with the edit view the domain errors"

        view == "edit"
        user.errors.errorCount == 1
    }

    void "update action updates a user"() {

        setup: "add the subscriber ids and the role to the params"

        params.subscribers = [subscriber1.id, subscriber2.id]
        params.role = role2.id

        and: "create an existing user role"

        userRole = UserRole.build(role: role1, user: user)

        when: "calling the action"

        request.method = 'PUT'
        controller.update(user)

        then: "disassociate any existing subscribers"

        1 * userSubscriberService.disassociateUserFromSubscribers(user)

        and: "create the user subscriber associations"

        1 * userSubscriberService.associateUserWithSubscribers([subscriber1, subscriber2], user) >> [subscriber1, subscriber2]

        and: "update the role"

        UserRole.count == 1
        UserRole.first().role == role2

        and: "redirect the user to show view with an updated message"

        response.redirectUrl == "/user/show/1"
        flash.message == "default.updated.message"
    }

    void "delete action handles a null instance"() {

        when: "calling the action"

        request.method = 'DELETE'
        controller.delete(null)

        then: "redirect to the index with an existing user error message"

        response.redirectUrl == '/user/index'
        flash.errors == ['default.not.found.message']
    }

    void "delete action deletes a user"() {

        when: "calling the action"

        request.method = 'DELETE'
        controller.delete(user)

        then: "remove the user role"

        UserRole.count == 0

        and: "remove any user subscriber associations"

        userSubscriberService.disassociateUserFromSubscribers(user)

        and: "redirect to the index with a deleted message"

        response.redirectUrl == '/user/index'
        flash.message == 'default.deleted.message'
    }
}
