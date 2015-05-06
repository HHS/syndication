package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.media.Periodical
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.util.Environment
import grails.transaction.Transactional

@Transactional
class MediaItemsService {

    def extendedAttributeService
    def grailsApplication
    def springSecurityService
    def cmsManagerKeyService
    def contentRetrievalService
    def solrIndexingService
    def remoteCacheService

    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    def delete(Long mediaId) {
        MediaItem mi = MediaItem.get(mediaId)
        def campaigns = []
        campaigns.addAll(mi.campaigns)

        for(campaign in campaigns){
            campaign.removeFromMediaItems(mi)
        }

        def collections = com.ctacorp.syndication.media.Collection.where{
            mediaItems{
                id == mi.id
            }
        }.list()

        for(collection in collections){
            collection.removeFromMediaItems(mi)
        }

        def users = User.where{
            likes{
                id == mi.id
            }
        }.list()

        users.each { col ->
            col.removeFromLikes(mi)
        }
        
        // Media Preview and thumbnails --------------------------------
        MediaPreview.where{
            mediaItem == mi
        }.deleteAll()

        MediaThumbnail.where{
            mediaItem == mi
        }.deleteAll()

        // Media Preview and thumbnails --------------------------------
        MediaPreview.where{
            mediaItem == mi
        }.deleteAll()

        MediaThumbnail.where{
            mediaItem == mi
        }.deleteAll()

        CachedContent.findByMediaItem(mi)?.delete(flush:true)
        FlaggedMedia.findByMediaItem(mi)?.delete(flush:true)
        MediaItemSubscriber.findByMediaItem(mi)?.delete(flush:true)
        mi.delete(flush:true)
    }

    Map getIndexResponse(params, MediaItemType){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            return [mediaItemList:MediaItemType.findAllByIdInList(publisherItems(),params), mediaItemInstanceCount: MediaItemType.countByIdInList(publisherItems() ?: [0],params)]
        }

