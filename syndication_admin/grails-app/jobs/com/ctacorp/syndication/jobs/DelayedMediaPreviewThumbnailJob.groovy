package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaThumbnail

class DelayedMediaPreviewThumbnailJob {
    def mediaPreviewThumbnailService

    def execute(context) {
        if(context.mergedJobDataMap.mediaId){
            log.info "regen thumbnail for ${context.mergedJobDataMap.mediaId}"
            Long mediaId = context.mergedJobDataMap.mediaId as Long
            mediaPreviewThumbnailService.generate(mediaId)
        } else{
            def mediaItems = MediaItem.list(sort:"id", order:"DESC")

            mediaItems.each{ mediaItem ->
                try {
                    def className = mediaItem.getClass().simpleName.toLowerCase()
                    if (className in ["html", "image", "infographic", "video"]) {
                        if (context.mergedJobDataMap.get('scope') == "all") {
                            mediaPreviewThumbnailService.generate(mediaItem.id)
                            log.info "regen thumbnail for ${mediaItem.id}"
                        } else {
                            MediaThumbnail existingThumb = MediaThumbnail.findByMediaItem(mediaItem)
                            if (!existingThumb) {
                                log.info "regen thumbnail for ${mediaItem.id}"
                                mediaPreviewThumbnailService.generate(mediaItem.id)
                            }
                        }
                    }
                } catch(e){
                    log.error "Problem generating thumbnail for ${mediaItem.id}: ${mediaItem.sourceUrl}"
                }
            }
        }
    }
}