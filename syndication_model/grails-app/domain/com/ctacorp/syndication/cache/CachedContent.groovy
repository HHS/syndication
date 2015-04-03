package com.ctacorp.syndication.cache

import com.ctacorp.syndication.media.MediaItem

class CachedContent {
    MediaItem mediaItem
    String content
    Date lastUpdated
    Date dateCreated

    static constraints = {
        content maxSize: 16777215
    }
}
