
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.commons.util.Util
import com.ctacorp.syndication.data.CampaignHolder
import com.ctacorp.syndication.data.SourceHolder
import com.ctacorp.syndication.data.TagHolder
import com.ctacorp.syndication.marshal.QuestionAndAnswerMarshaller
import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.storefront.UserMediaList
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import com.ctacorp.syndication.*
import grails.util.Holders
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import com.ctacorp.syndication.exception.*
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import com.ctacorp.syndication.exception.UnauthorizedException
import org.hibernate.NonUniqueResultException

import java.security.MessageDigest
import java.util.concurrent.Callable

@Transactional
class MediaService {
    def tagsService
    def tinyUrlService
    def contentRetrievalService
    def youtubeService
    def solrIndexingService
    def cmsManagerService
    def grailsApplication
    def analyticsService
    def guavaCacheService
    def contentCacheService
    def groovyPageRenderer
    def mediaService
    def assetResourceLocator
    def aws

    @NotTransactional
    String renderHtml(Html html, params){
        String extractedContent = getExtractedContent(html, params)
        groovyPageRenderer.render(template: "/media/htmlView", model: [extractedContent:extractedContent])
    }

    String renderImage(Image img, params){
        boolean thumbnailGeneration = Util.isTrue(params.thumbnailGeneration)
        boolean previewGeneration = Util.isTrue(params.previewGeneration)

        String content = groovyPageRenderer.render(template: "/media/imageView", model:[img:img, thumbnailGeneration:thumbnailGeneration, previewGeneration:previewGeneration])
        content = contentRetrievalService.wrapWithSyndicateDiv(content)
        if(!thumbnailGeneration){
            content = contentRetrievalService.addAttributionToExtractedContent(img.id, content)
            content += analyticsService.getGoogleAnalyticsString(img, params)
        }
        content
    }

    String renderInfographic(Infographic infographic, params){
        boolean thumbnailGeneration = Util.isTrue(params.thumbnailGeneration)
        boolean previewGeneration = Util.isTrue(params.previewGeneration)
        String content = groovyPageRenderer.render(template: "/media/infographicView", model:[infographic:infographic, thumbnailGeneration:thumbnailGeneration, previewGeneration:previewGeneration])
        content = contentRetrievalService.wrapWithSyndicateDiv(content)

        if(!thumbnailGeneration){
            content = contentRetrievalService.addAttributionToExtractedContent(infographic.id, content)
            content += analyticsService.getGoogleAnalyticsString(infographic, params)
        }
        content
    }

    String renderVideo(Video video, params){
        boolean thumbnailGeneration = Util.isTrue(params.thumbnailGeneration)
        boolean previewGeneration = Util.isTrue(params.previewGeneration)
        String player = youtubeService.getIframeEmbedCode(video.sourceUrl, params)
        String thumbnail = youtubeService.thumbnailLinkForUrl(video.sourceUrl)
        String content = groovyPageRenderer.render(template: "/media/youtubeView", model:[thumbnailGeneration:thumbnailGeneration, previewGeneration:previewGeneration, player:player, thumbnail:thumbnail])

        if(!thumbnailGeneration){
            content = contentRetrievalService.wrapWithSyndicateDiv(content)
            content = contentRetrievalService.addAttributionToExtractedContent(video.id, content)
        }
        content
    }

    String renderPdf(PDF pdf, params){
        def urlHash = MessageDigest.getInstance("MD5").digest(pdf.sourceUrl.bytes).encodeHex().toString()

        String content = groovyPageRenderer.render(template: "/media/pdfView", model: [pdf:pdf, s3Url:aws.s3().on("syndication-files").publicUrlFor(1.hour, "pdf-files/"+urlHash + ".pdf")])
        content = contentRetrievalService.wrapWithSyndicateDiv(content, pdf)
        content = contentRetrievalService.addAttributionToExtractedContent(pdf.id, content)
        content += analyticsService.getGoogleAnalyticsString(pdf, params)
        content
    }

