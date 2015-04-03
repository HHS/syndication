package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 2/12/15.
 */
class DelayedMetricAddJob {
    def mediaMetricService

    def execute(context) {
        def mediaId = context.mergedJobDataMap.get('mediaId')
        if(mediaId) {
            mediaMetricService.addStorefrontView(mediaId)
        }
    }
}
