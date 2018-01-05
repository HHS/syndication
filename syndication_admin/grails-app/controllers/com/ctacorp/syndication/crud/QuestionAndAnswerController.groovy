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

import static org.springframework.http.HttpStatus.*

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.QuestionAndAnswer
import com.ctacorp.syndication.MediaItemSubscriber
import grails.plugins.rest.client.RestBuilder
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class QuestionAndAnswerController {
    def mediaItemsService
    def tagService
    def cmsManagerKeyService
    def springSecurityService
    def config = Holders.config

    RestBuilder rest = new RestBuilder()

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def indexResponse = mediaItemsService.getIndexResponse(params, QuestionAndAnswer)
        respond indexResponse.mediaItemList, model: [questionAndAnswerInstanceCount: indexResponse.mediaItemInstanceCount, mediaType:"QuestionAndAnswer"]
    }

    def show(QuestionAndAnswer questionAndAnswerInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(questionAndAnswerInstance, params)

        respond questionAndAnswerInstance, model: [tags             : tagData?.tags,
                                                   languages        : tagData?.languages,
                                                   tagTypes         : tagData?.tagTypes,
                                                   languageId       : params.languageId,
                                                   tagTypeId        : params.tagTypeId,
                                                   selectedLanguage : tagData?.selectedLanguage,
                                                   selectedTagType  : tagData?.selectedTagType,
                                                   collections      : Collection.findAll("from Collection where ? in elements(mediaItems)", [questionAndAnswerInstance]),
                                                   apiBaseUrl       : config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                                                   subscriber       :cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(questionAndAnswerInstance)?.subscriberId)
        ]
    }

    def urlTest(String sourceUrl){
        redirect controller: "mediaTestPreview", action: "index", params:[sourceUrl:sourceUrl]
    }

    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer(language:Language.findByIsoCode("eng"))
        respond questionAndAnswer, model: [subscribers:subscribers]
    }

    @Transactional
    def save(QuestionAndAnswer questionAndAnswerInstance) {
        if (questionAndAnswerInstance == null) {
            notFound()
            return
        }

        questionAndAnswerInstance =  mediaItemsService.updateItemAndSubscriber(questionAndAnswerInstance, params.long('subscriberId'))
        if(questionAndAnswerInstance.hasErrors()){
            flash.errors = questionAndAnswerInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond questionAndAnswerInstance, view:'create', model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.created.message', args: [message(code: 'questionAndAnswerInstance.label', default: 'QuestionAndAnswer'), [questionAndAnswerInstance.name]])
                redirect questionAndAnswerInstance
            }
            '*' { respond questionAndAnswerInstance, [status: CREATED] }
        }
    }

    def edit(QuestionAndAnswer questionAndAnswerInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond questionAndAnswerInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(questionAndAnswerInstance)?.subscriberId)]
    }

    @Transactional
    def update(QuestionAndAnswer questionAndAnswerInstance) {
        if (questionAndAnswerInstance == null) {
            notFound()
            return
        }

        questionAndAnswerInstance =  mediaItemsService.updateItemAndSubscriber(questionAndAnswerInstance, params.long('subscriberId'))
        if(questionAndAnswerInstance.hasErrors()){
            flash.errors = questionAndAnswerInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:"edit", id:params.id
            return
        }

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.updated.message', args: [message(code: 'QuestionAndAnswer.label', default: 'QuestionAndAnswer'), [questionAndAnswerInstance.name]])
                redirect questionAndAnswerInstance
            }
            '*' { respond questionAndAnswerInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(QuestionAndAnswer questionAndAnswerInstance) {
        if (questionAndAnswerInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(questionAndAnswerInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(questionAndAnswerInstance, true)
        mediaItemsService.delete(questionAndAnswerInstance.id)

        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'QuestionAndAnswer.label', default: 'QuestionAndAnswer'), [questionAndAnswerInstance.name]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionAndAnswerInstance.label', default: 'QuestionAndAnswer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
