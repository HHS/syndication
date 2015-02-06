package com.ctacorp.tagcloud.controller

import com.ctacorp.syndication.Language
import tagcloud.TagsController
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType
import spock.lang.Specification



/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/16/14
 * Time: 1:41 PM
 */
class TagsControllerIntegrationSpec extends Specification {
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

    def "index should return a list of tags in json format"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "index action is called"
            controller.index()
        then: "There should be a response"
            controller.response.contentAsString != null
        and: "it should be json"
            controller.response.json != null
        and: "there should be at least 1 item"
            controller.response.json.size() == 8
    }

    def "index response should contain names, languages, and types"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "index action is called"
            controller.index()
        then: "the extra fields should be available"
            def j = controller.response.json
            j[0].containsKey("name")
            j[0].containsKey("language")
            j[0].language.containsKey("name")
            j[0].language.containsKey("id")
            j[0].containsKey("type")
            j[0].type.containsKey("name")
            j[0].type.containsKey("id")
    }

    def "index should include additional fields if pagination data is requested"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "index action is called"
            controller.request.parameters = [includePaginationFields:"true"]
            controller.index()
        then: "the expected fields should be available"
            def j = controller.response.json
            j.total != null
            j.dataSize != null
            j.max != null
            j.offset != null
    }

    def "delete tag should delete a tag and return json"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "index action is called"
            controller.request.method = "DELETE"
            controller.deleteTag(tags[1].id)
        then: "there should be a json response"
            controller.response.json != null
        and: "it should report success"
            controller.response.json.success == "Tag was deleted."
    }

    def "show action should show a tag in json format"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "show action is called"
            controller.request.method = "GET"
            String id = "${tags[1].id}"
            controller.request.parameters = [id:id]
            controller.show()
        then: "the expected fields should be available"
            def j = controller.response.json
            j.containsKey("class")
            j.containsKey("contentItems")
            j.containsKey("dateCreated")
            j.containsKey("id")
            j.containsKey("language")
            j.containsKey("lastUpdated")
            j.containsKey("name")
            j.containsKey("type")
    }

    def "findOrSaveTag should find existing tags"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "show action is called"
            controller.request.method = "POST"
            controller.request.parameters = [name: "Apple", type: "2", language: "1"]
            controller.findOrSaveTag()
        then: "There should be a json response"
            def j = controller.response.json
        and: "It should contain the correct tag info"
            j.id != null
            j.id == tags[1].id
            j.name == "Apple"
            j.type.id == 2
    }

    def "tagUrlByTagId should tag a url with a tag"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "tagUrlByTagId is called"
            controller.request.method = "POST"
            String tagId = tags[1].id
            controller.request.parameters = [
                    url:"http://www.someUrlAboutSomething.com",
                    tagId:tagId
            ]
            controller.tagUrlByTagId()
        then: "There should be a response showing the content item and tags"
            def j = controller.response.json
            j.id != null
            j.url == "http://www.someUrlAboutSomething.com"
            j.tags[0].id == tags[1].id
    }

    def "getTagsForSyndicationId should list tags associated with an id in json format"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "getTagsForSyndicationId action is called"
            controller.request.method = "GET"
            String syndicationId = "1"
            controller.request.parameters = [syndicationId:syndicationId]
            controller.getTagsForSyndicationId()
        then: "the expected fields should be available"
            def tagList = controller.response.json
            tagList[0].containsKey("name")
            tagList[0].containsKey("language")
            tagList[0].language.containsKey("name")
            tagList[0].language.containsKey("id")
            tagList[0].containsKey("type")
            tagList[0].type.containsKey("name")
            tagList[0].type.containsKey("id")
    }

    def "tagSyndicatedItemByTagIds should tag an item"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "tagSyndicatedItemByTagIds action is called"
            controller.request.method = "POST"
            controller.request.parameters = [syndicationId:"123456789", url:"http://www.example.com/jhgfjhgf654", tagIds:"${tags[1].id}" as String]
            controller.tagSyndicatedItemByTagIds()
        then: "there should be a json response (showing the content item)"
            controller.response.json != null
        and:  "it should have the expected fields"
            def j = controller.response.json
            j.containsKey("tags")
            j.containsKey("id")
            j.containsKey("syndicationId")
            j.containsKey("externalUID")
            j.containsKey("url")
    }

    def "tagSyndicatedItemByTagName should take a tag name and a syndicated item, and then tag that item"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "tagSyndicatedItemByTagName action is called"
            controller.request.method = "POST"
            controller.request.parameters = [
                syndicationId:"123456789",
                url:"http://www.example.com/jhgfjhgf654",
                tagName:"ThisIsATagName",
                typeId:"${topic.id}" as String,
                languageId:"${eng.id}"  as String
            ]
            controller.tagSyndicatedItemByTagName()
        then: "there should be a json response (showing the content item)"
            controller.response.json != null
        and:  "it should have the expected fields"
            def j = controller.response.json
            j.containsKey("tags")
            j.containsKey("id")
            j.containsKey("syndicationId")
            j.containsKey("externalUID")
            j.containsKey("url")
        and: "it should contain our new tag"
            controller.response.json.tags*.id[0] == Tag.findByName("ThisIsATagName").id
    }

    def "getTagsByTypeName should return a list of tags which belong to the type referenced by name"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "getTagsByTypeName action is called"
            controller.request.method = "GET"
            controller.request.parameters = [typeName:"Topic"]
            controller.getTagsByTypeName()
        then: "tags should be returned"
            def j = controller.response.json
            j.size() > 0
        and: "the types should only be topic"
            j*.type.unique()*.name == ["Topic"]
    }

    def "getTagsByTypeName should send 400 if typeName param is missing"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "getTagsByTypeName action is called"
            controller.request.method = "GET"
            controller.getTagsByTypeName()
        then: "There should be a 400 response"
            controller.response.status == 400
    }

    def "getTagsRelatedToTagId should return all tags related to a given tag"(){
        given: "a tagsController"
            TagsController controller = new TagsController()
        when: "getTagsRelatedToTagId action is called"
            controller.request.method = "GET"
            controller.request.parameters = [tagId:"${tags[1].id}" as String]
            controller.getTagsRelatedToTagId()
        then: "the expected tags should be returned"
            def j = controller.response.json
            j*.id[0] == tags[2].id
    }

    def "setTagsForSyndicatedItemByTagIds should update the current set of tags for a syndication id"(){
        given: "an item with known tags, and a controller instance"
            ContentItem ci = ContentItem.get(content[1].id)
            ci.tags = [tags[1], tags[2]]
            ci.save(flush:true)
            def controller = new TagsController()
        when: "when the controller action is called"
            String tagIds = "${tags[3].id},${tags[4].id}"
            controller.request.method = "POST"
            controller.request.parameters = [
                    syndicationId:"1",
                    url:"http://www.example.com/1",
                    tagTypeId:"2",
                    languageId:"1",
                    tagIds:tagIds as String
            ]
            controller.setTagsForSyndicatedItemByTagIds()
        then: "it should update the tags to the new set"
            def j = controller.response.json
                (j.tags[0].id as Long) in [tags[3].id as Long, tags[4].id as Long]
                (j.tags[1].id as Long) in [tags[3].id as Long, tags[4].id as Long]

    }

    def "tagSyndicatedItemsByTagIds should tag multiple items with multiple tags"(){
        given: "content items with no tags, and a controller instance"
            ContentItem ci1 = ContentItem.get(content[1].id)
            ContentItem ci2 = ContentItem.get(content[2].id)
            ci1.tags = []
            ci2.tags = []
            ci1.save(flush:true)
            ci2.save(flush:true)
            def controller = new TagsController()
        when: "when the controller action is called"
            String tagIds = "${tags[3].id},${tags[4].id}"
            controller.request.method = "POST"
            controller.request.parameters = [
                syndicationIds:"1,2",
                urls:"http://www.example.com/1,http://www.example.com/2",
                tagIds:tagIds
            ]
            controller.tagSyndicatedItemsByTagIds()
        then: "the content items should have the expected number of items"
            def j = controller.response.json
            j.size() == 2
        and: "The correct items should be there"
            j[0].id == content[1].id
            j[1].id == content[2].id
        and: "The expected tags should appear on all 4"
            def tags = [tags[3].id as Long, tags[4].id as Long]
            (j[0].tags[0].id as Long) in tags
            (j[0].tags[1].id as Long) in tags
            (j[1].tags[0].id as Long) in tags
            (j[1].tags[1].id as Long) in tags
    }
}