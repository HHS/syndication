package contentextractionservices

import com.ctacorp.syndication.contentextraction.YoutubeService
import com.ctacorp.syndication.media.Video
import grails.test.mixin.integration.Integration
import spock.lang.Specification

@Integration
class YoutubeServiceIntegrationSpec extends Specification {

    YoutubeService youtubeService
    String youtubeUrl
    String youtubeId

    def setup() {
        youtubeUrl = "https://www.youtube.com/watch?v=9APKBBr18Cc"
        youtubeId = "9APKBBr18Cc"
    }

    void "test video instance is null when a bogus url is provided"() {
        when:
            Video video = youtubeService.getVideoInstanceFromUrl("http://akdkadjhakjd")
        then:
            video == null
    }

    void "test valid Video instance is created when a youtube url is provided"() {
        when:
        Video video = youtubeService.getVideoInstanceFromUrl(youtubeUrl)

        then:
            video != null
        and:
            video.name == "Do your part to stop the spread of flu at home"
        and:
            video.sourceUrl == youtubeUrl
    }

    void "test a valid thumbnail url is generated when a youtube url is provided"() {
        when:
            String thumbnailUrl = youtubeService.thumbnailLinkForUrl(youtubeUrl)
        then:
            thumbnailUrl.contains(youtubeId) == true
    }

    void "embedded code is generated when a youtube url is provided"() {
        when:
            String embeddedCode = youtubeService.getIframeEmbedCode(youtubeUrl)
        then:
            embeddedCode.startsWith("<iframe") == true
        and:
            embeddedCode.contains(youtubeId) == true
        and:
            embeddedCode.contains("origin=http://syndication.hhs.gov") == true
        and:
            embeddedCode.endsWith("</iframe>") == true
    }
}
