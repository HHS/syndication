package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.metric.MediaMetric
import grails.transaction.NotTransactional
import grails.transaction.Transactional

/**
 * Created by nburk on 2/12/15.
 */
@Transactional
class MediaMetricService {

    def addStorefrontView(Long mediaId){
        def mediaItem = MediaItem.load(mediaId)
        if (!mediaItem) {
            log.error "Cannot set a metric on an item that cannot be found: ${mediaId}"
        }

        def metric = MediaMetric.findByMediaAndDayGreaterThanEquals(mediaItem, today, [lock:true])

        if (!metric) {
            metric = new MediaMetric(day: today, media: mediaItem, storefrontViewCount: 1)
        } else {
            metric.storefrontViewCount++
        }
        metric.save()
    }

    @NotTransactional
    static protected Date getToday() {
        new Date().clearTime()
    }
}
