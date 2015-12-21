/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package tagcloud

import com.ctacorp.syndication.Language
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType
import com.ctacorp.syndication.commons.util.Util

@Secured(['permitAll'])
class TagsController {
    static transactional = false
    static responseFormats = ['json']
    static allowedMethods = [
        index:'GET', show:'GET', getTagsForSyndicationId:'GET',
        getTagsByTypeName:'GET',
        deleteTag: 'DELETE', tagSyndicatedItemByName:'POST', findOrSaveTag:'POST',
        tagItemByTagId: 'POST', setTagsForSyndicatedItemByTagIds: 'POST', updateTag: 'POST',
        getTagsRelatedToTagId:'GET', tagUrlByTagId: 'POST', tagSyndicatedItemsByTagIds: 'POST',
        tagSyndicatedItemByTagName:'POST', findTagsByTagName:'GET'
    ]

    def tagService
    def contentItemService

    def index() {
        params.max = Math.min(params.int('max') ?: 20, 100000)
        JSON.use("tagList") {
            if (Util.isTrue(params.includePaginationFields, false)) {
                def tags = tagService.listTags(params)
                respond([tags: tags, total: tags.totalCount, dataSize: tags.size(), max: params.max, offset: params.offset?:0])
            } else {
                respond tagService.listTags(params) ?: []
            }
        }
    }

    def allSyndicationIds(){
        JSON.use("syndicationIdList") {
            render ContentItem.list() as JSON
        }
    }

    def deleteTag(Long id){
        tagService.deleteTag(id)
        Tag deletedTag = Tag.get(id)
        if(deletedTag){
            render text:[error:"Tag could not be deleted!"] as JSON, contentType: "application/json"
        } else{
            render text:[success:"Tag was deleted."] as JSON, contentType: "application/json"
        }
    }

    def findOrSaveTag(Tag tagInstance){
        Tag existing = Tag.findByNameAndLanguageAndType(tagInstance.name, tagInstance.language, tagInstance.type)
        if(!existing){
            if(tagInstance.hasErrors()){
                respond tagInstance.errors
                return
            }
            existing = tagInstance.save(flush:true)
        }

        JSON.use("showTag") {
            respond existing
        }
    }

    def show(Tag tagInstance) {
        JSON.use("showTag") {
            respond tagInstance
        }
    }

    def query(String q){
        def matchingTags = Tag.findAllByNameIlike("%${q}%", [max:20, sort:'name', order:'ASC'])
        JSON.use("tagList"){
            respond matchingTags
        }
    }

    def updateTag(Tag tagInstance){
        if (tagInstance == null) {
            render(text:[success:false, message:"Record not found"] as JSON, status: 400, contentType: "application/json")
            return
        }

        if (tagInstance.hasErrors()) {
            render(text:[success:false, message:"Invalid Values", details:tagInstance.errors] as JSON, status: 400, contentType: "application/json")
            return
        }

        Language newlanguage = Language.findById(params.languageId)
        TagType newTagType = TagType.findById(params.tagTypeId)
        tagInstance.setLanguage(newlanguage)
        tagInstance.setType(newTagType)
        tagInstance.save flush:true

        JSON.use("showTag") {
            respond tagInstance
        }
    }

    def getTagsForSyndicationId(Long syndicationId) {
        params.max = Util.getMax(params)
        JSON.use("tagList") {
            def tags = tagService.getTagsForSyndicationId(syndicationId, params)
            if (Util.isTrue(params.includePaginationFields, false)) {
                respond([tags: tags, total: tags.totalCount, max: params.max, offset: params.offset?:0])
            } else {
                respond tags
            }
        }
    }

    def tagUrlByTagId(){
        def data = request.JSON
        String url  = data.url
        Long tagId = data.tagId as Long

        Tag tag = tagService.getTag(tagId)
        ContentItem contentItem = contentItemService.findOrSaveContentItem(url)

        JSON.use("showContentItem"){
            respond tagService.tagContentItem(contentItem, tag)
        }
    }

    def tagSyndicatedItemByTagIds() {
        def data = request.JSON
        Long syndicationId = data.syndicationId as Long
        String url = data.url
        data.tagIds.split(",").collect{it as Long}.each{ Long tagId ->
            tagService.tagSyndicatedItem(tagId as Long, url, syndicationId)
        }

        JSON.use("showContentItem") {
            respond ContentItem.findBySyndicationId(syndicationId)
        }
    }

