package com.ctacorp.syndication.crud

import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.MediaMetric
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

/**
 * Created by nburk on 11/13/14.
 */

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class MetricReportController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def viewMetricService
    def springSecurityService

    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id ?: []}


    def mediaViewMetrics(Integer max){
        params.max = Math.min(max ?: 10, 100)
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
                mediaItems = MediaMetric.findAllByDayBetweenAndMediaInList(day, day + 1, MediaItem.findAllByIdInList(publisherItems()), params).media
                count = MediaMetric.countByDayBetweenAndMediaInList(day, day + 1, MediaItem.findAllByIdInList(publisherItems()), params)
            } else {
                results = MediaMetric.createCriteria().list(params){
                    between("day", day, day + 1)
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

        render view:"mediaViewMetrics", model:[mediaItemInstanceList:mediaItems, active: "date", day: day, mediaItemInstanceCount: count]
    }

    def mediaRangeViewMetrics(Integer max){
        params.max = Math.min(max ?: 10, 100)
        params.offset = params.offset ?: 0
        Date fromDay
        Date toDay
        def mediaItems
        def count

        if(params.fromDay && params.toDay) {
            fromDay = params.fromDay instanceof Date ? params.fromDay : new Date(params.fromDay as Long)
            toDay = params.toDay instanceof Date ? params.toDay : new Date(params.toDay as Long)
        } else if(params.month) {
            fromDay = new Date() - 30
            toDay = new Date()
        } else if(params.year) {
            fromDay = new Date() - 365
            toDay = new Date()
        } else {
            fromDay = new Date() - 6
            toDay = new Date()
        }
        fromDay = fromDay.clearTime()
        toDay = toDay.clearTime()

        if(params.sort == "storefrontViewCount" || params.sort == "apiViewCount") {
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                mediaItems = MediaMetric.findAllByDayBetweenAndMediaInList(fromDay, toDay + 1, MediaItem.findAllByIdInList(publisherItems()), params).media
                count = MediaMetric.countByDayBetweenAndMediaInList(fromDay, toDay + 1, MediaItem.findAllByIdInList(publisherItems()), params)
            } else {
                mediaItems = viewMetricService.findRangeOfViews(params, fromDay, toDay + 1).collect{it[0]}
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
                count = MediaItem.count()
            }
        }

        render view:"mediaRangeViewMetrics", model:[mediaItemInstanceList:mediaItems, active: "range", fromDay: fromDay, toDay: toDay, mediaItemInstanceCount: count]
    }

    def totalViews(Integer max){
        params.max = Math.min(max ?: 10, 100)
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
        
        render view:"totalViews", model:[mediaItemInstanceList:mediaItems, mediaItemInstanceCount: count, active:"total"]
    }

// all graphing actions
    def viewGraphs(){
        def mediaItems = MediaItem.list()
        def mediaToGraph = MediaItem.findAllByIdInList(params.mediaToGraph?.tokenize(',')) ?: []
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON

        render view:"viewGraphs", model:[mediaItemInstanceList: mediaItems, mediaToGraph:mediaToGraph, mediaForTokenInput: mediaForTokenInput, active:"graph",agencyList:Source.list()]
    }

    def mediaContent(){
        params.whichData = params.whichData ?: "storefrontViewCount"
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
