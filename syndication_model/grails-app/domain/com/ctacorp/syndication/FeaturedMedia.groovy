package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem

class FeaturedMedia {
    static belongsTo = [mediaItem:MediaItem]

    static constraints = {
        mediaItem nullable:false
    }
}
