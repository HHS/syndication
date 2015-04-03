package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional

@Transactional
class MediaListService {

    boolean addMediaToMediaList(Long mediaId, Long mediaListId) {
        MediaItem mediaItem = MediaItem.get(mediaId)
        UserMediaList userMediaList = UserMediaList.get(mediaListId)
        if(mediaItem && userMediaList){
            userMediaList.addToMediaItems(mediaItem)
            if(mediaItem in userMediaList.mediaItems){
                return true
            }
        }
        false
    }
}

