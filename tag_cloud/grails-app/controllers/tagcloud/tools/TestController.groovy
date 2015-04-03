package tagcloud.tools

import com.ctacorp.syndication.Language
import grails.plugin.springsecurity.annotation.Secured
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType

@Secured(['ROLE_ADMIN'])
class TestController {
    def tagService
    def contentItemService

    Random ran = new Random()

    def index() {
        render view:'index'
    }

    def runTests(){
        def results = [
                tagContentItemByCIAndTag(),
                tagContentItemByTagIDAndUrlAndSyndId(),
                findOrSaveTag(),
                getTagTypeById(),
                getTagsForContentItem(),
                getTagsForContentItems(),
                findOrSaveContentItemByUrl(),
                findOrSaveContentItemByUrlAndSyndId(),
                listTags(),
                getMediaForTagId(),
                getMediaForTagIds(),
                tagSyndicatedItem(),
                listTagTypes(),
                getTagsByTypeName(),
                groupTagsByType(),
                getTagsForSyndicationId(),
                getRelatedContentItemsBySyndicationId(),
                getRelatedTagsByTagId(),
                getContentForTagIds()
        ]

        results.each { it ->
            if (it.message == "Unexecuted") {
                render """<span style="color:gray;">[ ] ${it.name} : ${it.message}</span><br/>"""
            } else {
                if (it.success) {
                    render """<span style="color:blue;">${it.message} ${it.name}</span><br/>"""
                } else {
                    render """<span style="color:red;">X ${it.name} : ${it.message}</span><br/>"""
                }
            }
        }
    }

    private tagContentItemByCIAndTag() {
        int i = ran.nextInt(9999999)

        ContentItem ci = new ContentItem(url: "http://www.example.com/${i}", syndicationId: i).save(flush: true)
        Tag tag = new Tag(name: "Tagg${i}", type: TagType.get(1), language: Language.get(1)).save(flush: true)

        ci = tagService.tagContentItem(ci, tag)
        def lookedUp = Tag.get(tag.id)

        def message = "√"

        if (!lookedUp) {
            message = "it didn't persist the tag"
        }

        if (!lookedUp.contentItems.contains(ci)) {
            message = "it didn't add the content item to the tag"
        }

        lookedUp = ContentItem.get(ci.id)

        if (!lookedUp) {
            message = "it didn't persist the content"
        }

        if (!lookedUp.tags.contains(tag)) {
            message = "it didn't add the tag to the item"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }

        [success: success, name: "tagContentItemByCIAndTag", message: message]
    }

    private tagContentItemByTagIDAndUrlAndSyndId() {
        int i = ran.nextInt(9999999)

        ContentItem ci = new ContentItem(url: "http://www.example.com/${i}", syndicationId: i).save(flush: true)
        Tag tag = new Tag(name: "Tagg${i}", type: TagType.get(1), language: Language.get(1)).save(flush: true)

        ContentItem taggedCi = tagService.tagContentItem(ci, tag)

        def message = "√"

        if (!taggedCi.id || !ContentItem.get(taggedCi.id)) {
            message = "it didn't persist a content item"
        }

        taggedCi = ContentItem.get(taggedCi.id)

        if (!taggedCi.tags.contains(tag)) {
            message = "it didn't tag the item"
        }

        tag = Tag.load(tag.id)

        if (!tag) {
            message = "it didn't persist the tag"
        }

        if (!tag.contentItems.contains(taggedCi)) {
            message = "it didn't save the content item on the tag"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }

        [success: success, name: "tagContentItemByTagIDAndUrlAndSyndId", message: message]
    }

    private findOrSaveTag() {
        int i = ran.nextInt(9999999)
        def message = "√"

        Tag t = tagService.findOrSaveTag("Banana${i}", Language.get(1), TagType.get(1))

        boolean success = false

        if(!t){
            message = "Failed to save tag"
        }

        if(t.name != "Banana${i}"){
            message = "Tag name is wrong"
        }

        if (message == "√") {
            success = true
        }
        [success: success, name: "findOrSaveTag", message: message]
    }

