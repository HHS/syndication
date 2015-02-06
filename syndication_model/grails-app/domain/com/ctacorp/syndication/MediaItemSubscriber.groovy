package com.ctacorp.syndication

class MediaItemSubscriber {

    Long subscriberId
    MediaItem mediaItem

    static constraints = {
        subscriberId nullable: false, blank: false
        mediaItem nullable: false, blank: false
    }
}
