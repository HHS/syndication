package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import grails.buildtestdata.mixin.Build
import grails.plugins.rest.client.RestBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MediaService)
@Mock([FeaturedMedia, MediaItem, Language])
@Build([MediaItem])
class MediaServiceSpec extends Specification {

    def setup() {
        service.grailsApplication = [domainClasses: [[clazz: Html]]]
        def mi1 = new MediaItem(name: "Some Item", description: "scared from ebola", sourceUrl: "http://www.example.com/1", language: new Language(), source: new Source()).save(flush: true)
        RestBuilder.metaClass.get = { String s -> [json: [results: mi1]] }
    }

    def cleanup() {
    }

    def "get featured media"() {
        given: "items that are featured"
            def mi1 = new MediaItem(name: "Some Item", sourceUrl: "http://www.example.com/2", language: new Language(), source: new Source()).save(flush: true)
            def mi2 = new MediaItem(name: "Some Item2", sourceUrl: "http://www.example.com/3", language: new Language(), source: new Source()).save(flush: true)
            new FeaturedMedia(mediaItem: mi1).save(flush: true)
            new FeaturedMedia(mediaItem: mi2).save(flush: true)

        when: "get featured media is called"
            def featuredMedia = service.getFeaturedMedia()

        then: "the featured media should be returned"
            featuredMedia == [mi1, mi2]
    }

    def "get Media Types"() {
        when: "get mediaTypes is called"
            def types = service.getMediaTypes()*.name
        then: "list all of the current types"
            types == ["Article", "BlogPosting", "Html", "NewsArticle"]
    }

    def "findMediaByAll should find media by multiple criteria"() {
        given: "a title to search for"
            def params = [title:"Some"]
        when: "findMediaByAll is called"
            def results = service.findMediaByAll(params)
        then: "it should find results"
            results
    }

    def "mediaItemSolrSearch should return an empty list when solr cannot be reached"() {
        when: "search is called"
            def results = service.mediaItemSolrSearch("something")
        then: "it should return an empty list"
            results == []
    }

    def "mediaItemSolrSearch should return a list of media items"() {
        given: "a mocked solr service"
            service.solrSearchService = [search:{q,t->[getResults:{ [[id:"MEDIAITEM-1"]] }]}]
        when: "mediaItemSolrSearch is called"
            def results = service.mediaItemSolrSearch("something")
        then:
            results == [MediaItem.first()]
    }

    def "listNewestMedia test"() {
        //This has to be mocked because https://jira.grails.org/browse/GRAILS-8841
        given: "a mocked faceted search"
            MediaItem.metaClass.static.facetedSearch = { params -> [list:{p->[MediaItem.build()]}]}
        when: "service is called"
            def results = service.listNewestMedia([:])
        then: "there should be results"
            results
    }
}