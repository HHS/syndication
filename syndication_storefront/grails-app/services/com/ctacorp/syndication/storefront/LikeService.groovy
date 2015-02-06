package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.authentication.User
import grails.transaction.Transactional

@Transactional
class LikeService {
    def springSecurityService

    MediaItem likeMedia(Long mediaId) {
        def user = springSecurityService.currentUser as User
        MediaItem mi = MediaItem.get(mediaId)
        if(!mi){
            log.error "Tried to like media that doesn't exist: [${mediaId}]"
            return null
        }
        user.addToLikes(mi)
        mi
    }

    MediaItem undoLikeMedia(Long mediaId){
        def user = springSecurityService.currentUser as User
        MediaItem mi = MediaItem.get(mediaId)
        if(!mi){
            log.error "Tried to like media that doesn't exist: [${mediaId}]"
            return null
        }
        if(!user.getLikes().contains(mi))
        {
            log.error "Tried to undoLike on media that hasn't been liked."
            return null
        }
        user.removeFromLikes(mi)
        mi
    }

    @Transactional(readOnly = true)
    def getLikeCount(Long mediaId){
        User.createCriteria().get{
            likes{
                idEq mediaId
            }
            projections{
                rowCount()
            }
        }
    }

    Map getAllMediaLikeInfo(def mediaItems){
        User currentUser = springSecurityService.currentUser as User
        boolean alreadyLiked = false
        Map mediaLikeInfo = [:]

        mediaItems.each{ MediaItem item ->
            if(currentUser) {
                alreadyLiked = currentUser.likes.contains(item)
            }
            mediaLikeInfo["${item.id}"] = [likeCount:getLikeCount(item.id), alreadyLiked: alreadyLiked]
        }
        mediaLikeInfo
    }

    Map getAllMediaLikeInfoFromJson(def mediaItems){
        User currentUser = springSecurityService.currentUser as User
        boolean alreadyLiked = false
        Map mediaLikeInfo = [:]

        mediaItems.each{item ->
            if(currentUser) {
                alreadyLiked = currentUser.likes.contains(MediaItem.findById(item.id))
            }
            mediaLikeInfo["${item.id}"] = [likeCount:getLikeCount(item.id), alreadyLiked: alreadyLiked]
        }
        mediaLikeInfo
    }
}
