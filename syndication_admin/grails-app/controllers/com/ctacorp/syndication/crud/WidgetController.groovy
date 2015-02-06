
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole

import static org.springframework.http.HttpStatus.*

import com.ctacorp.syndication.Widget
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class WidgetController {

    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Widget)
        respond indexResponse.mediaItemList, model: [widgetInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(Widget widgetInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(widgetInstance, params)

        respond widgetInstance, model:[tags:tagData.tags,
                                      languages:tagData.languages,
                                      tagTypes:tagData.tagTypes,
                                      languageId:params.languageId,
                                      tagTypeId:params.tagTypeId,
                                      selectedLanguage:tagData.selectedLanguage,
                                      selectedTagType:tagData.selectedTagType,
                                      collections: Collection.findAll("from Collection where ? in elements(mediaItems)", [widgetInstance]),
                                      apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new Widget(params), model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(Widget widgetInstance) {
        if (widgetInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(widgetInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:'create', params:params
            return
        }

        solrIndexingService.inputMediaItem(widgetInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'widgetInstance.label', default: 'Widget'), [widgetInstance.name]])
                redirect widgetInstance
            }
            '*' { respond widgetInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_USER'])
    def edit(Widget widgetInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond widgetInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(widgetInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(Widget widgetInstance) {
        if (widgetInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(widgetInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:'edit', id:widgetInstance.id
            return
        }

        solrIndexingService.inputMediaItem(widgetInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Widget.label', default: 'Widget'), [widgetInstance.name]])
                redirect widgetInstance
            }
            '*'{ respond widgetInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Widget widgetInstance) {
        if (widgetInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(widgetInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeMediaItemsFromUserMediaLists(widgetInstance)
        solrIndexingService.removeMediaItem(widgetInstance)
        mediaItemsService.delete(widgetInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Widget.label', default: 'Widget'), [widgetInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'widgetInstance.label', default: 'Widget'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
