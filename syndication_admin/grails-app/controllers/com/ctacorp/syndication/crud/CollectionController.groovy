
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.social.TwitterStatusCollector

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import grails.converters.JSON

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class CollectionController {

    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Collection)
        respond indexResponse.mediaItemList, model: [collectionInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(Collection collectionInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(collectionInstance, params)

        respond collectionInstance, model:[tags:tagData.tags,
                                      languages:tagData.languages,
                                      tagTypes:tagData.tagTypes,
                                      languageId:params.languageId,
                                      tagTypeId:params.tagTypeId,
                                      selectedLanguage:tagData.selectedLanguage,
                                      selectedTagType:tagData.selectedTagType,
                                      apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    def create(Collection collectionInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        def featuredMedia = collectionInstance?.mediaItems
        String featuredMediaForTokenInput = featuredMedia.collect{ [id:it.id, name:"$it.id - ${it.name}"] } as JSON
        Collection collection = new Collection(params)
        collection.language = Language.findByIsoCode("eng")
        respond collection, model: [featuredMedia:featuredMedia,
                                                featuredMediaForTokenInput:featuredMediaForTokenInput,
                                                subscribers:subscribers]
    }

    @Transactional
    def save(Collection collectionInstance) {
        if (collectionInstance == null) {
            notFound()
            return
        }

        collectionInstance = mediaItemsService.updateItemAndSubscriber(collectionInstance, params.long('subscriberId'))
        if(collectionInstance.hasErrors()){
            flash.errors = collectionInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            def subscribers = cmsManagerKeyService.listSubscribers()
            respond collectionInstance, view:'create', model: [subscribers:subscribers]
            return
        }
        
        def mediaItems = params.allMediaItems ?:  ","
        mediaItems.split(",").collect{ it as Long }.each{ mediaId ->
            collectionInstance?.addToMediaItems(MediaItem.load(mediaId as Long))
        }
        
        solrIndexingService.inputMediaItem(collectionInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'collectionInstance.label', default: 'Collection'), collectionInstance.name])
                redirect collectionInstance
            }
            '*' { respond collectionInstance, [status: CREATED] }
        }
    }

    def edit(Collection collectionInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        String collectionMediaForTokenInput = collectionInstance?.mediaItems?.collect{
            [id:it.id, name:"${it.name}"]
        } as JSON

        respond collectionInstance, model:[collectionMediaForTokenInput:collectionMediaForTokenInput,
                                           subscribers   :subscribers,
                                           currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(collectionInstance)?.subscriberId)
        ]
    }

    @Transactional
    def update(Collection collectionInstance) {
        if (collectionInstance == null) {
            notFound()
            return
        }
        
        collectionInstance =  mediaItemsService.updateItemAndSubscriber(collectionInstance, params.long('subscriberId'))
        if(collectionInstance.hasErrors()){
            flash.errors = collectionInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:"edit", id:params.id
            return
        }

        //deletes and adds the media items back in one at a time because of Gorm issue not being able to query
        // all at once on a many-to-many relationship.
        def mediaItems = params.allMediaItems ?:  ","
        collectionInstance.mediaItems = []
        collectionInstance.save flush:true

        mediaItems.split(",").collect{ it as Long }.each{ mediaId ->
            collectionInstance?.addToMediaItems(MediaItem.load(mediaId))
        }

        solrIndexingService.inputMediaItem(collectionInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Collection.label', default: 'Collection'), [collectionInstance.name]])
                redirect collectionInstance
            }
            '*'{ respond collectionInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Collection collectionInstance) {
        if (collectionInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(collectionInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        TwitterStatusCollector.where {
            collection == collectionInstance
        }.deleteAll()
        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(collectionInstance, true)
        solrIndexingService.removeMediaItem(collectionInstance)
        mediaItemsService.delete(collectionInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Collection.label', default: 'Collection'), [collectionInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'collectionInstance.label', default: 'Collection'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