    private getTagTypeById() {
        def message = "√"

        TagType tt = tagService.getTagTypeById(1)

        if(tt.id != 1){
            message = "Wrong tag!"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getTagTypeById", message: message]
    }

    private getTagsForContentItem() {
        def message = "√"

        ContentItem.get(1).addToTags(Tag.load(1))
        ContentItem.get(1).addToTags(Tag.load(2))
        ContentItem.get(1).addToTags(Tag.load(3))

        def tags = tagService.getTagsForContentItem(1)

        if(!tags){
            message = "didn't find any tags"
        }

        if(tags.size() < 3){
            message = "All Tags did not associate"
        }

        if(!tags.contains(Tag.load(1)) || !tags.contains(Tag.load(2)) || !tags.contains(Tag.load(3))){
            message = "Correct Tags did not associate"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getTagsForContentItem", message: message]
    }

    private getTagsForContentItems() {
        int i = ran.nextInt(9999999)
        def message = "√"

        ContentItem ci1 = new ContentItem(url: "http://www.example.com/${i}", syndicationId: i).save(flush: true)
        ci1.addToTags(Tag.load(1))
        i = ran.nextInt(9999999)
        ContentItem ci2 = new ContentItem(url: "http://www.example.com/${i}", syndicationId: i).save(flush: true)
        ci2.addToTags(Tag.load(2))
        i = ran.nextInt(9999999)
        ContentItem ci3 = new ContentItem(url: "http://www.example.com/${i}", syndicationId: i).save(flush: true)
        ci3.addToTags(Tag.load(3))

        def tags = tagService.getTagsForContentItems([ci1, ci2, ci3])

        if(!tags){
            message = "no tags found"
        }

        if(tags.size() < 3){
            message = "not enough tags found"
        }

        if(!tags.contains(Tag.load(1)) && !tags.contains(Tag.load(3)) && !tags.contains(Tag.load(2))){
            message = "not all tags associated"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getTagsForContentItems", message: message]
    }

    private findOrSaveContentItemByUrl() {
        int i = ran.nextInt(9999999)
        def message = "√"

        ContentItem ci = contentItemService.findOrSaveContentItem("http://www.example.com/${i}")
        if(!ci?.id){
            message = "didn't create item"
        }

        if(ci.url != "http://www.example.com/${i}"){
            message = "url is wrong"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "findOrSaveContentItemByUrl", message: message]
    }

    private findOrSaveContentItemByUrlAndSyndId() {
        def message = "√"

        int i = ran.nextInt(9999999)

        ContentItem ci = contentItemService.findOrSaveContentItem("http://www.example.com/${i}", i)

        if(!ci?.id){
            message = "didn't create item"
        }

        if(ci.url != "http://www.example.com/${i}"){
            message = "url is wrong"
        }


        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "findOrSaveContentItemByUrlAndSyndId", message: message]
    }

    private listTags() {
        def message = "√"

        def tags = tagService.listTags()

        if(tags.size() < 3){
            message = "list is too small"
        }

        tags = tagService.listTags([max:1])

        if(tags.size() > 1){
            message = "list is too long"
        }

        tags = tagService.listTags([max:1, offset:1])

        if(tags[0].id != 2){
            message = "offset might not be working"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "listTags", message: message]
    }

    private getMediaForTagId() {
        def message = "√"

        def mediaCount = Tag.get(1).contentItems.size()

        def media = contentItemService.getContentForTagId(1)

        if(media.size() != mediaCount){
            message = "wrong content item set"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getMediaForTagId", message: message]
    }

    private getMediaForTagIds() {
        def message = "√"

        def mediaCount = (Tag.get(1).contentItems + Tag.get(2).contentItems).unique{it.id}.size()
        def media = contentItemService.getContentForTagIds("1,2")

        if(media.size() != mediaCount){
            message = "wrong content item set"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getMediaForTagIds", message: message]
    }

    private tagSyndicatedItem() {
        def message = "√"
        int i = ran.nextInt(9999999)
        Tag tag = tagService.findOrSaveTag("Tagggg", Language.load(1), TagType.load(1))
        ContentItem ci = tagService.tagSyndicatedItem(tag.id, "http://www.example.com/${i}", i)

        if(!ci.tags*.name.contains("Tagggg")){
            message = "item wasn't tagged"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "tagSyndicated", message: message]
    }

    private listTagTypes() {
        def message = "√"

        def types = TagType.list()
        def sTypes = tagService.listTagTypes()

        if(!types == sTypes){
            message = "sets don't match!"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "listTagTypes", message: message]
    }

    private getTagsByTypeName() {
        def message = "√"

        TagType type = TagType.get(1)
        def tags = Tag.findAllByType(type)

        def foundTags = tagService.getTagsByTypeName(type.name)

        if(tags != foundTags){
            message = "didn;t find the right set of tags!"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getTagsByTypeName", message: message]
    }

    private groupTagsByType() {
        def message = "√"

        def groupedTags = tagService.groupTagsByType(Tag.list())

        if(Tag.count() != groupedTags.total){
            message = "tag count is off"
        }

        if(Tag.list()*.type.unique().size() != groupedTags.tagGroups.size()){
            message = "not all groups accounted for"
        }

        def combined = []
        groupedTags.tagGroups.each{ key, value ->
            combined.addAll(value)
        }

        if(!combined.containsAll(Tag.list())){
            message = "some tags where missed!"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "groupTagsByType", message: message]
    }

    private getTagsForSyndicationId() {
        def message = "√"

        int i = ran.nextInt(9999999)
        Tag tag1 = tagService.findOrSaveTag("ABABA", Language.load(1), TagType.load(1))
        Tag tag2 = tagService.findOrSaveTag("NGNGNG", Language.load(1), TagType.load(1))
        Tag tag3 = tagService.findOrSaveTag("TUTUTU", Language.load(1), TagType.load(1))
        
        tagService.tagSyndicatedItem(tag1.id, "http://www.example.com/${i}", i)
        tagService.tagSyndicatedItem(tag2.id, "http://www.example.com/${i}", i)
        tagService.tagSyndicatedItem(tag3.id, "http://www.example.com/${i}", i)

        def tags = tagService.getTagsForSyndicationId(i)

        if(!tags.containsAll([tag1, tag2, tag3])){
            message = "Tags where missing!"
        }

        tags = tagService.getTagsForSyndicationId(i, [max:1])

        if(tags.size() != 1){
            message == "max param isn't working"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getTagsForSyndicationId", message: message]
    }

    private getRelatedContentItemsBySyndicationId() {
        def message = "√"

        int i = ran.nextInt(9999999)
        Tag tag1 = tagService.findOrSaveTag("ABABA", Language.load(1), TagType.load(1))
        Tag tag2 = tagService.findOrSaveTag("NGNGNG", Language.load(1), TagType.load(1))
        Tag tag3 = tagService.findOrSaveTag("TUTUTU", Language.load(1), TagType.load(1))
        //1 and 2
        ContentItem ci1 = tagService.tagSyndicatedItem(tag1.id, "http://www.example.com/${i}", i)
        ci1 = tagService.tagSyndicatedItem(tag2.id, "http://www.example.com/${i}", i)

        //2 and 3
        i = ran.nextInt(9999999)
        ContentItem ci2 = tagService.tagSyndicatedItem(tag2.id, "http://www.example.com/${i}", i)
        ci2 = tagService.tagSyndicatedItem(tag3.id, "http://www.example.com/${i}", i)

        //1
        i = ran.nextInt(9999999)
        ContentItem ci3 = tagService.tagSyndicatedItem(tag3.id, "http://www.example.com/${i}", i)

        def items = contentItemService.getRelatedContentItemsBySyndicationId(ci1.syndicationId)

        if(!items.containsAll([ci2])){
            message = "some related items are missing"
        }

        items = contentItemService.getRelatedContentItemsBySyndicationId(ci3.syndicationId)

        if(!items.containsAll([ci2])){
            message = "some related items are missing"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getRelatedMediaIds", message: message]
    }

    private getRelatedTagsByTagId() {
        def message = "√"

        def tags = tagService.getRelatedTagsByTagId(1)

        if(!tags){
            message = "related tags were not found"
        }
        
        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getRelatedMedia", message: message]
    }

    private getContentForTagIds(){
        def message = "√"

        def contentItems = contentItemService.getContentForTagIds("1")

        if(contentItems == null || contentItems.size() == 0){
            message = "no content items returned"
        }

        boolean success = false
        if (message == "√") {
            success = true
        }
        [success: success, name: "getContentForTagIds", message: message]
    }
}