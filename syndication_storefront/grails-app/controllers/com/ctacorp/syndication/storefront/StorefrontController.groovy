package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.contact.EmailContact
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.jobs.DelayedMetricAddJob
import com.ctacorp.syndication.jobs.DelayedQueryLogJob
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['permitAll'])
class StorefrontController {
    def mediaService
    def grailsApplication
    def tagService
    def springSecurityService
    def likeService
    def mediaListService

    def index() {
        def model = mediaTagHelper()
        model
    }

    def embedCodeForTag(Long id) {
        [tagName: params.tagName, id: id, renderTagList: true,userId: springSecurityService?.currentUser?.id ?: -1]
    }

    def embedCodeForSource(Long id) {
        [sourceName: params.sourceName, id: id, renderSourceList: true,userId: springSecurityService?.currentUser?.id ?: -1]
    }

    def listMediaForTag(Long id) {
        params.max = params.max ? Math.min(params.int('max'), 100) : 15
        def mediaItemInstanceList = tagService.getMediaForTagId(id, params)
        def tagsForMedia = [:]
        mediaItemInstanceList.each {
            def allTags = tagService.getTagsForMediaId(it.id)
            tagsForMedia[it.id] = allTags.collect { [name: it.name, id: it.id] }
        }
        [
                mediaItemInstanceList: mediaItemInstanceList,
                tagsForMedia         : tagsForMedia,
                tagName              : params.tagName,
                tagId                : id,
                likeInfo             : likeService.getAllMediaLikeInfo(mediaItemInstanceList),
                total                :mediaItemInstanceList.totalCount,
                tag                 :id
        ]
    }

    def listMediaForSource(Long id) {
        params.max = params.max ? Math.min(params.int('max'), 100) : 15
        def mediaItemInstanceList = MediaItem.facetedSearch([sourceId:id, active:true, visibleInStorefront:true, syndicationVisibleBeforeDate:new Date().toString()]).list(params)
        def tagsForMedia = [:]
        mediaItemInstanceList.each {
            def allTags = tagService.getTagsForMediaId(it.id)
            tagsForMedia[it.id] = allTags.collect { [name: it.name, id: it.id] }
        }
        [
                mediaItemInstanceList: mediaItemInstanceList,
                tagsForMedia         : tagsForMedia,
                tagId                : id,
                likeInfo             : likeService.getAllMediaLikeInfo(mediaItemInstanceList),
                total                : mediaItemInstanceList.totalCount,
                sourceId               : id,
                sourceName           :Source.get(id).name
        ]
    }

    def ajaxLike(Long id) {
        User currentUser = springSecurityService.currentUser as User
        if (currentUser) {
            likeService.likeMedia(id)
        }
        int likeCount = likeService.getLikeCount(id)

        render "&nbsp; ${likeCount} Users liked this content."
    }

    def ajaxUndoLike(Long id) {
        User currentUser = springSecurityService.currentUser as User
        if (currentUser) {
            likeService.undoLikeMedia(id)
        }
        int likeCount = likeService.getLikeCount(id)

        render "&nbsp; ${likeCount} Users liked this content."
    }

    def like(Long id) {
        likeService.likeMedia(id)
        redirect action: 'showContent', id: id
    }

    def usageGuidelines() {
        render view: 'usageGuidelines', model: [syndicationEmail: grailsApplication.config.grails.mail.default.from]
    }

    def roadMap() {}

    def faq() {}

    def qa() {
        def adminEmail = grailsApplication.config.grails.mail.default.from

        [adminEmail: adminEmail]
    }

    def reportAProblem() {
        render template: "reportAProblem"
    }

    def sendProblemReport() {
        flash.message = "Report has been filed."
        def mailRecipiants = EmailContact.list()?.email ?: "syndication@ctacorp.com"

        sendMail {
            to mailRecipiants
            subject "Issue Report: ${new Date()}"
            body """\
            Syndication Error Report: ${new Date()}

            URL in question: ${params.badURL}

            Problem Description: ${params.problemDescription}

            Reply to email: ${params.reporterEmailAddress}
            """
        }

        render view: "thankyou"
    }

    def sendSyndicationRequest() {
        flash.message = "request has been sent."
        def mailRecipiants = EmailContact.list()?.email ?: "syndication@ctacorp.com"

        sendMail {
            to mailRecipiants
            subject "syndication request: ${new Date()}"
            body """\
            Syndication Request: ${new Date()}

            URL in question: ${params.contentSourceURL}

            Comments / Details: ${params.comments}

            Reply to email: ${params.requesterEmailAddress}
            """
        }

        render view: "thankyou"
    }

    def requestSyndication() {
        render template: "requestSyndication"
    }

