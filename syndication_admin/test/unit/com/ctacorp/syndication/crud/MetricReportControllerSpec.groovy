package com.ctacorp.syndication.crud

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.ViewMetricService
import com.ctacorp.syndication.analytics.GoogleAnalyticsService
import com.ctacorp.syndication.audit.QueryAuditService
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.cache.GuavaCacheService
import com.ctacorp.syndication.media.MediaItem
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import java.util.concurrent.Callable

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(MetricReportController)
@Mock([MediaItem, UserRole, Source, Role, User, MediaItemSubscriber])
class MetricReportControllerSpec extends Specification {

    def viewMetricService = Mock(ViewMetricService)
    def springSecurityService = Mock(SpringSecurityService)
    def queryAuditService = Mock(QueryAuditService)
    def googleAnalyticsService = Mock(GoogleAnalyticsService)
    def guavaCacheService = Mock(GuavaCacheService)

    def setup() {

        controller.viewMetricService = viewMetricService
        controller.springSecurityService = springSecurityService
        controller.queryAuditService = queryAuditService
        controller.googleAnalyticsService = googleAnalyticsService
        controller.guavaCacheService = guavaCacheService

        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1").save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        controller.springSecurityService = [currentUser:User.get(1)]
    }

    def cleanup() {
    }

    def "when totalViews is called with a sort findTotalViews should be called as a publisher"() {
        setup:""
            User user = new User(name:"admin", username: "test2@example.com", enabled: true, password: "SomerandomPass1").save()
            Role role = new Role(authority: "ROLE_PUBLISHER").save()
            UserRole.create user, role, true
            controller.springSecurityService = [currentUser:User.get(2)]
            def mediaItemsBySubscriberCalled = false
            def mediaItemsCountCalled = false
            MediaItem.metaClass.static.findAllByIdInList = {ArrayList items -> mediaItemsBySubscriberCalled = true;[]}
            MediaItem.metaClass.static.countByIdInList = {ArrayList items -> mediaItemsCountCalled = true;[]}

        when:"totalViews is called"
            params.sort = "storefrontViewCount"
            controller.totalViews(10)

        then:"MediaItem.list() should be called"
            view == "/metricReport/totalViews"
            mediaItemsBySubscriberCalled
            mediaItemsCountCalled
            model.mediaItemInstanceCount == 0

        when:"totalViews is called"
            mediaItemsBySubscriberCalled = false
            mediaItemsCountCalled = false
            params.sort = "apiViewCount"
            controller.totalViews(10)

        then:"MediaItem.list() should be called"
            view == "/metricReport/totalViews"
            mediaItemsBySubscriberCalled
            mediaItemsCountCalled
            model.mediaItemInstanceCount == 0

    }

    def "when totalViews is called with a sort findTotalViews should be called"() {
        setup:""

        when:"totalViews is called"
            params.sort = "storefrontViewCount"
            controller.totalViews(10)

        then:"MediaItem.list() should be called"
            view == "/metricReport/totalViews"
            1 * controller.viewMetricService.findTotalViews(params)
            model.mediaItemInstanceCount == 0

        when:"totalViews is called"
            params.sort = "apiViewCount"
            controller.totalViews(10)

        then:"MediaItem.list() should be called"
            view == "/metricReport/totalViews"
            1 * controller.viewMetricService.findTotalViews(params)
            model.mediaItemInstanceCount == 0

    }

    def "when totalViews is called without a sort MediaItem.list should be used"() {
        setup:""
            MediaItem.metaClass.static.list = {Map params ->
                ["all items"]
            }

        when:"totalViews is called"
            controller.totalViews(10)

        then:"MediaItem.list() should be called"
            view == "/metricReport/totalViews"
            model.mediaItemInstanceList == ["all items"]
            model.mediaItemInstanceCount == 0

    }

    def "when view graphs is called it should return the correct json"() {
        when:"viewGraphs is called"
            controller.viewGraphs()

        then:"the correct render response is returned"
            view == "/metricReport/viewGraphs"
            model.toString() == "[mediaItemInstanceList:[], mediaToGraph:[], mediaForTokenInput:[], agencyList:[], secondTabActive:null]"
    }

    def "when mediaContent is called a data map should be created"() {
        when:"mediaContent is called"
            request.contentType = JSON_CONTENT_TYPE
            controller.mediaContent()

        then:"the correct json is returned"
            response.text == '{"data":null,"xkey":"month","ykeys":[],"labels":[]}'
    }

    def "updatePercentOfTotalView should call get media totals and have a couple of default values"() {
        when:"updatePercentOfTotalView is called"
            controller.updatePercentOfTotalViews()

        then:"getMediaTotals should be called with a default range and which value"
            1 * controller.viewMetricService.getMediaTotals(null, 'storefrontViewCount', 1)
            params.whichData == "storefrontViewCount"
            params.range == 1
    }


//TODO: need to mock googleGuava
//    def "getTopTen should call findTopTen"() {
//        when:"getTopTen is called"
//            controller.getTopTen()
//
//        then:" findTopTen should be called"
//        1 * controller.viewMetricService.findTopTen(params)
//            params.range == 1
//    }
//
//    def "getAgencyTopTen should call findAgencyTopTen"() {
//        when:"getAgencyTopTen is called"
//        controller.getAgencyTopTen()
//
//        then:"findAgencyTopTen should be called"
//        1 * controller.viewMetricService.findAgencyTopTen(params)
//    }
//
//    void "getAgencyViews should should call findAgencyViews"() {
//        when:"getAgencyViews is called"
//            controller.getAgencyViews()
//
//        then:"findAgencyViews should be called"
//            1 * controller.viewMetricService.findAgencyViews(params)
//            params.range == 1
//    }
}
