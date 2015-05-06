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
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.jobs.UpdateSolrIndexJob
import grails.plugins.rest.client.RestBuilder
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class HtmlController {
    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService
    def jobService

    RestBuilder rest = new RestBuilder()

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        
        def indexResponse = mediaItemsService.getIndexResponse(params, Html)
        respond indexResponse.mediaItemList, model: [htmlInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(Html htmlInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(htmlInstance, params)

        respond htmlInstance, model: [tags            : tagData?.tags,
                                      languages       : tagData?.languages,
                                      tagTypes        : tagData?.tagTypes,
                                      languageId      : params.languageId,
                                      tagTypeId       : params.tagTypeId,
                                      selectedLanguage: tagData?.selectedLanguage,
                                      selectedTagType : tagData?.selectedTagType,
                                      collections: Collection.findAll("from Collection where ? in elements(mediaItems)", [htmlInstance]),
                                      apiBaseUrl      : grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    def urlTest(String sourceUrl){
        redirect controller: "mediaTestPreview", action: "index", params:[sourceUrl:sourceUrl]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new Html(), model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def save(Html htmlInstance) {
        if (htmlInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(htmlInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            respond htmlInstance, view:'create', model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        htmlInstance.save flush: true
        jobService.solrUpdate10SecondDelay(htmlInstance.id)

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.created.message', args: [message(code: 'htmlInstance.label', default: 'Html'), [htmlInstance.name]])
                redirect htmlInstance
            }
            '*' { respond htmlInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_USER'])
    def edit(Html htmlInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond htmlInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(htmlInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(Html htmlInstance) {
        if (htmlInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(htmlInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:"edit", id:params.id
            return
        }

        htmlInstance.save flush: true
        jobService.solrUpdate10SecondDelay(htmlInstance.id)

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Html.label', default: 'Html'), [htmlInstance.name]])
                redirect htmlInstance
            }
            '*' { respond htmlInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Html htmlInstance) {
        if (htmlInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(htmlInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeMediaItemsFromUserMediaLists(htmlInstance, true)
        solrIndexingService.removeMediaItem(htmlInstance)
        mediaItemsService.delete(htmlInstance.id)

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Html.label', default: 'Html'), [htmlInstance.name]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'htmlInstance.label', default: 'Html'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
