package com.ctacorp.tagcloud.service

import com.ctacorp.syndication.Language
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import grails.test.spock.IntegrationSpec
import spock.lang.Specification
import tagcloud.domain.TagType


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/12/14
 * Time: 3:20 PM
 */
class ContentItemServiceIntegrationSpec extends Specification {
    def contentItemService

    Language eng
    TagType topic

    def tags
    def content

    def setup() {
        content = [null]
        tags = [null]

        eng   = Language.get(1)
        topic = TagType.get(2)
        new TagType(name:"OtherType", description:"Some other tag type").save(flush:true)

        def tagNames = ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]
        tagNames.each{ tagName ->
            tags << new Tag(name:tagName, type:topic, language: eng).save(flush:true)
        }

        (1..10).each{
            def syndId = null
            if(it < 5){
                syndId = it
            }
            content << new ContentItem(url:"http://www.example.com/${it}", syndicationId: syndId).save(flush:true)
        }

        content[1].addToTags(tags[1])
        content[1].addToTags(tags[2])
        content[2].addToTags(tags[2])
        content[3].addToTags(tags[3])
        content[4].addToTags(tags[4])
    }

    def "findOrSaveContentItem with id and url should return null if the content item cannot be created"(){
        expect: "if the url is bogus, it shouldn't work"
            contentItemService.findOrSaveContentItem("not a url", 1) == null
    }

    def "getContentForTagIds should work with a single id"(){
        expect:
            contentItemService.getContentForTagIds("${tags[1].id}")*.id.containsAll(Tag.get(tags[1].id).contentItems*.id)
    }

    def "getContentForTagIds should work for multiple ids"(){
        expect:
            contentItemService.getContentForTagIds("${tags[1].id},${tags[2].id}").size() > 0
        and: "more tags should be returned with 2 ids than 1"
            contentItemService.getContentForTagIds("${tags[1].id},${tags[2].id}").size() > contentItemService.getContentForTagIds("${tags[1].id}").size()
    }

    def "deleting tagged items should be allowed"(){
        given: "a tagged content item"
            ContentItem ci = new ContentItem(url:"http://www.example.com/asdfgh").save(flush:true)
            Tag tag = new Tag(name:"SomeTag", type:TagType.load(1), language: Language.load(1)).save(flush:true)
            ci.addToTags(tag)
            ci = ContentItem.get(ci.id)

        when: "the content item is deleted with the service"
            ContentItem.count() == 11
            ci.tags.size() > 0
            contentItemService.delete(ci)

        then: "the item should actually delete"
            ContentItem.count() == 10
    }

    //This is an integration test because 'def tagIds = ci.tags*.id' causes a cast exception in unit test mode (from service)
    def "getRelatedContentItemsBySyndicationId should return related items"(){
        expect: "synd id 1 and 2 share a tag, they should be related"
            contentItemService.getRelatedContentItemsBySyndicationId(1)*.id.contains(content[2].id)
        and:
            contentItemService.getRelatedContentItemsBySyndicationId(2)*.id.contains(content[1].id)
    }
}