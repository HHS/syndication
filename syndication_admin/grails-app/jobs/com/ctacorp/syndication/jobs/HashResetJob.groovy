package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.media.Html

/**
 * Created by sgates on 11/10/15.
 */
class HashResetJob {
    def mediaItemsService

    def execute(context) {
        def ids
        if (context.mergedJobDataMap.get('restrictToDomain')) {
            ids = Html.executeQuery("select mi.id from MediaItem mi where mi.class='com.ctacorp.syndication.media.Html' and mi.sourceUrl like ?", ["%${context.mergedJobDataMap.get('domain')}%"])
        } else {
            ids = Html.executeQuery("select mi.id from MediaItem mi where mi.class='com.ctacorp.syndication.media.Html'")
        }
        ids.each {
            mediaItemsService.resetHash(it as Long)
        }
    }
}