        return [mediaItemList:MediaItemType.list(params), mediaItemInstanceCount: MediaItemType.count()]
    }
    
    @Transactional(readOnly = true)
    def getAllMediaIds(){
        MediaItem.executeQuery("select mi.id from MediaItem mi")
    }
    
    @Transactional()
    def getPublisherMediaIds(subscriberId){
        MediaItemSubscriber.executeQuery("select mi.mediaItem.id from MediaItemSubscriber mi where mi.subscriberId = ?",[subscriberId])
    }

    //returns the model at index 0 and and the error message if at index 1
    String addExtendedAttribute(MediaItem mi, ExtendedAttribute attributeInstance){
        attributeInstance.mediaItem = mi

        if (attributeInstance == null) {
            return "attribute doesn't exist"
        }

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
    void removeMediaItemsFromUserMediaLists(MediaItem mediaItem, deletingMediaItem = false){
        if(MediaItem.get(mediaItem.id)?.getPersistentValue("visibleInStorefront") && !mediaItem.visibleInStorefront ||
                MediaItem.get(mediaItem.id)?.getPersistentValue("active") && !mediaItem.active){

           List<UserMediaList> userMedia = UserMediaList.containsMediaItem(mediaItem).list() ?: []
            userMedia.each{
                it.mediaItems.remove(mediaItem)
            }
        }
        if(deletingMediaItem){
            List<UserMediaList> userMedia = UserMediaList.containsMediaItem(mediaItem).list() ?: []
            userMedia.each{
                it.mediaItems.remove(mediaItem)
            }
        }
    }

    def findMediaByAll(params){
        params.sort = params.sort ?: "id"
        params.order = params.order ?: "asc"
        params.title = params.title ?: ""
        params.url = params.url ?: ""

            def mediaItems = MediaItem.facetedSearch([
                    id:params.int("id"),
                    nameContains:params.title.replace('%', '\\%'),
                languageName:Language.get(params.language),
//                sourceName:params.source,
                    sourceUrlContains:params.url,
                    restrictToSet: params.inList,
                mediaTypes:params.mediaType
//                restrictToSet: params.topicItems
            ]).list(max:params.max, offset:params.offset,sort:params.sort, order:params.order)

        return mediaItems
    }

    def getMediaTypes() {
        def mediaTypes = []
        grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.media.MediaItem") {
                def simpleName = it.clazz.simpleName
                if(simpleName == "SocialMedia"){
                    mediaTypes << "Social Media"
                }else{
                    mediaTypes << simpleName
                }
            }
        }
        mediaTypes.sort()
    }

    def ifPublisherValid(MediaItem mediaItem){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !publisherItems()?.contains(mediaItem?.id)){
            return false
        }
        return true
    }

    def updateItemAndSubscriber(MediaItem mediaItem, Long subscriberId){
        def errors = []
        def mediaItemSubscriber = null
        mediaItem.validate()
        if (mediaItem.hasErrors()) {
            def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib');
            errors = mediaItem.errors.allErrors.collect { [message: g.message([error: it])] }
            return errors
        }
        
        if(mediaItem instanceof Html || mediaItem instanceof Periodical){
            def extractedContent
            try {
                def contentAndHash = contentRetrievalService.getContentAndMd5Hashcode(mediaItem.sourceUrl)
                extractedContent = contentAndHash.content
                mediaItem.hash = contentAndHash.hash
                if(!extractedContent){
                    errors = [[message:  "The system could not find syndication markup at the provided URL. Please verify the media source to ensure it contains at least one <div> element containing the 'syndicate' class."]]
                    return errors
                }
            } catch(e){
                errors = [[message:  "The provided URL doesn't appear to be accessible, perhaps it's invalid? Please make " +
                        "sure it is fully qualified and correct."]]
                return errors
            }
        }

        //TODO this needs to be renamed, it makes it sound like it removes this item from all user media lists
        removeMediaItemsFromUserMediaLists(mediaItem)

        if(Environment.current != Environment.DEVELOPMENT || (Environment.current == Environment.DEVELOPMENT && cmsManagerKeyService.listSubscribers()) || UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){

            if(mediaItem.id){
                mediaItemSubscriber = MediaItemSubscriber.findByMediaItem(mediaItem)
            }
            if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
                if (!springSecurityService.currentUser.subscriberId) {
                    errors << [message: "You do not have a valid subscriber. For help contact " + grailsApplication.config.grails.mail.default.from]
                    log.error("The publisher " + springSecurityService.currentUser.name)
                }
                if (mediaItemSubscriber) {
                    mediaItemSubscriber.subscriberId = springSecurityService.currentUser.subscriberId
                } else {
                    mediaItemSubscriber = new MediaItemSubscriber([mediaItem: mediaItem, subscriberId: springSecurityService.currentUser.subscriberId])
                }
            } else if (["ROLE_ADMIN","ROLE_USER","ROLE_MANAGER"].contains(UserRole.findByUser(springSecurityService.currentUser).role.authority)) {
                if (!subscriberId) {
                    errors << [message: "You did not select a valid subscriber."]
                    log.error("The publisher " + springSecurityService.currentUser.name)
                } else if (mediaItemSubscriber) {
                    mediaItemSubscriber.subscriberId = subscriberId as Long
                } else {
                    mediaItemSubscriber = new MediaItemSubscriber([mediaItem: mediaItem, subscriberId: subscriberId as Long])
                }
            }
        }
        
        if(errors != []){
            transactionStatus.setRollbackOnly()
            return errors
        }
        
        mediaItem.save(flush: true)
        mediaItemSubscriber?.save(flush: true)

        return null
    }

    @Transactional
    def scanContentForUpdates() {
        log.info "Daily Periodical Scan initiated"
        //Update happens at midnight server time
        def periodicals = Periodical.findAllByManuallyManaged(true)
        updateAndNotify(periodicals)

        log.info "Daily Html Scan initiated"
        def htmlItems = Html.findAllByManuallyManaged(true)
        updateAndNotify(htmlItems)
    }

    private updateAndNotify(mediaItems){
        
        mediaItems.each { MediaItem mediaItem ->
            try {
                Map freshExtraction = contentRetrievalService.getContentAndMd5Hashcode(mediaItem.sourceUrl)
                if (freshExtraction.hash != mediaItem.hash) {
                    mediaItem.hash = freshExtraction.hash
                    mediaItem.save(flush: true)
                    solrIndexingService.inputMediaItem(mediaItem, freshExtraction.content)
                }
            }catch(e) {
                log.error("Could not extract content, page was found but not marked up at url: ${mediaItem.sourceUrl}")
            }
        }
        //remote cache flushed in syndication model MediaItemChangeListener
    }
    
}