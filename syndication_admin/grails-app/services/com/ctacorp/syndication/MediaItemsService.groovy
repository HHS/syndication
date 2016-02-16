package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.media.QuestionAndAnswer
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.plugin.cache.web.filter.PageFragmentCachingFilter
import grails.transaction.NotTransactional
import grails.util.Environment
import grails.transaction.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import org.springframework.transaction.support.DefaultTransactionStatus

@Transactional
class MediaItemsService {

    def extendedAttributeService
    def grailsApplication
    def springSecurityService
    def cmsManagerKeyService
    def contentRetrievalService
    def solrIndexingService

    def publisherItems = {
        MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id
    }

    def getPublisherItemsByType(mediaItemSubclass) {
        //If logged in as admin, bypass the owner only content rule
        if(Role.findByAuthority("ROLE_ADMIN") in springSecurityService.currentUser.authorities){
            return MediaItem.withCriteria {
                eq 'class', mediaItemSubclass?.name
            }
        }

        def ownedItemIds = publisherItems()

        if (ownedItemIds) {
            return MediaItem.withCriteria {
                eq 'class', mediaItemSubclass?.name
                'in' 'id', ownedItemIds
            }
        }
        []
    }

    def delete(Long mediaId) {
        MediaItem mi = MediaItem.get(mediaId)
        if(!mi){
            return
        }
        def campaigns = []
        campaigns.addAll(mi.campaigns ?: [])

        for (campaign in campaigns) {
            campaign.removeFromMediaItems(mi)
        }

        //Collections -----------------------------------------------
        def collections = com.ctacorp.syndication.media.Collection.where {
            mediaItems {
                id == mi.id
            }
        }.list()

        for (collection in collections) {
            collection.removeFromMediaItems(mi)
        }

        //User Media Lists ------------------------------------------
        def userMediaLists = UserMediaList.where {
            mediaItems {
                id == mi.id
            }
        }.list()

        for (userMediaList in userMediaLists) {
            userMediaList.removeFromMediaItems(mi)
        }

        //Q&A - FAQ --------------------------------------------------
        if(mi instanceof QuestionAndAnswer) {
            def faqs = FAQ.where {
                questionAndAnswers {
                    id == mi.id
                }
            }.list()

            for (faq in faqs) {
                faq.removeFromQuestionAndAnswers(mi)
            }
        }

        def users = User.where {
            likes {
                id == mi.id
            }
        }.list()

        users.each { col ->
            col.removeFromLikes(mi)
        }

        // Media Preview and thumbnails --------------------------------
        MediaPreview.where {
            mediaItem == mi
        }.deleteAll()

        MediaThumbnail.where {
            mediaItem == mi
        }.deleteAll()

        // Media Preview and thumbnails --------------------------------
        MediaPreview.where {
            mediaItem == mi
        }.deleteAll()

        MediaThumbnail.where {
            mediaItem == mi
        }.deleteAll()

        CachedContent.findByMediaItem(mi)?.delete(flush: true)
        FlaggedMedia.findByMediaItem(mi)?.delete(flush: true)
        MediaItemSubscriber.findByMediaItem(mi)?.delete(flush: true)
        mi.delete(flush: true)
    }

    @Transactional(readOnly = true)
    Map getIndexResponse(params, MediaItemType) {
        if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            return [
                    mediaItemList         : MediaItemType.findAllByIdInList(publisherItems(), params),
                    mediaItemInstanceCount: MediaItemType.countByIdInList(publisherItems() ?: [0], params)
            ]
        }

