package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.PDF
import com.ctacorp.syndication.media.QuestionAndAnswer
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Tweet

import com.ctacorp.syndication.preview.MediaThumbnail
import com.ctacorp.syndication.preview.MediaPreview
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import grails.util.Holders
import grails.io.IOUtils

@Transactional
class MediaPreviewThumbnailService {
    def assetResourceLocator

    def config = Holders.config

    def generate(Long mediaId) {
        MediaItem mi = MediaItem.read(mediaId)
        if(!mi){
            log.error("Tried to load a media item that doesn't exist: ${mediaId}")
            return null
        }
        def htmlUrl = Holders.config.API_SERVER_URL + "/api/v2/resources/media/${mi.id}/content?imageFloat=left&imageMargin=0,10,10,0"
        def imageAndVideoUrl = "${Holders.config.API_SERVER_URL}/api/v2/resources/media/${mi.id}/syndicate.html"

        switch(mi){
            case Html:
                savePreview(mi, generatePreview(htmlUrl))
                saveThumbnail(mi, generateThumbnail(htmlUrl, ".5"))
                break
            case Collection:
                savePreview(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream)
                saveThumbnail(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream)
                break
            case PDF:
                savePreview(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream)
                saveThumbnail(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream)
                break
            case FAQ:
                savePreview(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream)
                saveThumbnail(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream)
                break
            case QuestionAndAnswer:
                savePreview(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream)
                saveThumbnail(mi, assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream)
                break
            case Image:
            case Infographic:
            case Video:
            case Tweet:
                savePreview(mi, generatePreview(imageAndVideoUrl  + "?previewGeneration=1"))
                saveThumbnail(mi, generateThumbnail(imageAndVideoUrl + "?thumbnailGeneration=1"))
                break
            default:
                log.error("Tried to generate thumbnail for an unsupported media type: ${mi.getClass()}")
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
        "${Holders.config.API_SERVER_URL}/api/v2/resources/media/${id}/thumbnail.jpg"
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
        Byte[] imageData = IOUtils.copyToByteArray(is);
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
        Byte[] imageData = IOUtils.copyToByteArray(is);
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
        Byte[] imageData = IOUtils.copyToByteArray(is);
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
        Byte[] imageData = IOUtils.copyToByteArray(is);
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
