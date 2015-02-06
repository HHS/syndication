package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import grails.plugin.springsecurity.annotation.Secured

import java.util.regex.Pattern

@Secured(['permitAll'])
class StorefrontController {
    def mediaService
    def grailsApplication
    def tagService
    def springSecurityService
    def likeService
    def mediaListService

    def index(){
        mediaTagHelper()
    }

    def listMediaForTag(Long id){
        params.max = params.max ? Math.min(params.int('max'), 100): 15
        def mediaItemInstanceList = tagService.getMediaForTagId(id, params)
        def tagsForMedia = [:]
        mediaItemInstanceList.each{
            def allTags = tagService.getTagsForMediaId(it.id)
            tagsForMedia[it.id] = allTags.collect{ [name:it.name, id:it.id] }
        }
        [
                mediaItemInstanceList: mediaItemInstanceList,
                tagsForMedia:tagsForMedia,
                tagName:params.tagName,
                likeInfo:likeService.getAllMediaLikeInfo(mediaItemInstanceList)
        ]
    }

    def ajaxLike(Long id){
        User currentUser = springSecurityService.currentUser as User
        if(currentUser) {
            likeService.likeMedia(id)
        }
        int likeCount = likeService.getLikeCount(id)

        render "&nbsp; ${likeCount} Users liked this content."
    }

    def ajaxUndoLike(Long id){
        User currentUser = springSecurityService.currentUser as User
        if(currentUser) {
            likeService.undoLikeMedia(id)
        }
        int likeCount = likeService.getLikeCount(id)

        render "&nbsp; ${likeCount} Users liked this content."
    }

    def like(Long id){
        likeService.likeMedia(id)
        redirect action:'showContent', id:id
    }

    def usageGuidelines(){}

    def roadMap(){}

    def faq(){}

    def qa(){}

    def reportAProblem(){
        render template:"reportAProblem"
    }

    def sendProblemReport(){
        flash.message = "Report has been filed."
        
        sendMail {
            to grailsApplication.config.SyndicationStorefront.mail.syndicate.to
            subject "Issue Report: ${new Date()}"
            body """\
            Syndication Error Report: ${new Date()}

            URL in question: ${params.badURL}

            Problem Description: ${params.problemDescription}

            Reply to email: ${params.reporterEmailAddress}
            """
        }

        render view:"thankyou"
    }

    def sendSyndicationRequest(){
        flash.message = "request has been sent."

        sendMail {
            to grailsApplication.config.SyndicationStorefront.mail.syndicate.to
            subject "syndication request: ${new Date()}"
            body """\
            Syndication Request: ${new Date()}

            URL in question: ${params.contentSourceURL}

            Comments / Details: ${params.comments}

            Reply to email: ${params.requesterEmailAddress}
            """
        }

        render view:"thankyou"
    }

    def requestSyndication(){
        render template:"requestSyndication"
    }

    def browse(String selectedMediaType){
        params.max = params.max ? Math.min(params.int('max'), 100): 15
        if(!selectedMediaType){
            selectedMediaType = "Html"
        }
        params.mediaTypes = selectedMediaType

        def allMediaTypes = mediaService.getMediaTypes()
        def mediaList = MediaItem.facetedSearch(params).list(params)

        [
          selectedMediaType:selectedMediaType,
          mediaTypes:allMediaTypes,
          apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
          mediaItemInstanceList:mediaList,
          tagsForMedia:getTagsForMedia(mediaList),
          total:mediaList.totalCount
        ]
    }

