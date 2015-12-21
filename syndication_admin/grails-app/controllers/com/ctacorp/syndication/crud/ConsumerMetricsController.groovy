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
        String profileId = Holders.config.google.analytics.profileId
        def results
        def items

        def mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(',') ?: []) ?: []
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        String mediaFilters = getMediaFilters(mediaToGraph)

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        def endDate = Calendar.getInstance()
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }
        if(params.end) {
            endDate.set(params.int("end_year"), params.int("end_month") - 1, params.int("end_day"))
        }

        results = googleAnalyticsService.executeQuery(profileId, startDate, endDate, "viewsByLocation", [mediaFilters: ";"+mediaFilters, startIndex: 1])
        items = mapAnalyticsResults(results)
        def numPages = Math.ceil(results.getTotalResults() / 10000)

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
        String mediaFilters = getMediaFilters([])

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        def endDate = Calendar.getInstance()
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }
        if(params.end) {
            endDate.set(params.int("end_year"), params.int("end_month") - 1, params.int("end_day"))
        }

        String profileId = Holders.config.google.analytics.profileId
        def pagination = params.int("pagination")
        def results
        ArrayList<Map> items = []
        results = googleAnalyticsService.executeQuery(profileId, startDate, endDate, "viewsByLocation", [mediaFilters: ";"+mediaFilters,startIndex: pagination * 10000 + 1])
        items.addAll(mapAnalyticsResults(results))
        render items as JSON
    }

    def consumers() {
        Credential cred = googleAnalyticsService.authorize()
        cred.refreshToken()

        def mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(',') ?: []) ?: []
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        String mediaFilters = getMediaFilters(mediaToGraph, ";ga:hostname!=digitalmedia.hhs.gov;ga:hostname!=api.digitalmedia.hhs.gov;ga:hostname!=digitalmedia.hhs.gov.googleweblight.com")

        [
                mediaForTokenInput: mediaForTokenInput,
                tempAccessToken:cred.getAccessToken(),
                totalMediaItems:MediaItem.count(),
                totalSubscribers:cmsManagerKeyService.listSubscribers().size(),
                header:"Embedders by Domain Name",
                mediaFilters:mediaFilters
        ]
    }

    def generalViews() {
        Credential cred = googleAnalyticsService.authorize()
        cred.refreshToken()
        String profileId = Holders.config.google.analytics.profileId

        def mediaToGraph = MediaItem.findAllByIdInList(params.mediaItem?.tokenize(',') ?: []) ?: []
        String mediaForTokenInput = mediaToGraph.collect{ [id:it.id, name:"${it.name}"] } as JSON

        String mediaFilters = getMediaFilters(mediaToGraph)

        def startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, -30)
        if(params.start) {
            startDate.set(params.int("start_year"), params.int("start_month") - 1, params.int("start_day"))
        }

        def googleOverview = null
        try{
            def googleResponse = googleAnalyticsService.executeQuery(profileId,startDate, new Date(), "overview",[mediaFilters: mediaFilters ?: "ga:dimension2!=-1"])
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
                startDate: startDate
        ]
    }

    /**
     *
     * @param mediaItemsToSearchFor - the mediaItems the the filter will restrict the set to
     * @param replacementOfCharacter - The first character needs replaced with ';' instead of '' if it is being added
     * on to existing filters.
     * @param additionalFilter - custom filters seperated by ;
     * @return
     */
    private getMediaFilters(def mediaItemsToSearchFor, String additionalFilter = "") {
        String mediaFilters = ""
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !mediaItemsToSearchFor) {
            publisherItems().each{
                mediaFilters+=",ga:dimension2==" + it + additionalFilter
            }
        }else if (mediaItemsToSearchFor){
            mediaItemsToSearchFor.each{
                mediaFilters+=",ga:dimension2==" + it.id + additionalFilter
            }
        } else {
            mediaFilters=",ga:pageviews!=-1" + additionalFilter
        }
        return mediaFilters.replaceFirst(',',"")
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
