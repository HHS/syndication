
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional
import com.ctacorp.syndication.AlternateImage

@Transactional
class AlternateImagesService {

    def save(AlternateImage imageInstance, Long mediaId) {
        MediaItem mi = MediaItem.load(mediaId)
        def existingImageInstance = AlternateImage.findByNameAndMediaItem(imageInstance.name,mi)
        if(existingImageInstance) {
            existingImageInstance.width = imageInstance.width
            existingImageInstance.height = imageInstance.height
            existingImageInstance.url = imageInstance.url
            existingImageInstance.save(flush: true)
        }else {
            imageInstance.mediaItem = mi
            imageInstance.save(flush: true)
            if (imageInstance.id) {
                mi.addToAlternateImages(imageInstance)
            } else {
                return null
            }
        }
        imageInstance
    }

    @Transactional(readOnly = true)
    def listAlternateImages(params) {
        if (params.id) {
            return [AlternateImage.get(params.id)]
        }
        if (params.url) {
            return AlternateImage.findAllByUrl(params.url)
        }
        if (params.name) {
            return AlternateImage.findAllByName(params.name)
        }
    }

    @Transactional(readOnly = true)
    def show(Long id) {
        AlternateImage.get(id)
    }


}
