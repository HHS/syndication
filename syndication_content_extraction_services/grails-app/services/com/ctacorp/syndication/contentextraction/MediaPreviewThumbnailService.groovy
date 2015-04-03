package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.media.Periodical
import com.ctacorp.syndication.media.Html

import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.preview.MediaThumbnail
import com.ctacorp.syndication.preview.MediaPreview
import grails.transaction.Transactional

class MediaPreviewThumbnailService {

    def grailsApplication
    def fileService
    def youtubeService
    def grailsLinkGenerator
    def nativeToolsService

    @Transactional
    def generate(MediaItem mi) {

        File fullRez

        switch(mi){
            case Periodical:
            case Html:
                fullRez = getFullRezHtml(mi)
                processFullRez(fullRez, mi)
                break
            case Image:
                fullRez = getFullRezImage(mi as Image)
                processFullRez(fullRez, mi)
                break
            case Infographic:
                fullRez = getFullRezImage(mi as Infographic)
                processFullRez(fullRez, mi)
                break
            case Video:
                try {
                    fullRez = getFullRezYoutube(mi as Video)
                    processFullRez(fullRez, mi)
                } catch(e){
                    log.error("There was an error extracting a youtube preview image.")
                }
                break
            default:
                "blah"
        }

        previewAndThumbnail(mi)
    }

    def previewAndThumbnail(MediaItem m) {
        return [preview: MediaPreview.findByMediaItem(m), thumbnail: MediaThumbnail.findByMediaItem(m)]
    }

    private getFullRezHtml(MediaItem mi){
        def windowSize = 640
        def extractedContentUrl = grailsApplication.config.syndication.serverUrl + "/api/v2/resources/media/${mi.id}/content?imageFloat=left&imageMargin=0,10,10,0"
        boolean failure = false
        try {
            if (!new URL(extractedContentUrl).text) {
                log.error "${mi.id} looks bad"
                failure = true
            }
        } catch(e){
            log.error "${mi.id} looks bad"
            failure = true
        }
        if(failure){
            extractedContentUrl = grailsLinkGenerator.resource(dir: 'images/defaultIcons/thumbnail', file: 'bad.png', absolute: true)
        }
        getImageOfHtml(extractedContentUrl, windowSize)
    }

    private File getImageOfHtml(String url, int width) {
        String tmpFileName = "${Hash.md5(url + System.nanoTime())}-full"
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/tmp/${tmpFileName}.png")

        if(fileService.fileCheck(dest)){
            return dest
        }

        String command = ""
        switch(grailsApplication.config.syndication.htmlRenderingEngine){
            case "cutycapt": command = "DISPLAY=:7 ${grailsApplication.config.cutycapt.location}/cutycapt --url=\"${url}\" --min-width=${width} --out=${dest.absolutePath}"; break;
            case "cutycaptMac": command = "${grailsApplication.config.cutycapt.location}/cutycapt --url=\"${url}\" --min-width=${width} --out=${dest.absolutePath}"; break; //cutycapt running on mac only
            default: command = "Missing or invalid html rendering engine specified"
        }

        nativeToolsService.exe(command)
        return dest
    }

    private getFullRezImage(image){
        if(image instanceof Image || image instanceof Infographic) {
            fileService.saveImage(image.sourceUrl)
        } else{
            log.error ("Tried to get full rez image of non-image type! id: ${image.id}, type: ${image.getClass()}")
        }
    }

    private getFullRezYoutube(Video v){
        return fileService.saveImage(youtubeService.thumbnailLinkForUrl(v.sourceUrl))
    }

    private processFullRez(File fullRez, MediaItem mi) {
        generateThumbnail(fullRez, mi)
        generatePreview(fullRez, mi)
        fullRez.delete()
    }

    private generateThumbnail(File original, MediaItem mi) {
        int width = 250
        int height = 188
        String name = "${Hash.md5(original.bytes)}-thumbnail"
        File thumbnailFile = new File("${grailsApplication.config.syndication.scratch.root}/preview/tmp/${name}.jpg")

        String command = "${grailsApplication.config.imageMagick.location}/convert ${original.absolutePath} -resize ${width}x${height}\\^ -gravity north -extent ${width}x${height} ${thumbnailFile.absolutePath}"
        nativeToolsService.exe(command)

        thumbnailFile = fileService.verify(thumbnailFile)
        def mediaThumbnail = MediaThumbnail.findByMediaItem(mi)
        if(thumbnailFile) {
            if (mediaThumbnail) {
                mediaThumbnail.imageData = thumbnailFile.bytes
            } else {
                mediaThumbnail = new MediaThumbnail(imageData: thumbnailFile.bytes, mediaItem: mi)
            }
            mediaThumbnail.save(flush: true)
            thumbnailFile.delete()
        }

    }

    private generatePreview(File original, MediaItem mi){
        int width = 1024
        int height = 768
        String name = "${Hash.md5(original.bytes)}-preview"
        File previewFile = new File("${grailsApplication.config.syndication.scratch.root}/preview/tmp/${name}.jpg")

        String command = "${grailsApplication.config.imageMagick.location}/convert ${original.absolutePath} -resize ${width}x${height}\\^ -gravity north -extent ${width}x${height} ${previewFile.absolutePath}"
        nativeToolsService.exe(command)

        previewFile = fileService.verify(previewFile)
        def mediaPreview = MediaPreview.findByMediaItem(mi)
        if(previewFile) {
            if (mediaPreview) {
                mediaPreview.imageData = previewFile.bytes
            } else {
                mediaPreview = new MediaPreview(imageData: previewFile.bytes, mediaItem: mi)
            }
            mediaPreview.save(flush: true)
            previewFile.delete()
        }
    }

}
