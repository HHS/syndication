
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.metrics

import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional
import com.ctacorp.syndication.authentication.User

@Transactional
class LikeService {
    def springSecurityService

    def like(Long mediaId) {
        MediaItem mediaItem = MediaItem.load(mediaId)
        if(springSecurityService.isLoggedIn()){
            User currentUser = springSecurityService.getCurrentUser()
            currentUser.addToLikes(mediaItem)
            return [likes:likeCountForMedia()]
        } else{
            return [error:"User not logged in"]
        }
    }

    def unlike(Long mediaId){
        MediaItem mediaItem = MediaItem.get(mediaId)
        if(springSecurityService.isLoggedIn()){
            User currentUser = springSecurityService.getCurrentUser()
            currentUser.removeFromLikes(mediaItem)
            return [likes:likeCountForMedia()]
        } else{
            return [error:"User not logged in"]
        }
    }

    @Transactional(readOnly = true)
    long likeCountForMedia(Long id){
        def cri = User.createCriteria()
        def count = cri.count{
            likes{
                idEq id
            }
        }
        count
    }

    @Transactional(readOnly = true)
    def mostPopular(params = null){
        int max = params?.int('max') ?: 20
        int offset = params?.int('offset') ?: 0
        ArrayList<Long> mediaIds = User.executeQuery("select likes.id from User user inner join user.likes likes group by likes.id order by count(likes.id) desc", [max:max, offset:offset])
        if(mediaIds) {
            mediaIds = mediaIds.findAll{
                MediaItem.findById(it).active == true
            }
            return MediaItem.getAll(mediaIds)
        }
        mediaIds
    }
}
