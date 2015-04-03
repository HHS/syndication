package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional

@Transactional
class FeaturedMediaService {

    def featureMedia(Long mediaId) {
        def mi = MediaItem.get(mediaId)
        if(!mi){ return null }

        def featured = FeaturedMedia.findByMediaItem(mi)
        if(featured){ return featured }

        new FeaturedMedia(mediaItem: mi).save()
    }

    void unfeatureMedia(Long mediaId){
        def mi = MediaItem.get(mediaId)
        if(!mi){ return }

        def featured = FeaturedMedia.findByMediaItem(mi)
        if(!featured){ return }

        featured.delete()
    }

    void clear(){
        FeaturedMedia.where{
            id > 0L
        }.deleteAll()
    }

    def listFeatured(params = [:]){
        FeaturedMedia.list(params)*.mediaItem
    }
}
