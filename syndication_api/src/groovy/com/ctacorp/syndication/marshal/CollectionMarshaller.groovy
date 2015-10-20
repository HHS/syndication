
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import grails.converters.JSON

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 11/18/13
 * Time: 2:23 PM
 *
 */
class CollectionMarshaller {
    def services

    CollectionMarshaller(){
        JSON.registerObjectMarshaller(com.ctacorp.syndication.media.Collection){ com.ctacorp.syndication.media.Collection c ->
            def campaigns = services.mediaService.getCampaignsForAPIResponse(c)
            def mediaItems = services.mediaService.getCollectionMediaItemsForAPIResponse(c)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${c.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:              "Collection",
                    id:                     c.id,
                    name:                   c.name,
                    description:            c.description,
                    sourceUrl:              c.sourceUrl,
                    targetUrl:              c.targetUrl,
                    dateContentAuthored:    c.dateContentAuthored,
                    dateContentUpdated:     c.dateContentUpdated,
                    dateContentPublished:   c.dateContentPublished,
                    dateContentReviewed:    c.dateContentReviewed,
                    dateSyndicationVisible: c.dateSyndicationVisible,
                    dateSyndicationCaptured:c.dateSyndicationCaptured,
                    dateSyndicationUpdated: c.dateSyndicationUpdated,
                    language:               c.language,
                    externalGuid:           c.externalGuid,
                    contentHash:            c.hash,
                    source:                 c.source,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(c),
                    alternateImages:        c.alternateImages,
                    mediaItems:             mediaItems,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(c.id),
                    previewlUrl:            services.urlService.getPreviewUrl(c.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(c.extendedAttributes),
                    customThumbnailUrl:     c.customThumbnailUrl,
                    customPreviewUrl:       c.customPreviewUrl
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