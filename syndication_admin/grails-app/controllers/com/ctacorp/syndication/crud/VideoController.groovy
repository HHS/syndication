
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

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItemSubscriber

import com.ctacorp.syndication.media.Video
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class VideoController {

    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService
    def youtubeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST", importVideo: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Video)
        respond indexResponse.mediaItemList, model: [videoInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(Video videoInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(videoInstance, params)

        respond videoInstance, model:[tags:tagData.tags,
                                            languages:tagData.languages,
                                            tagTypes:tagData.tagTypes,
                                            languageId:params.languageId,
                                            tagTypeId:params.tagTypeId,
                                            selectedLanguage:tagData.selectedLanguage,
                                            selectedTagType:tagData.selectedTagType,
                                            collections: Collection.findAll("from Collection where ? in elements(mediaItems)", [videoInstance]),
                                            apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def create() {
        flash.error = null
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new Video(params), model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def save(Video videoInstance) {
        if (videoInstance == null) {
            notFound()
            return
        }

        videoInstance =  mediaItemsService.updateItemAndSubscriber(videoInstance, params.long('subscriberId'))
        if(videoInstance.hasErrors()){
            flash.errors = videoInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond videoInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        solrIndexingService.inputMediaItem(videoInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'videoInstance.label', default: 'Video'), [videoInstance.name]])
                redirect videoInstance
            }
            '*' { respond videoInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_USER'])
    def edit(Video videoInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond videoInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(videoInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(Video videoInstance) {
        if (videoInstance == null) {
            notFound()
            return
        }

        videoInstance =  mediaItemsService.updateItemAndSubscriber(videoInstance, params.long('subscriberId'))
        if(videoInstance.hasErrors()){
            flash.errors = videoInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:'edit', id:params.id
            return
        }

        solrIndexingService.inputMediaItem(videoInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Video.label', default: 'Video'), [videoInstance.name]])
                redirect videoInstance
            }
            '*'{ respond videoInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Video videoInstance) {
        if (videoInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(videoInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeMediaItemsFromUserMediaLists(videoInstance, true)
        solrIndexingService.removeMediaItem(videoInstance)
        mediaItemsService.delete(videoInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Video.label', default: 'Video'), [videoInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }


    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def importVideo(String videoUrl) {
        flash.error = null
        if (!videoUrl) {
            notFound()
            return
        }
        Video videoInstance = youtubeService.getVideoInstanceFromUrl(videoUrl)

        if (!videoInstance) {
            flash.error = "The video could not be imported, either it doesn't exist or is private. Please verify the video url is correct and the visibility on the video is public."
            render view: "create"
            return
        }
        respond videoInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers()]
    }


    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'videoInstance.label', default: 'Video'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
