package com.ctacorp.syndication.preview

import com.ctacorp.syndication.media.MediaItem

class MediaPreview {
    Date dateCreated
    Date lastUpdated
    byte[] imageData

    static belongsTo = [mediaItem:MediaItem]

    static constraints = {
        imageData nullable: false, maxSize: 4 * 1024 * 1024 //4MB
        mediaItem nullable: false
    }
}