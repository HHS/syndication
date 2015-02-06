package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.UserRole
import grails.transaction.Transactional
import org.hibernate.FetchMode
import com.ctacorp.syndication.MediaMetric

/**
 * Created by nburk on 11/6/14.
 */
@Transactional(readOnly = true)
class ViewMetricService {

    def springSecurityService

    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    def findTotalViews(def params) {

        def metrics = MediaMetric.createCriteria().list{
            projections {
                groupProperty("media", "media")
                sum(params.sort, params.sort)
                order(params.sort, params.order)
                maxResults(params.max)
                firstResult(params.int("offset"))
            }
        }
        MediaItem[] mediaItems
        mediaItems = metrics.collect{it[0]}
        return mediaItems
    }

    def findRangeOfViews(def params, Date day1, Date day2) {
        def metrics = null

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && MediaItem.findAllByIdInList(publisherItems())){
            metrics = MediaMetric.createCriteria().list{

                projections {
                    groupProperty("media", "media")
                    between("day", day1, day2)
                    sum(params.sort, params.sort)
                    'in'("media", MediaItem.findAllByIdInList(publisherItems()))
                    order(params.sort, params.order)
                    maxResults(params.max)
                    firstResult(params.int("offset"))
                }.properties
            }
        } else if(UserRole.findByUser(springSecurityService.currentUser).role.authority != "ROLE_PUBLISHER") {
            metrics = MediaMetric.createCriteria().list{

                projections {
                    groupProperty("media", "media")
                    between("day", day1, day2)
                    sum(params.sort, params.sort)
                    order(params.sort, params.order)
                    maxResults(params.max)
                    firstResult(params.int("offset"))
                }.properties
            }
        }


