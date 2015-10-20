package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(StorefrontTagLib)
@Mock([User, Role, UserRole])
class StorefrontTagLibSpec extends Specification {
    def setup() {
        tagLib.grailsApplication = [
                config:[syndication:[   serverUrl:"http://www.example.com",
                                        apiPath:"/api/v1/abc123"
                ]]
        ]
    }

    def "pageContentAnchor should render a page content anchor tag"() {
        expect: "pageContentAnchor returns an anchor"
            tagLib.pageContentAnchor().toString() == "<a style='float:left' id='pageContent'>&nbsp;</a>"
    }

    def "mediaListApiLink should generate links for the api"() {
        expect: "mediaListApiLink renders a link with the correct id"
            tagLib.mediaListApiLink(id:1L).toString() == '<a href=\'http://www.example.com/api/v1/abc123/resources/userMediaLists/1\'>http://www.example.com/api/v1/abc123/resources/userMediaLists/1</a>'
    }

    def "mediaListApiLink should return and empty string when no ID is provided"() {
        expect: "mediaListApiLink without ID should be an empty string"
            tagLib.mediaListApiLink().toString() == ""
    }

    def "hasErrors should read the errors from the flash object"(){
        given: "Some errors in flash"
            flash.errors = [
                    [message:"Too long"],
                    [message:"Too short"],
                    [message:"Too fast"],
                    [message:"Too slow"]
            ]
        when: "tagLib is rendered"
            def result = tagLib.hasErrors().toString()
        then: "We should have the expected output"
            def errorMessages = flash.errors*.message.collect{ "<li>${it}</li>" }.join()
            "$result" == "<div class=\"row\"><div class=\"alert alert-danger alert-dismissable break-word\"><button type=\"button\" class=\"close pull-right\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><ul>${errorMessages}</ul></div></div>"
    }

    def "hasErrors should be an empty string if no errors exist in flash"() {
        given: "empty flash"
            flash.errors = null
        when: "tag is called"
            def result = tagLib.hasErrors().toString()
        then: "result should be an empty string"
            result == ""
    }

    def "currentuser should render a register and a login button when not logged in"() {
        given: "A un-logged session"
            tagLib.springSecurityService = [currentUser:null]
        when: "currentUser is called"
            def result = tagLib.currentUser().toString()
        then: "the expected nav links should be rendered, and nothing more"
            result.contains("Register")
            result.contains("Login")
            !result.contains("Lists")
            !result.contains("Microsites")
            !result.contains("Logout")
    }

    def "currentUser should render a logout button and a name if logged in"(){
        given: "a logged in user"
            User user = new User(name: "someGuy", username: "someGuy@example.com", password:"abc123DEF").save()
            Role role = new Role(authority: "ROLE_BANANA").save()
            UserRole.create(user, role, true)
            tagLib.springSecurityService = [currentUser:User.read(1)]
        when: "currentUser is called"
            def result = tagLib.currentUser().toString()
        then: "the expected nav links should be rendered and nothing more"
            result.contains("someGuy".encodeAsHTML())
            result.contains("Lists")
            result.contains("Logout")
            !result.contains("Register")
            !result.contains("Microsites")
            !result.contains("Login")
    }

    def "currentUser should also render microsites link if user is a publisher"(){
        given: "a logged in user"
            User user = new User(name: "someGuy", username: "someGuy@example.com", password:"abc123DEF").save()
            Role role = new Role(authority: "ROLE_PUBLISHER").save()
            UserRole.create(user, role, true)
            tagLib.springSecurityService = [currentUser:User.read(1)]
        when: "currentUser is called"
            def result = tagLib.currentUser().toString()
        then: "the expected nav links should be rendered and nothing more"
            result.contains("someGuy".encodeAsHTML())
            result.contains("Lists")
            result.contains("Microsites")
            result.contains("Logout")
            !result.contains("Register")
            !result.contains("Login")
    }
}
