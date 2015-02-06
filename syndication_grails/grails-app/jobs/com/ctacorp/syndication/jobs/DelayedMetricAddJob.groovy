package com.ctacorp.syndication.jobs


class DelayedMetricAddJob {
    def mediaMetricService

    def execute(context) {
        def mediaId = context.mergedJobDataMap.get('mediaId')
        if(mediaId) {
            mediaMetricService.addApiView(mediaId)
        }
    }
}