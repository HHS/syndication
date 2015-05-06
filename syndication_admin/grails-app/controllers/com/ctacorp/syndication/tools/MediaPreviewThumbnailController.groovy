package com.ctacorp.syndication.tools

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.jobs.DelayedMediaPreviewThumbnailJob
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
@Transactional
class MediaPreviewThumbnailController {
    def mediaPreviewThumbnailService
    def remoteCacheService

    def index() {
        redirect action: 'allThumbnails'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def flush(MediaItem mi) {
        mediaPreviewThumbnailService.generate(mi)

        flash.message = "The media item's thumbnail and preview images have been marked for regeneration"
        redirect controller:'mediaItem', action:'show', id: mi.id
    }

    @Secured(['ROLE_ADMIN'])
    def allThumbnails(){
        params.max = params.max?:100
        [mediaItems:MediaItem.list(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def regenerateThumbnailPreviewForSingleItem(MediaItem mi){
        mediaPreviewThumbnailService.generate(mi)
        String key = Hash.md5("thumbnail/${mi.id}?[action:[GET:thumbnail], controller:media, id:${mi.id}]")
        remoteCacheService.flushRemoteCacheByNameAndKey("imageCache", key)
        render """<img src="${grails.util.Holders.config.syndication.serverUrl}/api/v2/resources/media/${mi.id}/thumbnail.jpg"/>"""
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
        flash.message = "${new Date()} Preview and Thumbnail regeneration in progress"
        redirect action: 'allThumbnails', params: [max:params.max, offset:params.offset]
    }
}
