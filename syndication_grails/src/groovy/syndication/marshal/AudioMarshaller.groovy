package syndication.marshal

import clover.com.lowagie.text.html.HtmlEncoder
import com.ctacorp.syndication.Audio
import com.ctacorp.syndication.ExtendedAttribute
import grails.converters.JSON
import syndication.preview.ThumbnailService
import syndication.rest.MediaService
import syndication.tag.TagsService
import syndication.tinyurl.TinyUrlService
import syndication.youtube.YoutubeService

/**
 * Created by sgates on 8/19/14.
 */
class AudioMarshaller {
    ThumbnailService thumbnailService
    TinyUrlService tinyUrlService
    TagsService tagsService
    MediaService mediaService
    YoutubeService youtubeService

    AudioMarshaller() {
        JSON.registerObjectMarshaller(Audio) { Audio a ->
            def tinyInfo = mediaService.getTinyUrlInfoForMediaItemForAPIResponse(a)
            def campaigns = mediaService.getCampaignsForAPIResponse(a)

            def attr = HtmlEncoder.encode("<div id='hhsAttribution'>Content provided and maintained by <a href='${a.source.websiteUrl}' target='_blank'>Health and Human Services</a> (HHS). Please see our system <a href='http:syndication.hhs.gov' target='_blank'>usage guidelines and disclaimer</a>.</div>")

            return [
                    mediaType:              "Audio",
                    id:                     a.id,
                    name:                   a.name,
                    description:            a.description,
                    sourceUrl:              a.sourceUrl,
                    targetUrl:              a.targetUrl,
                    customThumbnailUrl:      a.customThumbnailUrl,
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
                    tags:                   mediaService.getTagsForMediaItemForAPIResponse(a),
                    tinyUrl:                tinyInfo.tinyUrl,
                    tinyToken:              tinyInfo.tinyUrlToken,
                    thumbnailUrl:           thumbnailService.getThumbnailUrl(a.id),
                    attribution:            attr,
                    extendedAttributes:     marshalDescriptor(a.extendedAttributes)
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
