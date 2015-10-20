/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.tagcloud

import com.ctacorp.syndication.Language
import grails.transaction.Transactional
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import tagcloud.domain.TagType

@Transactional(readOnly = true)
class TagService {
    def contentItemService
    def solrIndexingService

    @Transactional
    ContentItem tagContentItem(ContentItem ci, Tag tag) {
        ci.addToTags(tag)
    }

    @Transactional
    ContentItem clearTags(String url, Long syndicationId, Long tagTypeId, Long languageId){
        ContentItem ci = contentItemService.findOrSaveContentItem(url, syndicationId)
        ci = ContentItem.lock(ci.id)
        if(ci.tags){
            def tags = Tag.where{
                type{
                    id == tagTypeId
                }
                language{
                    id == languageId
                }
                contentItems{
                    id == ci.id
                }
            }.list()

            tags.each{ tag ->
                tag.contentItems.remove(ci)
            }
            tags.each{ tag ->
                ci.tags.remove(tag)
            }
        }
        ci
    }

    @Transactional
    ContentItem tagSyndicatedItemByName(String tagName, String url, Long syndicationId, Long languageId, Long typeId){
        Tag tag = findOrSaveTag(tagName, Language.load(languageId), TagType.load(typeId))
        ContentItem ci = contentItemService.findOrSaveContentItem(url, syndicationId)
        tagContentItem(ci, tag)
        ci
    }

    @Transactional
    ContentItem tagSyndicatedItem(Long tagId, String url, Long syndicationId) {
        Tag tag = Tag.get(tagId)
        if (!tag) {
            log.error "Tag could not be found with id ${tagId}"
            return null
        }

        ContentItem ci = contentItemService.findOrSaveContentItem(url, syndicationId)
        if (!ci) {
            log.error "Could not load or save content item with url ${url} and syndication id ${syndicationId}"
            return null
        }

        if(!tag.contentItems*.id?.contains(ci.id)) {
            tag.addToContentItems(ci)
        }
        ci
    }

    //convenience method for findOrSaveTag
    @Transactional
    Tag findOrSaveTag(String tagName, Long languageId, Long tagTypeId){
        Tag tag = findOrSaveTag(tagName, Language.load(languageId), TagType.load(tagTypeId))
        solrIndexingService.inputTag("${tag.id}", tagName)
        tag
    }

    @Transactional
    Tag findOrSaveTag(tagName, Language language, TagType type) {
        Tag.findOrSaveByNameAndLanguageAndType(tagName, language, type, [lock: true])
    }

    @Transactional
    def deleteTag(Long id){
        Tag tag = Tag.get(id)
        tag.delete(flush:true)
    }

    def findTagsByTagName(String tagName, params = [:]){
        Tag.where{
            if(params.tagTypeId){
                type{
                    id == params.long("tagTypeId")
                }
            }

            if(params.languageId){
                language{
                    id == params.long("languageId")
                }
            }

            name =~ "%${tagName}%"
        }.list(params)
    }

    TagType getTagTypeById(Long id) {
        TagType.get(id)
    }

    TagType getTagTypeByName(String name) {
        TagType.findByName(name)
    }

    Tag getTag(Long id){
        Tag.get(id)
    }

    def getTagsForContentItem(Long id, params = [:]){
        ContentItem.get(id).tags
    }

    def getTagsForContentItems(contentItems, params = [:]) {
        def tags = []
        contentItems*.tags.minus(null).each{
            tags.addAll(it)
        }
        tags.unique { it.name }
    }

    def listTags(params = [:]){
        def limits = [max:params.max, offset:params.offset]
        Tag.facetedSearch(params).list(limits)
    }

    @Transactional
    def updateTagType(TagType tagType){
        tagType.save(flush:true)
    }

    @Transactional
    def saveTagType(TagType tagType){
        tagType.save(flush:true)
    }

    def listTagTypes(params = [:]) {
        def limits = [max:params.max, offset:params.offset]
        TagType.facetedSearch(params).list(limits)
    }

    def getTagsByTypeName(String typeName, params = [:]) {
        TagType type = TagType.findByNameIlike(typeName)
        Tag.findAllByType(type, params)
    }

    def groupTagsByType(tags) {
        def allTags = [:]
        tags*.type.name.unique().each{ name ->
            allTags << ["${name}":tags.findAll{it.type.name == name}]
        }
        [total:tags.size(), tagGroups:allTags]
    }

    def getTagsForSyndicationId(Long id, params = [:]) {
        ContentItem ci = ContentItem.findBySyndicationId(id)
        if(!ci){
            return []
        }
        Tag.where{
            contentItems{
                id == ci.id
            }
            if(params.tagTypeId){
                type{
                    id == params.long("tagTypeId")
                }
            }
            if(params.languageId){
                language{
                    id == params.long("languageId")
                }
            }
        }.list(params) ?: []
    }

    def getRelatedTagsByTagId(Long tagId, params = [:]) {
        def contentItems = ContentItem.where{
            tags{
                id == tagId
            }
        }.list()

        //TODO we can probably do this a better way
        def tags = []
        contentItems*.tags.each{
            tags.addAll(it)
        }
        tags = tags.unique() - Tag.load(tagId)
        Tag.where{
            id in tags*.id
        }.list(params) ?: []
    }

    def tagExists(Language languageInstance) {
        def tags = Tag.findByLanguage(languageInstance)
        if (!tags) {
            return false
        }
        return true
    }
}