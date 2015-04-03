package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.media.MediaItem

class DelayedMediaPreviewThumbnailJob {
    def mediaPreviewThumbnailService

    def execute(context) {
        def mediaId = context.mergedJobDataMap.get('mediaId')
        if(mediaId){
            MediaItem mi = MediaItem.get(mediaId)
            mediaPreviewThumbnailService.generate(mi)
        }
    }
}
