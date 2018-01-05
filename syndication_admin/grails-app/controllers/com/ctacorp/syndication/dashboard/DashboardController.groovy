
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.media.PDF
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.audit.SystemEvent
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.util.Holders
import org.apache.commons.lang.RandomStringUtils

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PUBLISHER"])
@Transactional(readOnly = true)
class DashboardController {
    def systemEventService
    def springSecurityService
    def tagService
    def config= Holders.config
    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    static defaultAction = "syndDash"

    def syndDash() {
        def mediaList
        params.max = 10
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            mediaList = MediaItem.facetedSearch([visibleInStorefront:true,restrictToSet:publisherItems().join(","),sort:"-id"]).list([max:10])
        } else {
            mediaList = MediaItem.facetedSearch([visibleInStorefront:true,sort:"-id"]).list([max:10])
        }

        def timelineEvents = []
        mediaList.each{ mi ->
            timelineEvents << [
                title:mi.name,
                id:mi.id,
                type:mi.getClass().simpleName.toLowerCase(),
                message:mi.description,
                timestamp:mi.dateSyndicationCaptured
            ]
        }

        def recentEvents = systemEventService.listRecentEvents(10)
        if(!tagService.status()) {
            flash.error = message(code: "tag.failure.UNREACHABLE")
        }

