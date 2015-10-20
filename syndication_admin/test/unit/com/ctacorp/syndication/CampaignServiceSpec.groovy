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
@TestFor(CampaignService)
@Mock([User, UserRole, Role, Campaign, CampaignSubscriber, MediaItem, Source])
class CampaignServiceSpec extends Specification {

    def campaignRollbackCalled = false
    def subscriberRollbackCalled = false

    def setup(){
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1", subscriberId: 1).save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        service.springSecurityService = [currentUser:User.get(1)]

        Campaign.metaClass.'static'.withTransaction = {Closure callable ->
            callable.call([setRollbackOnly:{campaignRollbackCalled = true}])
        }

        CampaignSubscriber.metaClass.'static'.withTransaction = {Closure callable ->
            callable.call([setRollbackOnly:{subscriberRollbackCalled = true}])
        }
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

    def "test that updateItemAndSubscriber saves a mediaItem with valid subscriber"() {
        setup: "create mediaItem"
            MediaItem mi = new MediaItem(populateValidParams()).save(flush:true)
            def camp = new Campaign([name:"valid name", url:"http://www.example.com", startDate: new Date(), mediaItem: mi, source: new Source()])

        when: "the method is called to save the item"
            def countSubs = CampaignSubscriber.count()
            service.updateCampaignAndSubscriber(camp, 1)

        then: "the mediaItem should be saved and associated to a subscriber"
            countSubs < CampaignSubscriber.count()
            camp.id != null
            !campaignRollbackCalled
            !subscriberRollbackCalled

        when: "the mediaItem gets updated"
            countSubs = CampaignSubscriber.count()
            service.updateCampaignAndSubscriber(camp, 1)

        then: "the mediaItem should be updated with no errors and saved to the same subscriber"
            countSubs == CampaignSubscriber.count()
            camp.id != null
            !campaignRollbackCalled
            !subscriberRollbackCalled
    }

    def "test that updateItemAndSubscriber handles subscriberIds correctly"() {
        setup:"create mediaItem"
            MediaItem mi = new MediaItem(populateValidParams())
            def camp = new Campaign([name:"valid name", url:"http://www.example.com", startDate: new Date(), mediaItem: mi, source: new Source()])

        when:"the method is called with an invalid subscriberId"
            service.updateCampaignAndSubscriber(camp, null)

        then:"transaction should be rolled back with the correct error on the mediaItem instance"
            campaignRollbackCalled
            subscriberRollbackCalled

    }

    def "test invalid campaigns are rolledbacked"(){
        setup:"create mediaItem"
            def camp = new Campaign([name:"valid name", url:"http://www.example.com", startDate: new Date()])

        when:"the method is called with an invalid subscriberId"
            service.updateCampaignAndSubscriber(camp, 15)

        then:"transaction should be rolled back with the correct error on the mediaItem instance"
            campaignRollbackCalled
            subscriberRollbackCalled
    }

    def "test ifPublisherValid with valid publisher"() {
        setup:"a valid alt image with a mediaItem that the publisher owns"
            User user = new User(name:"admin", username: "test2@example.com", enabled: true, password: "SomerandomPass1", subscriberId: 1).save()
            Role role = new Role(authority: "ROLE_PUBLISHER").save()
            UserRole.create user, role, true
            service.springSecurityService = [currentUser:User.get(2)]
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def camp = new Campaign([name:"valid name", url:"http://www.example.com", mediaItem: mi])
            new CampaignSubscriber(subscriberId: 1, campaign: camp).save(flush:true)

        when:"ifPublisherValid is called"
            def response = service.publisherValid(camp)

        then:"the publisher should be valid"
            response
    }

    def "test ifPublisherValid with invalid publisher"() {
        setup: "a valid image with a mediaItem that doesn't belong to the publisher"
            User user = new User(name:"admin", username: "test2@example.com", enabled: true, password: "SomerandomPass1", subscriberId: 1).save()
            Role role = new Role(authority: "ROLE_PUBLISHER").save()
            UserRole.create user, role, true
            service.springSecurityService = [currentUser:User.get(2)]
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def camp = new Campaign([name:"valid name", url:"http://www.example.com", mediaItem: mi])

        when:"ifPublisherValid is called"
            def response = service.publisherValid(camp)

        then:"the publisher should not valid"
            !response
    }


}