    String renderTweet(Tweet tweet, params){
        boolean thumbnailGeneration = Util.isTrue(params.thumbnailGeneration)
        boolean previewGeneration = Util.isTrue(params.previewGeneration)

        String content = groovyPageRenderer.render(template: "/media/tweetView", model:[tweet:tweet, thumbnailGeneration:thumbnailGeneration, previewGeneration:previewGeneration, badImage:assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/twitter.jpg")])
        content = contentRetrievalService.wrapWithSyndicateDiv(content)
        if(!thumbnailGeneration){
            content = contentRetrievalService.addAttributionToExtractedContent(tweet.id, content)
            content += analyticsService.getGoogleAnalyticsString(tweet, params)
        }
        content
    }

    String renderUserMediaList(UserMediaList userMediaList, params){
        renderMediaList(userMediaList.mediaItems.sort{it.name}, params)
    }

    String renderCollection(com.ctacorp.syndication.media.Collection collection, params){
        params.iframeName = params.iframeName ?: collection.name
        params.id = collection.id
        params.mediaSource = "collection"
        def mediaItems
        if(Util.isTrue(params.ignoreHiddenMedia,false)){
            mediaItems = collection.mediaItems.findAll{it.visibleInStorefront}
        } else {
            mediaItems = collection.mediaItems
        }

        //Handle nested collections
        mediaItems = resolveNestedCollections(mediaItems)

        renderMediaList(mediaItems.sort{it.name}, params)
    }

    String renderFAQ(FAQ faq, params){
        params.iframeName = params.iframeName ?: faq.name
        params.id = faq.id
        params.mediaSource = "faq"

        String content = groovyPageRenderer.render(template: "/media/faqView", model:[faq:faq])
        content = contentRetrievalService.wrapWithSyndicateDiv(content)

        content = contentRetrievalService.addAttributionToExtractedContent(faq.id, content)
        content += analyticsService.getGoogleAnalyticsString(faq, params)

        content
    }

    String renderQuestionAndAnswer(QuestionAndAnswer questionAndAnswer, params){
        boolean thumbnailGeneration = Util.isTrue(params.thumbnailGeneration)
        boolean previewGeneration = Util.isTrue(params.previewGeneration)

        String content = groovyPageRenderer.render(template: "/media/questionAndAnswerView", model:[questionAndAnswer:questionAndAnswer, thumbnailGeneration:thumbnailGeneration, previewGeneration:previewGeneration, badImage:assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg")])
        content = contentRetrievalService.wrapWithSyndicateDiv(content)
        if(!thumbnailGeneration){
            content = contentRetrievalService.addAttributionToExtractedContent(questionAndAnswer.id, content)
            content += analyticsService.getGoogleAnalyticsString(questionAndAnswer, params)
        }
        content
    }

    private resolveNestedCollections(initialList){
        def items = []
        def collectionsAlreadyChecked = []
        items = resolveNestedCollectionsHelper(initialList, collectionsAlreadyChecked, items)
        items
    }

    private resolveNestedCollectionsHelper(initialList, collectionsAlreadyChecked, items){
        initialList.each{ mediaItem ->
            //If it isn't a collection and it isn't already in the list, add it
            if(!(mediaItem instanceof com.ctacorp.syndication.media.Collection) && !(items.contains(mediaItem))){
                items << mediaItem
            } else if(!(mediaItem in collectionsAlreadyChecked) && mediaItem instanceof com.ctacorp.syndication.media.Collection){ //it's a nested collection, if it hasn't already been checked
                collectionsAlreadyChecked << mediaItem
                resolveNestedCollectionsHelper(mediaItem.mediaItems, collectionsAlreadyChecked, items)
            }
        }
        items
    }

    String renderMediaForTag(Long id, params){
        params.id = id
        params.max = 50
        def mediaItems = tagsService.getMediaForTagId(params) ?: []
        renderMediaList(mediaItems, params)
    }

    String renderMediaForCampaign(Long id, params){
        params.id = id
        params.max = 50
        def mediaItems = Campaign.get(id).mediaItems
        renderMediaList(mediaItems, params)
    }

    String renderMediaForSource(Source source, params){
        params.max = params.max ?: 50
        params.sort = params.sort ?: "name"
        params.order = params.order ?: "desc"
        def mediaItems = MediaItem.findAllBySource(source, params)
        renderMediaList(mediaItems, params)
    }

    String renderMediaViewerSnippet(mediaSource, params){
        String url
        switch(mediaSource){
            case UserMediaList:
                url = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath +"/resources/userMediaLists/${mediaSource.id}/syndicate.html?${mediaService.getExtractionParams(params)}"
                break
            case TagHolder:
                url = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath +"/resources/tags/${mediaSource.id}/syndicate.html?${mediaService.getExtractionParams(params)}"
                break;
            case SourceHolder:
                url = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath +"/resources/sources/${mediaSource.id}/syndicate.html?${mediaService.getExtractionParams(params)}"
                break;
            case CampaignHolder:
                url = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath +"/resources/campaigns/${mediaSource.id}/syndicate.html?${mediaService.getExtractionParams(params)}"
                break;
            default:
                url = grailsApplication.config.syndication.contentExtraction.urlBase + "/${mediaSource.id}/syndicate.html?${mediaService.getExtractionParams(params)}"
        }
        String width = params.width ?: "775"
        String height = params.height ?: "650"
        String iframeName = params.iframeName ?: mediaSource.name
        "<iframe src=\"${url}\" width=\"${width}\" height=\"${height}\" name=\"${iframeName}\" scrolling=\"no\" frameborder=\"0\"></iframe>".encodeAsHTML()
    }

    String renderIframeSnippet(String url, params){
        url += "/syndicate.html?${mediaService.getExtractionParams(params)}"
        String width = params.width ?: "660"
        String height = params.height ?: "480"
        String iframeName = params.iframeName ?: "Syndicated Content"
        "<iframe src=\"${url}\" width=\"${width}\" height=\"${height}\" name=\"${iframeName}\" frameborder=\"0\"></iframe>".encodeAsHTML()
    }

    String renderJSSnippet(String url, mediaSource, params){
        url += "/syndicate.json?${mediaService.getExtractionParams(params)}&callback=?"
        String jqeuryEmbed = """<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>"""
        String divId = params.divId ?: "syndicatedContent_${mediaSource.id}_${System.nanoTime()}"
        String includedDiv = """<div id="${divId}"></div>"""
        boolean excludeJquery = Util.isTrue(params.excludeJquery)
        boolean excludeDiv = Util.isTrue(params.excludeDiv)

        """${excludeJquery ? "" : jqeuryEmbed}<script>\$(document).ready(function(){\$.getJSON('${url}', function(data){\$('#${divId}').html(data.results[0].content);});});</script>${excludeDiv ? "" : includedDiv}""".encodeAsHTML()
    }

    @NotTransactional
    def getRenderedContentByMediaType(mediaList, params){
        def mediaItemContent = []
        mediaList.each{ mediaItem ->
            switch(mediaItem) {
                case Collection:
                    //do nothing, we don't do nesting
                    break
                case FAQ:
                    //do nothing, don't render whole faq inside list
                case Html:
                    mediaItemContent << [content:renderHtml(mediaItem as Html, params), meta:mediaItem]
                    break
                case Image:
                    mediaItemContent << [content:renderImage(mediaItem as Image, params), meta:mediaItem]
                    break
                case Infographic:
                    mediaItemContent << [content:renderInfographic(mediaItem as Infographic, params), meta:mediaItem]
                    break
                case Video:
                    mediaItemContent << [content:renderVideo(mediaItem as Video, params), meta:mediaItem]
                    break
                case PDF:
                    mediaItemContent << [content:renderPdf(mediaItem as PDF, params), meta:mediaItem]
                    break
                case QuestionAndAnswer:
                    mediaItemContent << [content:renderQuestionAndAnswer(mediaItem as QuestionAndAnswer, params), meta:mediaItem]
                    break
                case Tweet:
                    mediaItemContent << [content:renderTweet(mediaItem as Tweet, params), meta:mediaItem]
                    break
                default:
                    log.error "Unsupported media type: ${mediaItem.getClass()}"
            }
        }
        mediaItemContent
    }

    @Transactional
    def renderMediaList(mediaList, params){
        String content
        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                if(!Util.isTrue(params.autoplay, false)){ params.autoplay = 0 }
                if(mediaList?.size() > 20){
                    mediaList = mediaList.toArray().sort{it.name}[0..19]
                }
                def mediaItemContent = getRenderedContentByMediaType(mediaList, params)
                content = params.controllerContext([mediaItemContent:mediaItemContent])
                break
            case "feed":
                if(!Util.isTrue(params.autoplay, false)){ params.autoplay = 0 }
                def mediaItemContent = getRenderedContentByMediaType(mediaList, params)
                content = groovyPageRenderer.render(template: "/media/mediaFeedView", model:[mediaItemContent:mediaItemContent])
                break
            case "list":
            default:
                content = groovyPageRenderer.render(template: "/media/mediaListView", model:[mediaItems:mediaList])
                content = contentRetrievalService.wrapWithSyndicateDiv(content)
                content = contentRetrievalService.addAttributionToExtractedContent(params.long('id'), content)
        }
        content
    }

    @NotTransactional
    String getExtractionParams(params){
        String queryParams = "stripStyles="+    Util.isTrue(params.stripStyles)
        queryParams += "&stripScripts="+        Util.isTrue(params.stripScripts)
        queryParams += "&stripBreaks="+         Util.isTrue(params.stripBreaks)
        queryParams += "&stripImages="+         Util.isTrue(params.stripImages)
        queryParams += "&stripClasses="+        Util.isTrue(params.stripClasses)
        queryParams += "&stripIds="+            Util.isTrue(params.stripIds)
        queryParams += "&displayMethod="+       params.displayMethod ?: "feed"
        queryParams += "&autoplay="+            Util.isTrue(params.autoplay, false)
        queryParams += "&userId="+              params.userId
        queryParams
    }

    @Transactional(readOnly = true)
    def getMediaItem(Long id){
        MediaItem.read(id)
    }

    @NotTransactional
    String getExtractedContent(MediaItem mi, params){
        try {
            //for debugging
            if(Holders.config.disableGuavaCache){
                log.warn "cache is disabled for this request"
                Map extractedContentAndHash = contentRetrievalService.getContentAndMd5Hashcode(mi.sourceUrl, params)
                String extractedContent = extractedContentAndHash.content
                String newHash = extractedContentAndHash.hash
                def lastKnownGood = contentCacheService.get(mi)
                if(!extractedContent){
                    if(lastKnownGood && lastKnownGood.content){
                        extractedContent = lastKnownGood.content
                    } else {
                        throw new ContentNotExtractableException("There is no syndicated markup/content found!")
                        return
                    }
                } else{ //Else update the lastKnownGood if it's out of date
                    if(!lastKnownGood || lastKnownGood.mediaItem.hash != newHash){
                        contentCacheService.cache(mi.id, extractedContent)
                    }
                }

                extractedContent = contentRetrievalService.addAttributionToExtractedContent(mi.id, extractedContent)
                extractedContent += analyticsService.getGoogleAnalyticsString(mi, params)
                return extractedContent
            }

            //With ajax requests from storefront, a random id and callback will cause the cache to miss every time
            //This corrects that
            def keyParts = params.clone()
            keyParts.remove("_")
            keyParts.remove("callback")
            keyParts.remove("controllerContext")
            keyParts.remove("newUrlBase")

            String key = Hash.md5(mi.sourceUrl + keyParts.sort().toString())

            String extractedContent = guavaCacheService.extractedContentCache.get(key, new Callable<String>() {
                @Override
                public String call(){
                    log.info "cache miss: ${keyParts}"
                    Map extractedContentAndHash = contentRetrievalService.getContentAndMd5Hashcode(mi.sourceUrl, params)
                    String extractedContent = extractedContentAndHash.content
                    String newHash = extractedContentAndHash.hash
                    def lastKnownGood = contentCacheService.get(mi)

                    if(!extractedContent){
                        if(lastKnownGood && lastKnownGood.content){
                            extractedContent = lastKnownGood.content
                        } else {
                            throw new ContentNotExtractableException("There is no syndicated markup/content found!")
                            return
                        }
                    } else{ //Else update the lastKnownGood if it's out of date
                        if(!lastKnownGood || lastKnownGood.mediaItem.hash != newHash){
                            contentCacheService.cache(mi.id, extractedContent)
                        }
                    }
                    extractedContent
                }
            });
            extractedContent = contentRetrievalService.addAttributionToExtractedContent(mi.id, extractedContent)
            extractedContent += analyticsService.getGoogleAnalyticsString(mi, params)
            return extractedContent
        }catch(e){
            log.error(e)
            return null
        }
    }

    def saveCollection(Collection collectionInstance) {
        if (!collectionInstance.validate()) {
            return collectionInstance
        }

        MediaItemSubscriber mediaItemSubscriber = createAndFindMediaItemSubscriber(collectionInstance)
        
        collectionInstance.manuallyManaged = false
        collectionInstance.save(flush: true)
        mediaItemSubscriber.save()

        collectionInstance
    }

    def saveFAQ(FAQ faqInstance) {
        if (!faqInstance.validate()) {
            return faqInstance
        }

        MediaItemSubscriber mediaItemSubscriber = createAndFindMediaItemSubscriber(faqInstance)

        faqInstance.manuallyManaged = false
        faqInstance.save(flush: true)
        mediaItemSubscriber.save()

        faqInstance
    }
    private generalMediaItemLoader = { MediaItem item ->
        def result
        try {
            result = MediaItem.createCriteria().get {
                eq 'sourceUrl', item.sourceUrl

                lock true
            }
        } catch (NonUniqueResultException){
            log.error("Found Duplicate SourceURL! ${item.sourceUrl}")
        } catch(e){
            log.error e
        }
        result
    }

    def saveHtml(Html htmlInstance, params) throws ContentNotExtractableException, ContentUnretrievableException{
        //Extract the content (or try to)
        params.disableFailFast = true
        def contentAndHash = contentRetrievalService.getContentAndMd5Hashcode(htmlInstance.sourceUrl, params)
        if (!contentAndHash.content) { //Extraction failed
            log.error("Could not extract content, page was found but not marked up at url: ${htmlInstance.sourceUrl}")
            throw new ContentNotExtractableException("Could not extract content, page was found but not marked up at url: ${htmlInstance.sourceUrl}")
        } else { // Content extracted fine, can we save?
            htmlInstance.hash = contentAndHash.hash

            //Try to add a meaningful description if one isn't provided
            if(!htmlInstance.description){
                try {
                    String desc = contentRetrievalService.getDescriptionFromContent(contentAndHash.content, htmlInstance.sourceUrl)
                    if(desc){
                        htmlInstance.description = desc
                    }
                } catch(e){
                    log.error(e)
                }
            }

            htmlInstance = createOrUpdateMediaItem(htmlInstance, generalMediaItemLoader, contentAndHash) as Html      //load or update and existing record if it exists
        }
        if(contentAndHash.content) {
            contentCacheService.cache(htmlInstance.id, contentAndHash.content)
        }
        htmlInstance
    }

    Image saveImage(Image imageInstance) {
        createOrUpdateMediaItem(imageInstance, generalMediaItemLoader) as Image      //load or update and existing record if it exists
    }

    Infographic saveInfographic(Infographic infographicInstance) {
        createOrUpdateMediaItem(infographicInstance, generalMediaItemLoader) as Infographic     //load or update and existing record if it exists
    }

    PDF savePDF(PDF PDFInstance){
        createOrUpdateMediaItem(PDFInstance, generalMediaItemLoader) as PDF
    }

    QuestionAndAnswer saveQuestionAndAnswer(QuestionAndAnswer questionAndAnswerInstance){
        createOrUpdateMediaItem(questionAndAnswerInstance, generalMediaItemLoader) as QuestionAndAnswer
    }

    Video saveVideo(Video videoInstance) {
        Video fromImport
        try {
            fromImport = youtubeService.getVideoInstanceFromUrl(videoInstance.sourceUrl)
        } catch (InaccessibleVideoException e){
            log.error("The video is private and cannot be imported! ${videoInstance.sourceUrl} : ${e}")
            videoInstance.duration = 1
            videoInstance.validate()
            videoInstance.errors.rejectValue("sourceUrl", "Video is Private", "The specified video is private and cannot be imported/published to syndication. Please contact the video's owner.")
            return videoInstance
        } catch(e){
            log.error e
            return null
        }

        videoInstance.description = videoInstance?.description ?: fromImport?.description
        videoInstance.duration = videoInstance?.duration ?: fromImport?.duration
        videoInstance.externalGuid = videoInstance?.externalGuid ?: fromImport?.externalGuid
        videoInstance.name = videoInstance?.name ?: fromImport?.name

        videoInstance = createOrUpdateMediaItem(videoInstance, { MediaItem video ->
            String videoId = youtubeService.getVideoId(video.sourceUrl)
            MediaItem.sourceUrlContains(videoId).get()
        }) as Video      //load or update and existing record if it exists

        videoInstance
    }

    def MediaItem createOrUpdateMediaItem(MediaItem item, Closure mediaFinder, Map contentAndHash = [:]){
        boolean savetoDB = true
        MediaItem existing = mediaFinder(item)
        
        if (existing) {
            def result = updateMediaItem(existing, item)
            // -- media type specific updates here --
            if(!result.changed && contentAndHash.hash == existing.hash){
                log.info "Item not changed, skipping update ${result.updatedRecord.sourceUrl}"
                item = existing // if there were no changes, return the existing record instead
                savetoDB = false
            } else {
                log.info "Item changed, updating - ${item.id}: ${item.sourceUrl}"
                item = result.updatedRecord
            }
        }

        if(savetoDB) {
            MediaItemSubscriber mediaItemSubscriber = createAndFindMediaItemSubscriber(item)
            item.manuallyManaged = false
            //Added merge to investigate concurrent publish
            def result
            try {
                result = item.save()
            } catch (e) {
                log.error ("Could not save mediaItem: ${item.id}: ${item.sourceUrl}")
                println e
            }
            if(!result){
                log.error "Media mediaSource didn't save. Errors were: ${item.errors}"
            } else {
                mediaItemSubscriber.save()

                log.info "media saved: ${item.id} - ${item.sourceUrl}"
                if (grailsApplication.config.syndication.solrService.useSolr) {
                    solrIndexingService.inputMediaItem(item, contentAndHash.content)
                }
            }
            log.info "Media Item Created/Updated: ${item.id} - ${item.sourceUrl}"
        }
        item
    }

    /**
     * Validates a that the publisher is able to access/modify the current mediaItem
     * @param mediaItem
     * @return
     */
    MediaItemSubscriber createAndFindMediaItemSubscriber(MediaItem mediaItem){
        GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
        def request = webUtils.getCurrentRequest()
        String[] apiKey = request.getHeader("Authorization").substring("syndication_api_key ".length()).split(':')
        def apiKeyLength = apiKey.length

        if (apiKeyLength != 2) {
            mediaItem.discard()
            log.error("(${System.currentTimeMillis() as String}) api key is malformed")
            throw new UnauthorizedException("You do not have permission to access this mediaSource.")
        }

        def senderPublicKey = apiKey[0] as String
        def mediaItemSubscriber = null
        Long subscriberId = null
        def cmsManagerResponse = null
        try{
            cmsManagerResponse = cmsManagerService.getSubscriber(senderPublicKey)
            subscriberId = cmsManagerResponse?.id as Long
            if(mediaItem.id){
                mediaItemSubscriber = MediaItemSubscriber.findBySubscriberIdAndMediaItem(subscriberId, mediaItem)
                if(mediaItemSubscriber?.id) {
                    return mediaItemSubscriber
                }
            } else {
                return new MediaItemSubscriber(subscriberId:subscriberId, mediaItem:mediaItem)
            }
        } catch(e){
            StringWriter sw = new StringWriter()
            PrintWriter pw = new PrintWriter(sw)
            e.printStackTrace(pw)
            log.error("${e} \n ${sw.toString()}")
        }
        log.error("Permission denied, could not find or create a valid MediaItemSubscriber for the mediaItem! Details: \n" +
                "mediaItemId: ${mediaItem.id}\nsourceUrl: ${mediaItem.sourceUrl}\nsubscriberId: ${subscriberId}\nmediaItemSubscriber id: ${mediaItemSubscriber?.id}\n" +
                "Api Key: ${apiKey}\nSender publoc key: ${senderPublicKey}" +
                "Raw CMS Manager Response:\n${cmsManagerResponse}")
        mediaItem.discard()
        throw new UnauthorizedException("You do not have permission to access this mediaSource.")
    }

    @NotTransactional
    def getMetaDataTemplate(params, response) {
        def metaMap = apiResponseBuilderService.getMetaData(params, response)
        metaMap
    }

    @Transactional(readOnly = true)
    def listRelatedMediaItems(params) {
        params.max = getMax(params)
        def media = tagsService.listRelatedMediaIds(params)
        media
    }

    @Transactional(readOnly = true)
    def listMediaItems(params) {
        params.max = getMax(params)
        if (params.id) {
            return [MediaItem.get(params.id as Long)]
        }
        if(params?.order == "desc"){
            params.sort = "-" + params.sort
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
        results ?: []
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
    def getCollectionMediaItemsForAPIResponse(com.ctacorp.syndication.media.Collection collection) {
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
        Math.min(params.int("max") ?: 20, 1000)
    }

    //Ignore updates to these fields
    private ignoredFields = ["lastUpdated", "dateCreated", "dateSyndicationCaptured", "dateSyndicationUpdated", "manuallyManaged"] //These will never match between an existing mediaSource and a saved mediaSource, so ignore them

    //This would be needed in the case where a new instance is created in memory from a publish
    //but an existing mediaSource with the same sourceURL already exists in the DB. The new mediaSource would not have an
    //id, but would represent the same mediaSource logically. In that case, copy any updated properties from the
    //new mediaSource to the existing mediaSource and then save it, otherwise if all fields are the same, ignore it.
    private Map updateMediaItem(MediaItem o, MediaItem u) { //original, updated
        boolean changed = false

        def mediaItemClass = new DefaultGrailsDomainClass(o.getClass())

        //Iterate over all MediaItem properties except oneToMany and ManyToMany
        mediaItemClass.getPersistentProperties().findAll{!it.isOneToMany() && !it.isManyToMany()}.each { p ->
            if(!(p.name in ignoredFields)) {
                def oProp = o.properties[p.name]
                def uProp = u.properties[p.name]

                if(uProp instanceof Date){
                    Date oDate = oProp as Date
                    Date uDate = uProp as Date
                }

                if (uProp && uProp != oProp) {
                    o.properties[p.name] = uProp
                    changed = true
                }
            }
        }

        [changed: changed, updatedRecord: o]
    }
}
