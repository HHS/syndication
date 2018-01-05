package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.jobs.DelayedTinyUrlJob
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.media.QuestionAndAnswer
import com.ctacorp.syndication.metric.MediaMetric
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.transaction.NotTransactional
import grails.util.Environment
import grails.transaction.Transactional
import grails.util.Holders

@Transactional
class MediaItemsService {

    def extendedAttributeService
    def grailsApplication
    def springSecurityService
    def cmsManagerKeyService
    def contentRetrievalService
    def elasticsearchService
    def tagService

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

    @NotTransactional
    def delete(Long id) {
        if(!MediaItem.exists(id)){
            return
        }

        def clazz = MediaItem.get(id).class.name

        //Alternate Images -----------------------------------------------
        AlternateImage.withTransaction {
            MediaItem mi = MediaItem.get(id)
            if (!mi) {
                return
            }
            def alternateImages = AlternateImage.where {
                mediaItem == mi
            }.list()

            for (altImage in alternateImages) {
                altImage.delete()
            }
        }

        //Campaigns -----------------------------------------------
        Campaign.withTransaction {
            MediaItem mi = MediaItem.get(id)
            def campaigns = []
            campaigns.addAll(mi.campaigns ?: [])

            for (campaign in campaigns) {
                campaign.removeFromMediaItems(mi)
            }
        }

        //Collections -----------------------------------------------
        com.ctacorp.syndication.media.Collection.withTransaction {
            MediaItem mi = MediaItem.get(id)

            def collections = com.ctacorp.syndication.media.Collection.where {
                mediaItems {
                    id == mi.id
                }
            }.list()

            for (collection in collections) {
                collection.removeFromMediaItems(mi)
            }
        }

        //Extended Attributes -----------------------------------------------
        ExtendedAttribute.withTransaction {
            MediaItem mi = MediaItem.get(id)

            def extendedAttributes = ExtendedAttribute.where {
                mediaItem == mi
            }.list()

            for (extendedAttr in extendedAttributes) {
                extendedAttr.delete()
            }
        }

        //Media Metrics -----------------------------------------------
        MediaMetric.withTransaction {
            MediaItem mi = MediaItem.get(id)

            def mediaMetrics = MediaMetric.where {
                media == mi
            }.list()

            for (metric in mediaMetrics) {
                metric.delete()
            }
        }

        //User Media Lists ------------------------------------------
        UserMediaList.withTransaction {
            MediaItem mi = MediaItem.get(id)
            def userMediaLists = UserMediaList.where {
                mediaItems {
                    id == mi.id
                }
            }.list()

            for (userMediaList in userMediaLists) {
                userMediaList.removeFromMediaItems(mi)
            }
        }

        //Q&A - FAQ --------------------------------------------------
        QuestionAndAnswer.withTransaction {
            MediaItem mi = MediaItem.get(id)
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
        }

        // Media Preview and thumbnails --------------------------------
        MediaPreview.withTransaction {
            MediaItem mi = MediaItem.get(id)
            MediaPreview.where {
                mediaItem == mi
            }.deleteAll()
        }

        //thumbnails ---------------------------------------------------
        MediaThumbnail.withTransaction {
            MediaItem mi = MediaItem.get(id)
            MediaThumbnail.where {
                mediaItem == mi
            }.deleteAll()
        }

        //Users --------------------------------------------------
        User.withTransaction {
            MediaItem mi = MediaItem.get(id)
            def users = User.where {
                likes {
                    id == mi.id
                }
            }.list()

            users.each { col ->
                col.removeFromLikes(mi)
            }
        }

        //Collection Items --------------------------------------------------
        MediaItem.withTransaction {
            MediaItem mi = MediaItem.get(id)
            if(mi instanceof Collection) {
                mi.mediaItems = []
            }
        }

        //Cached Content --------------------------------------------------
        CachedContent.withTransaction {
            MediaItem mi = MediaItem.get(id)
            CachedContent.findByMediaItem(mi)?.delete(flush: true)
        }

        //Flagged Media --------------------------------------------------
        FlaggedMedia.withTransaction {
            MediaItem mi = MediaItem.get(id)
            FlaggedMedia.findByMediaItem(mi)?.delete(flush: true)
        }

        //Subscribers --------------------------------------------------
        MediaItemSubscriber.withTransaction {
            MediaItem mi = MediaItem.get(id)
            MediaItemSubscriber.findByMediaItem(mi)?.delete(flush: true)
        }

        //The media item itself --------------------------------------------------
        MediaItem.withTransaction {
            MediaItem mi = MediaItem.get(id)
              mi.delete()
        }

        tagService.removeContentItem(id)
        elasticsearchService.deleteMediaItemIndex(id, clazz)
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
        params.language = params.language ?: "1"
        params.languageName = Language.get(params.language)
        params.sourceUrlContains = params.url
        params.restrictToSet = params.inList
        params.mediaTypes = params.mediaType
        params.createdByContains = params.createdBy ?: ""
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

        mediaItem = updateHash(mediaItem)

        removeInvisibleMediaItemsFromUserMediaLists(mediaItem)

        if (Environment.current != Environment.DEVELOPMENT || (Environment.current == Environment.DEVELOPMENT && cmsManagerKeyService.listSubscribers()) || UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            if (mediaItem.id) {
                mediaItemSubscriber = MediaItemSubscriber.findByMediaItem(mediaItem)
            }
            if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if (!springSecurityService.currentUser.subscriberId) {
                    mediaItem.errors.reject("SubscriberId invalid", "You do not have a valid subscriber. For help contact " + Holders.config.MAIL_DEFAULT_FROM)
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
        elasticsearchService.indexMediaItem(mediaItem)

        //Add URL Mapping
        DelayedTinyUrlJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:mediaItem.id, sourceUrl: mediaItem.sourceUrl, externalGuid:mediaItem.externalGuid])

        return mediaItem
    }

    def scanContentForUpdates() {
        log.info "Daily Html Scan initiated"
        def htmlItems = Html.findAllByManuallyManaged(true)
        updateAndNotify(htmlItems)
    }

    private updateAndNotify(mediaItems) {

        mediaItems.each { MediaItem mediaItem ->

            try {

                def freshExtraction = contentRetrievalService.getContentAndMd5Hashcode(mediaItem.sourceUrl)

                if (freshExtraction.hash != mediaItem.hash) {

                    mediaItem.hash = freshExtraction.hash
                    mediaItem.save(flush: true)

                    elasticsearchService.indexMediaItem(mediaItem, freshExtraction.content)
                }

            } catch (e) {
                log.error "Could not extract content, page was found but not marked up at url: ${mediaItem.sourceUrl}", e
            }
        }
    }

    def resetHash(Long mediaId) {
        log.info "resetting hash for ${mediaId}"
        MediaItem mi = MediaItem.get(mediaId)
        mi.hash = -1
        if(!mi.validate()){
            log.error "Couldn't reset hash because: ${mi.errors}"
        }
    }

    def resetDBCache(Long mediaId){
        log.info "resetting db cached content for for ${mediaId}"
        MediaItem mi = MediaItem.read(mediaId)
        if(mi){
            CachedContent.where{
                mediaItem == mi
            }.deleteAll()
        }
    }
}