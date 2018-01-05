package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.contact.EmailContact
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.jobs.DelayedMetricAddJob
import com.ctacorp.syndication.jobs.DelayedQueryLogJob
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import grails.util.Holders

@Secured(['permitAll'])
class StorefrontController {

    def elasticsearchService
    def mediaService
    def tagService
    def springSecurityService
    def likeService
    def mediaListService
    def recaptchaService
    def config = Holders.config

    RestBuilder rest = new RestBuilder()

    def index() {
        mediaTagHelper()
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
        render view: 'index', model: [
                mediaItemInstanceList: mediaItemInstanceList,
                tagsForMedia         : tagsForMedia,
                tagName              : params.tagName,
                tagId                : id,
                likeInfo             : likeService.getAllMediaLikeInfo(mediaItemInstanceList),
                total                : mediaItemInstanceList.totalCount,
                tag                  : id,
                language             : params.language,
                languageList         : Language.findAllByIsActive(true),
                sourceList           : Source.list(sort: "name", order: "ASC"),
                source               : params.source,
                mediaTypes           : mediaService.getMediaTypes(),
                mediaType            : params.mediaType,
                title                : params.title
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
                sourceName           :Source.get(id).name,
                API_SERVER_URL : config?.API_SERVER_URL
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
        render view: 'usageGuidelines', model: [syndicationEmail: config?.MAIL_DEFAULT_FROM]
    }

    def roadMap() {}

    def faq() {}

    def qa() {
        def adminEmail = config?.MAIL_DEFAULT_FROM

        [adminEmail: adminEmail]
    }

    def reportAProblem() {
        render template: "reportAProblem"
    }

    def sendProblemReport() {

        boolean validCaptcha = recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)

        if (!validCaptcha) {
            flash.error = "Recaptcha verfication failed!"
            render view:"_reportAProblem"
            return
        }

        flash.message = "Report has been filed."
        def mailRecipiants = EmailContact.list()?.email ?: "syndication@ctacorp.com"
        if(params.badURL!='' || params.problemDescription!='' ||  params.reporterEmailAddress!=''){
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
        }

        render view: 'thankyou'

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

        if(!mediaItemInstance) {
            log.error("Trying to show media that doesn't exist")
            response.sendError(404)
            return
        }

        User currentUser = springSecurityService.currentUser as User
        boolean alreadyLiked = false
        if (currentUser) {
            alreadyLiked = currentUser.likes.contains(mediaItemInstance)
        }
        int likeCount = likeService.getLikeCount(mediaItemInstance?.id)
        def userMediaLists = UserMediaList.findAllByUser(currentUser)

        if (!mediaItemInstance.active || !mediaItemInstance.visibleInStorefront) {
            log.error("Trying to show media that is inactive: ${mediaItemInstance.id}\nActive:${mediaItemInstance.active}\nVisible in Storefront:${mediaItemInstance.visibleInStorefront}")
            response.sendError(404)
            return
        } else {
            DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: mediaItemInstance.id])
        }

        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        def stripScripts = (!params.submitChangesButton || params.stripScripts == "on")
        def stripStyles = (!params.submitChangesButton || params.stripStyles == "on")
        def stripImages = !params.stripImages || params.stripStyles != "on"  ? false : true
        [
                tags             : getTagsForMediaItem(mediaItemInstance),
                userMediaLists   : userMediaLists,
                alreadyLiked     : alreadyLiked,
                likeCount        : likeCount,
                mediaItemInstance: mediaItemInstance,
                apiBaseUrl       : config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                userId           : springSecurityService?.currentUser?.id ?: -1,
                stripScripts     : stripScripts,
                stripStyles      : stripStyles,
                stripImages      : stripImages,
                outsidePreview   : mediaItemInstance.foreignSyndicationAPIUrl ? rest.get("${config?.API_SERVER_URL}/api/v2/resources/media/${mediaItemInstance.id}/embed.json?autoplay=0&userId=${springSecurityService?.currentUser?.id ?: -1}&stripImages=${stripImages ? 1 : 0}&stripStyles=${stripStyles ? 1 : 0}&stripScripts=${stripScripts ? 1 : 0}").json.results[0].snippet.decodeHTML() : null
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

    def releaseInfo() {
        [releaseNotes:ReleaseNote.list(sort:'releaseDate', order: 'DESC')]
    }

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
    Map mediaTagHelper() {

        def mediaItems = []
        def isDefaultSearch = true
        def searchFields = 'fullText,title,createdBy,sourceUrl,mediaType,sourceId,languageId'

        params.max = params.max ?: 10
        params.offset = params.offset ?: 0

        searchFields.split(',').each {

            if(params[it]) {
                isDefaultSearch = false
            }
        }

        def results = [total: 0, ids: []]
        def filters = params.clone() as Map
        filters.visibleInStorefront = true

        if(params.languageId) {
            filters.language = params.languageId
        }

        if(params.sourceUrl) {
            filters.url = params.sourceUrl
        }

        if(isDefaultSearch) {

            filters.sort = 'mediaId'
            filters.order = 'desc'
        }

        filters.max = 10000
        filters.offset = 0

        if(params.tagId) {

            def accessibleIds = tagService.getMediaForTagId(params.tagId.toLong(), [:]).collect { it.id }
            results = elasticsearchService.searchForIds(filters, accessibleIds)

        } else {
            results = elasticsearchService.searchForIds(filters)
        }

        def contentTitle = isDefaultSearch ? 'Newest Syndicated Content' : "${results.total} Search Results"

        if(results.ids) {

            results.total = MediaItem.countByIdInList(results.ids)

            def idsForQuery = results.ids.join(',')
            def foundIds = MediaItem.executeQuery("select m.id from MediaItem m where m.id in (${idsForQuery}) order by field (m.id,${idsForQuery})", [:], params)
            mediaItems = MediaItem.getAll(foundIds)
        }

        def likeInfo = likeService.getAllMediaLikeInfo(mediaItems)
        DelayedQueryLogJob.schedule(new Date(System.currentTimeMillis() + 10000), [queryString: "${request.queryString}"])

        User currentUser = springSecurityService.currentUser as User
        [
                mediaItemInstanceList: mediaItems,
                total                : results.total,
                apiBaseUrl           : config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                tagsForMedia         : getTagsForMediaItems(mediaItems),
                featuredMedia        : mediaService.getFeaturedMedia(max: 10),
                userMediaLists       : UserMediaList.findAllByUser(currentUser),
                contentTitle         : contentTitle,
                title                : params.title,
                language             : params.language,
                languageList         : Language.findAllByIsActive(true),
                domain               : params.domain,
                mediaType            : params.mediaType,
                createdBy            : params.createdBy,
                searchType           : "FIX THIS",
                sourceList           : Source.list(sort: "name", order: "ASC"),
                source               : params.source,
                topic                : params.topic,
                topicList            : tagService.getTagsByType('topic'),
                mediaTypes           : mediaService.getMediaTypes(),
                advancedSearch       : "FIX THIS",
                likeInfo             : likeInfo,
                tagId                : params.tagId,
                API_SERVER_URL : config?.API_SERVER_URL
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
        [featuredMedia: mediaService.getFeaturedMedia(max: 10),API_SERVER_URL : config?.API_SERVER_URL]
    }
}