        return metrics
    }

    def getMediaData(mediaToGraph, whichData){
        def data = []
        def dates = getDateRangesForLast12Months()
        mediaToGraph = MediaItem.findAllByIdInList(mediaToGraph?.tokenize(',[]'))

        dates.each{ def date ->
            def monthData = [month:"${date.date}"]
            data << monthData
            mediaToGraph.each{ MediaItem mediaItem ->
                def totals = getMetricBetweenDateAndMedia(whichData, date, mediaItem)
                monthData << [
                              "${mediaItem.name}":totals
                ]
            }
        }
        return data
    }

    def getMetricBetweenDateAndMedia(selector, date, mediaItem){
        def items = MediaMetric.findAllByDayBetweenAndMedia(date.firstDay as Date, (date.lastDay + 1) as Date, mediaItem)
        switch(selector){
            case "storefrontViewCount": return items.storefrontViewCount?.sum() ?: 0
            case "apiViewCount": return items.apiViewCount?.sum() ?: 0
            default: log.error "invalid selector used: ${selector}"
        }
        null
    }

    def getMediaTotals(mediaToGraph, whichData, range){
        mediaToGraph = MediaItem.findAllByIdInList(mediaToGraph?.tokenize(',[]'))
        def labels = []
        def noData = true
        Date today = new Date().clearTime() + 1
        mediaToGraph.each{MediaItem mediaItem ->
            def singleLabel = [label:mediaItem.id + "-" + checkLength(mediaItem.name),value:getMetricBetweenDateAndMedia(whichData, [firstDay:today - range,lastDay:today], mediaItem)]
            labels << singleLabel
            if(singleLabel.value > 0){
                noData = false
            }
        }
        labels << [label: "Other", value:(MediaMetric.findAllByDayBetween(today - range, today)."${whichData}".sum() ?: 0) - (labels.value.sum() ?: 0)]
        if(noData){
            labels = [[label:"No Data", value: 0]]
        }
        labels
    }

    def findTopTen(params){
        params.max = 10
        params.offset = 0
        params.sort = params.whichData
        params.order = "desc"
        def noData = true
        def today = new Date().clearTime() + 1
        def metrics = findRangeOfViews(params, today - params.int("range"), today)
        if(params.typeGraph == "line"){ //line graph
            def mediaIds = metrics.collect{it[0].id} as String
            return getLineDataForMediaItemsInList(mediaIds, params)
        } else { //donut graph
            def labels = []
            metrics?.each{metric ->
                if(metric[1]> 0){
                    noData=false
                }
                labels << [label:"${metric[0].id + "-" + checkLength(metric[0].name)}", value:metric[1]]
            }
            labels << [label: "other", value:(MediaMetric.findAllByDayBetween(today - params.int("range"), today)."${params.whichData}".sum() ?: 0) - (labels.value.sum() ?: 0)]
            if(noData){
                labels = [[label:"No Data", value: 0]]
            }
            return labels
        }
    }

    def getLineDataForMediaItemsInList(mediaIds, params){
        def data = [
                data:[],
                xkey:"month",
                ykeys:MediaItem.findAllByIdInList(mediaIds?.tokenize(',[]') ?: []).name,
                labels:MediaItem.findAllByIdInList(mediaIds?.tokenize(',[]') ?: []).collect{
                    it.id+"-"+ checkLength(it.name)
                }
        ]
        data.data = getMediaData(mediaIds, params.whichData)
        return data
    }

    def findAgencyTopTen(params){
        params.max = 10
        params.offset = 0
        params.sort = params.whichData
        params.order = "desc"
        def today = new Date().clearTime() + 1
        def mediaItemsFromAgency = MediaItem.findAllBySource(Source.get(params.agency))
        def metrics = []
        if(mediaItemsFromAgency){
            metrics = MediaMetric.createCriteria().list{
                projections {
                    groupProperty("media", "media")
                    between("day", today - params.int("range"), today)
                    'in'("media", mediaItemsFromAgency)
                    sum(params.sort, params.sort)
                    order(params.sort, params.order)
                    maxResults(params.max)
                    firstResult(params.offset)
                }
            }
        } else {
            return metrics
        }

        if(params.graphType == "line"){ //line graph
            def mediaIds = metrics.collect{it[0].id} as String
            return getLineDataForMediaItemsInList(mediaIds, params)
        } else {
            def labels = []
            metrics?.each{metric ->
                labels << [label:"${metric[0]}", value:metric[1]]
            }
            labels << [label: "other", value:(MediaMetric.findAllByDayBetween(today - params.int("range"), today)."${params.whichData}".sum() ?: 0) - (labels.value.sum() ?: 0)]
            return labels
        }
    }

    def findAgencyViews(params){
        params.max = 10
        params.offset = 0
        params.sort = params.whichData
        params.order = "desc"
        def today = new Date().clearTime() + 1
        def metrics = findRangeOfViews(params, today - params.int("range"), today)
        def agencies = Source.list()

        if(params.graphType == "line"){
            def data = [
                    data:[],
                    xkey:"month",
                    ykeys:agencies.name,
                    labels:agencies.collect{agency ->
                        agency.id+"-"+checkLength(agency.name)
                    }
            ]

            def dates = getDateRangesForLast12Months()
            dates.each{ def date ->
                def monthData = [month:"${date.date}"]
                data.data << monthData
                agencies.each{Source agency ->
                    def totals = 0
                    MediaItem.findAllBySource(agency).each{ MediaItem mediaItem ->
                        totals += getMetricBetweenDateAndMedia(params.whichData, date, mediaItem)
                    }
                    monthData << [
                            "${agency.name}":totals
                    ]
                }
            }
            return data
        } else{
            def noData= true
            def labels = []
            agencies.each{source ->
                labels << [label:source.id+"-"+source.name, value: 0]
            }
            metrics.each{metric ->
                labels.each{ label ->
                    if(label.label == "${metric[0].source.id+"-"+metric[0].source}"){
                        noData = false
                        label.value += metric[1]
                    }
                }
            }
            if(noData){
                labels = [[label:"No Data", value: 0]]
            }
          return  labels
        }
    }

    private static List getDateRangesForLast12Months() {
        def dates = []
        (0..<12).each { month ->
            Calendar cal = new GregorianCalendar()
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - month)
            cal.set(Calendar.DATE, 1)
            Date firstDay = cal.getTime().clearTime()
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
            Date lastDay = cal.getTime().clearTime()
            String date = lastDay.format("yyyy-MM-dd")

            dates << [
                    firstDay: firstDay,
                    lastDay : lastDay,
                    date    : date
            ]
        }
        dates.reverse()
    }
    
    def checkLength(String name){
        if(name.length() > 22){
            return name.substring(0, Math.min(name.length(), 20)) + "... "
        }
        name
    }
}