    def tagSyndicatedItemsByTagIds() {
        def data = request.JSON
        String syndicationIds = data.syndicationIds
        String urls = data.urls
        String tagIds = data.tagIds

        def taggedItems = []
        if(syndicationIds && urls && tagIds) {
            def tIds = tagIds.split(",").collect { it as Long }
            def sIds = syndicationIds.split(",").collect { it as Long }
            def sUrls = urls.split(",")

            for (i in 0..tIds.size() - 1) {
                for (j in 0..sIds.size() - 1) {
                    taggedItems << tagService.tagSyndicatedItem(tIds[i], sUrls[j], sIds[j])
                }
            }
        }
        JSON.use("showContentItem") {
            respond taggedItems.unique()
        }
    }

    def bulkTag(){
        def data = request.JSON
        log.info "Bulk tag request for: ${data}"
        def taggedItems = []
        data.bulkTags.each{ tagEntry ->
            log.debug tagEntry
            tagEntry.value.tagNames.each{ String tagName ->
                log.debug tagName
                taggedItems << tagService.tagSyndicatedItemByName(
                        tagName,
                        tagEntry.value.url as String,
                        tagEntry.key as Long,
                        data.language as Long,
                        data.tagType as Long)
            }
        }
        JSON.use("showContentItem") {
            render taggedItems as JSON
        }
    }

    def tagSyndicatedItemByTagName() {
        def data = request.JSON

        ContentItem ci = tagService.tagSyndicatedItemByName(
                data.tagName as String,
                data.url as String,
                data.syndicationId as Long,
                data.languageId as Long,
                data.typeId as Long)
        if(!ci){
            response.sendError(400, "item could not be created")
            String code = Long.toString(System.nanoTime(), 32)
            log.error("${code} tagSyndicatedItemByName failed with syndicationId:${data.syndicationId}," +
                    " url:${data.url}, " +
                    "tagName:${data.tagName}, " +
                    "typeId:${data.typeId}, " +
                    "languageId:${data.languageId}")
            return
        }
        JSON.use("showContentItem") {
            respond ci
        }
    }

    //This is a pretty naive way of doing this, if it becomes a problem, we should do
    //more than just clear all tags and re-tag
    def setTagsForSyndicatedItemByTagIds() {
        def data = request.JSON
        Long syndicationId = data.syndicationId
        String url = data.url
        String tagIds = data.tagIds
        Long tagTypeId = data.tagTypeId as Long
        Long languageId = data.languageId as Long
        ContentItem ci = null

        if(tagIds.size() == 0){
            ci = tagService.clearTags(url, syndicationId, tagTypeId, languageId)
        } else {
            def tIds = tagIds.split(",").collect { it as Long }
            ci = tagService.clearTags(url, syndicationId, tagTypeId, languageId)

            tIds.each{ tagId ->
                tagService.tagSyndicatedItem(tagId, url, syndicationId)
            }
        }
        JSON.use("showContentItem") {
            respond ci
        }
    }

    def getTagsByTypeName(String typeName) {
        if (!typeName) {
            response.sendError(400, "Missing 'typeName' parameter.")
        }
        params.max = Util.getMax(params)
        JSON.use("tagList") {
            def tags = tagService.getTagsByTypeName(typeName, params)
            if (Util.isTrue(params.includePaginationFields, false)) {
                respond([tags: tags, total: tags.totalCount, max: params.max, offset: params.offset?:0])
            } else {
                respond tags
            }
        }
    }

    def getTagsRelatedToTagId(Long tagId){
        params.max = Util.getMax(params)
        JSON.use("tagList") {
            def tags = tagService.getRelatedTagsByTagId(tagId, params)
            if(Util.isTrue(params.includePaginationFields, false)) {
                respond ([tags:tags, total:tags.totalCount, max:params.max, offset:params.offset?:0])
            } else{
                respond tags
            }
        }
    }

    def findTagsByTagName(String tagName){
        params.max = Util.getMax(params)
        JSON.use("tagList") {
            def tags = tagService.findTagsByTagName(tagName, params)
            if (Util.isTrue(params.includePaginationFields, false)) {
                respond([tags: tags, total: tags.totalCount, max: params.max, offset: params.offset?:0])
            } else {
                respond tags
            }
        }
    }

    def checkTagExistence(){
        Tag existing = Tag.findByNameAndLanguageAndType("${params.name}", Language.get(params.language), TagType.get(params.tagTypeId))
        if(!existing){
            respond null
            return
        }
        JSON.use("showTag") {
            respond existing
        }
    }
}