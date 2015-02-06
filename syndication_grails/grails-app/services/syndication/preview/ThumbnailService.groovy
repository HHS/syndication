
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.preview

import com.ctacorp.syndication.*

class ThumbnailService {
    static transactional = false
    def htmlPreviewService
    def imageMagickService
    def grailsApplication
    def fileService
    def youtubeService
    def grailsLinkGenerator

    def getThumbnailUrl(Long id){
        grailsApplication.config.syndication.serverUrl + "/api/v2/resources/media/${id}/thumbnail.jpg"
    }

    def getThumbnail(Long id){
        MediaItem mi = MediaItem.get(id)
        File fullRez

        def f = new File("")
        println f.absolutePath

        switch(mi){
            case Html:
                fullRez = getFullRezHtml(mi as Html)
                break
            case Image:
                fullRez = getFullRezImage(mi as Image)
                break
            case Infographic:
                fullRez = getFullRezImage(mi as Infographic)
                break
            case Video:
                fullRez = getFullRezYoutube(mi as Video)
                break
            case com.ctacorp.syndication.Collection:
                grailsApplication.mainContext.getResource("assets/images/defaultIcons/thumbnail/collection.png")
                return new File("assets/images/defaultIcons/thumbnail/collection.png")
                break
            case SocialMedia:
                return new File("assets/images/defaultIcons/thumbnail/social.png")
                break
            default:
                return new File("assets/images/defaultIcons/thumbnail/unknown.png")
        }

        imageMagickService.thumbnailCropped(fullRez)
    }

    String getDefaultCollectionThumbnail(){
        return grailsLinkGenerator.asset(file:'defaultIcons/thumbnail/collection.png', absolute: true)
    }

    String getDefaultSocialThumbnail(){
        return grailsLinkGenerator.asset(file:'defaultIcons/thumbnail/social.png', absolute: true)
    }

    String getDefaultUnkownThumbnail(){
        return grailsLinkGenerator.asset(file:'defaultIcons/thumbnail/unknown.png', absolute: true)
    }

    private getFullRezHtml(Html html){
        def windowSize = grailsApplication.config.syndication.preview.browserWindowSize
        def extractedContentUrl = grailsApplication.config.grails.serverURL + "/api/v2/resources/media/${html.id}/content?imageFloat=left&imageMargin=0,10,10,0"
        if(! new URL(extractedContentUrl).text){
            println "${html.id} looks bad"
            extractedContentUrl = grailsLinkGenerator.resource(dir:'images/defaultIcons/thumbnail', file:'bad.png', absolute: true)
        }
        htmlPreviewService.getImageOfHtml(extractedContentUrl, windowSize)
    }

    private getFullRezImage(image){
        if(image instanceof Image || image instanceof Infographic) {
            fileService.saveImage(image.sourceUrl)
        } else{
            log.error ("Tried to get full rez image of non-image type! id: ${image.id}, type: ${image.getClass()}")
        }
    }

    private getFullRezYoutube(Video v){
        fileService.saveImage(youtubeService.thumbnailLinkForUrl(v.sourceUrl))
    }
}
