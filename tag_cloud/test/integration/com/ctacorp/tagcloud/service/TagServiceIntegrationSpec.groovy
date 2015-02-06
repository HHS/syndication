package com.ctacorp.tagcloud.service

import com.ctacorp.syndication.Language
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import grails.test.spock.IntegrationSpec
import spock.lang.*
import tagcloud.domain.TagType

class TagServiceIntegrationSpec extends Specification {
    def tagService

    Language eng
    TagType topic

    def tags
    def content

    def setup() {
        content = [null]
        tags = [null]

        eng = Language.get(1)
        topic = TagType.get(2)
        new TagType(name: "OtherType", description: "Some other tag type").save(flush: true)

        def tagNames = ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]
        tagNames.each { tagName ->
            tags << new Tag(name: tagName, type: topic, language: eng).save(flush: true)
        }

        (1..10).each {
            def syndId = null
            if (it < 5) {
                syndId = it
            }
            content << new ContentItem(url: "http://www.example.com/${it}", syndicationId: syndId).save(flush: true)
        }

        content[1].addToTags(tags[1])
        content[1].addToTags(tags[2])
        content[2].addToTags(tags[2])
        content[3].addToTags(tags[3])
        content[4].addToTags(tags[4])
    }

    def "(Sanity Check) there should be instances of required objects in the DB"() {
        expect: "There should be the expected number of each object type before we test"
            Language.count() == 485
            TagType.count() == 4
            Tag.count() == 8
            ContentItem.count() == 10

        and: "and they should contain the values we expect"
            Language.get(1).name == "English"
            Language.get(1).isoCode == "eng"

            TagType.get(1).name == "General"
            TagType.get(1).description == "Default tag type for general purpose tagging."

            TagType.get(2).name == "Topic"
            TagType.get(2).description == "Classification."

            TagType.get(3).name == "Audience"
            TagType.get(3).description == "Targeted group of people."

            Tag.list()*.name.sort() == ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]

        and: "some content items should be tagged"
            Tag.get(tags[1].id).contentItems.contains(ContentItem.get(content[1].id))
            Tag.get(tags[2].id).contentItems.contains(ContentItem.get(content[2].id))
            Tag.get(tags[3].id).contentItems.contains(ContentItem.get(content[3].id))
            Tag.get(tags[4].id).contentItems.contains(ContentItem.get(content[4].id))
    }

    //Faceted Search Tests
    def "faceted search by typeId should return records"() {
        expect: "searching for tags by typeId should return records"
            tagService.listTags([typeId: 2, long: { str -> 2L }]).size() > 0
        and: "invalid typeIds should not return records"
            tagService.listTags([typeId: -1, long: { str -> -1L }]).size() == 0
    }

    def "faceted search by typeNameContains should allow partial searches by name"() {
        expect: "searching by typeName should return records"
            tagService.listTags(typeNameContains: "Top").size() == 8
        and: "invalid partial names should return no records"
            tagService.listTags([typeName: "ZZ"]).size() == 0
    }

    def "faceted search by URL should find expected records"() {
        expect: "searching by url should find at least one record"
            tagService.listTags([url: "http://www.example.com/1"]).size() > 0
        and: "the record found should be the expected record"
            tagService.listTags([url: "http://www.example.com/1"])[0].contentItems.contains(content[1])
    }

    def "faceted search by partial url should find records"() {
        expect: "searching by partial url should have at least 1 match"
            tagService.listTags([urlContains: "http://www"]).size() > 0
    }

    def "listTags should list all the tags"() {
        expect: "when we call listTags, all the items should be listed"
            def tags = tagService.listTags()
            tags.size() == 8
    }

    def "getTagsForSyndicationId should return tags for an ID known to be tagged"() {
        expect:
            tagService.getTagsForSyndicationId(1)*.id.sort() == [tags[1].id, tags[2].id]
    }

    def "getRelatedTagsByTagId should return related tags"(){
        expect:
            tagService.getRelatedTagsByTagId(tags[1].id)
    }
}