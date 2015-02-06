package com.ctacorp.syndication

class FeaturedMedia {
    static belongsTo = [mediaItem:MediaItem]

    static constraints = {
        mediaItem nullable:false
    }
}
