/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */


package com.ctacorp.syndication.tools

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.CampaignSubscriber
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication_elasticsearch_plugin.ElasticsearchJob
import com.ctacorp.syndication_elasticsearch_plugin.ReindexJob
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class ContentIndexController {

    def elasticsearchService
    def tagService
    def contentCacheService
    def springSecurityService

    def index() {
        [
                languages: tagService.getAllActiveTagLanguages(),
                tagTypes: tagService.getTagTypes(),
                lastJobExecutionTime: (ReindexJob.last()?.dateCreated ?: new Date(0)).toLocaleString(),
                fullReindexRunning: fullReindexRunning(),
                itemCount: elasticsearchService.getItemCount()
        ]
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

        log.info "Request made to reindex ${mediaIds}"

        def mediaIdList = mediaIds?.tokenize(",")?.collect() { it as Long }

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            mediaIdList = MediaItemSubscriber.findAllByMediaItemInListAndSubscriberId(MediaItem.findAllByIdInList(mediaIdList),springSecurityService.currentUser.subscriberId).mediaItem.id
        }

        if(!mediaIds.isEmpty()) {

            def urlContent = ""
            def cachedContent

            try {
                mediaIdList?.each { mediaId ->

                    def mi = MediaItem.get(mediaId)
                    def className = mi.getClass().simpleName

                    if (className.equalsIgnoreCase("html")) {

                        cachedContent = contentCacheService.cache(mediaId)
                        urlContent = cachedContent?.content

                    } else {
                        urlContent = ""
                    }

                    elasticsearchService.indexMediaItem(mi, urlContent)
                }

                flash.message = "Re-indexing started for media id[s]: ${mediaIds}"

            } catch (e) {

                def message = "Error in indexing media id[s]: ${mediaIds}"
                flash.message = message
                log.error(message, e)
            }
        }

        redirect action: 'index'
    }

    def bulkReindex() {

        Integer itemCount = null
        String lastJobExecutionTime = null

        if(!fullReindexRunning()) {

            ElasticsearchJob.triggerNow(command: ElasticsearchJob.FULL_REINDEX)
            flash.message = 'Full re-index started'
            itemCount = 0
            lastJobExecutionTime = new Date().toLocaleString()

        } else {

            flash.message = 'Full re-index already running'
            itemCount = elasticsearchService.getItemCount()
            lastJobExecutionTime = (ReindexJob.last()?.dateCreated ?: new Date(0)).toLocaleString()
        }

        redirect view: 'index', model:         [
                languages: tagService.getAllActiveTagLanguages(),
                tagTypes: tagService.getTagTypes(),
                lastJobExecutionTime: lastJobExecutionTime,
                fullReindexRunning: true,
                itemCount: itemCount
        ]
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
            flash.message = "Re-indexing started for source id[s]: ${sourceIds}"
        } catch (e) {
            flash.message  = "Error in indexing source id[s]: ${sourceIds}"
        }
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reindexAllSource() {

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
            }
            flash.message = "Re-indexing started for campaign id[s]: ${campaignIds}"
        } catch (e) {
            flash.message = "Error in indexing campaign id[s]: ${campaignIds}"
        }
        redirect action: 'index'
    }

    def reindexAllCampaign() {
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
            flash.message = "Re-indexing started for tag id[s]: ${tagIds}"
        } catch (e) {
            flash.message = "Error in indexing tag id[s]: ${tagIds}"
        }
        redirect action: 'index'
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def reindexAllTags() {
        log.info "Starting re-index job on all tags"
        flash.message = "Re-indexing started on all tag content"
        redirect action: 'index'
    }

    protected static fullReindexRunning() {
        ElasticsearchJob.grails_plugins_quartz_QuartzJob__internalScheduler$get().currentlyExecutingJobs.size() > 0
    }

    protected String getTextContentForMediaItem(MediaItem mediaItem) {

        if (mediaItem.getClass().simpleName.equalsIgnoreCase("html")) {

            def cachedContent = contentCacheService.cache(mediaItem.id)
            return cachedContent?.content

        }

        ""
    }
}
