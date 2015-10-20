package com.ctacorp.syndication.authentication

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User])
@Build([User, UserRole, Role])
class UserServiceSpec extends Specification {
    User user
    def params

    def setup() {
        service.passwordService = [validatePassword: {p1, p2 -> [valid:true]}]

        user = User.build()
        params = [:]
        user.properties.each{ key, val ->
            params[key] = val
        }
        service.springSecurityService = [
                currentUser:user,
                isLoggedIn:{ true }
        ]
    }

    def "if user is not logged in, they shouldn't be able to change credentials"(){
        setup: "mocked spring security service"
            service.springSecurityService = [
                    currentUser:[],
                    isLoggedIn:{ false }
            ]
            params = [name:"some other name"]
        when: "update account is called"
            def result = service.updateUser(params)
        then: "name should be updated to be null"
            !result
    }

    def "updateUserAccount should ignore update requests for bad passwords"(){
        setup: "mocked spring security service"
            service.passwordService = [validatePassword: {p1, p2 -> [valid:false]}]
            params = [password:"a", passwordRepeat : "a"]
        when: "update account is called"
            User updatedUser = service.updateUser(params)
        then: "there should be a user instance"
            updatedUser
        and: "it should have a null name"
            !updatedUser.name
    }

    def "updateUserAccount should ignore update requests invalid updates to data"(){
        setup: "mocked spring security service"
            params = [username:null]
        when: "update account is called"
            User updatedUser = service.updateUser(params)
        then: "name should be updated to be null"
            updatedUser.errors
            updatedUser.errors["password"]
    }

    def "updateUserAccount should allow a null username"(){
        setup: "mocked spring security service"
            params.name = null
            params.username = "someUser@example.com"
            params.password = "ABC123def"
            params.passwordRepeat = params.password
        when: "update account is called"
            User updatedUser = service.updateUser(params)
        then: "name should be updated to be null"
            !updatedUser.name
    }

    def "updateUserAccount should allow changing a username"(){
        setup: "mocked spring security service"
            params.name = "some other name"
            params.username = "someUser@example.com"
            params.password = "ABC123def"
            params.passwordRepeat = params.password
        when: "update account is called"
            User updatedUser = service.updateUser(params)
        then: "name should be updated to be null"
            updatedUser.name == "some other name"
    }
}
