package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.MediaValidationService
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.jobs.MediaValidationJob
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import javax.print.attribute.standard.Media

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HealthReportController)
@Mock([MediaItem,FlaggedMedia, UserRole, User, Role])
class HealthReportControllerSpec extends Specification {

    def mediaValidationService = Mock(MediaValidationService)

    def setup() {
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1").save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        Math.metaClass.static.round = {Float num -> 1}
        controller.springSecurityService = [currentUser:User.get(1)]
        controller.mediaValidationService = mediaValidationService
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "Test the index responds with the correct model"() {
        when:"the index is called"
            def model = controller.index(10)

        then:"the model should be correct"
            model.flaggedMediaItems == []
            model.totalCount == 0
    }

    void "Test checkAllMedia triggers media validation job"() {
        setup:""
            def triggerCalled = false
            MediaValidationJob.metaClass.static.triggerNow = {-> triggerCalled = true}

        when:"the checkAllMedia action is called"
            controller.checkAllMedia()

        then:"the media validation trigger is called"
            triggerCalled
            response.redirectedUrl == "/healthReport/index"
    }

    void "Test the checkMediaItem action rescans the given item"() {
        when:"The check Media Item is called"
            controller.checkMediaItem(1)

        then:"the correct service method is called"
            1 * mediaValidationService.rescanItem(1)
            flash.message != null
            response.redirectedUrl == "/healthReport/index"
    }

    void "Test the ignored action"() {
        when:"the ignored action is called"
            def model = controller.ignored(10)

        then:"the model is correct"
            model.flaggedMediaItems == []
            model.totalCount == 0
    }

    void "Test ignoreFlagged Media"() {
        setup: ""
        def flaggedMedia = new FlaggedMedia([mediaItem:new MediaItem(),message:"test flagged item",dateFlagged:new Date(), ignored:false, failureType:FlaggedMedia.FailureType.UNREACHABLE]).save()

        when:"ignoreFlaggedMedia is called"
            controller.ignoreFlaggedMedia(flaggedMedia)

        then:"the item attribute ignored gets changed to true"
            flaggedMedia.ignored
            flash.message != null
            response.redirectedUrl == "/healthReport/index"
    }

    void "test unignoredFlagged Media"() {
        setup: ""
            def flaggedMedia = new FlaggedMedia([mediaItem:new MediaItem(),message:"test flagged item",dateFlagged:new Date(), ignored:true, failureType:FlaggedMedia.FailureType.UNREACHABLE]).save()

        when:"ignoreFlaggedMedia is called"
            controller.unignoreFlaggedMedia(flaggedMedia)

        then:"the flagged media ingored attribute should be false"
            !flaggedMedia.ignored
            flash.message != null
            response.redirectedUrl == "/healthReport/ignored"
    }

}
