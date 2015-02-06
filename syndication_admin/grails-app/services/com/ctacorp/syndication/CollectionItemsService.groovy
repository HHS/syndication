package com.ctacorp.syndication

import grails.transaction.Transactional

@Transactional
class CollectionItemsService {

    def handleMediaItems(Collection collection, params = [:]) {
        def mediaItems = []
        mediaItems.addAll(collection.mediaItems)
        if (mediaItems) {
            mediaItems.each { it ->
                collection.removeFromMediaItems(it)
            }
        }
        params.mediaItems.each { it ->
            def mi = MediaItem.get(it)
            collection.addToMediaItems(mi)
        }
    }




}