    def showContent(MediaItem mediaItemInstance) {
        User currentUser = springSecurityService.currentUser as User
        boolean alreadyLiked = false
        if (currentUser) {
            alreadyLiked = currentUser.likes.contains(mediaItemInstance)
        }
        int likeCount = likeService.getLikeCount(mediaItemInstance?.id)
        def userMediaLists = UserMediaList.findAllByUser(currentUser)

        if (mediaItemInstance == null || !mediaItemInstance?.active || !mediaItemInstance?.visibleInStorefront) {
            log.error("Trying to show media that doesn't exist or is inactive: ${mediaItemInstance?.id}\nActive:${mediaItemInstance?.active}\nVisible in Storefront:${mediaItemInstance?.visibleInStorefront}")
            response.sendError(404)
        } else {
            DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: mediaItemInstance.id])
        }

        [
                tags             : getTagsForMediaItem(mediaItemInstance),
                userMediaLists   : userMediaLists,
                alreadyLiked     : alreadyLiked,
                likeCount        : likeCount,
                mediaItemInstance: mediaItemInstance,
                apiBaseUrl       : grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                userId           : springSecurityService?.currentUser?.id ?: -1
        ]
    }

    def storefrontPreviewMetricHit() {
        DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: params.long('id')])
        render {}
    }

    def addMediaToUserMediaList(Long mediaId, Long mediaList) {
        def success = mediaListService.addMediaToMediaList(mediaId, mediaList)
        if (success) {
            flash.message = "This item has been added to your list."
        } else {
            flash.error = "There was a problem adding this item to your list, please try again."
        }
        redirect action: "showContent", id: mediaId
    }

    def releaseInfo() {}

    def showCampaign() {}

    def showAgency() {}

    private getTagsForMediaItem(MediaItem mediaItemInstance) {
        tagService.getTagsForMediaId(mediaItemInstance?.id).groupBy { it.language.isoCode }
    }

    private getTagsForMediaItems(Collection mediaItemInstanceList) {
        def tagsForMedia = [:]

        mediaItemInstanceList.each {
            def allTags = tagService.getTagsForMediaId(it.id)
            tagsForMedia[it.id] = allTags.collect { [name: it.name, id: it.id] }
        }

        tagsForMedia
    }

    @Transactional
    def Map mediaTagHelper() {
        if(params.sourceId){
            redirect action: "listMediaForSource", id:params.sourceId, params:params
        }
        if(params.tag){
            redirect action: "listMediaForTag", id:params.tag, params:params
        }
        params.max = params.max ? Math.min(params.int('max'), 100) : 15

        String searchQuery = ""
        String searchType = "basic"
        String contentTitle
        String advanced = null

        def mediaItemInstanceList
        def total
        Map likeInfo

        //used if basic search is submitted
        if (params.searchQuery) {
            DelayedQueryLogJob.schedule(new Date(System.currentTimeMillis() + 10000), [queryString: params.searchQuery])
            searchQuery = params.searchQuery
            mediaItemInstanceList = mediaService.mediaItemSolrSearch(searchQuery, params)
            total = mediaItemInstanceList?.totalCount
            contentTitle = "Search Results: '${params.searchQuery}'"
            likeInfo = likeService.getAllMediaLikeInfo(mediaItemInstanceList)
        }

        //used if advanced search is submitted
        else if (params.advancedSearch) {
            if (params.title) {
                DelayedQueryLogJob.schedule(new Date(System.currentTimeMillis() + 10000), [queryString: params.title])
            }
            params.topicItems = tagService.getMediaForTagId((params?.topic ?: 0) as Long, params).id.toListString() - '[' - ']'
            mediaItemInstanceList = mediaService.findMediaByAll(params)
            total = mediaItemInstanceList?.totalCount
            searchType = "advanced"
            contentTitle = "Search Results: 'Advanced Search'"
            advanced = "true"
            likeInfo = likeService.getAllMediaLikeInfo(mediaItemInstanceList)
        } else{ // regular index listing
            mediaItemInstanceList = mediaService.listNewestMedia(params)
            total = mediaItemInstanceList.totalCount
            likeInfo = likeService.getAllMediaLikeInfo(mediaItemInstanceList)
            contentTitle = "Newest Syndicated Content"
        }

        User currentUser = springSecurityService.currentUser as User
        [
                mediaItemInstanceList: mediaItemInstanceList,
                total                : total,
                apiBaseUrl           : grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                tagsForMedia         : getTagsForMediaItems(mediaItemInstanceList),
                featuredMedia        : mediaService.getFeaturedMedia(max: 10),
                userMediaLists       : UserMediaList.findAllByUser(currentUser),
                contentTitle         : contentTitle,
                searchQuery          : searchQuery,
                title                : params.title,
                language             : params.language,
                languageList         : Language.findAllByIsActive(true),
                domain               : params.domain,
                mediaType            : params.mediaType,
                searchType           : searchType,
                sourceList           : Source.list(sort: "name", order: "ASC"),
                source               : params.source,
                topic                : params.topic,
                topicList            : tagService.getTagsByType('topic'),
                mediaTypes           : mediaService.getMediaTypes(),
                advancedSearch       : advanced,
                likeInfo             : likeInfo
        ]
    }

    def otherLookupOptions() {
        render template: 'otherLookupOptions', model: [
                sourceList  : Source.list(sort: "name", order: "ASC"),
                mediaTypes  : mediaService.getMediaTypes(),
                languageList: Language.findAllByIsActive(true, [sort: "name", order: "ASC"])]
    }

    def basicSearch() {
        render template: 'basicSearch'
    }

    def fiveOhEight() {
        [featuredMedia: mediaService.getFeaturedMedia(max: 10)]
    }
}