package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaThumbnail

class DelayedMediaPreviewThumbnailJob {
    def mediaPreviewThumbnailService
    def remoteCacheService

    def execute(context) {
        switch (context.mergedJobDataMap.scope) {
            case "single":
                log.info "regen thumbnail for ${context.mergedJobDataMap.mediaId}"
                Long mediaId = context.mergedJobDataMap.mediaId as Long
                mediaPreviewThumbnailService.generate(mediaId)
                break
            case "range":
                def mediaItems = MediaItem.findAllByIdGreaterThanEqualsandIdLessThanEquals(context.mergedJobDataMap.start, context.mergedJobDataMap.end)
                generateThumbnails(mediaItems)
                break
            case "missing":
                def allMediaItems = MediaItem.list(sort: "id", order: "ASC")
                def mediaWithoutThumbnail = []
                allMediaItems.each { mediaItem ->
                    MediaThumbnail existingThumb = MediaThumbnail.findByMediaItem(mediaItem)
                    if (!existingThumb) {
                        log.info "regen thumbnail for ${mediaItem.id}"
                        mediaWithoutThumbnail << mediaItem
                    }
                    generateThumbnails(mediaWithoutThumbnail)
                }

                break
            case "collection":
                def collectionMediaItems = com.ctacorp.syndication.media.Collection.get(context.mergedJobDataMap.collectionId)?.mediaItems
                generateThumbnails(collectionMediaItems)
                break
            case "all":
                def mediaItems = MediaItem.list(sort: "id", order: "ASC")
                generateThumbnails(mediaItems)
                break
            default:
                log.error "Tried to regenerate thumbnails with an unknown scope: ${context.mergedJobDataMap.scope}"
        }
    }

    def generateThumbnails(mediaItems) {
        mediaItems.each { mediaItem ->
            try {
                mediaPreviewThumbnailService.generate(mediaItem.id)
                log.info "regen thumbnail for ${mediaItem.id}"
            } catch (e){
                e.printStackTrace()
                log.error "Problem generating thumbnail for ${mediaItem.id}: ${mediaItem.sourceUrl}"
            }
        }

        remoteCacheService.flushRemoteCacheByName("imageCache")
    }
}