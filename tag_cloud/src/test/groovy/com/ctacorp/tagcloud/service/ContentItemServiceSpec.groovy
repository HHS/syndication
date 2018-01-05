package com.ctacorp.tagcloud.service

import com.ctacorp.syndication.Language
import com.ctacorp.tagcloud.ContentItemService
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/9/14
 * Time: 10:10 AM
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(ContentItemService)
@Mock([ContentItem, Tag, Language, TagType])
@Build([ContentItem, Tag, Language, TagType])
class ContentItemServiceSpec extends Specification {
    Language eng
    TagType topic

    def setup(){
        eng   = new Language(name:"English", isoCode: "eng").save(flush:true)
        topic = new TagType(name:"Topic", description:"Tags associated with a topic").save(flush:true)
        new TagType(name:"OtherType", description:"Some other tag type").save(flush:true)

        def tagNames = ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]
        tagNames.each{ tagName ->
            new Tag(name:tagName, type:topic, language: eng).save(flush:true)
        }

        (1..10).each{
            def syndId = null
            if(it < 5){
                syndId = it
            }
            new ContentItem(url:"http://www.example.com/${it}", syndicationId: syndId).save(flush:true)
        }

        ContentItem.get(1).addToTags(Tag.load(1))
        Tag.get(1).addToContentItems(ContentItem.get(1))
        ContentItem.get(1).addToTags(Tag.load(2))
        Tag.get(2).addToContentItems(ContentItem.get(1))
        ContentItem.get(2).addToTags(Tag.load(2))
        Tag.get(2).addToContentItems(ContentItem.get(2))
        ContentItem.get(3).addToTags(Tag.load(3))
        Tag.get(3).addToContentItems(ContentItem.get(3))
        ContentItem.get(4).addToTags(Tag.load(4))
        Tag.get(4).addToContentItems(ContentItem.get(4))
    }

    Map populateValidParams(params = [:]) {
        assert params != null
        //mediaItem required attributes
        params.int = { String value -> params."$value" }
        params
    }

    def "(Sanity Check) there should be instances of required objects in the DB"(){
        expect: "There should be the expected number of each object type before we test"
            Language.count() == 1
            TagType.count() == 2
            Tag.count() == 8
            ContentItem.count() == 10

        and: "and they should contain the values we expect"
            Language.get(1).name == "English"
            Language.get(1).isoCode == "eng"

            TagType.get(1).name == "Topic"
            TagType.get(1).description == "Tags associated with a topic"

            Tag.list()*.name.sort() == ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]

        and: "some content items should be tagged"
            Tag.get(1).contentItems.contains(ContentItem.get(1))
            Tag.get(2).contentItems.contains(ContentItem.get(2))
            Tag.get(3).contentItems.contains(ContentItem.get(3))
            Tag.get(4).contentItems.contains(ContentItem.get(4))
    }

    def "deleting untagged content items should be allowed"(){
        given: "a fresh new content item"
            ContentItem ci = new ContentItem(url:"http://www.example.com/ASDFGHJK", syndicationId: 12345678)
            ci.save(flush:true)
            ci.id != null
            ContentItem.count() == 11
        when: "The tag is deleted with the service"
            service.delete(ci)
        then: "we should be back to 10 items"
            ContentItem.count() == 10
    }

    def "findOrSaveContentItem should create new content items if they don't exist"(){
        expect: "if a url doesn't exist, a new content item should be created"
            def ci = service.findOrSaveContentItem("http://www.google.com")
            ci.id != null
            ci.url == "http://www.google.com"
    }

    def "findOrSaveContentItem should return null if the item cannot be created"(){
        expect: "if the url is bogus, it shouldn't work"
            service.findOrSaveContentItem("not a url") == null
    }

    def "findOrSaveContentItem with a syndication ID should create a new record if it doesn't exist [case 1]"(){
        expect: "if a new url is provided with synd id, new content should be created"
            def ci = service.findOrSaveContentItem("http://www.google.com", 1234)
            ci.id != null
            ci.url == "http://www.google.com"
            ci.syndicationId == 1234
    }

    def "findOrSaveContentItem should load content items if they do exist [case 2]"(){
        expect: "if a known url is supplied, existing item should be loaded"
            def ci = service.findOrSaveContentItem("http://www.example.com/1")
            ci.id == 1
    }

    def "findOrSaveContentItem with a syndication ID should add an id to an existing url without an id [case 3]"(){
        expect: "if a new url is provided with synd id, new content should be created"
            ContentItem.get(5).syndicationId == null    // item without synd id
            def ci = service.findOrSaveContentItem("http://www.example.com/5", 1234)
            ci.id != null
            ci.url == "http://www.example.com/5"
            ci.syndicationId == 1234
            ContentItem.get(5).syndicationId == 1234
    }

    def "findOrSaveContentItem with a syndication ID and a different url should update url on existing item [case 4]"(){
        expect: "if a new url is provided for an item with synd id and existing url, update the url"
            ContentItem.get(1).syndicationId == 1
            ContentItem.get(1).url == "http://www.example.com/1"
            def ci = service.findOrSaveContentItem("http://www.example.com/ABC", 1)
            ci.url == "http://www.example.com/ABC"
            ci.syndicationId == 1
            ContentItem.get(1).syndicationId == 1
            ContentItem.get(1).url == "http://www.example.com/ABC"
    }

    def "getContentForTagId should return content for tags which have content"(){
        expect:
            service.getContentForTagId(1, populateValidParams([:])).size() > 0
    }

    def "getContentForTagId should return null for invalid tag ids"(){
        expect:
            service.getContentForTagId(-1, populateValidParams([:])) == null
    }
}