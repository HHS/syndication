package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem

class MediaItemSubscriber {

    Long subscriberId
    MediaItem mediaItem

    static constraints = {
        subscriberId nullable: false, blank: false
        mediaItem nullable: false, blank: false
    }
}
