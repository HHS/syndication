package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.media.Widget
import grails.converters.JSON

/**
 * Created by sgates on 2/11/15.
 */
class WidgetMarshaller {
    def services

    WidgetMarshaller(){
        JSON.registerObjectMarshaller(Widget){ Widget w ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(w)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(w)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${w.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")
            return [
                    mediaType:             "Widget",
                    id:                     w.id,
                    name:                   w.name,
                    description:            w.description,
                    sourceUrl:              w.sourceUrl,
                    targetUrl:              w.targetUrl,
                    customThumbnailUrl:     w.customThumbnailUrl,
                    dateContentAuthored:    w.dateContentAuthored,
                    dateContentUpdated:     w.dateContentUpdated,
                    dateContentPublished:   w.dateContentPublished,
                    dateContentReviewed:    w.dateContentReviewed,
                    dateSyndicationVisible: w.dateSyndicationVisible,
                    dateSyndicationCaptured:w.dateSyndicationCaptured,
                    dateSyndicationUpdated: w.dateSyndicationUpdated,
                    language:               w.language,
                    externalGuid:           w.externalGuid,
                    contentHash:            w.hash,
                    source:                 w.source,
                    alternateImages:        w.alternateImages,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(w),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(w.id),
                    previewlUrl:            services.urlService.getPreviewUrl(w.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(w.extendedAttributes),
                    customThumbnailUrl:     w.customThumbnailUrl,
                    customPreviewUrl:       w.customPreviewUrl
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
