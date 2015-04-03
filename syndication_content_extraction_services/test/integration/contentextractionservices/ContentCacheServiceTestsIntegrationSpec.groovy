package contentextractionservices

import com.ctacorp.syndication.contentextraction.ContentCacheService
import grails.test.spock.IntegrationSpec
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.cache.CachedContent

class ContentCacheServiceTestsIntegrationSpec extends IntegrationSpec {
    ContentCacheService contentCacheService

    def setup() {
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
        assert html.id
        assert CachedContent.count() == 0
    }

    def cleanup() {
    }

    void "sanity test"() {
        expect: "There should be the correct number of each object"
            Language.count() == 1
            Source.count() == 1
            Html.count() == 1
    }

    void "cache service caches a new item successfully"(){
        when: "An existing item is cached"
            contentCacheService.cache(Html.first())
        then: "There should be 1 cached item"
            CachedContent.count() == 1
        and: "The content should be cached"
            contentCacheService.get(Html.first()).content != null
            contentCacheService.get(Html.first()).content.size() > 0
    }

    void "caching by media and id successfully caches an item"(){
        when: "An existing item is cached"
            String content = "<div class='syndicate'>Hi There!</div>"
            contentCacheService.cache(Html.first(), content)
        then: "There should be 1 cached item"
            CachedContent.count() == 1
        and: "The content should be cached"
            contentCacheService.get(Html.first()).content != null
            contentCacheService.get(Html.first()).content.size() > 0
            contentCacheService.get(Html.first()).content == content
    }

    void "a cached item can be returned"(){
        when: "An existing item is cached"
            contentCacheService.cache(Html.first())
        then: "the item should be available via cache"
            def cached = contentCacheService.get(Html.first())
            cached != null
            cached.content != null
            cached.content.size() > 0
    }

    void "when a cached item is flushed by media item - that it goes away"(){
        when: "An existing item is cached"
            contentCacheService.cache(Html.first())
        then: "The the cache is flushed"
            contentCacheService.flush(Html.first())
        and: "now it should be null"
            contentCacheService.get(Html.first()) == null
    }

    void "flush by url flushes a cached item"(){
        when: "An existing item is cached"
            contentCacheService.cache(Html.first())
        then: "The the cache is flushed"
            contentCacheService.flush(Html.first().sourceUrl)
        and: "now it should be null"
            contentCacheService.get(Html.first()) == null
    }

    void "flushall flushes all cached items"(){
        when: "An existing item is cached"
            contentCacheService.cache(Html.first())
        then: "The the cache is flushed"
            contentCacheService.flushAll()
        and: "now it should be null"
            contentCacheService.get(Html.first()) == null
            CachedContent.count() == 0
    }
}
