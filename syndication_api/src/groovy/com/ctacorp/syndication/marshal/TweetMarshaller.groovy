
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.media.Tweet
import grails.converters.JSON
/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 1/16/14
 * Time: 4:22 PM
 */
class TweetMarshaller {
    def services

    TweetMarshaller() {
        JSON.registerObjectMarshaller(Tweet) { Tweet tw ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(tw)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(tw)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${tw.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:                  "Tweet",
                    id:                         tw.id,
                    name:                       tw.name,
                    twitterAccountName:         tw.account.accountName,
                    twitterAccountSubscriberId: tw.account.subscriberId,
                    tweetId:                    "${tw.tweetId}",
                    messageText:                tw.messageText,
                    mediaUrl:                   tw.mediaUrl,
                    videoUrl:                   tw.videoVariantUrl,
                    tweetDate:                  tw.tweetDate,
                    description:                tw.description,
                    sourceUrl:                  tw.sourceUrl,
                    targetUrl:                  tw.targetUrl,
                    dateContentAuthored:        tw.dateContentAuthored,
                    dateContentUpdated:         tw.dateContentUpdated,
                    dateContentPublished:       tw.dateContentPublished,
                    dateContentReviewed:        tw.dateContentReviewed,
                    dateSyndicationVisible:     tw.dateSyndicationVisible,
                    dateSyndicationCaptured:    tw.dateSyndicationCaptured,
                    dateSyndicationUpdated:     tw.dateSyndicationUpdated,
                    language:                   tw.language,
                    externalGuid:               tw.externalGuid,
                    contentHash:                tw.hash,
                    source:                     tw.source,
                    alternateImages:            tw.alternateImages,
                    campaigns:                  campaigns,
                    tags:                       services.mediaService.getTagsForMediaItemForAPIResponse(tw),
                    tinyUrl:                    tinyInfo.tinyUrl,
                    tinyToken:                  tinyInfo.tinyUrlToken,
                    thumbnailUrl:               services.urlService.getThumbnailUrl(tw.id),
                    previewlUrl:                services.urlService.getPreviewUrl(tw.id),
                    attribution:                attr,
                    extendedAttributes:         marshalDescriptor(tw.extendedAttributes),
                    customThumbnailUrl:         tw.customThumbnailUrl,
                    customPreviewUrl:           tw.customPreviewUrl
            ]
        }
    }

    private marshalDescriptor(descriptors){
        def desc = [:]
        descriptors.each{ ExtendedAttribute d ->
            desc[d.name] = d.value
        }
        desc
    }
}