        [ADMIN_SERVER_URL:config?.ADMIN_SERVER_URL, API_SERVER_URL:config?.API_SERVER_URL, syndication_storefront:config?.STOREFRONT_SERVER_URL, timelineEvents:timelineEvents.sort{it.timestamp}.reverse(), events:recentEvents]
    }

    def listEvents(){
        [eventInstanceList:systemEventService.listEvents(params), total:SystemEvent.count()]
    }

    def contentTypeDistributionDonut(){
        def data
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            data = [
                    [label:"Article", value:MediaItem.facetedSearch([mediaTypes:"ARTICLE",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"BlogPosting", value:MediaItem.facetedSearch([mediaTypes:"BLOG_POSTING",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"NewsArticle", value:MediaItem.facetedSearch([mediaTypes:"NEWS_ARTICLE",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Html", value:MediaItem.facetedSearch([mediaTypes:"html",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Video", value:MediaItem.facetedSearch([mediaTypes:"video",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Image", value:MediaItem.facetedSearch([mediaTypes:"image",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Infographic", value:MediaItem.facetedSearch([mediaTypes:"infographic",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Collection", value:MediaItem.facetedSearch([mediaTypes:"collection",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"Tweet", value:MediaItem.facetedSearch([mediaTypes:"tweet",restrictToSet:publisherItems().join(","),sort:"-id"]).count()],
                    [label:"pdf", value:MediaItem.facetedSearch([mediaTypes:"pdf",restrictToSet:publisherItems().join(","),sort:"-id"]).count()]
            ]
        } else {
            data = [
                    [label:"Html", value:Html.count()],
                    [label:"Video", value:Video.count()],
                    [label:"Image", value:Image.count()],
                    [label:"Infographic", value:Infographic.count()],
                    [label:"Collection", value:com.ctacorp.syndication.media.Collection.count()],
                    [label:"Tweet", value:Tweet.count()],
                    [label:"pdf", value:PDF.count()]
            ]
        }


        render data as JSON
    }

    def contentByAgencyDonut() {
        def data = []

        def sources = Source.list()
        sources.each { Source source ->
            data << [
                label: source.acronym,
                value: MediaItem.countBySource(source)
            ]
        }
        render data as JSON
    }

    def contentByAgencyAreaChart(String whichDate) {
        def data = [
            data:[],
            xkey:"month",
            ykeys:Source.list()*.acronym,
            labels:Source.list()*.acronym,
            ymax:'auto'
        ]

        def dates = getDateRangesForLast12Months()
        def sources = Source.list()

        dates.eachWithIndex{ date, index ->
            def monthData = [month:"${date.date}"]
            data.data << monthData
            sources.each{ Source source ->
                def items = null
                switch(whichDate) {
                    case "areaDateSelectorSyndicationCaptured": items   = MediaItem.countBySourceAndDateSyndicationCapturedBetween(source, date.firstDay, date.lastDay); break;
                    case "areaDateSelectorSyndicationUpdated": items    = MediaItem.countBySourceAndDateSyndicationUpdatedBetween(source, date.firstDay, date.lastDay); break;
                    case "areaDateSelectorContentAuthored": items       = MediaItem.countBySourceAndDateContentAuthoredBetween(source, date.firstDay, date.lastDay); break;
                    case "areaDateSelectorContentUpdated": items        = MediaItem.countBySourceAndDateContentUpdatedBetween(source, date.firstDay, date.lastDay); break;
                    case "areaDateSelectorContentPublished": items      = MediaItem.countBySourceAndDateContentPublishedBetween(source, date.firstDay, date.lastDay); break;
                    case "areaDateSelectorContentReviewed": items       = MediaItem.countBySourceAndDateContentReviewedBetween(source, date.firstDay, date.lastDay); break;
                    default: items                                      = MediaItem.countBySourceAndDateSyndicationCapturedBetween(source, date.firstDay, date.lastDay);
                }
                if(items && items >= 1000) {
                    data.ymax = '1000'
                }
                monthData << [
                    "${source.acronym}":items
                ]
            }
        }

        render data as JSON
    }

    def ContentByPublisherAreaChart(String whichDate) {
        def data = [
                data:[],
                xkey:"month",
                ykeys:["Count"],
                labels:["Count"]
        ]

        def dates = getDateRangesForLast12Months()
        def sources = Source.list()
        //68

        dates.eachWithIndex{ date, index ->
            def monthData = [month:"${date.date}"]
            data.data << monthData
            def items = null
            switch(whichDate) {
                case "areaDateSelectorSyndicationCaptured": items   = MediaItem.countByDateSyndicationCapturedBetweenAndIdInList(date.firstDay, date.lastDay, publisherItems()); break;
                case "areaDateSelectorSyndicationUpdated": items    = MediaItem.countByDateSyndicationUpdatedBetweenAndIdInList(date.firstDay, date.lastDay, publisherItems()); break;
                case "areaDateSelectorTotal":           items       = MediaItem.countByDateSyndicationCapturedLessThanAndIdInList(date.firstDay, publisherItems()); break;
                default: items                                      = MediaItem.countByDateSyndicationCapturedBetweenAndIdInList(date.firstDay, date.lastDay, publisherItems());
            }
            monthData << [
                    "Count":items
            ]
        }

        render data as JSON
    }

    def error500() {

        def errorCode = RandomStringUtils.randomAlphabetic(10).toUpperCase()
        log.error(errorCode)
        render view:'/error', model:[errorCode:errorCode]
    }

    private Collection getDateRangesForLast12Months(){
        def dates = []
        (0..12).each{ month ->
            Calendar cal = new GregorianCalendar()
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - month)
            cal.set(Calendar.DATE, 1)
            Date firstDay = cal.getTime().clearTime()
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
            cal.add(Calendar.DATE, 1) //add one then clear time to get the full last day of the month
            Date lastDay = cal.getTime().clearTime()
            String date = lastDay.format("yyyy-MM-dd")

            dates << [
                firstDay:firstDay,
                lastDay:lastDay,
                date:date
            ]
        }
        dates.reverse()
    }

//    def index() {}
//
//    def flot() {}
//
//    def morris() {}
//
//    def tables() {}
//
//    def forms() {}
//
//    def panelsAndWells() {}
//
//    def buttons() {}
//
//    def notifications() {}
//
//    def typography() {}
//
//    def grid() {}
//
//    def blank() {}
//
//    def login() {}
}
