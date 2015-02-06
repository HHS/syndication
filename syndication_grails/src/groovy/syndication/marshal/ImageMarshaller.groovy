
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
import grails.converters.JSON
import com.ctacorp.syndication.*
import syndication.rest.MediaService
import syndication.tag.TagsService
import syndication.preview.ThumbnailService
import syndication.tinyurl.TinyUrlService

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 9/9/13
 * Time: 1:15 PM
 *
 */
class ImageMarshaller {
    ThumbnailService thumbnailService
    TinyUrlService tinyUrlService
    TagsService tagsService
    MediaService mediaService

    ImageMarshaller() {
        JSON.registerObjectMarshaller(Image) { Image i ->
            def tinyInfo = mediaService.getTinyUrlInfoForMediaItemForAPIResponse(i)
            def campaigns = mediaService.getCampaignsForAPIResponse(i)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${i.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:      "Image",
                    id:                     i.id,
                    name:                   i.name,
                    description:            i.description,
                    sourceUrl:              i.sourceUrl,
                    targetUrl:              i.targetUrl,
                    customThumbnailUrl:      i.customThumbnailUrl,
                    width:                  i.width,
                    height:                 i.height,
                    dateContentAuthored:    i.dateContentAuthored,
                    dateContentUpdated:     i.dateContentUpdated,
                    dateContentPublished:   i.dateContentPublished,
                    dateContentReviewed:    i.dateContentReviewed,
                    dateSyndicationVisible: i.dateSyndicationVisible,
                    dateSyndicationCaptured:i.dateSyndicationCaptured,
                    dateSyndicationUpdated: i.dateSyndicationUpdated,
                    language:               i.language,
                    externalGuid:           i.externalGuid,
                    contentHash:            i.hash,
                    source:                 i.source,
                    alternateImages:        i.alternateImages,
                    tags:                   mediaService.getTagsForMediaItemForAPIResponse(i),
                    campaigns:              campaigns,
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           thumbnailService.getThumbnailUrl(i.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(i.extendedAttributes)
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
