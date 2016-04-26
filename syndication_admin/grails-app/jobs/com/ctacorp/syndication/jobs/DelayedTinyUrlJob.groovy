package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 3/11/16.
 */
class DelayedTinyUrlJob {

    def tinyUrlService

    def execute(context) {
        def instanceId = context.mergedJobDataMap.get('mediaId')
        log.info("begin tiny url job for mediaID ${instanceId}")
        def sourceUrl = context.mergedJobDataMap.get('sourceUrl')
        def externalGuid = context.mergedJobDataMap.get('externalGuid')
        tinyUrlService.createMapping(sourceUrl,instanceId,externalGuid)
    }
}
