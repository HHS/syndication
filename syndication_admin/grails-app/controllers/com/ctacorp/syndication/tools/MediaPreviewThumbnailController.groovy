package com.ctacorp.syndication.tools

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.jobs.DelayedMediaPreviewThumbnailJob
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.util.Holders
import org.apache.commons.io.IOUtils

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional
class MediaPreviewThumbnailController {
    def mediaPreviewThumbnailService
    def remoteCacheService

    def index() {
        redirect action: 'allThumbnails'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    def flush(MediaItem mi) {
        mediaPreviewThumbnailService.generate(mi.id)
        String key = Hash.md5("${mi.id}")
        remoteCacheService.flushRemoteCacheByNameAndKey("imageCache", key)
        flash.message = "The media item's thumbnail and preview images are now being regenerated."
        redirect controller:'mediaItem', action:'show', id: mi.id
    }

    @Secured(['ROLE_ADMIN'])
    def allThumbnails(){
        params.max = params.max?:100
        [mediaItems:MediaItem.list(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailPreviewForSingleItem(MediaItem mi){
        def errorCode = System.nanoTime()
        try {
            mediaPreviewThumbnailService.generate(mi.id)
            String key = Hash.md5("${mi.id}")
            remoteCacheService.flushRemoteCacheByNameAndKey("imageCache", key)
            render """<img src="${Holders.config.syndication.serverUrl}/api/v2/resources/media/${mi.id}/thumbnail.jpg"/>"""
        } catch(e){
            log.error("Error Code: ${errorCode}\n"+e)
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
        DelayedMediaPreviewThumbnailJob.schedule(new Date(System.currentTimeMillis() + (1000)), ["scope":"missing"])
        flash.message = "${new Date()} Preview and Thumbnail generation in progress"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailPreviewForAllMedia(){
        DelayedMediaPreviewThumbnailJob.schedule(new Date(System.currentTimeMillis() + (1000)), ["scope":"all"])
        remoteCacheService.flushRemoteCacheByName("imageCache")
        flash.message = "${new Date()} Preview and Thumbnail regeneration in progress"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }
}
