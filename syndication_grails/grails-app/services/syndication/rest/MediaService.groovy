
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import com.ctacorp.syndication.commons.util.Hash
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import com.ctacorp.syndication.*
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import com.ctacorp.syndication.exception.*
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

@Transactional
class MediaService {
    def tagsService
    def tinyUrlService
    def contentRetrievalService
    def youtubeService
    def solrIndexingService
    def cmsManagerService
    def grailsApplication

    @Transactional(readOnly = true)
    def getMediaItem(Long id){
        MediaItem.get(id)
    }

    def saveAudio(Audio audioInstance) {
        if (!audioInstance.validate()) {
            return audioInstance
        }

        audioInstance.save(flush: true)
        createOrUpdateMediaItemSubscriber(audioInstance)
        audioInstance
    }

    def saveCollection(Collection collectionInstance) {
        if (!collectionInstance.validate()) {
            return collectionInstance
        }
        collectionInstance.save(flush: true)
        createOrUpdateMediaItemSubscriber(collectionInstance)
        collectionInstance
    }

    Html saveHtml(Html htmlInstance, params) throws ContentNotExtractableException, ContentUnretrievableException{
        //Extract the content (or try to)
        String content = contentRetrievalService.extractSyndicatedContent(htmlInstance.sourceUrl, params)
        if (!content) { //Extraction failed
            log.error("Could not extract content, page was found but not marked up at url: ${htmlInstance.sourceUrl}")
            throw new ContentNotExtractableException("Could not extract content, page was found but not marked up at url: ${htmlInstance.sourceUrl}")
        } else { // Content extracted fine, can we save?
            htmlInstance.hash = Hash.md5(content)                   //rehash content

            //Try to add a meaningful description if one isn't provided
            if(!htmlInstance.description){
                try {
                    String desc = contentRetrievalService.getDescriptionFromContent(content, htmlInstance.sourceUrl)
                    if(desc){
                        htmlInstance.description = desc
                    }
                } catch(e){
                    log.error(e)
                }
            }

            htmlInstance = createOrUpdateMediaItem(htmlInstance, content,{ MediaItem item -> MediaItem.findBySourceUrl(item.sourceUrl) })      //load or update and existing record if it exists
            createOrUpdateMediaItemSubscriber(htmlInstance)
        }
        htmlInstance
    }

    Periodical savePeriodical(Periodical periodicalInstance){
        String content = contentRetrievalService.extractSyndicatedContent(periodicalInstance.sourceUrl)
        if (!content) { //Extraction failed
            log.error("Could not extract content, page was found but not marked up at url: ${periodicalInstance.sourceUrl}")
            throw new ContentNotExtractableException("Could not extract content, page was found but not marked up at url: ${periodicalInstance.sourceUrl}")
        } else { // Content extracted fine, can we save?
            periodicalInstance.hash = Hash.md5(content)                   //rehash content

            //Try to add a meaningful description if one isn't provided
            if(!periodicalInstance.description){
                try {
                    String desc = contentRetrievalService.getDescriptionFromContent(content, periodicalInstance.sourceUrl)
                    if(desc){
                        periodicalInstance.description = desc
                    }
                } catch(e){
                    log.error(e)
                }
            }

            periodicalInstance = createOrUpdateMediaItem(periodicalInstance, content,{ MediaItem item -> MediaItem.findBySourceUrl(item.sourceUrl) })      //load or update and existing record if it exists
        }
        periodicalInstance
    }

    def saveImage(Image imageInstance) {
        imageInstance = createOrUpdateMediaItem(imageInstance, { MediaItem image ->
            MediaItem.findBySourceUrl(image.sourceUrl)
        })      //load or update and existing record if it exists
        createOrUpdateMediaItemSubscriber(imageInstance)
        imageInstance
    }

    def saveInfographic(Infographic infographicInstance) {
        if (!infographicInstance.validate()) {
            return infographicInstance
        }
        infographicInstance.save(flush: true)
        createOrUpdateMediaItemSubscriber(infographicInstance)
        infographicInstance
    }

    def saveSocialMedia(SocialMedia socialMediaInstance) {
        if (!socialMediaInstance.validate()) {
            return socialMediaInstance
        }
        socialMediaInstance.save(flush: true)
        createOrUpdateMediaItemSubscriber(socialMediaInstance)
        socialMediaInstance
    }

    def saveVideo(Video videoInstance) {
        Video fromImport = youtubeService.getVideoInstanceFromUrl(videoInstance.sourceUrl)

        videoInstance.description = videoInstance.description ?: fromImport.description
        videoInstance.duration = videoInstance.duration ?: fromImport.duration
        videoInstance.externalGuid = videoInstance.externalGuid ?: fromImport.externalGuid
        videoInstance.name = videoInstance.name ?: fromImport.name

        videoInstance = createOrUpdateMediaItem(videoInstance, { MediaItem video ->
            String videoId = youtubeService.getVideoId(video.sourceUrl)
            MediaItem.sourceUrlContains(videoId).get()
        }) as Video      //load or update and existing record if it exists

        if (!videoInstance.validate()) {
            createOrUpdateMediaItemSubscriber(videoInstance)
            return videoInstance
        }
    }

    def saveWidget(Widget widgetInstance) {
        if (!widgetInstance.validate()) {
            return widgetInstance
        }
        widgetInstance.save(flush: true)
        createOrUpdateMediaItemSubscriber(widgetInstance)
        widgetInstance
    }

