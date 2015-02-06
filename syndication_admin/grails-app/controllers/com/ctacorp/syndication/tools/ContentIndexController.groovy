/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */


package com.ctacorp.syndication.tools

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.CampaignSubscriber
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import com.ctacorp.syndication.jobs.ReindexMediaJob
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class ContentIndexController {
    def solrIndexingService
    def tagService
    def springSecurityService

    def index() {
        def languages = tagService.getAllActiveTagLanguages()
        def tagTypes = tagService.getTagTypes()

        [languages:languages, tagTypes:tagTypes]
    }

    def mediaSearch(String q) {
        def items
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            def publisherMediaIds = MediaItemSubscriber.findAllBySubscriberId(springSecurityService.currentUser.subscriberId).mediaItem.id
            items = MediaItem.findAllByNameIlikeAndIdInList("${q}%", publisherMediaIds, [max: 25, order: 'asc', sort: 'name'])
        } else {
            items = MediaItem.findAllByNameIlike("${q}%", [max: 25, order: 'asc', sort: 'name'])
        }
        def contentForJSON = []
        items.each {
            contentForJSON << [id: it.id, name: "${it.id} - ${it.name}"]
        }

        render contentForJSON as JSON
    }

    def reindexMedia(String mediaIds) {
        def mediaIdList = mediaIds?.tokenize(",")?.collect() { it as Long }
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            mediaIdList = MediaItemSubscriber.findAllByMediaItemInListAndSubscriberId(MediaItem.findAllByIdInList(mediaIdList),springSecurityService.currentUser.subscriberId).mediaItem.id
        }
        if (!mediaIds.isEmpty()) {
            def urlContent
            try {
                mediaIdList?.each { mediaId ->
                    def mi = MediaItem.get(mediaId)
                    def className = mi.getClass().simpleName
                    if (className.equalsIgnoreCase("html")) {
                        urlContent = solrIndexingService.rest.get(solrIndexingService.serverAddress + "/${mi.id}/content/").text
                    } else {
                        urlContent = ""
                    }
                    solrIndexingService.inputMediaItem(mi, urlContent)
                }
                flash.message = "Re-indexing started for media id[s]: ${mediaIds}"


            } catch (e) {
                flash.message = "Error in indexing media id[s]: ${mediaIds}"
            }
        }
        redirect action: 'index'
    }
    
    def reindexAllMedia() {
        ReindexMediaJob.triggerNow([type: "mediaItems", subscriberId:springSecurityService.currentUser?.subscriberId])
        flash.message = "Re-indexing started on all media content"
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def sourceSearch(String q) {
        def items = Source.findAllByNameIlike("${q}%", [max: 25, order: 'asc', sort: 'name'])
        def contentForJSON = []
        items.each {
            contentForJSON << [id: it.id, name: "${it.id} - ${it.name}"]
        }

        render contentForJSON as JSON
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reIndexSource(String sourceIds) {
        def parts = sourceIds.split(",")
        if (parts.size() > 1) {
            sourceIds = (parts as Set).join(',')
        }
        try {
            sourceIds?.tokenize(',').collect { it as Long }.each { sourceId ->
                Source source = Source.get(sourceId)
                solrIndexingService.inputSource(source)
            }
            flash.message = "Re-indexing started for source id[s]: ${sourceIds}"
        } catch (e) {
            flash.message  = "Error in indexing source id[s]: ${sourceIds}"
        }
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reindexAllSource() {
        ReindexMediaJob.triggerNow([type:"sources"])
        flash.message = "Re-indexing started on all source content"
        redirect action: 'index'
    }

    def campaignSearch(String q) {
        def items
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            def publisherCampaignIds = CampaignSubscriber.findAllBySubscriberId(springSecurityService.currentUser.subscriberId).campaign.id
            items = Campaign.findAllByNameIlikeAndIdInList("${q}%", publisherCampaignIds, [max: 25, order: 'asc', sort: 'name'])
        } else {
            items = Campaign.findAllByNameIlike("${q}%", [max: 25, order: 'asc', sort: 'name'])
        }

        def contentForJSON = []
        items.each {
            contentForJSON << [id: it.id, name: "${it.id} - ${it.name}"]
        }

        render contentForJSON as JSON
    }

    def reIndexCampaign(String campaignIds) {
        def campaignIdList = campaignIds?.tokenize(",")?.collect() { it as Long }
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            campaignIdList = CampaignSubscriber.findAllByCampaignInListAndSubscriberId(Campaign.findAllByIdInList(campaignIdList),springSecurityService.currentUser.subscriberId).campaign.id
        }
        try {
            campaignIdList?.each { campaignId ->
                Campaign campaign = Campaign.get(campaignId)
                solrIndexingService.inputCampaign(campaign)
            }
            flash.message = "Re-indexing started for campaign id[s]: ${campaignIds}"
        } catch (e) {
            flash.message = "Error in indexing campaign id[s]: ${campaignIds}"
        }
        redirect action: 'index'
    }

    def reindexAllCampaign() {
        ReindexMediaJob.triggerNow([type: "campaigns", subscriberId:springSecurityService.currentUser?.subscriberId])
        flash.message = "Re-indexing started on all campaign content"
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reIndexTag(String tagIds) {
        def parts = tagIds.split(",")
        if (parts.size() > 1) {
            tagIds = (parts as Set).join(',')
        }

        try {
            tagIds?.tokenize(',').collect { it as Long }.each { tagId ->
                def tag = tagService.getTag(tagId)
                solrIndexingService.inputTag(String.valueOf(tag.id), tag.name)
            }
            flash.message = "Re-indexing started for tag id[s]: ${tagIds}"
        } catch (e) {
            flash.message = "Error in indexing tag id[s]: ${tagIds}"
        }
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reindexAllTags() {
        ReindexMediaJob.triggerNow([type: "tags", params: params])
        flash.message = "Re-indexing started on all tag content"
        redirect action: 'index'
    }
}
