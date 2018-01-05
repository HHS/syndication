package com.ctacorp.syndication.tools

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.media.MediaItem
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional
class MediaPreviewThumbnailController {

    static final GET_IMAGE_BASE_URL = "${System.getenv('API_SERVER_URL')}${System.getenv('SYNDICATION_APIPATH')}"

    def remoteCacheService
    def mediaPreviewThumbnailJobService
    def awsSnsThumbnailPreviewService

    def index() {
        redirect action: 'allThumbnails'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    def flush(MediaItem mi) {

        awsSnsThumbnailPreviewService.generatePreviewAndThumbnail(mi)
        flash.message = "The media item's thumbnail and preview images are now being regenerated."
        redirect controller:'mediaItem', action:'show', id: mi.id
    }

    @Secured(['ROLE_ADMIN'])
    def allThumbnails() {
        params.max = params.max?:100
        def mediaItems = MediaItem.list(params)
        [mediaItems: mediaItems]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailsForCollection(Long collectionId, Long manualCollectionId) {
        if(manualCollectionId){
            if(!com.ctacorp.syndication.media.Collection.exists(manualCollectionId)){
                flash.error = "No campaign found with ID: ${manualCollectionId}"
                redirect action: "index"
                return
            }
            mediaPreviewThumbnailJobService.regenerateCollection(manualCollectionId)
            flash.message = "Preview and Thumbnail regeneration in progress for Collection: ${manualCollectionId}"
        } else{
            mediaPreviewThumbnailJobService.regenerateCollection(collectionId)
            flash.message = "Preview and Thumbnail regeneration in progress for Collection: ${collectionId}"
        }
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailsForRange(Long start, Long end){
        mediaPreviewThumbnailJobService.regenerateRange(start, end)
        flash.message = "Preview and Thumbnail regeneration in progress for media IDs ${start} to ${end} (inclusive)"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailPreviewForSingleItem(MediaItem mi){
        def errorCode = System.nanoTime()
        try {
            awsSnsThumbnailPreviewService.generatePreviewAndThumbnail(mi.id)
            String key = Hash.md5("${mi.id}")
            remoteCacheService.flushRemoteCacheByNameAndKey("imageCache", key)
            render """<img src="${GET_IMAGE_BASE_URL}/resources/media/${mi.id}/thumbnail.jpg"/>"""
        } catch(e){
            log.error("Error Code: ${errorCode}", e)
            e.printStackTrace()
            render "Error code: ${errorCode}"
        }
    }

    @Secured(['ROLE_ADMIN'])
    def flushCache(){
        remoteCacheService.flushRemoteCacheByName("imageCache")
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }

    @Secured(['ROLE_ADMIN'])
    def generateMissing() {
        mediaPreviewThumbnailJobService.regenerateMissing()
        flash.message = "${new Date()} Preview and Thumbnail generation in progress"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailPreviewForAllMedia(){
        mediaPreviewThumbnailJobService.regenerateAll()
        flash.message = "${new Date()} Preview and Thumbnail regeneration in progress"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }
}