    private MediaItem createOrUpdateMediaItem(MediaItem item, String content = null, Closure mediaFinder){
        boolean savetoDB = true
        MediaItem existing = mediaFinder(item)
        if (existing) {
            def result = updateMediaItem(existing, item)
            // -- media type specific updates here --
            if (!result.changed) {
                log.info "Item not changed, skipping update ${result.updatedRecord.sourceUrl}"
                item = existing // if there were no changes, return the existing record instead
                savetoDB = false
            } else {
                log.info "Item changed, updating"
                item = result.updatedRecord
            }
        }
        if(savetoDB) {
            item.save(flush: true)
            log.info "media saved: ${item.id}"
            //TODO we need a trigger here to refresh hash, and thumbnails, expire cache - all that
            if (grailsApplication.config.syndication.solrService.useSolr) {
               solrIndexingService.inputMediaItem(item,content)
            }
        }

        item
    }

    def createOrUpdateMediaItemSubscriber(MediaItem mediaItem){
        GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
        def request = webUtils.getCurrentRequest()
        String[] apiKey = request.getHeader("Authorization").substring("syndication_api_key ".length()).split(':')
        def apiKeyLength = apiKey.length

        if (apiKeyLength != 2) {
            log.error("(${System.currentTimeMillis() as String}) api key is malformed")
            return null
        }

        def senderPublicKey = apiKey[0] as String
        Long subscriberId = cmsManagerService.getSubscriber(senderPublicKey)?.id as Long

        MediaItemSubscriber.findOrSaveBySubscriberIdAndMediaItem(subscriberId, mediaItem).save(flush: true)
    }

    @NotTransactional
    def getMetaDataTemplate(params, response) {
        def metaMap = apiResponseBuilderService.getMetaData(params, response)
        return metaMap
    }

    @Transactional(readOnly = true)
    def listRelatedMediaItems(params) {
        params.max = getMax(params)
        def media = tagsService.listRelatedMediaIds(params)
        return media
    }

    @Transactional(readOnly = true)
    def listMediaItems(params) {
        params.max = getMax(params)
        if (params.id) {
            return [MediaItem.get(params.id as Long)]
        }
        if (params.tagIds) {
            def mediaIds = tagsService.getMediaForTagIds(params.tagIds).join(",")
            if (!mediaIds) {
                return []
            }
            params.restrictToSet = mediaIds

        }

        def limits = [max: params.max, offset: params.offset]
        params['active'] = true
        def results = MediaItem.facetedSearch(params).list(limits)
        return results ?: []
    }

    @Transactional(readOnly = true)
    def listMediaItemsForCampaign(Long campaignId, params) {
        def pag = [max: params.max, offset: params.offset]
       MediaItem.facetedSearch(active:true, mediaForCampaign: [id:campaignId, sort:params.sort]).list(pag) ?: []
    }

    @NotTransactional
    def getTagsForMediaItemForAPIResponse(MediaItem item) {
        def tagInfo
        try {
            tagInfo = tagsService.getTagsForMediaId(item.id)
        } catch (e) {
            log.error("Tag service could not be reached - ${e}")
        }

        if (tagInfo instanceof org.codehaus.groovy.grails.web.json.JSONObject) {
            if (tagInfo.error) {
                return []
            }
        }
        tagInfo
    }

    @Transactional(readOnly = true)
    def getCollectionMediaItemsForAPIResponse(com.ctacorp.syndication.Collection collection) {
        def mediaItems = []
        collection.mediaItems.each { mi ->
            def tmi = MediaItem.get(mi.id)
            mediaItems << [id: mi.id, name: mi.name, mediaType: tmi.getClass().simpleName]
        }

        mediaItems
    }

    @NotTransactional
    def getTinyUrlInfoForMediaItemForAPIResponse(MediaItem item) {
        def tinyInfo = tinyUrlService.getMappingByMediaItemId(item.id)

        if (!tinyInfo) {
            tinyInfo = [tinyUrl: "Unavailable", tinyUrlToken: "Unavailable"]
        } else if (tinyInfo.error) {
            tinyInfo = [tinyUrl: "NotMapped", tinyUrlToken: "N/A"]
        }
        tinyInfo
    }

    @Transactional(readOnly = true)
    def getCampaignsForAPIResponse(MediaItem item) {
        def campaigns = []
        item.campaigns.each { Campaign c ->
            campaigns << [id: c.id, name: c.name]
        }
        campaigns
    }

    private static int getMax(params) {
        //TODO we need to define a global max somewhere, and load that here
        Math.min(params.int("max") ?: 20, 1000)
    }

    private Map updateMediaItem(MediaItem o, MediaItem u) { //original, updated
        boolean changed = false

        def mediaItemClass = new DefaultGrailsDomainClass(MediaItem)

        //Iterate over all MediaItem properties except oneToMany and ManyToMany
        mediaItemClass.getPersistentProperties().findAll{!it.isOneToMany() && !it.isManyToMany()}.each { p ->

            def oProp = o.properties[p.name]
            def uProp = u.properties[p.name]

            if( uProp && uProp != oProp) {
                o.properties[p.name] = uProp
                changed = true
            }
        }

        [changed: changed, updatedRecord: o]
    }
}
