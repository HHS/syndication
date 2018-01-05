package com.ctacorp.tagcloud.controller

import grails.test.mixin.integration.Integration
import com.ctacorp.syndication.Language
import grails.transaction.*
import spock.lang.Specification
import tagcloud.ContentController
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType


/**
 * Created by sgates on 7/29/14.
 */
@Integration
@Rollback
class ContentControllerIntegrationSpec extends Specification {
    def content
    def tags
    def eng
    def topic

    def setup() {
        content = [null]
        tags = [null]

        eng = Language.get(1)
        topic = TagType.get(2)

        def tagNames = ["Apple", "Banana", "Coconut", "Durian", "EggPlant", "Fig", "Grapefruit", "Honeydew"]
        tagNames.each { tagName ->
            tags << new Tag(name: tagName, type: topic, language: eng).save(flush: true)
        }

        (1..10).each {
            def syndId = null
            if (it < 5) {
                syndId = it
            }
            def ci = new ContentItem(url: "http://www.example.com/${it}", syndicationId: syndId)
            content << ci.save(flush: true)
        }

        content[1].addToTags(tags[1])
        content[1].addToTags(tags[2])
        content[2].addToTags(tags[2])
        content[3].addToTags(tags[3])
        content[4].addToTags(tags[4])
    }

    def "index should list content in the system"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "Index is called"
            controller.index()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "it should have at least 4 items"
            controller.response.json.size() >= 4
    }

    def "getRelatedContentBySyndicationId should return related content"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getRelatedContentBySyndicationId is called"
            controller.request.parameters = [syndicationId:"1"]
            controller.getRelatedContentBySyndicationId()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() >= 1
    }

    def "getRelatedContentBySyndicationId should not return anything for invalid id"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getRelatedContentBySyndicationId is called with invalid id"
            controller.request.parameters = [syndicationId:"-1"]
            controller.getRelatedContentBySyndicationId()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be no items"
            controller.response.json.size() == 0
    }

    def "getContentRelatedToUrl should return related items"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getContentRelatedToUrl is called"
            controller.request.parameters = [url:"http://www.example.com/1"]
            controller.getContentRelatedToUrl()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() >= 1
    }

    def "getContentRelatedToUrl shouldn't return anything if the requested url isn't found"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getContentRelatedToUrl is called"
            controller.request.parameters = [url:"http://www.notAUrl.com"]
            controller.getContentRelatedToUrl()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() == 0
    }

    def "getContentRelatedByTagId should return content related to a tag"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getContentRelatedByTagId is called"
            String tagId = tags[1].id
            controller.request.parameters = [tagId:tagId]
            controller.getContentRelatedByTagId()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() >= 1
    }

    def "getContentForTagId should return the content tagged with this tag"(){
        given: "A contentController"
            def controller = new ContentController()
        when: "getContentForTagId is called"
            String tagId = tags[1].id
            controller.request.parameters = [tagId:tagId]
            controller.getContentForTagId()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() >= 1
    }
}