package com.ctacorp.syndication.crud

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.metric.MediaMetric
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import static java.util.Calendar.*

/**
 * Created by nburk on 11/13/14.
 */

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class MetricReportController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def viewMetricService
    def springSecurityService
    def queryAuditService
    def googleAnalyticsService

    static defaultAction = "overview"

    def publisherItems = { MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id ?: [] }

    def overview(OverviewMetaHolder overviewMeta){
        if(!overviewMeta){
            overviewMeta = new OverviewMetaHolder()
        }

        def mostPopular = null
        switch(overviewMeta.popularRange){
            case "week":    mostPopular = queryAuditService.getMostPopular(new Date()-7); break
            case "month":   mostPopular = queryAuditService.getMostPopular(); break
            case "year":    mostPopular = queryAuditService.getMostPopular(new Date()-365); break
            case "alltime": mostPopular = queryAuditService.getMostPopularAllTime(); break
        }

        def googleOverview = null
        try{
            def googleResponse = googleAnalyticsService.getDashboardOverviewData(overviewMeta?.start)
//            println ((googleResponse as JSON).toString(true))
            googleOverview = [
                    stats : [:],
                    error : googleResponse.error
            ]
            if(googleResponse?.rows && googleResponse?.rows[0]?.size() > 0){
                for(def i = 0; i < googleResponse.rows[0].size(); i++){
                    googleOverview.stats[googleResponse.columnHeaders[i].name-'ga:'] = googleResponse.rows[0][i]
                }
            } else{
                googleOverview.error = "There are no metrics available for the select date range."
            }

        }catch(e){
            log.error "Couldn't retrieve google anayltics info"
            e.printStackTrace()
        }

        [
            start:overviewMeta.start,
            googleOverview: googleOverview,
            mostPopular: mostPopular,
            subscribers: UserRole.findAllByRole(Role.findByAuthority("ROLE_STOREFRONT_USER"))*.user,
            popularRange: overviewMeta.popularRange
        ]
    }

    def mediaViewMetrics(Integer max){
        params.max = Math.min(max ?: 10, 1000)
        def mediaItems
        def count = MediaItem.count()

        Date day
        if(params.day) {
            day = params.day instanceof Date ? params.day : new Date(params.day as Long)
        } else {
            day = new Date()
        }
        day = day.clearTime()

        if(params.sort == "storefrontViewCount" || params.sort == "apiViewCount") {
            def results
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                mediaItems = MediaMetric.findAllByDayAndMediaInList(day, MediaItem.findAllByIdInList(publisherItems()), params).media
                count = MediaMetric.countByDayAndMediaInList(day, MediaItem.findAllByIdInList(publisherItems()), params)
            } else {
                results = MediaMetric.createCriteria().list(params){
                    eq("day", day)
                }
                count = results.totalCount
                mediaItems = results.media
            }

        } else{
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if(publisherItems()){
                    mediaItems = MediaItem.createCriteria().list(params){
                        'in'("id", publisherItems())
                    }
                }
                count = mediaItems?.totalCount ?: 0
            } else {
                mediaItems = MediaItem.list(params)
            }
        }

        [mediaItemInstanceList:mediaItems, day: day, mediaItemInstanceCount: count]
    }

    def mediaRangeViewMetrics(Integer max){
        params.max = Math.min(max ?: 10, 1000)
        params.offset = params.offset ?: 0
        Date fromDay
        Date toDay
        def mediaItems
        def count

        if(params.fromDay && params.toDay) {
            fromDay = params.fromDay instanceof Date ? params.fromDay : new Date(params.fromDay as Long)
            toDay = params.toDay instanceof Date ? params.toDay : new Date(params.toDay as Long)
        } else{
            switch(params.rangePreset){
                case "week":
                    fromDay = new Date() - 6
                    toDay = new Date()
                    break
                case "month":
                    fromDay = new Date() - 30
                    toDay = new Date()
                    break
                case "year":
                    fromDay = new Date() - 365
                    toDay = new Date()
                    break
                case "ytd":
                    GregorianCalendar cal = new GregorianCalendar()
                    cal.set(DAY_OF_YEAR, 1)
                    fromDay = cal.time
                    toDay = new Date()
                    break
                default:
                    fromDay = new Date() - 6
                    toDay = new Date()
            }
        }

        fromDay = fromDay.clearTime()
        toDay = toDay.clearTime()

        if(params.sort == "storefrontViewCount" || params.sort == "apiViewCount") {
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                mediaItems = viewMetricService.findRangeOfViews(params, fromDay, toDay)
                count = MediaMetric.createCriteria().listDistinct {
                    projections {
                        groupProperty("media", "media")
                        between("day", fromDay, toDay)
                        'in'("media", MediaItem.findAllByIdInList(publisherItems()))
                    }
                }.size()
            } else {
                mediaItems = viewMetricService.findRangeOfViews(params, fromDay, toDay).collect{it[0]}
                count = MediaMetric.createCriteria().listDistinct {
                    projections {
                        groupProperty("media", "media")
                        between("day", fromDay, toDay)
                    }
                }.size()
            }
        } else {
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if(publisherItems()){
                    mediaItems = MediaItem.createCriteria().list(params) { 
                        'in'("id", publisherItems())
                    }
                }
                count = mediaItems?.totalCount ?: 0
            } else {
                mediaItems = MediaItem.list(params)
                count = MediaItem.count()
            }
        }

        render view:"mediaRangeViewMetrics", model:[mediaItemInstanceList:mediaItems, fromDay: fromDay, toDay: toDay, mediaItemInstanceCount: count]
    }

    def totalViews(Integer max){
        params.max = Math.min(max ?: 10, 1000)
        params.offset = params.offset ?: 0
        def count = MediaItem.count()
        def mediaItems

        if(params.sort == "storefrontViewCount" || params.sort == "apiViewCount") {
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                mediaItems = MediaItem.findAllByIdInList(publisherItems())
                count = MediaItem.findAllByIdInList(publisherItems()) ?: 0
            } else {
                mediaItems = viewMetricService.findTotalViews(params)
                def metrics = MediaItem.list().metrics
                count = 0
                metrics.each{
                    if(it){
                        count++
                    }
                }
            }

        } else {
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if(publisherItems()){
                    mediaItems = MediaItem.createCriteria().list(params) {
                        'in'("id", publisherItems())
                    }
                }
                count = mediaItems?.totalCount ?: 0
            } else {
                mediaItems = MediaItem.list(params)
            }
        }
        
        render view:"totalViews", model:[mediaItemInstanceList:mediaItems, mediaItemInstanceCount: count]
    }

