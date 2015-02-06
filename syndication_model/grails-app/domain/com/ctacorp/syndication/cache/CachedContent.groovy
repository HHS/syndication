package com.ctacorp.syndication.cache

import com.ctacorp.syndication.MediaItem

class CachedContent {
    MediaItem mediaItem
    String content
    Date lastUpdated
    Date dateCreated

    static constraints = {
        content maxSize: 16777215
    }
}
