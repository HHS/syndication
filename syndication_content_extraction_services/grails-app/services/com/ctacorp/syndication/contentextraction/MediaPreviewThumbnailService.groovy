package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.media.Periodical
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Tweet

import com.ctacorp.syndication.preview.MediaThumbnail
import com.ctacorp.syndication.preview.MediaPreview
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import grails.util.Holders
import org.apache.commons.io.IOUtils

@Transactional
class MediaPreviewThumbnailService {
    def grailsApplication

    def config = Holders.config

    def generate(MediaItem mi) {
        def htmlUrl = grailsApplication.config.syndication.serverUrl + "/api/v2/resources/media/${mi.id}/content?imageFloat=left&imageMargin=0,10,10,0"
        def imageAndVideoUrl = "${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/${mi.id}/syndicate.html?thumbnailGeneration=1"

        switch(mi){
            case Periodical:
            case Html:
                savePreview(mi, generatePreview(htmlUrl))
                saveThumbnail(mi, generateThumbnail(htmlUrl, ".5"))
                break
            case Image:
            case Infographic:
            case Video:
                savePreview(mi, generatePreview(imageAndVideoUrl))
                saveThumbnail(mi, generateThumbnail(imageAndVideoUrl))
                break
            case Tweet:
                savePreview(mi, generatePreview(imageAndVideoUrl))
                saveThumbnail(mi, generateThumbnail(imageAndVideoUrl))
                break
            default:
                log.error("Tried to generate thumbnail for an unsupported media type: ${mi}")
        }

        if(mi.customPreviewUrl) {
            saveCustomPreview(mi, new URL(mi.customPreviewUrl).openStream())
        } else {
            MediaPreview mt = MediaPreview.findByMediaItem(mi)
            if(mt){
                mt.customImageData = null
            }
        }

        if(mi.customThumbnailUrl) {
            saveCustomThumbnail(mi, new URL(mi.customThumbnailUrl).openStream())
        } else {
            MediaThumbnail mt = MediaThumbnail.findByMediaItem(mi)
            if(mt){
                mt.customImageData = null
            }
        }

        previewAndThumbnail(mi)
    }

    @NotTransactional
    String getThumbnailUrl(Long id){
        grailsApplication.config.syndication.contentExtraction.urlBase + "/${id}/thumbnail.jpg"
    }

    @Transactional(readOnly = true)
    def previewAndThumbnail(MediaItem m) {
        return [preview: MediaPreview.findByMediaItem(m), thumbnail: MediaThumbnail.findByMediaItem(m)]
    }

    private InputStream generateThumbnail(String sourceUrl, String scale = "1") {
        int width = 250
        int height = 188

        String manetCommand = "${config?.manet?.server?.url ?: 'http://localhost:8891'}/" +
                "?url=${URLEncoder.encode(sourceUrl, "UTF-8")}" +
                "&format=jpg" +
                "&width=${width+1}" +
                "&height=${height+1}" +
                "&quality=1" +
                "&zoom=${scale}" +
                "&clipRect=1%2C1%2C${width}%2C${height}"
        try {
            return new URL(manetCommand).openStream()
        } catch(e){
            throw new Exception("Manet service couldn't be reached, tried command: ${manetCommand}")
        }
        null
    }

    private generatePreview(String sourceUrl){
        int width = 1024
        int height = 768
        String scale = '1'

        String manetCommand = "${config?.manet?.server?.url ?: 'http://localhost:8891'}/" +
                "?url=${URLEncoder.encode(sourceUrl, "UTF-8")}" +
                "&format=jpg" +
                "&width=${width+1}" +
                "&height=${height+1}" +
                "&zoom=${scale}" +
                "&quality=1" +
                "&clipRect=1%2C1%2C${width}%2C${height}"
        try {
            return new URL(manetCommand).openStream()
        } catch(e){
            throw new Exception("Manet service couldn't be reached, tried command: ${manetCommand}")
        }
        null
    }

    protected saveThumbnail(MediaItem mi, InputStream is){
        Byte[] imageData = IOUtils.toByteArray(is);
        MediaThumbnail mt = MediaThumbnail.findByMediaItem(mi)
        if(mt){
            mt.imageData = imageData
        } else{
            mt = new MediaThumbnail(mediaItem: mi, imageData:imageData)
        }
        def result = mt.save(flush:true)
        if(!result){
            log.error("Error saving thumbnail: ${mt.errors}")
        }
    }

    protected saveCustomThumbnail(MediaItem mi, InputStream is){
        Byte[] imageData = IOUtils.toByteArray(is);
        MediaThumbnail mt = MediaThumbnail.findByMediaItem(mi)
        if(mt){
            mt.customImageData = imageData
        } else{
            mt = new MediaThumbnail(mediaItem: mi, customImageData:imageData)
        }
        def result = mt.save(flush:true)
        if(!result){
            log.error("Error saving thumbnail: ${mt.errors}")
        }
    }

    protected savePreview(MediaItem mi, InputStream is){
        Byte[] imageData = IOUtils.toByteArray(is);
        MediaPreview mt = MediaPreview.findByMediaItem(mi)
        if(mt){
            mt.imageData = imageData
        } else{
            mt = new MediaPreview(mediaItem: mi, imageData:imageData)
        }
        def result = mt.save(flush:true)
        if(!result){
            log.error("Error saving thumbnail: ${mt.errors}")
        }
    }

    protected saveCustomPreview(MediaItem mi, InputStream is){
        Byte[] imageData = IOUtils.toByteArray(is);
        MediaPreview mt = MediaPreview.findByMediaItem(mi)
        if(mt){
            mt.customImageData = imageData
        } else{
            mt = new MediaPreview(mediaItem: mi, customImageData:imageData)
        }
        def result = mt.save(flush:true)
        if(!result){
            log.error("Error saving thumbnail: ${mt.errors}")
        }
    }
}
