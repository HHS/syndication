package com.ctacorp.syndication.crud

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.MediaItem
import com.google.api.client.auth.oauth2.Credential
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.util.Holders

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", 'ROLE_PUBLISHER'])
class ConsumerMetricsController {

    def googleAnalyticsService
    def cmsManagerKeyService
    def springSecurityService

    def publisherItems = { MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id ?: [] }

    def viewLocation() {
        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def results
        def items = []

        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }

        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        def mediaFilters = getPublisherFilters(mediaToGraph)

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        def endDate = Calendar.getInstance()
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }
        if(params.end) {
            endDate.set(params.int("end_year"), params.int("end_month") - 1, params.int("end_day"))
        }

        def totalResults = 0
        mediaFilters.each{filterSet ->
            results = googleAnalyticsService.executeQuery(profileId, startDate, endDate, "viewsByLocation", [mediaFilters: ";"+filterSet, startIndex: 1])
            items.addAll(mapAnalyticsResults(results))
            totalResults += results.getTotalResults()
        }

        def numPages = Math.ceil(totalResults / 10000)

        [
                items:items as JSON,
                mediaForTokenInput: mediaForTokenInput,
                header:"View Consumption by Location",
                startDate:startDate,
                endDate: endDate,
                numPages:numPages
        ]
    }



    def getpaginatedLocations() {
        def mediaFilters = getPublisherFilters([])

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        def endDate = Calendar.getInstance()
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }
        if(params.end) {
            endDate.set(params.int("end_year"), params.int("end_month") - 1, params.int("end_day"))
        }

        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def pagination = params.int("pagination")
        def results
        ArrayList<Map> items = []
        mediaFilters.each { filterSet ->
            results = googleAnalyticsService.executeQuery(profileId, startDate, endDate, "viewsByLocation", [mediaFilters: ";" + filterSet, startIndex: pagination * 10000 + 1])
            items.addAll(mapAnalyticsResults(results))
        }
        render items as JSON
    }

    def consumers() {
        Credential cred = googleAnalyticsService.authorize()
        cred.refreshToken()

        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }

        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        [
                mediaForTokenInput: mediaForTokenInput,
                tempAccessToken:cred.getAccessToken(),
                totalMediaItems:MediaItem.count(),
                totalSubscribers:cmsManagerKeyService.listSubscribers().size(),
                header:"Embedders by Domain Name",
                mediaToGraph:params.mediaItem
        ]
    }

    def getWhosEmbedding() {
        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def items = []
        def results
        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }

        def mediaFilters = getPublisherFilters(mediaToGraph, ";ga:hostname!=digitalmedia.hhs.gov;ga:hostname!=api.digitalmedia.hhs.gov;ga:hostname!=digitalmedia.hhs.gov.googleweblight.com")
        mediaFilters.each { filterSet ->
            if(mediaFilters.size() == 1){
                results = googleAnalyticsService.executeQuery(profileId, params.startDate, params.endDate, "whosEmbedding", [mediaFilters: filterSet, startIndex: 1,size:params.int("size")])
                items.addAll(results.rows)
            } else {
                results = googleAnalyticsService.executeQuery(profileId, params.startDate, params.endDate, "whosEmbedding", [mediaFilters: filterSet, startIndex: 1,size:10000])
                items = googleAnalyticsService.mergeResults(items,results.rows)
            }
        }
        if(mediaFilters.size() != 1){
            items = googleAnalyticsService.restrictItemsToSize(items,params.int("size"))
        }
        render items as JSON
    }

    def generalViews() {
        Credential cred = googleAnalyticsService.authorize()

        cred.refreshToken()
        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        def mediaFilters = getPublisherFilters(mediaToGraph)

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }

        def googleOverview = null
        try{
            mediaFilters.each { filterSet ->
                def googleResponse = googleAnalyticsService.executeQuery(profileId, startDate, new Date(), "overview", [mediaFilters: filterSet ?: "ga:dimension2!=-1"])

                googleOverview = [
                        stats: [:],
                        error: googleResponse.error
                ]
                if (googleResponse?.rows && googleResponse?.rows[0]?.size() > 0) {
                    for (def i = 0; i < googleResponse.rows[0].size(); i++) {
                        googleOverview.stats[googleResponse.columnHeaders[i].name - 'ga:'] = googleResponse.rows[0][i]
                    }
                } else {
                    googleOverview.error = "There are no metrics available for the select date range."
                }
            }
        }catch(e){
            log.error "Couldn't retrieve google anayltics info"
            StringWriter sw = new StringWriter()
            PrintWriter pw = new PrintWriter(sw)
            e.printStackTrace(pw)
            log.error(sw.toString())
            googleOverview = [stats:[:], error:"Couldn't connect to google analytics service.", exception:sw.toString()]
        }

        [
                mediaForTokenInput: mediaForTokenInput,
                tempAccessToken:cred.getAccessToken(),
                totalMediaItems:MediaItem.count(),
                totalSubscribers:cmsManagerKeyService.listSubscribers().size(),
                header:"General Views",
                mediaFilters:mediaFilters,
                googleOverview: googleOverview,
                startDate: startDate,
                mediaToGraph:params.mediaItem
        ]
    }

    def getTotalViews() {
        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def items = []
        def results

        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }

        def mediaFilters = getPublisherFilters(mediaToGraph)
        mediaFilters.each { filterSet ->
            if(mediaFilters.size() == 1){
                results = googleAnalyticsService.executeQuery(profileId, null, null, "generalTotalViews", [mediaFilters: filterSet, startIndex: 1])
                items.addAll(results.rows)
            } else {
                results = googleAnalyticsService.executeQuery(profileId, null, null, "generalTotalViews", [mediaFilters: filterSet, startIndex: 1])
                items = googleAnalyticsService.mergeResults(items,results.rows)
            }
        }
        items = googleAnalyticsService.formatResponseDate(items)
        render items as JSON
    }

    def getTotalMobileViews() {
        String profileId = Holders.config.GOOGLE_ANALYTICS_PROFILEID
        def items = []
        def results
        def mediaToGraph
        if(params.mediaItem?.tokenize(',')) {
            mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(','))
        } else {
            mediaToGraph = []
        }

        def mediaFilters = getPublisherFilters(mediaToGraph)
        mediaFilters.each { filterSet ->
            if(mediaFilters.size() == 1){
                results = googleAnalyticsService.executeQuery(profileId, null, null, "generalTotalMobileViews", [mediaFilters: filterSet, startIndex: 1])
                items.addAll(results.rows)
            } else {
                results = googleAnalyticsService.executeQuery(profileId, null, null, "generalTotalMobileViews", [mediaFilters: filterSet, startIndex: 1])
                items = googleAnalyticsService.mergeResults(items,results.rows)
            }
        }
        items = googleAnalyticsService.formatResponseDate(items)
        render items as JSON
    }

    private getPublisherFilters(def mediaItemsToSearchFor, String additionalFilter = "") {
        ArrayList<String> mediaFilters = []
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !mediaItemsToSearchFor) {
            int count = 0
            publisherItems().each{
                mediaFilters[(count/100) as int] = (mediaFilters[(count/100) as int] ?: "") + ",ga:dimension2==" + it
                count ++
            }
        } else if(mediaItemsToSearchFor){
            mediaItemsToSearchFor.each{
                mediaFilters[0] = (mediaFilters[0] ?: "") + ",ga:dimension2==" + it.id
            }
        } else {
            mediaFilters[0]=",ga:pageviews!=-1"
        }

        int index = 0
        mediaFilters.each{filterSet ->
            mediaFilters[index] =  (mediaFilters[index] + additionalFilter).replaceFirst(',',"")
            index++
        }

        mediaFilters
    }

    private mapAnalyticsResults(results) {
        ArrayList<Map> items = []
        def mediaIds = []
        results.rows.each{
            mediaIds.add(it[2])
        }
        def mediaItems = MediaItem.findAllByIdInList(mediaIds)
        def mediaTable = [:]
        mediaItems.each{mi ->
            mediaTable."$mi.id" = mi.name
        }
        results.rows.each{//sets up the data format for esri map input
            items << [name:mediaTable."${it[2]}", region:it[0], city:it[1], id:it[2],views:it[5], longitude:it[3],latitude:it[4]]
        }
        return items
    }
}
