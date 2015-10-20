package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.storefront.MediaMetricService
import spock.lang.Specification


/**
 * Created by sgates on 6/12/15.
 */
class DelayedMetricAddJobSpec extends Specification {
    DelayedMetricAddJob job

    def setup() {
        job = new DelayedMetricAddJob()
        job.mediaMetricService = Mock(MediaMetricService)
    }

    def "job should call mediaMetricService"() {
        given: "a media id"
            Long id = 1L
        when: "job is executed"
            def context = [mergedJobDataMap:[mediaId:id]]
            job.execute(context)
        then: "Add storefront view should be called"
            1 * job.mediaMetricService.addStorefrontView(id)
    }
}