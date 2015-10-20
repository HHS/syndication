package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by nburk on 6/23/15.
 */
@TestFor(ExtendedAttributeService)
@Mock([User, UserRole, Role, ExtendedAttribute, MediaItem, MediaItemSubscriber])
class ExtendedAttributeServiceSpec extends Specification {

    def setup(){
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1", subscriberId: 1).save()
        Role role = new Role(authority: "ROLE_PUBLISHER").save()
        UserRole.create user, role, true
        service.springSecurityService = [currentUser:User.get(1)]

    }

    Map populateValidParams(params = [:]) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
        params
    }

    def "test getUpdateInformation to see if attribute already exists and should exist"() {
        setup: ""
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def ea = new ExtendedAttribute([name:"valid name", value:"15", url:"http://www.example.com", mediaItem: mi]).save(flush:true)

        when:"when the method is called"
            def response = service.getUpdateInformation(ea)

        then:"the item should be returned because it already exists in the database"
            response != null
    }

    def "test getUpdateInformation to see if attribute already exists and should not exist"() {
        setup: ""
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def ea = new ExtendedAttribute([name:"valid name", value:"15", url:"http://www.example.com", mediaItem: mi])

        when:"when the method is called"
            def response = service.getUpdateInformation(ea)

        then:"the item should be returned because it already exists in the database"
            response == null
    }

    def "test ifPublisherValid with valid publisher"() {
        setup:"a valid alt image with a mediaItem that the publisher owns"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            new MediaItemSubscriber(subscriberId: 1, mediaItem: mi).save(flush:true)
            def ea = new ExtendedAttribute([name:"valid name", value:"15", url:"http://www.example.com", mediaItem: mi])


        when:"ifPublisherValid is called"
            def response = service.ifPublisherValid(ea)

        then:"the publisher should be valid"
            response
    }

    def "test ifPublisherValid with invalid publisher"() {
        setup: "a valid image with a mediaItem that doesn't belong to the publisher"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def ea = new ExtendedAttribute([name:"valid name", value:"15", url:"http://www.example.com", mediaItem: mi])

        when:"ifPublisherValid is called"
            def response = service.ifPublisherValid(ea)

        then:"the publisher should not valid"
            !response
    }
}