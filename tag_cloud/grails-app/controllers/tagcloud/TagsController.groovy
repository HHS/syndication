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
import tag_cloud.api.PaginatedTag
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
        getTagsByTypeName:'GET', deleteContentItem: 'DELETE',
        deleteTag: 'DELETE', tagSyndicatedItemByName:'POST', findOrSaveTag:'POST',
        tagItemByTagId: 'POST', setTagsForSyndicatedItemByTagIds: 'POST', updateTag: 'POST',
        getTagsRelatedToTagId:'GET', tagUrlByTagId: 'POST', tagSyndicatedItemsByTagIds: 'POST',
        tagSyndicatedItemByTagName:'POST', findTagsByTagName:'GET'
    ]

    def tagService
    def contentItemService

    def index() {
        params.max = Math.min(params.int('max') ?: 20, 100000)
        if (Util.isTrue(params.includePaginationFields, false)) {
            def tags = tagService.listTags(params)
            PaginatedTag object = new PaginatedTag([tagList: tags, total: tags.totalCount ?: 0, dataSize: tags.size(), max: params.max as long, offset: params.offset as long])
            respond object, view:"paginatedIndex"
        } else {
            respond tagService.listTags(params) ?: []
        }
    }

    def allSyndicationIds(){
        respond ContentItem.list()
    }

    def deleteContentItem(Long id) {

        def contentItem = ContentItem.findBySyndicationId(id)
        def tags = contentItem?.tags ?: []

        tags.each { tag ->

            tag.contentItems?.remove(contentItem)
            tag.save(flush: true)
        }

        contentItem?.delete(flush: true)

        sendEmptyResponse(204)
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

    def findOrSaveTag() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        def type = TagType.findById(data.type as Long)
        def language = Language.findById(data.language as Long)

        if (!type || !language) {
            render400('language or type not found')
            return
        }

        def tagInstance = Tag.findOrCreateWhere(
                name: data.name,
                type: type,
                language: language
        )

        tagInstance.save(flush: true)

        if (tagInstance.hasErrors()) {
            render400(tagInstance.errors as JSON)
        } else {
            respond tagInstance, view: 'showTag', status: 200
        }
    }

    def show(Long id) {

        def tag = Tag.findById(id)

        if(!tag) {
            sendEmptyResponse(404)
        } else {
            respond tag, view:"showTag"
        }
    }

    def query(String q) {

        def matchingTags = Tag.findAllByNameIlike("%${q}%", [max:20, sort:'name', order:'ASC'])
        respond matchingTags, view:"index"
    }

    def updateTag() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        def tagInstance = Tag.findById(data.id as Long)
        def language = Language.findById params.languageId as Long
        def tagType = TagType.findById params.tagTypeId as Long

        if (!tagInstance) {
            sendEmptyResponse(404)
            return
        }

        if (!language || !tagType) {
            render400('language or type not found')
            return
        }

        tagInstance.language = language
        tagInstance.type = tagType
        tagInstance.name = data.name
        tagInstance.save flush:true

        if(tagInstance.hasErrors()) {
            render400(tagInstance.errors as JSON)
        } else {
            respond tagInstance, view: 'showTag', status: 200
        }
    }


    def getTagsForSyndicationId() {
        params.max = Util.getMax(params)
        def tags = tagService.getTagsForSyndicationId(params.syndicationId as Long, params)
        if (Util.isTrue(params.includePaginationFields, false)) {
            PaginatedTag object = new PaginatedTag([tagList: tags, total: tags.totalCount ?: 0, dataSize: tags.size(), max: params.max, offset: params.offset?:0])
            respond object, view:"paginatedIndex"
        } else {
            respond tags, view:"index"
        }
    }

    def tagUrlByTagId() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        String url  = data.url
        Long tagId = data.tagId as Long
        Tag tag = tagService.getTag(tagId)
        ContentItem contentItem = contentItemService.findOrSaveContentItem(url)
        respond tagService.tagContentItem(contentItem, tag), view:"showContentItem"
    }

    def tagSyndicatedItemByTagIds() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        Long syndicationId = data.syndicationId as Long
        String url = data.url

        data.tagIds?.split(',')?.collect{it as Long}?.each{ Long tagId ->
            tagService.tagSyndicatedItem(tagId as Long, url, syndicationId)
        }

        respond ContentItem.findBySyndicationId(syndicationId), view:"showContentItem"
    }

    def tagSyndicatedItemsByTagIds() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        def syndicationIds = data.syndicationIds?.split(',')?.collect { it as Long } ?: []
        def urls = (data.urls?.split(",") ?: []) as Collection<String>
        def tagIds = data.tagIds?.split(",")?.collect { it as Long } ?: []

        def taggedItems = []

        if(syndicationIds.size() > 0 && syndicationIds.size() == urls.size()) {

            for (i in 0..tagIds.size() - 1) {
                for (j in 0..syndicationIds.size() - 1) {
                    taggedItems << tagService.tagSyndicatedItem(tagIds[i], urls[j], syndicationIds[j])
                }
            }

            respond taggedItems.unique(), view:"showContentItems"
        } else {
            render400('{"error": "syndicationIds, urls and tagIds must be the same length"}')
        }
    }

    def bulkTag() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        log.info "Bulk tag request for: ${data}"
        def taggedItems = []

        data.bulkTags.each { tagEntry ->
            tagEntry.value.tagNames.each { String tagName ->
                def taggedItem = tagService.tagSyndicatedItemByName(
                        tagName,
                        tagEntry.value.url as String,
                        tagEntry.key as Long,
                        data.language as Long,
                        data.tagType as Long)
                taggedItems << taggedItem
            }
        }

        respond taggedItems.unique(), view:"showContentItems"
    }

    def tagSyndicatedItemByTagName() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

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
        respond ci, view:"showContentItem"
    }

    //This is a pretty naive way of doing this, if it becomes a problem, we should do
    //more than just clear all tags and re-tag
    def setTagsForSyndicatedItemByTagIds() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

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
                tagService.tagSyndicatedItem(tagId as long, url, syndicationId)
            }
        }
        respond ci, view:'showContentItem'
    }

    def getTagsByTypeName() {

        if (!params.typeName) {
            render400("Missing 'typeName' parameter.")
            return
        }

        params.max = Util.getMax(params)
        def tags = tagService.getTagsByTypeName(params.typeName, params)
        if (Util.isTrue(params.includePaginationFields, false)) {
            PaginatedTag object = new PaginatedTag([tagList: tags, total: tags.totalCount ?: 0, dataSize: tags.size(), max: params.max, offset: params.offset?:0])
            respond object, view:"paginatedIndex"
        } else {
            respond tags, view:"index"
        }
    }

    def getTagsRelatedToTagId() {

        if (!params.tagId) {
            render400("Missing 'tagId' parameter.")
            return
        }

        params.max = Util.getMax(params)
        def tags = tagService.getRelatedTagsByTagId(params.tagId as Long, params)
        if(Util.isTrue(params.includePaginationFields, false)) {
            PaginatedTag object = new PaginatedTag([tagList: tags, total: tags.totalCount ?: 0, dataSize: tags.size(), max: params.max, offset: params.offset?:0])
            respond object, view:"paginatedIndex"
        } else{
            respond tags, view:"index"
        }
    }

    def findTagsByTagName() {

        if (!params.tagName) {
            render400("Missing 'tagName' parameter.")
            return
        }

        params.max = Util.getMax(params)
        def tags = tagService.findTagsByTagName(params.tagName as String, params)
        if (Util.isTrue(params.includePaginationFields, false)) {
            PaginatedTag object = new PaginatedTag([tagList: tags, total: tags.totalCount ?: 0, dataSize: tags.size(), max: params.max, offset: params.offset?:0])
            respond object, view:"paginatedIndex"
        } else {
            respond tags, view:"index"
        }
    }

    def getTagNameByIds() {

        def data = isJSONValid(request)

        if(!data) {
            render400('invalid json')
            return
        }

        def tagName=[]
        def ids = data.ids

        ids.each {
            tagName << Tag.findById(it as long).getName()
        }

        render tagName
    }

    private sendEmptyResponse(code) {
        render status: code, text: '', contentType: 'application/json'
    }

    private render400(message) {
        render status: 400, text: "{\"error\":\"${message}\"}", contentType: 'application/json'
    }

    def isJSONValid(request) {

        try {
            return request.JSON
        } catch (ignored) {
            false
        }
    }
}
