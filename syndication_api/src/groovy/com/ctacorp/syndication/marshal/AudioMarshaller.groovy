package com.ctacorp.syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.media.Audio
import com.ctacorp.syndication.ExtendedAttribute
import grails.converters.JSON

/**
 * Created by sgates on 8/19/14.
 */
class AudioMarshaller {
    def services

    AudioMarshaller() {
        JSON.registerObjectMarshaller(Audio) { Audio a ->
            def tinyInfo = services.mediaService.getTinyUrlInfoForMediaItemForAPIResponse(a)
            def campaigns = services.mediaService.getCampaignsForAPIResponse(a)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${a.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:              "Audio",
                    id:                     a.id,
                    name:                   a.name,
                    description:            a.description,
                    sourceUrl:              a.sourceUrl,
                    targetUrl:              a.targetUrl,
                    dateContentAuthored:    a.dateContentAuthored,
                    dateContentUpdated:     a.dateContentUpdated,
                    dateContentPublished:   a.dateContentPublished,
                    dateContentReviewed:    a.dateContentReviewed,
                    dateSyndicationVisible: a.dateSyndicationVisible,
                    dateSyndicationCaptured:a.dateSyndicationCaptured,
                    dateSyndicationUpdated: a.dateSyndicationUpdated,
                    language:               a.language,
                    externalGuid:           a.externalGuid,
                    contentHash:            a.hash,
                    source:                 a.source,
                    alternateImages:        a.alternateImages,
                    campaigns:              campaigns,
                    tags:                   services.mediaService.getTagsForMediaItemForAPIResponse(a),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           services.urlService.getThumbnailUrl(a.id),
                    previewlUrl:            services.urlService.getPreviewUrl(a.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(a.extendedAttributes),
                    customThumbnailUrl:     a.customThumbnailUrl,
                    customPreviewUrl:       a.customPreviewUrl
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
