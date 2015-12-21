package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.media.PDF
import grails.converters.JSON

/**
 * Created by nburk on 11/10/15.
 */
class PDFMarshaller {

    def services

    PDFMarshaller() {
        JSON.registerObjectMarshaller(PDF){ PDF p ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(p)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(p)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${p.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")
            return [
                    mediaType:             "PDF",
                    id:                     p.id,
                    name:                   p.name,
                    description:            p.description,
                    sourceUrl:              p.sourceUrl,
                    targetUrl:              p.targetUrl,
                    dateContentAuthored:    p.dateContentAuthored,
                    dateContentUpdated:     p.dateContentUpdated,
                    dateContentPublished:   p.dateContentPublished,
                    dateContentReviewed:    p.dateContentReviewed,
                    dateSyndicationVisible: p.dateSyndicationVisible,
                    dateSyndicationCaptured:p.dateSyndicationCaptured,
                    dateSyndicationUpdated: p.dateSyndicationUpdated,
                    language:               p.language,
                    externalGuid:           p.externalGuid,
                    contentHash:            p.hash,
                    source:                 p.source,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(p),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(p.id),
                    previewlUrl:            services.urlService.getPreviewUrl(p.id),
                    alternateImages:        p.alternateImages,
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(p.extendedAttributes),
                    customThumbnailUrl:     p.customThumbnailUrl,
                    customPreviewUrl:       p.customPreviewUrl
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
