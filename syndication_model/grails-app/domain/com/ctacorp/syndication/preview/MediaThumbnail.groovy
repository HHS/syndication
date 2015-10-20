package com.ctacorp.syndication.preview

import com.ctacorp.syndication.media.MediaItem

class MediaThumbnail {
    Date dateCreated
    Date lastUpdated
    byte[] imageData
    byte[] customImageData

    static belongsTo = [mediaItem: MediaItem]

    static constraints = {
        imageData nullable: false, maxSize: 1 * 1024 * 1024 //1MB
        customImageData nullable: true, maxSize: 1 * 1024 * 1024 //1MB
        mediaItem nullable: false
    }
}
