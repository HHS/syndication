package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.SystemEventService
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.audit.SystemEvent
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.Audio
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.PDF
import com.ctacorp.syndication.media.Periodical
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.media.Widget
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(DashboardController)
@Mock([User, UserRole, Role, MediaItem, SystemEvent, Html, Video, Image, Audio, Infographic, Collection, Widget, Tweet, Periodical, PDF])
class DashboardControllerSpec extends Specification {

    def systemEventService = Mock(SystemEventService)
    def tagService = Mock(TagService)

    def setup() {
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1").save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        controller.springSecurityService = [currentUser:User.get(1)]
        controller.systemEventService = systemEventService
        controller.tagService = tagService
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "Test syndDash"() {
        setup:"media item"
        populateValidParams(params)
        new MediaItem(params).save(flush:true)

        when: "syndDash is called"
            def model = controller.syndDash()

        then:"the model should be correct"
        1 * controller.systemEventService.listRecentEvents(10) >> {int num ->["displayed in model"]}
            model.timelineEvents
            model.events == ["displayed in model"] as List<SystemEvent>
    }

    void "test listEvents"() {
        when: "listEvents is called"
            def model = controller.listEvents()

        then:"the model should be correct"
        1 * controller.systemEventService.listEvents(params) >> {params -> ["called method"]}
            model.eventInstanceList == ["called method"] as List<SystemEvent>
            model.total == 0
    }

    void "test contentTypeDistributionDonut"() {
        when: "contentTypeDistributionDonut is called"
            controller.contentTypeDistributionDonut()

        then: "the response should be correct"
            response.text == '[{"label":"Html","value":0},{"label":"Video","value":0},{"label":"Image","value":0},{"label":"Infographic","value":0},{"label":"Collection","value":0},{"label":"Audio","value":0},{"label":"Widget","value":0},{"label":"Tweet","value":0},{"label":"Periodical","value":0},{"label":"pdf","value":0}]'

    }

}
