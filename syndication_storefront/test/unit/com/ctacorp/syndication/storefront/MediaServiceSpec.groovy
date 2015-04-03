package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.SocialMedia
import grails.plugins.rest.client.RestBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MediaService)
@Mock([FeaturedMedia, MediaItem, Language])
class MediaServiceSpec extends Specification {


    def setup() {
        service.grailsApplication = [domainClasses:[[clazz:Html], [clazz:SocialMedia]]]
        def mi1 = new MediaItem(name:"Some Item", description: "scared from ebola", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
        RestBuilder.metaClass.get = {String s -> [json:[results:mi1]]}
    }

    def cleanup() {
    }

    void "test something"() {
    }

    def "get featured media"() {
        given:"items that are featured"
            def mi1 = new MediaItem(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            def mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/fdfdfdf", language: new Language(), source: new Source()).save(flush:true)
            new FeaturedMedia(mediaItem:mi1).save(flush: true)
            new FeaturedMedia(mediaItem:mi2).save(flush: true)

        when:"get featured media is called"
            def featuredMedia = service.getFeaturedMedia()

        then:"the featrued media should be returned"
            featuredMedia == [mi1,mi2]
    }

    //TODO: figure out why ignoreCase:true doesn't work for facetedSearch
    def "listNewestMedia which should not include spanish content"() {
//        given:"two items with one being spanish"
//        def lang1 = new Language(name:"english", isoCode: "eng", isActive: true).save(flush: true)
//        def lang2 = new Language(name:"spanish", isoCode: "spa", isActive: true).save(flush: true)
//            def mi1 = new MediaItem(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: lang1, source: new Source(), visibleInStorefront:true,active: true).save(flush:true)
//            def mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/fdfdfdf", language: lang2, source: new Source(), visibleInStorefront:true,active: true).save(flush:true)
//            new FeaturedMedia(mediaItem:mi1).save(flush: true)
//            new FeaturedMedia(mediaItem:mi2).save(flush: true)
//        when:"listNewestMedia is called"
//            def media = service.listNewestMedia([max:10,offset: 0])
//        then:"spanish content should be excluded"
//            media == [mi1]
    }

    def "solr search"() {
        when:"solr search is called"
            def mi1 = new MediaItem(name:"Some Item", description: "scared from ebola", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            def mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/fdfdfdf", language: new Language(), source: new Source()).save(flush:true)
        def result = service.mediaItemSolrSearch("search string")
        then:"the right items should be returned"
            result.results.name == [results:mi1].results.name
    }

    def "get Media Types"() {
        when:"get mediaTypes is called"
            def types = service.getMediaTypes()
        then:"list all of the current types"
            types == ["Html", "Social Media"]
    }

}
