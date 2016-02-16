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
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.QuestionAndAnswer
import grails.converters.JSON
import static com.ctacorp.syndication.media.MediaItem.StructuredContentType.*

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 9/9/13
 * Time: 12:24 PM
 *
 */
class QuestionAndAnswerMarshaller {
    def services

    QuestionAndAnswerMarshaller() {
        JSON.registerObjectMarshaller(QuestionAndAnswer) { QuestionAndAnswer qAndA ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(qAndA)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(qAndA)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${qAndA.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType              : "QuestionAndAnswer",
                    id                     : qAndA.id,
                    name                   : qAndA.name,
                    question               : qAndA.name,
                    answer                 : qAndA.answer,
                    description            : qAndA.description,
                    sourceUrl              : qAndA.sourceUrl,
                    targetUrl              : qAndA.targetUrl,
                    dateContentAuthored    : qAndA.dateContentAuthored,
                    dateContentUpdated     : qAndA.dateContentUpdated,
                    dateContentPublished   : qAndA.dateContentPublished,
                    dateContentReviewed    : qAndA.dateContentReviewed,
                    dateSyndicationVisible : qAndA.dateSyndicationVisible,
                    dateSyndicationCaptured: qAndA.dateSyndicationCaptured,
                    dateSyndicationUpdated : qAndA.dateSyndicationUpdated,
                    language               : qAndA.language,
                    externalGuid           : qAndA.externalGuid,
                    contentHash            : qAndA.hash,
                    source                 : qAndA.source,
                    createdBy              : qAndA.createdBy,
                    campaigns              : campaigns,
                    tags                   : services.mediaService.getTagsForMediaItemForAPIResponse(qAndA),
                    tinyUrl                : tinyInfo.tinyUrl,
                    tinyToken              : tinyInfo.tinyUrlToken,
                    thumbnailUrl           : services.urlService.getThumbnailUrl(qAndA.id),
                    previewlUrl            : services.urlService.getPreviewUrl(qAndA.id),
                    alternateImages        : qAndA.alternateImages,
                    attribution            : attr,
                    extendedAttributes     : marshalDescriptor(qAndA.extendedAttributes),
                    customThumbnailUrl     : qAndA.customThumbnailUrl,
                    customPreviewUrl       : qAndA.customPreviewUrl
            ]
        }
    }

    private marshalDescriptor(descriptors) {
        def desc = [:]
        descriptors.each { ExtendedAttribute d ->
            desc[d.name] = d.value
        }
        desc
    }
}
