package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by nburk on 6/22/15.
 */
@TestFor(AlternativeImageService)
@Mock([User, UserRole, Role, AlternateImage, MediaItem, MediaItemSubscriber])
class AlternativeImageServiceSpec extends Specification {

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

    def "test add alternative image"() {
        setup:"set up valid alt image"
            def ai2 = new AlternateImage([name:"valid name", url:"http://www.example.com", mediaItem: new MediaItem()])
            def ai = null

        when:"add alternative image is called"
            def response = service.addAlternativeImage(ai)

        then: "image not found should be returned"
            response == "Image not found"

        when:"add alternative image is called with errors"
            ai = new AlternateImage()
            response = service.addAlternativeImage(ai)

        then: "error response must be returned"
            response == "You must include a valid Name and Url"
            ai.id == null

        when:"add alternative image is called with errors"
            response = service.addAlternativeImage(ai2)

        then: "error response must be returned"
            ai2.id == 1 as Long
            response == null
    }

    def "test ifPublisherValid with valid publisher"() {
        setup:"a valid alt image with a mediaItem that the publisher owns"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            new MediaItemSubscriber(subscriberId: 1, mediaItem: mi).save(flush:true)
            def ai = new AlternateImage([name:"valid name", url:"http://www.example.com", mediaItem: mi])


        when:"ifPublisherValid is called"
            def response = service.ifPublisherValid(ai)

        then:"the publisher should be valid"
            response
    }

    def "test ifPublisherValid with invalid publisher"() {
        setup: "a valid image with a mediaItem that doesn't belong to the publisher"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def ai = new AlternateImage([name:"valid name", url:"http://www.example.com", mediaItem: mi])

        when:"ifPublisherValid is called"
            def response = service.ifPublisherValid(ai)

        then:"the publisher should not valid"
            !response
    }

}