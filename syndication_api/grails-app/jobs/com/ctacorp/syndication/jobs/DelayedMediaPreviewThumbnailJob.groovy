package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.media.MediaItem

class DelayedMediaPreviewThumbnailJob {
    def mediaPreviewThumbnailService

    def execute(context) {
        def mediaId = context.mergedJobDataMap.get('mediaId')
        if(mediaId){
            try {
                mediaPreviewThumbnailService.generate(mediaId)
            } catch(ConnectException e){
                log.error("Couldn't connect to Manet Preview Server for mediaId: ${mediaId}")
            } catch(e){
                log.error e
            }
        }
    }
}