    def showContent(MediaItem mediaItemInstance){
        User currentUser = springSecurityService.currentUser as User
        boolean alreadyLiked = false
        if(currentUser) {
            alreadyLiked = currentUser.likes.contains(mediaItemInstance)
        }
        int likeCount = likeService.getLikeCount(mediaItemInstance?.id)
        def userMediaLists = UserMediaList.findAllByUser(currentUser)

        if(mediaItemInstance == null || !mediaItemInstance?.active || !mediaItemInstance?.visibleInStorefront){
            log.error("Trying to show media that doesn't exist or is inactive: ${mediaItemInstance?.id}\nActive:${mediaItemInstance?.active}\nVisible in Storefront:${mediaItemInstance?.visibleInStorefront}")
            response.sendError(404)
        }

        [
            userMediaLists:userMediaLists,
            alreadyLiked:alreadyLiked,
            likeCount:likeCount,
            mediaItemInstance:mediaItemInstance,
            apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    def addMediaToUserMediaList(Long mediaId, Long mediaList){
        def success = mediaListService.addMediaToMediaList(mediaId, mediaList)
        if(success){
            flash.message = "This item has been added to your list."
        } else{
            flash.error = "There was a problem adding this item to your list, please try again."
        }
        redirect action: "showContent", id:mediaId
    }

    def showCampaign(){}

    def showAgency(){}

    private getTagsForMedia(mediaItemInstanceList){
        def tagsForMedia = [:]

        mediaItemInstanceList.each{
            def allTags = tagService.getTagsForMediaId(it.id)
            tagsForMedia[it.id] = allTags.collect{ [name:it.name, id:it.id] }
        }

        tagsForMedia
    }

    def Map mediaTagHelper(){
        params.max = params.max ? Math.min(params.int('max'), 100): 15
        def mediaItemInstanceList = mediaService.listNewestMedia(params)
        def total = mediaItemInstanceList.totalCount
        String searchQuery = ""
        String searchType = "basic"
        String contentTitle = "Newest Syndicated Content"
        String advanced = null
        Map likeInfo = likeService.getAllMediaLikeInfo(mediaItemInstanceList)

        //used if basic search is submitted
        if(params.searchQuery){
            searchQuery = params.searchQuery
            mediaItemInstanceList = mediaService.mediaItemSolrSearch(searchQuery, params)
            total = mediaItemInstanceList?.totalCount
            mediaItemInstanceList = mediaItemInstanceList?.results
            contentTitle = "Search Results: '${params.searchQuery}'"
            likeInfo = likeService.getAllMediaLikeInfoFromJson(mediaItemInstanceList)
        }

        //used if advanced search is submitted
        else if(params.advancedSearch){
            params.topicItems = tagService.getMediaForTagId((params?.topic ?: 0)as Long, params).id.toListString() - '[' - ']'
            mediaItemInstanceList = mediaService.findMediaByAll(params)
            total = mediaItemInstanceList?.totalCount
            searchType = "advanced"
            contentTitle = "Search Results: 'Advanced Search'"
            advanced = "true"
            likeInfo = likeService.getAllMediaLikeInfo(mediaItemInstanceList)
        }

        User currentUser = springSecurityService.currentUser as User
        [
                mediaItemInstanceList:mediaItemInstanceList,
                total:total,
                apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                tagsForMedia:getTagsForMedia(mediaItemInstanceList),
                featuredMedia: mediaService.getFeaturedMedia(max:20),
                userMediaLists: UserMediaList.findAllByUser(currentUser),
                contentTitle:contentTitle,
                searchQuery: searchQuery,
                title: params.title,
                language: params.language,
                languageList: Language.findAllByIsActive(true),
                domain: params.domain,
                mediaType: params.mediaType,
                searchType: searchType,
                sourceList: Source.list(),
                source: params.source,
                topic:params.topic,
                topicList:tagService.getTagsByType('topic'),
                mediaTypes: mediaService.getMediaTypes(),
                advancedSearch: advanced,
                likeInfo: likeInfo

        ]
    }

    def otherLookupOptions(){
        render template:'otherLookupOptions', model:[topicList:tagService.getTagsByType('topic'), sourceList:Source.list(), mediaTypes: mediaService.getMediaTypes(), languageList:Language.findAllByIsActive(true)]
    }

    def basicSearch(){
        render template: 'basicSearch'
    }
}