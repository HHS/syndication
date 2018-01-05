package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.media.Video
import grails.util.Holders

class MediaItemMarshaller {

    def services
    def youtubeService = Holders.grailsApplication.mainContext.getBean 'youtubeService'
    def mediaService = Holders.grailsApplication.mainContext.getBean 'mediaService'
    def urlService = Holders.grailsApplication.mainContext.getBean 'urlService'


    MediaItem mediaItem
    def tags
    def tinyUrl//html
    def tinyToken//html
    def thumbnailUrl
    def previewUrl
    def attribution
    def questionAndAnswers //for FAQ
    def width//for image, infographic
    def height//for image, infographic
    //for twitter items
    def twitterAccountName
    def twitterAccountSubscriberId
    def tweetId
    def messageText
    def mediaUrl
    def videoUrl
    def tweetDate
    //video
    def youtubeMetaData

    def mediaItems //collection

    MediaItemMarshaller(MediaItem item) {

        mediaItem = item
        tags = mediaService.getTagsForMediaItemForAPIResponse(mediaItem)//get tags
        def tinyUrlInfo = mediaService.getTinyUrlInfoForMediaItemForAPIResponse(mediaItem)
        tinyUrl = tinyUrlInfo.tinyUrl
        tinyToken = tinyUrlInfo.tinyUrlToken
        questionAndAnswers = item.class == FAQ ? (item as FAQ).questionAndAnswers : null
        width = item.class == Image ? (item as Image).width : null
        width = item.class == Infographic ? (item as Infographic).width : width
        height = item.class == Image ? (item as Image).height : null
        height = item.class == Infographic ? (item as Infographic).height : height
        twitterAccountName = item.class == Tweet ? (item as Tweet).account.accountName : null
        twitterAccountSubscriberId = item.class == Tweet ? (item as Tweet).account.subscriberId : null
        tweetId = item.class == Tweet ? (item as Tweet).tweetId : null
        messageText = item.class == Tweet ? (item as Tweet).messageText : null
        mediaUrl = item.class == Tweet ? (item as Tweet).mediaUrl : null
        videoUrl = item.class == Tweet ? (item as Tweet).videoVariantUrl : null
        tweetDate = item.class == Tweet ? (item as Tweet).tweetDate : null
        youtubeMetaData = item.class == Video ? youtubeService.getMetaDataForVideoUrl(item.sourceUrl) : null//get metadata

        mediaItems = item.class == Collection ? {

            (item as Collection).mediaItems.collect {
                [
                        id: it.id,
                        name: it.name,
                        mediaType: it.class == FAQ ? 'Faq' : it.class.simpleName
                ]
            }

        }() : []

        thumbnailUrl = urlService.getThumbnailUrl(item.id)
        previewUrl = urlService.getPreviewUrl(item.id)
        attribution = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${item.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")
    }
}
