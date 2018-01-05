
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Language
import grails.util.Holders

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItemSubscriber

import com.ctacorp.syndication.media.Infographic
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class InfographicController {

    def mediaItemsService
    def tagService
    def cmsManagerKeyService
    def springSecurityService
    def config = Holders.config

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Infographic)
        respond indexResponse.mediaItemList, model: [infographicInstanceCount: indexResponse.mediaItemInstanceCount, mediaType:"Infographic"]
    }

    def show(Infographic infographicInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(infographicInstance, params)

        respond infographicInstance, model:[tags        :tagData.tags,
                                      languages         :tagData.languages,
                                      tagTypes          :tagData.tagTypes,
                                      languageId        :params.languageId,
                                      tagTypeId         :params.tagTypeId,
                                      selectedLanguage  :tagData.selectedLanguage,
                                      selectedTagType   :tagData.selectedTagType,
                                      collections       :Collection.findAll("from Collection where ? in elements(mediaItems)", [infographicInstance]),
                                      apiBaseUrl        :config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                                      subscriber        :cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(infographicInstance)?.subscriberId)
        ]
    }

    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        Infographic infographic = new Infographic(params)
        infographic.language = Language.findByIsoCode("eng")
        respond infographic, model: [subscribers:subscribers, formats:["jpg", "png"]]
    }

    @Transactional
    def save(Infographic infographicInstance) {
        if (infographicInstance == null) {
            notFound()
            return
        }

        infographicInstance =  mediaItemsService.updateItemAndSubscriber(infographicInstance, params.long('subscriberId'))
        if(infographicInstance.hasErrors()){
            flash.errors = infographicInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond infographicInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers(), formats:["jpg", "png"]]
            return
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'infographicInstance.label', default: 'Infographic'), [infographicInstance.name]])
                redirect infographicInstance
            }
            '*' { respond infographicInstance, [status: CREATED] }
        }
    }

    def edit(Infographic infographicInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond infographicInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(infographicInstance)?.subscriberId), formats:["jpg", "png"]]
    }

    @Transactional
    def update(Infographic infographicInstance) {
        if (infographicInstance == null) {
            notFound()
            return
        }

        infographicInstance =  mediaItemsService.updateItemAndSubscriber(infographicInstance, params.long('subscriberId'))
        if(infographicInstance.hasErrors()){
            flash.errors = infographicInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:'edit', id:params.id
            return
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Infographic.label', default: 'Infographic'), [infographicInstance.name]])
                redirect infographicInstance
            }
            '*'{ respond infographicInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Infographic infographicInstance) {
        if (infographicInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(infographicInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(infographicInstance, true)
        mediaItemsService.delete(infographicInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Infographic.label', default: 'Infographic'), [infographicInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'infographicInstance.label', default: 'Infographic'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
