
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.metrics

import com.ctacorp.syndication.*
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional(readOnly = true)
class MediaMetricService {
    /**
     * Add a view to a media item (a +1 view)
     * @param mediaId
     * @return
     */

    @Transactional
    def addApiView(Long mediaId) {
        def mediaItem = MediaItem.load(mediaId)
        if (!mediaItem) {
            log.error "Cannot set a metric on an item that cannot be found: ${mediaId}"
        }

        def metric = MediaMetric.findByMediaAndDayGreaterThanEquals(mediaItem, today, [lock:true])

        if (!metric) {
            metric = new MediaMetric(day: today, media: mediaItem, apiViewCount: 1)
        } else {
            metric.apiViewCount++
        }
        metric.save()
    }

    /**
     * Get the total number of views today for an item
     * @param mediaId
     * @return
     */
    def long getApiViewCountToday(Long mediaId) {
        def mediaItem = MediaItem.load(mediaId)
        if (!mediaItem) {
            log.error "Cannot get a metric on an item that cannot be found: ${mediaId}"
            return -1
        }

        def metric = MediaMetric.findByMediaAndDayGreaterThanEquals(mediaItem, today)
        return metric?.apiViewCount ?: 0
    }

    /**
     * Get the API view count for all items for today
     * @return
     */
    def long getApiViewCountToday() {
        MediaMetric.createCriteria().get {
            gte 'day', today
            projections {
                sum('apiViewCount')
            }
        } ?: 0
    }

    /**
     * Get the API view count for all items for the last 7 days
     * @param mediaId
     * @return
     */
    def long getApiViewCountLast7Days() {
        Date lastWeek = today - 7

        MediaMetric.createCriteria().get {
            gte 'day', lastWeek
            projections {
                sum('apiViewCount')
            }
        } ?: 0
    }

    /**
     * Get the API view count for a specific item for the last 7 days
     * @param mediaId
     * @return
     */
    def long getApiViewCountLast7Days(Long mediaId) {
        def mediaItem = MediaItem.load(mediaId)
        if (!mediaItem) {
            log.error "Cannot get a metric on an item that cannot be found: ${mediaId}"
            return -1L
        }

        Date lastWeek = today - 7
        MediaMetric.createCriteria().get {
            gte 'day', lastWeek
            media {
                eq 'id', mediaId
            }

            projections {
                sum 'apiViewCount'
            }
        } ?: 0L
    }

    /**
     * Get the api view count total (for all time) for a specific item
     * @param mediaId
     * @return
     */
    def long getApiViewCount(Long mediaId) {
        MediaMetric.createCriteria().get {
            eq 'id', mediaId
            projections {
                sum('apiViewCount')
            }
        } ?: 0
    }

    /**
     * Get the api view count total (for all time) for all items
     * @param mediaId
     * @return
     */
    def long getApiViewCount() {
        MediaMetric.createCriteria().get {
            projections {
                sum('apiViewCount')
            }
        } ?: 0
    }

    /**
     * Get the api view count for a specific item on a specific day
     * @param mediaId
     * @param day
     * @return
     */
    def long getApiViewCountForDay(Long mediaId, Date day) {
        def mediaItem = MediaItem.load(mediaId)
        if (!mediaItem) {
            log.error "Cannot get a metric on an item that cannot be found: ${mediaId}"
            return -1L
        }
        day = day.clearTime()
        def metric = MediaMetric.findByMediaAndDayGreaterThanEqualsAndDayLessThan(mediaItem, day, day + 1)
        metric?.apiViewCount ?: 0L
    }

    /**
     * Get the api view count for a specific item on a specific day
     * @param mediaId
     * @param day
     * @return
     */
    def long getApiViewCountForDay(Date day) {
        day = day.clearTime()
        MediaMetric.createCriteria().get {
            eq 'day', day
            projections {
                sum 'apiViewCount'
            }
        } ?: 0
    }

    /**
     * Get the api view count for a specific item for a range of days
     * @param mediaId
     * @param dayFrom
     * @param dayTo
     * @return
     */
    def long getApiViewCountForRange(Long mediaId, Date dayFrom, Date dayTo) {
        MediaMetric.createCriteria().get {
            media {
                idEq mediaId
            }

            ge 'day', dayFrom
            le 'day', dayTo

            projections {
                sum 'apiViewCount'
            }
        } ?: 0
    }

    /**
     * Get the api view count for a specific item for a range of days
     * @param mediaId
     * @param dayFrom
     * @param dayTo
     * @return
     */
    def long getApiViewCountForRange(Date dayFrom, Date dayTo) {
        MediaMetric.createCriteria().get {
            ge 'day', dayFrom
            le 'day', dayTo

            projections {
                sum 'apiViewCount'
            }
        } ?: 0
    }

    private getToday() {
        new Date().clearTime()
    }
}
