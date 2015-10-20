package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.test.spock.IntegrationSpec

class MediaServiceIntegrationSpec extends IntegrationSpec {
    def mediaService
    Source source

    def setup() {
        source = new Source(name:"Org Inc", acronym: "OI", websiteUrl:"http://www.example.gov").save(flush:true)
        assert source
        def eng = new Language(name:"English", isoCode: "eng").save()
        def spa = new Language(name:"Spanish", isoCode: "spa").save(flush:true)
        assert Language.findByIsoCode("eng")
        assert Language.findByIsoCode("spa")
    }

    def cleanup() {
    }

    /*
    This needs to be an integration test, because the queries rely on hibernate criteria builder, which doesn't seemed to
    get mocked by the grails mocking system for unit tests. The work of mocking all that doesn't seem to be worth it!
     */
    def "listNewestMedia which should not include spanish content"() {
        given: "two items with one being spanish"
            def eng = Language.findByIsoCode("eng")
            def spa = Language.findByIsoCode("spa")
            def englishMedia = new MediaItem(name: "Some Item", sourceUrl: "http://www.example.com/jhgfjhg", language: eng, source: source, visibleInStorefront: true, active: true).save(flush: true)
            def spanishMedia = new MediaItem(name: "Some Item2", sourceUrl: "http://www.example.com/fdfdfdf", language: spa, source: source, visibleInStorefront: true, active: true).save(flush: true)
            new FeaturedMedia(mediaItem: englishMedia).save(flush: true)
            new FeaturedMedia(mediaItem: spanishMedia).save(flush: true)
        when: "listNewestMedia is called"
            def media = mediaService.listNewestMedia([max: 10, offset: 0])
        then: "spanish content should be excluded"
            !(spanishMedia in media)
        and: "english content should be in newest"
            englishMedia in media
    }
}
