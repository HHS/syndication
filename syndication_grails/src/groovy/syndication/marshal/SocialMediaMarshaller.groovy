
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
import com.ctacorp.syndication.media.SocialMedia
import grails.converters.JSON
/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 1/16/14
 * Time: 4:22 PM
 */
class SocialMediaMarshaller {
    def services

    SocialMediaMarshaller() {
        JSON.registerObjectMarshaller(SocialMedia) { SocialMedia sm ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(sm)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(sm)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${sm.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:              "SocialMedia",
                    id:                     sm.id,
                    name:                   sm.name,
                    description:            sm.description,
                    sourceUrl:              sm.sourceUrl,
                    targetUrl:              sm.targetUrl,
                    dateContentAuthored:    sm.dateContentAuthored,
                    dateContentUpdated:     sm.dateContentUpdated,
                    dateContentPublished:   sm.dateContentPublished,
                    dateContentReviewed:    sm.dateContentReviewed,
                    dateSyndicationVisible: sm.dateSyndicationVisible,
                    dateSyndicationCaptured:sm.dateSyndicationCaptured,
                    dateSyndicationUpdated: sm.dateSyndicationUpdated,
                    language:               sm.language,
                    externalGuid:           sm.externalGuid,
                    contentHash:            sm.hash,
                    source:                 sm.source,
                    alternateImages:        sm.alternateImages,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(sm),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(sm.id),
                    previewlUrl:            services.urlService.getPreviewUrl(sm.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(sm.extendedAttributes)
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
