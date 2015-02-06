package syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.Periodical
import grails.converters.JSON
import syndication.preview.ThumbnailService
import syndication.rest.MediaService
import syndication.tag.TagsService
import syndication.tinyurl.TinyUrlService

/**
 * Created by sgates on 12/19/14.
 */
class PeriodicalMarshaller {
    ThumbnailService thumbnailService
    TinyUrlService tinyUrlService
    TagsService tagsService
    MediaService mediaService

    PeriodicalMarshaller(){
        JSON.registerObjectMarshaller(Periodical){ Periodical p ->
            def tinyInfo = mediaService.getTinyUrlInfoForMediaItemForAPIResponse(p)
            def campaigns = mediaService.getCampaignsForAPIResponse(p)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${p.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")
            return [
                    mediaType:             "Periodical",
                    id:                     p.id,
                    name:                   p.name,
                    description:            p.description,
                    period:                 p.period.name(),
                    sourceUrl:              p.sourceUrl,
                    targetUrl:              p.targetUrl,
                    customThumbnailUrl:      p.customThumbnailUrl,
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
                    tags:                   mediaService.getTagsForMediaItemForAPIResponse(p),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           thumbnailService.getThumbnailUrl(p.id),
                    alternateImages:        p.alternateImages,
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(p.extendedAttributes)
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
