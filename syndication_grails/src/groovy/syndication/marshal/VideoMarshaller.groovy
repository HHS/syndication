
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.media.Video
import grails.converters.JSON

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 10/16/13
 * Time: 2:07 PM
 *
 */
class VideoMarshaller {
    def services

    VideoMarshaller() {
        JSON.registerObjectMarshaller(Video) { Video v ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(v)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(v)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${v.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:              "Video",
                    id:                     v.id,
                    name:                   v.name,
                    description:            v.description,
                    sourceUrl:              v.sourceUrl,
                    targetUrl:              v.targetUrl,
                    dateContentAuthored:    v.dateContentAuthored,
                    dateContentUpdated:     v.dateContentUpdated,
                    dateContentPublished:   v.dateContentPublished,
                    dateContentReviewed:    v.dateContentReviewed,
                    dateSyndicationVisible: v.dateSyndicationVisible,
                    dateSyndicationCaptured:v.dateSyndicationCaptured,
                    dateSyndicationUpdated: v.dateSyndicationUpdated,
                    language:               v.language,
                    externalGuid:           v.externalGuid,
                    contentHash:            v.hash,
                    source:                 v.source,
                    alternateImages:        v.alternateImages,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(v),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(v.id),
                    previewUrl:             services.urlService.getPreviewUrl(v.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(v.extendedAttributes),
                    youtubeMetaData:        services.youtubeService.getMetaDataForVideoUrl(v.sourceUrl)
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
