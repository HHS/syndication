/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.CampaignSubscriber
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON

import com.ctacorp.syndication.Campaign
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class CampaignController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def featuredMediaService
    def solrIndexingService
    def springSecurityService
    def campaignService
    def cmsManagerKeyService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if(isCurrentUserPublisher()){
            respond Campaign.findAllByIdInList(campaignService.publisherCampaigns(), params), model: [campaignInstanceCount: Campaign.countByIdInList(campaignService.publisherCampaigns() ?: [0],params)]
            return
        }
        
        respond Campaign.list(params), model: [campaignInstanceCount: Campaign.count()]
    }


    def show(Campaign campaignInstance) {
        respond campaignInstance
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def create(Campaign campaignInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        def featuredMedia = campaignInstance?.mediaItems
        String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
        
        respond new Campaign(params), model:[featuredMedia:featuredMedia, featuredMediaForTokenInput:featuredMediaForTokenInput, subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def save(Campaign campaignInstance) {
        if (campaignInstance == null) {
            notFound()
            return
        }

        def mediaItems = params.mediaItemsToAdd ?:  ","
        params.remove("mediaItemsToAdd")
        mediaItems.split(",").collect{ it as Long }.each{ mediaId ->
            if(!params.subscriberId || MediaItemSubscriber.findAllByMediaItem(MediaItem.get(mediaId as Long)).subscriberId.contains(params.long("subscriberId"))){
                campaignInstance?.addToMediaItems(MediaItem.load(mediaId))
            }
        }
        
        campaignInstance =  campaignService.updateCampaignAndSubscriber(campaignInstance, params.subscriberId)
        if (campaignInstance.hasErrors()) {
            flash.errors = campaignInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            def featuredMedia = campaignInstance?.mediaItems
            String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
            respond campaignInstance, view:'create', model:[featuredMedia:featuredMedia, featuredMediaForTokenInput:featuredMediaForTokenInput, subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        solrIndexingService.inputCampaign(campaignInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'campaignInstance.label', default: 'Campaign'), [campaignInstance.name]])
                redirect campaignInstance
            }
            '*' { respond campaignInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_PUBLISHER'])
    def edit(Campaign campaignInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()

        if (campaignInstance == null) {
            notFound()
            return
        }

        def featuredMedia = campaignInstance.mediaItems
        String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON

        respond campaignInstance, model:[featuredMedia:featuredMedia, featuredMediaForTokenInput:featuredMediaForTokenInput, subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(CampaignSubscriber.findByCampaign(campaignInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(Campaign campaignInstance) {
        def mediaItems = params.mediaItemsToAdd ?:  ","

        if (campaignInstance == null) {
            notFound()
            return
        }

        campaignInstance.validate()
        campaignInstance =  campaignService.updateCampaignAndSubscriber(campaignInstance, params.subscriberId)
        if (campaignInstance.hasErrors()) {
            flash.errors = campaignInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            def subscribers = cmsManagerKeyService.listSubscribers()
            def featuredMedia = campaignInstance.mediaItems
            String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
            respond campaignInstance, view: 'edit', imodel:[featuredMedia:featuredMedia, featuredMediaForTokenInput:featuredMediaForTokenInput, subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(CampaignSubscriber.findByCampaign(campaignInstance)?.subscriberId)]
            return
        }
        
        //deletes and adds the media items back in one at a time because of Gorm issue not being able to query
        // all at once on a many-to-many relationship.
        params.remove("mediaItemsToAdd")
        campaignInstance.properties = new Campaign(params).properties
        mediaItems.split(",").collect{ it as Long }.each{ mediaId ->
            if(campaignService.mediaItemBelongsToSubscriber(campaignInstance,mediaId))
                campaignInstance?.addToMediaItems(MediaItem.load(mediaId))
        }

        campaignInstance.save flush: true
        solrIndexingService.inputCampaign(campaignInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Campaign.label', default: 'Campaign'), [campaignInstance.name]])
                redirect campaignInstance
            }
            '*' { respond campaignInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Campaign campaignInstance) {

        if (campaignInstance == null) {
            notFound()
            return
        }

        solrIndexingService.removeCampaign(campaignInstance)
        CampaignSubscriber.findByCampaign(campaignInstance)?.delete(flush:true)
        campaignInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Campaign.label', default: 'Campaign'), [campaignInstance.name]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'campaignInstance.label', default: 'Campaign'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def addMediaItem(Campaign campaign){
        if (!campaign.id) {
            flash.message = "Could not find the campaign."
            redirect controller:'mediaItem', action:'show', id: params.mediaItem
            return
        }

        if(campaign.mediaItems.id.contains(params.mediaItem as Long)){
            flash.message = "The Campaign already contains this media item."
        } else {
            campaign.mediaItems.add(MediaItem.get(params.mediaItem))
            campaign.save(flush: true)
            flash.message = "The media item has been added to the '${campaign.name}' campaign."
        }

        redirect controller:'mediaItem', action:'show', id: params.mediaItem
    }

    private boolean isCurrentUserPublisher() {
        UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"
    }
}
