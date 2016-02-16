package com.ctacorp.syndication

import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Video

import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.health.HealthReport
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.plugins.rest.client.RestBuilder
import grails.transaction.NotTransactional
import grails.transaction.Transactional

@Transactional
class MediaValidationService {
    def grailsApplication
    private RestBuilder rest
    def OK_STATUSES = [200, 301, 302]
    def messageSource
    def mediaItemsService
    def springSecurityService

    HealthReport performValidation(Long mediaId) {
        MediaItem mediaItem = MediaItem.get(mediaId)
        if(!mediaItem || !mediaItem.active) {
            log.error "Tried to validate a media item that doesn't exist ${mediaId} or is inactive."
            return null
        }

        processValidation(mediaItem)
    }

    @NotTransactional
    def fullHealthScan(subscriberId = null){
        def ids
        if(subscriberId){
            ids = mediaItemsService.getPublisherMediaIds(subscriberId)
        } else{
            ids = mediaItemsService.allMediaIds
        }
        def badItems = []
        ids.each{ id ->
            def healthReport = performValidation(id)
            if(!healthReport.valid){
                flagMediaItem(healthReport)
                badItems << healthReport
            } else{
                removeFlag(id)
            }
        }
        badItems
    }
    
    @Transactional(readOnly = true)
    def getFlaggedMedia(){
        FlaggedMedia.findAllNotIgnored([sort:'failureType', order: 'ASC'])
    }
    
    @Transactional(readOnly = true)
    def getPublisherFlaggedMedia(subscriberId){
        FlaggedMedia.findAllByMediaItemInList(MediaItemSubscriber.findAllBySubscriberId(subscriberId).mediaItem)
    }

    def rescanItem(Long mediaId){
        HealthReport report = performValidation(mediaId)
        if(report != null && !report.valid){
            flagMediaItem(report)
        } else{
            removeFlag(mediaId)
        }
        report
    }

    def removeFlag(Long mediaId){
        MediaItem mi = MediaItem.load(mediaId)
        if(!mi) { return null }
        def flaggedItem = FlaggedMedia.findByMediaItem(mi)
        if(flaggedItem){
            flaggedItem.delete(flush:true)
        }
    }

    def flagMediaItem(HealthReport report){
        if(report.valid){
            return
        }
        def mediaItem = MediaItem.load(report.mediaId)
        def alreadyFlagged = FlaggedMedia.findByMediaItem(mediaItem)
        if(!alreadyFlagged){
            String reason = (report.statusCode ? "[${report.statusCode}] " : "") + messageSource.getMessage("healthReport.failure.${report.failureType}" as String, null, Locale.US)
            def flaggedItem = new FlaggedMedia(mediaItem:mediaItem, message:reason, failureType: report.failureType)
            flaggedItem.save(flush:true)
        } else if(!alreadyFlagged.ignored){
            alreadyFlagged.dateFlagged = new Date()
            alreadyFlagged.failureType = report.failureType
            alreadyFlagged.message = (report.statusCode ? "[${report.statusCode}] " : "") + messageSource.getMessage("healthReport.failure.${report.failureType}" as String, null, Locale.US)
            alreadyFlagged.save(flush:true)
        }
    }

