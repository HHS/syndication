
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import groovyx.net.http.URIBuilder

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

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
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

        respond videoInstance, model:[      tags            :tagData.tags,
                                            languages       :tagData.languages,
                                            tagTypes:       tagData.tagTypes,
                                            languageId      :params.languageId,
                                            tagTypeId       :params.tagTypeId,
                                            selectedLanguage:tagData.selectedLanguage,
                                            selectedTagType :tagData.selectedTagType,
                                            collections     : Collection.findAll("from Collection where ? in elements(mediaItems)", [videoInstance]),
                                            apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                                            subscriber      :cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(videoInstance)?.subscriberId)
        ]
    }

    def create() {
        flash.error = null
        def subscribers = cmsManagerKeyService.listSubscribers()
        Video video = new Video(params)
        video.language = Language.findByIsoCode("eng")
        respond video, model: [subscribers:subscribers]
    }

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

    def edit(Video videoInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond videoInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(videoInstance)?.subscriberId)]
    }

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

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(videoInstance, true)
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

    @Transactional
    def importVideo(String videoUrl) {
        flash.error = null
        if (!videoUrl) {
            notFound()
            return
        }

        def url = new URIBuilder(videoUrl)
        if(!url.query?.v && url.query?.list){
            render view: "create", model:[playlist:true, sourceUrl:videoUrl]
            return
        }

        if(url.query?.v && url.query?.list && params._action_importVideo != "Import Single Video"){
            render view: "create", model:[possiblePlaylist:true, sourceUrl:videoUrl]
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

    @Transactional
    def importPlaylist(String videoUrl) {
        flash.error = null
        if (!videoUrl) {
            notFound()
            return
        }

        if(params._action_importPlaylist == "Import Entire Playlist") {
            render view: "create", model:[playlist:true, sourceUrl:videoUrl]
            return
        }

        Collection collectionInstance = youtubeService.getCollectionInstanceFromVideoPlaylist(videoUrl,Language.get(params.int("language.id")), Source.get(params.int("source.id")))

        if (!collectionInstance) {
            flash.error = "The playlist could not be imported, either it doesn't exist or is private. Please verify the video url is correct and the visibility on the video is public."
            render view: "create"
            return
        }

        collectionInstance.save()
        if(collectionInstance.hasErrors()){
            if(!params.videoAndPlaylist){
                flash.errors = collectionInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            }
            render view: "create", model:[playlist:true, sourceUrl:videoUrl]
            return
        }

        youtubeService.saveMediaItemSubscriber(collectionInstance)

        flash.message = "Created all videos in the playlist and placed them in the following Collection."
        redirect controller: "collection", action:'show',id: collectionInstance.id, model:[subscribers:cmsManagerKeyService.listSubscribers()]
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
