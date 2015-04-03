package contentextractionservices

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.contentextraction.MediaPreviewThumbnailService
import com.ctacorp.syndication.media.Html
import grails.test.spock.IntegrationSpec

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class MediaPreviewThumbnailServiceIntegrationSpec extends IntegrationSpec {

    MediaPreviewThumbnailService mediaPreviewThumbnailService

    def setup() {
        assert mediaPreviewThumbnailService.grailsApplication.config.syndication.scratch.root
        Language language = new Language(name:"English", isoCode: "eng")
        language.save(flush:true)
        if(language.hasErrors()){println language.errors}
        assert language.id
        Source source = new Source(name:"Health and Human Services", acronym: "HHS", websiteUrl:"http://www.HHS.gov")
        source.save(flush:true)
        if(source.hasErrors()){println source.errors}
        assert source.id
        Html html = new Html(name:"Some Content", sourceUrl: "http://www.cdc.gov/Features/HealthyTips/", source:source, language:language)
        html.save(flush:true)
        if(html.hasErrors()){println html.errors}
    }

    def cleanup() {
    }

    void "generate successfully creates a thumbnail and preview"() {
        when:
            Html html = Html.first()
        then:
            mediaPreviewThumbnailService.generate(html)
            def previewAndThumnail = mediaPreviewThumbnailService.previewAndThumbnail(html)
            previewAndThumnail.preview != null
            previewAndThumnail.preview.imageData.size() > 0
        then:
            previewAndThumnail.thumbnail != null
            previewAndThumnail.thumbnail.imageData.size() > 0
    }
}
