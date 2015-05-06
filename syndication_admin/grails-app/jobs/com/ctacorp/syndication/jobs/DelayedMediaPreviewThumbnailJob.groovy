package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaThumbnail

class DelayedMediaPreviewThumbnailJob {
    def mediaPreviewThumbnailService
    def remoteCacheService

    def execute(context) {
        def mediaIds = MediaItem.executeQuery('select mi.id from MediaItem mi order by mi.id')

        mediaIds.each{ mediaId ->
            try {
                MediaItem mi = MediaItem.read(mediaId)
                def className = mi.getClass().simpleName.toLowerCase()
                if (className in ["html", "image", "infographic", "periodical", "video"]) {
                    if (context.mergedJobDataMap.get('scope') == "all") {
                        mediaPreviewThumbnailService.generate(mi)
                    } else {
                        MediaThumbnail existingThumb = MediaThumbnail.findByMediaItem(mi)
                        if (!existingThumb) {
                            mediaPreviewThumbnailService.generate(mi)
                        }
                    }
                    String key = Hash.md5("thumbnail/${mi.id}?[action:[GET:thumbnail], controller:media, id:${mi.id}]")
                    remoteCacheService.flushRemoteCacheByNameAndKey("imageCache", key)
                }
            } catch(e){
                log.error "Problem generating thumbnail for ${mediaId}"
            }
        }
    }
}