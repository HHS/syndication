/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.tagcloud

import grails.transaction.Transactional
import tagcloud.domain.ContentItem
import tagcloud.domain.Tag
import com.ctacorp.syndication.commons.util.Util

@Transactional
class ContentItemService {

    def delete(ContentItem ci) {
        def tagIds = ci.tags*.id
        tagIds.each { id ->
            def tag = Tag.load(id)
            tag.removeFromContentItems(ci)
        }

        ci.delete flush: true
    }

    def findOrSaveContentItem(String url) {
        ContentItem ci = ContentItem.findByUrl(url, [lock: true])
        if (!ci) {
            ci = new ContentItem(url: url).save(flush: true)
        }

        if (!ci?.id) {
            log.error("Could not create content item")
            return null
        }
        ci
    }

    def findOrSaveContentItem(String url, Long syndicationId) {
        //[4] cases
        //1 doesn't exist at all, create it
        //2 exists, load
        //3 url exists without synd id, add it
        //4 url exists with different synd id, update url

        ContentItem ci = ContentItem.findBySyndicationIdAndUrl(syndicationId, url, [lock: true])
        if (!ci) { // not case 2
            ci = ContentItem.findBySyndicationId(syndicationId)
            if (!ci) { // not case 4
                ci = ContentItem.findByUrl(url, [lock: true])
                if (ci) { //then it's case 3
                    ci.syndicationId = syndicationId
                    ci.save(flush: true)
                } else { //then it's case 1
                    ci = new ContentItem(url: url, syndicationId: syndicationId).save(flush: true)
                }
            } else { // then it's case 4
                ci.url = url
                if (!ci.validate()) {
                    log.error("Could not create content item")
                    return null
                }
                ci.save(flush: true)
            }
        }
        // else it's case 2, do nothing, it's already loaded

        if (!ci.id) { // if it didn't persist - blow up!
            log.error("Could not create content item")
            return null
        }
        ci
    }

    def getContentForTagId(long tagId, params = [:]) {
        params.max = Util.getMax(params)
        params.tagId = tagId
        Tag.get(tagId)?.contentItems
    }

    def getContentForTagIds(String tagIds, params = [:]) {
        params.max = Util.getMax(params)
        def ids = tagIds.split(",").collect { it as Long }.unique()

        ContentItem.createCriteria().list(params) {
            tags {
                inList('id', ids)
            }
        }
    }

    def getRelatedContentItemsByContentItem(ContentItem ci, params = [:]){
        def tagIds = ci.tags*.id

        ContentItem.where {
            tags {
                id in tagIds
            }
        }.list(params) - [ci]
    }

    def getRelatedContentItemsBySyndicationId(Long syndId, params = [:]) {
        params.max = Util.getMax(params)
        ContentItem ci = ContentItem.findBySyndicationId(syndId)
        def tagIds = ci?.tags*.id

        if(tagIds){
            return ContentItem.where {
                tags {
                    id in tagIds
                }
            }.list(params) - [ci]
        }
        []
    }

    def getRelatedContentByTagId(Long tagId, params = [:]){
        ContentItem.where{
            tags{
                id == tagId
            }
        }.list(params)
    }
}