    private HealthReport processValidation(MediaItem mi){
        rest = new RestBuilder()
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        def sourceUrlCode = null
        try{
            sourceUrlCode = rest.head(URI.decode(mi.sourceUrl)).getStatus()
        }catch(error){
            return new HealthReport(mediaId: mi.id, statusCode: 404, details:error, failureType: FlaggedMedia.FailureType.UNREACHABLE)
        }
        
        def thumbnail = MediaThumbnail.findByMediaItem(mi)
        def preview = MediaPreview.findByMediaItem(mi)

        if(sourceUrlCode in OK_STATUSES) {
            switch(mi) {
                case Video:
                    def youtubeMetaData = fetchVideoMetadata(mi)
                    def errorCode = youtubeMetaData?.json?.results[0]?.error?.code
                    if(!youtubeMetaData || (errorCode && !(errorCode in OK_STATUSES))){
                        return new HealthReport(mediaId: mi.id, statusCode: 404, details:youtubeMetaData?.json?.results[0]?.error?.message, failureType: FlaggedMedia.FailureType.UNREACHABLE)
                    }
                    if(!(youtubeMetaData.status in OK_STATUSES)){
                        return new HealthReport(mediaId: mi.id, statusCode: youtubeMetaData.status, FlaggedMedia.FailureType.SERVER_ERROR)
                    }
                    if(!thumbnail || !preview){
                        return new HealthReport(mediaId: mi.id, failureType: FlaggedMedia.FailureType.NO_PREVIEW_THUMBNAIL)
                    }
                    return new HealthReport(mediaId: mi.id, valid: true)
                case Image:
                case Infographic:
                    if(!thumbnail || !preview){
                        return new HealthReport(mediaId: mi.id, failureType: FlaggedMedia.FailureType.NO_PREVIEW_THUMBNAIL)
                    }
                    return new HealthReport(mediaId: mi.id, valid: true)
                case Html:
                    def mediaContent = fetchContent(mi)
                    if(!(mediaContent.status in OK_STATUSES)){
                        switch (mediaContent.status){
                            case 404:
                                return new HealthReport(mediaId: mi.id, statusCode: 404, failureType: FlaggedMedia.FailureType.UNREACHABLE)
                            case 500:
                                if(mediaContent.json){
                                    return new HealthReport(mediaId: mi.id, statusCode: 500, details: mediaContent.json?.meta?.messages[0]?.errorMessage, failureType: FlaggedMedia.FailureType.UNEXTRACTABLE)
                                }
                                return new HealthReport(mediaId: mi.id, statusCode: 500, failureType: FlaggedMedia.FailureType.SERVER_ERROR)
                            default:
                                return new HealthReport(mediaId: mi.id, statusCode: mediaContent.status, failureType: FlaggedMedia.FailureType.SERVER_ERROR)
                        }
                    }

                    if(mediaContent.text.size() < 600){
                        return new HealthReport(mediaId: mi.id, failureType: FlaggedMedia.FailureType.SHORT_CONTENT)
                    }

                    if(!thumbnail || !preview){
                        return new HealthReport(mediaId: mi.id, failureType: FlaggedMedia.FailureType.NO_PREVIEW_THUMBNAIL)
                    }
                    return new HealthReport(mediaId: mi.id, valid: true)
                default:
                    return new HealthReport(mediaId: mi.id, valid: true)
            }
        } else{
            switch(sourceUrlCode){
                case 500:
                    return new HealthReport(mediaId: mi.id, statusCode: 500, failureType: FlaggedMedia.FailureType.SERVER_ERROR)
                case 404:
                    return new HealthReport(mediaId: mi.id, statusCode: 404, failureType: FlaggedMedia.FailureType.UNREACHABLE)
                default:
                    return new HealthReport(mediaId: mi.id, statusCode: sourceUrlCode, failureType: FlaggedMedia.FailureType.SERVER_ERROR)
            }
        }
    }

    private getImageData(url){
        new URL(url).bytes
    }

    private fetchContent(MediaItem mi) {
        String apiUrl = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        def mediaContent
        try {
            rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
            mediaContent = rest.get("${apiUrl}/resources/media/${mi.id}/syndicate.html")
        }catch(org.springframework.web.client.ResourceAccessException e){
            log.error "Syndication api server could not be reached!\n${e}"
            return null
        }catch(e){
            log.error "An unhandeled error has occured trying to get content from syndication server\n${e}"
            return null
        }
        mediaContent
    }

    private fetchVideoMetadata(MediaItem mi) {
        String apiUrl = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        def youtubeMetaData
        try {
            rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
            youtubeMetaData = rest.get("${apiUrl}/resources/media/${mi.id}/youtubeMetaData.json")
        }catch(org.springframework.web.client.ResourceAccessException e){
            log.error "Syndication api server could not be reached!\n${e}"
        }catch(e){
            log.error "An unhandeled error has occured trying to get content from syndication server\n${e}"
        }
        youtubeMetaData
    }
}