        return [
                mediaItemList         : MediaItemType.list(params),
                mediaItemInstanceCount: MediaItemType.count()
        ]
    }

    @Transactional(readOnly = true)
    def getAllMediaIds() {
        MediaItem.executeQuery("select mi.id from MediaItem mi")
    }

    @Transactional(readOnly = true)
    def getPublisherMediaIds(subscriberId) {
        MediaItemSubscriber.executeQuery("select mi.mediaItem.id from MediaItemSubscriber mi where mi.subscriberId = ?", [subscriberId])
    }

    //returns the model at index 0 and and the error message if at index 1
    String addExtendedAttribute(MediaItem mi, ExtendedAttribute attributeInstance) {
        attributeInstance?.mediaItem = mi

        if (attributeInstance == null) {
            return "attribute doesn't exist"
        }

        attributeInstance.validate()
        if (attributeInstance.hasErrors()) {
            return "Enter a Attribute and Value"
        }

        def isDuplicateItem = extendedAttributeService.getUpdateInformation(attributeInstance)
        if (isDuplicateItem) {
            return "Attribute already created"
        } else {
            attributeInstance.save flush: true
        }
        return null
    }

    //removes mediaItems from user media lists if active or visibleInStorefront went from true to false
    void removeInvisibleMediaItemsFromUserMediaLists(MediaItem mediaItem, deletingMediaItem = false) {
        if (MediaItem.get(mediaItem.id)?.getPersistentValue("visibleInStorefront") && !mediaItem.visibleInStorefront ||
                MediaItem.get(mediaItem.id)?.getPersistentValue("active") && !mediaItem.active) {

            List<UserMediaList> userMedia = UserMediaList.containsMediaItem(mediaItem).list() ?: []
            userMedia.each {
                it.mediaItems.remove(mediaItem)
            }
        }
        if (deletingMediaItem) {
            List<UserMediaList> userMedia = UserMediaList.containsMediaItem(mediaItem).list() ?: []
            userMedia.each {
                it.mediaItems.remove(mediaItem)
            }
        }
    }

    @Transactional(readOnly = true)
    def findMediaByAll(params) {
        params.sort = params.sort ?: "id"
        params.order = params.order ?: "asc"
        params.title = params.title ?: ""
        params.url = params.url ?: ""
        params.nameContains = params.title.replace('%', '\\%')
        params.languageName = Language.get(params.language)
        params.sourceUrlContains = params.url
        params.restrictToSet = params.inList
        params.mediaTypes = params.mediaType
        if (params.order == "desc" && params.sort[0] != '-') {
            params.sort = "-" + params.sort
        }

        def mediaItems = MediaItem.facetedSearch(params).list(max: params.max, offset: params.offset)

        return mediaItems
    }

    @NotTransactional
    def getMediaTypes() {
        def mediaTypes = []
        grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.media.MediaItem") {
                mediaTypes << [name: it.clazz.simpleName, id: it.clazz.simpleName]
            }
        }

        MediaItem.StructuredContentType.enumConstants.each {

            mediaTypes << [name: it.prettyName, id: it.name()]
        }

        mediaTypes.sort { it.name }
    }

    @Transactional(readOnly = true)
    def ifPublisherValid(MediaItem mediaItem) {
        if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !publisherItems()?.contains(mediaItem?.id)) {
            return false
        }
        return true
    }

    def updateHash(MediaItem mediaItem) {
        if (mediaItem instanceof Html) {
            def extractedContent
            try {
                def contentAndHash = contentRetrievalService.getContentAndMd5Hashcode(mediaItem.sourceUrl)
                extractedContent = contentAndHash.content
                if (!extractedContent) {
                    mediaItem.errors.rejectValue("sourceUrl", "SourceUrl invalid", "The system could not find syndication markup at the provided URL. Please verify the media source to ensure it contains at least one <div> element containing the 'syndicate' class.")
                }
                mediaItem.hash = contentAndHash.hash
            } catch (e) {
                mediaItem.errors.rejectValue("sourceUrl", "SourceUrl invalid", "The provided URL doesn't appear to be accessible, perhaps it's invalid? Please make " +
                        "sure it is fully qualified and correct.")
            }
        }
        mediaItem
    }

    def updateItemAndSubscriber(MediaItem mediaItem, Long subscriberId) {
        def mediaItemSubscriber = null
        def isValid = mediaItem.validate()
        if (!isValid) {
            log.error("Instance is invalid:\n${mediaItem.errors}")
        }
        if (isDuplicateUrl(mediaItem)) {
            mediaItem.errors.rejectValue("sourceUrl", "Duplicate Url", "The Source Url is already in use.")
        }

        mediaItem = updateHash(mediaItem)

        removeInvisibleMediaItemsFromUserMediaLists(mediaItem)

        if (Environment.current != Environment.DEVELOPMENT || (Environment.current == Environment.DEVELOPMENT && cmsManagerKeyService.listSubscribers()) || UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            if (mediaItem.id) {
                mediaItemSubscriber = MediaItemSubscriber.findByMediaItem(mediaItem)
            }
            if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if (!springSecurityService.currentUser.subscriberId) {
                    mediaItem.errors.reject("SubscriberId invalid", "You do not have a valid subscriber. For help contact " + grailsApplication.config.grails.mail.default.from)
                    log.error("The publisher " + springSecurityService.currentUser.name)
                }
                if (mediaItemSubscriber) {
                    mediaItemSubscriber.subscriberId = springSecurityService.currentUser.subscriberId
                } else {
                    mediaItemSubscriber = new MediaItemSubscriber([mediaItem: mediaItem, subscriberId: springSecurityService.currentUser.subscriberId])
                }
            } else if (["ROLE_ADMIN", "ROLE_MANAGER"].contains(UserRole.findByUser(springSecurityService.currentUser).role.authority)) {
                if (!subscriberId) {
                    mediaItem.errors.reject("SubscriberId invalid", "You did not select a valid subscriber.")
                    log.error("The publisher " + springSecurityService.currentUser.name)
                } else if (mediaItemSubscriber) {
                    mediaItemSubscriber.subscriberId = subscriberId as Long
                } else {
                    mediaItemSubscriber = new MediaItemSubscriber([mediaItem: mediaItem, subscriberId: subscriberId as Long])
                }
            }
        }

        if (mediaItem.hasErrors()) {
            MediaItem.withTransaction { status ->
                //more explicit for testing purposes
                status.setRollbackOnly()
            }
            MediaItemSubscriber.withTransaction { status ->
                status.setRollbackOnly()
            }
            return mediaItem
        }

        mediaItem.save(flush: true)
        mediaItemSubscriber?.save(flush: true)

        return mediaItem
    }

    @Transactional
    def scanContentForUpdates() {
        log.info "Daily Html Scan initiated"
        def htmlItems = Html.findAllByManuallyManaged(true)
        updateAndNotify(htmlItems)
    }

    private updateAndNotify(mediaItems) {
        mediaItems.each { MediaItem mediaItem ->
            try {
                Map freshExtraction = contentRetrievalService.getContentAndMd5Hashcode(mediaItem.sourceUrl)
                if (freshExtraction.hash != mediaItem.hash) {
                    mediaItem.hash = freshExtraction.hash
                    mediaItem.save(flush: true)
                    solrIndexingService.inputMediaItem(mediaItem, freshExtraction.content)
                }
            } catch (e) {
                log.error("Could not extract content, page was found but not marked up at url: ${mediaItem.sourceUrl}")
            }
        }
        //remote cache flushed in syndication model MediaItemChangeListener
    }

    @Transactional(readOnly = true)
    def isDuplicateUrl(MediaItem mediaItem) {
        if (!mediaItem.id && MediaItem.facetedSearch(sourceUrl: mediaItem.sourceUrl).count() == 1) {
            return true
        } else if (mediaItem.id && MediaItem.facetedSearch(sourceUrl: mediaItem.sourceUrl).count() == 2) {
            return true
        }
        return false
    }

    def resetHash(Long mediaId) {
        MediaItem mi = MediaItem.get(mediaId)
        def oldHash = mi.hash
        mi.hash = null
        mi = updateHash(mi)
        mi.save(flush: true)
        log.info "resetting hash for ${mediaId}\n${oldHash} --> ${mi.hash}"
        mi
    }
}