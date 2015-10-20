package com.ctacorp.syndication

import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.health.HealthReport
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.plugin.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by nburk on 6/24/15.
 */
@TestFor(MediaValidationService)
@Mock([MediaItem,MediaThumbnail, MediaPreview, FlaggedMedia, MediaItemSubscriber])
class MediaValidationServiceSpec extends Specification {

    def messageSource = Mock(PluginAwareResourceBundleMessageSource)
    def mediaItemsService = Mock(MediaItemsService)
    def springSecurityService = Mock(SpringSecurityService)
    def grailsApplication = Mock(GrailsApplication)

    def setup() {
        service.messageSource = messageSource
        service.mediaItemsService = mediaItemsService
        service.springSecurityService = springSecurityService
        service.grailsApplication = grailsApplication

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

    def "Test perform validation"() {
        setup:"create a valid mediaItem"
            new MediaItem(populateValidParams()).save()
            service.metaClass.processValidation = {MediaItem item -> new HealthReport(details:"process called")}

        when:"perform validation is called with an invalid mediaItem id"
            def response = service.performValidation(5)

        then:"fails if mediaitem doesn't exist"
            response == null

        when:"perform validation is called with a valid mediaItem id"
            response = service.performValidation(1)

        then:"process validation is called"
        //TODO:update this test once Grails version 2.5 has a fix to mock private methods
//            response.details == "process called"
    }

    def "test full health scan"() {
        setup:"create a valid mediaItem"
            MediaItem mi = new MediaItem(populateValidParams()).save()
            MediaItem mi2 = new MediaItem(populateValidParams()).save()
            MediaItemSubscriber miSub = new MediaItemSubscriber([mediaItem:mi, subscriberId:1])
            def removeFlagCalled = false
            def getMediaIdsCalled = false
            def flagMediaItemCalled = false
            def performValidationCalled = false
            service.metaClass.removeFlag = {Long id -> removeFlagCalled = true}
            service.metaClass.flagMediaItem = {HealthReport report -> flagMediaItemCalled = true}
            service.metaClass.performValidation = {Long id -> performValidationCalled=true; new HealthReport()}
            service.mediaItemsService.metaClass.getAllMediaIds = {getMediaIdsCalled = true;MediaItem.list().id}
            service.messageSource.metaClass.getMessage = {String str, Object obj, Locale locale -> "reason for failure"}

        when:"full health scan is called with a null subscriberId"
            service.fullHealthScan()

        then:""
            getMediaIdsCalled
            flagMediaItemCalled
            performValidationCalled

        when:"full health scan is called with a health report that is valid"
            service.metaClass.performValidation = {Long id -> performValidationCalled=true; new HealthReport([statusCode:"123",valid:true, mediaId: 1,details: "details"])}
            service.fullHealthScan()

        then:"remove flag should be called"
            removeFlagCalled

        when:"full health scan is called with a subscriberId"
            service.fullHealthScan(1)

        then:""
            1 * service.mediaItemsService.getPublisherMediaIds(1)
    }

    def "get FlaggedMedia returns flagged media that isn't ignored"() {
        setup:"create flagged media and some with a subscriber"
            MediaItem mi = new MediaItem(populateValidParams()).save()
            MediaItem mi2 = new MediaItem(populateValidParams()).save()
            MediaItem mi3 = new MediaItem(populateValidParams()).save()
            new MediaItemSubscriber([mediaItem: mi2,subscriberId: 1]).save()
            def flag1 = new FlaggedMedia([mediaItem: mi,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            def flag2 = new FlaggedMedia([mediaItem: mi2,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            def flag3 = new FlaggedMedia([mediaItem: mi3,message:"message",ignored:true, failureType: FlaggedMedia.FailureType.NO_CONTENT]).save(flush:true)
            FlaggedMedia.metaClass.'static'.findAllByMediaItemInList = {List<MediaItem> items -> items}

        when:"getflaggedMedia is called"
            def response = service.getFlaggedMedia()

        then:"all of the flagged media that isn't ignored is returned"
            response.size() == 2

        when:"getPublisherFlaggedMedia"
            def response2 = service.getPublisherFlaggedMedia(1)

        then:"only the publisher owned items flagged items are returned"
            response2 == [mi2]
    }

    def "test rescan item"() {
        setup:"create items and flaggedMedia"
            MediaItem mi = new MediaItem(populateValidParams()).save()
            MediaItem mi2 = new MediaItem(populateValidParams()).save()
            new FlaggedMedia([mediaItem: mi,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            new FlaggedMedia([mediaItem: mi2,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            service.metaClass.performValidation = {Long id -> if(id == 1 as Long){new HealthReport([statusCode:"123",valid:false, mediaId: 1,details: "details"])} else{new HealthReport([statusCode:"123",valid:true, mediaId: 1,details: "details"])}}
            def removeFlagCalled = false
            def flagMediaItemCalled = false
            service.metaClass.removeFlag = {Long id -> removeFlagCalled = true}
            service.metaClass.flagMediaItem = {HealthReport report -> flagMediaItemCalled = true}

        when:"rescanItem is called with a health report that isn't valid anymore"
            service.rescanItem(mi2.id)

        then:"remove flag should be called"
            removeFlagCalled

        when:"rescanItem is called with valid health report"
            service.rescanItem(mi.id)

        then:"flagMedaiItem should be called"
            flagMediaItemCalled
    }

    def "test remove flag"() {
        setup:""
            MediaItem mi = new MediaItem(populateValidParams()).save()
            MediaItem mi2 = new MediaItem(populateValidParams()).save()
            new FlaggedMedia([mediaItem: mi,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            new FlaggedMedia([mediaItem: mi2,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()

        when:"remove flag is called with invalid mediaItem"
            def response = service.removeFlag(5)

        then:"then null is returned"
            response == null

        when:"remove flag is called with invalid mediaItem"
            def flagCount = FlaggedMedia.count()
            service.removeFlag(1)

        then:"the flag should be removed"
            flagCount > FlaggedMedia.count()
    }

    def "test flagMediaItem"() {
        setup:""
            MediaItem mi = new MediaItem(populateValidParams()).save()
            new MediaItem(populateValidParams()).save()
            new FlaggedMedia([mediaItem: mi,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            HealthReport validReport = new HealthReport([statusCode:"123",valid:true, mediaId: 1,details: "details"])
            HealthReport invalidReport = new HealthReport([statusCode:"123",valid:false, mediaId: 1,details: "details"])
            HealthReport newInvalidReport = new HealthReport([statusCode:"123",valid:false, mediaId: 2,details: "details", failureType: FlaggedMedia.FailureType.NO_CONTENT])
            def getMessageCalled = false
            service.messageSource.metaClass.getMessage = {String str, Object obj, Locale locale -> getMessageCalled=true;"reason for failure"}

        when:"flagMediaItem is called"
            def response = service.flagMediaItem(validReport)

        then:"null is returned"
            response == null
            !getMessageCalled

        when:"flagMediaItem is called with an already flagged report"
            service.flagMediaItem(invalidReport)

        then:"response should return true"
            getMessageCalled

        when:"flagMediaItem is called with a report that hasn't been flagged yet"
            getMessageCalled = false
            def flagCount = FlaggedMedia.count()
            service.flagMediaItem(newInvalidReport)

        then:"should create a new flaggedMediaItem"
            getMessageCalled
            flagCount < FlaggedMedia.count()
    }

}