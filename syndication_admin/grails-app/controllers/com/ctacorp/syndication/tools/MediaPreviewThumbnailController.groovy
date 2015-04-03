package com.ctacorp.syndication.tools

import com.ctacorp.syndication.media.MediaItem
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
@Transactional
class MediaPreviewThumbnailController {

    def mediaPreviewThumbnailJobService
    def remoteCacheService

    def index() {}

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def flush(MediaItem mi) {

        remoteCacheService.flushRemoteCache()
        mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(mi.id)

        flash.message = "The media item's thumbnail and preview images have been marked for regeneration"

        redirect controller:'mediaItem', action:'show', id: mi.id
    }
}
