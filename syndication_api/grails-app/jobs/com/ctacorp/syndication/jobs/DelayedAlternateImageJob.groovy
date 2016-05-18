package com.ctacorp.syndication.jobs


import com.ctacorp.syndication.AlternateImage

/**
 * Created by nburk on 4/8/16.
 */
class DelayedAlternateImageJob {

    def alternateImagesService

    def execute(context) {
        def instanceId = context.mergedJobDataMap.get('mediaId')
        def alternateImageData = context.mergedJobDataMap.get('alternateImages')
        log.info("begin alternate Image job for mediaID ${instanceId}")
        alternateImageData.each{ alternateImage ->
            alternateImagesService.save(new AlternateImage([name:alternateImage.name,
                                                            width:alternateImage.width,
                                                            height:alternateImage.height,
                                                            url:alternateImage.url]),
                    instanceId)
        }
    }
}