// all graphing actions
    def viewGraphs(){
        def mediaItems = MediaItem.list()
        def mediaToGraph = MediaItem.findAllByIdInList(params.mediaToGraph?.tokenize(',')) ?: []
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
        def secondTabActive = null
        if(params.fromSecondTab){
            secondTabActive = true
        }

        render view:"viewGraphs", model:[mediaItemInstanceList: mediaItems, mediaToGraph:mediaToGraph, mediaForTokenInput: mediaForTokenInput ,agencyList:Source.list(), secondTabActive:secondTabActive]
    }

    def mediaContent(){
        params.whichData = params.whichData ?: "apiViewCount"
        def data = [
                data:[],
                xkey:"month",
                ykeys:MediaItem.findAllByIdInList(params.mediaToGraph?.tokenize(',[]') ?: []).name,
                labels:MediaItem.findAllByIdInList(params.mediaToGraph?.tokenize(',[]') ?: []).collect{
                    it.id+" " + viewMetricService.checkLength(it.name)
                }
        ]
        
        data.data = viewMetricService.getMediaData(params.mediaToGraph, params.whichData)

        render data as JSON
    }


    def updatePercentOfTotalViews(){
        params.whichData = params.whichData ?: "storefrontViewCount"
        params.range = params.range ?: 1
        def data = viewMetricService.getMediaTotals(params.mediaToGraph, params.whichData, params.int("range"))

        render data as JSON
    }


    def getTopTen(){
        params.range = params.range ?: 1
        def data = viewMetricService.findTopTen(params)

        render data as JSON
    }

    def getAgencyTopTen(){
        def data = viewMetricService.findAgencyTopTen(params)
        render data as JSON
    }

    def getAgencyViews(){
        params.range = params.range ?: 1
        def data = viewMetricService.findAgencyViews(params)

        render data as JSON
    }
}

class OverviewMetaHolder {
    Date start = new Date()
    String popularRange = "week"
}