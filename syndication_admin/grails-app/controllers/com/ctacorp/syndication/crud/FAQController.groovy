/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.media.QuestionAndAnswer
import grails.util.Holders

import static org.springframework.http.HttpStatus.*

import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import grails.converters.JSON

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class FAQController {
    def mediaItemsService
    def tagService
    def cmsManagerKeyService
    def springSecurityService
    def config = Holders.config

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, FAQ)
        respond indexResponse.mediaItemList, model: [faqInstanceCount: indexResponse.mediaItemInstanceCount, mediaType:"FAQ"]
    }

    def show(FAQ faqInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(faqInstance, params)
        respond faqInstance, model: [tags               : tagData?.tags,
                                     languages          : tagData?.languages,
                                     tagTypes           : tagData?.tagTypes,
                                     selectedLanguage   : tagData?.selectedLanguage,
                                     selectedTagType    : tagData?.selectedTagType,
                                     languageId         : params.languageId,
                                     tagTypeId          : params.tagTypeId,
                                     apiBaseUrl         : config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                                     subscriber         : cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(faqInstance)?.subscriberId)
        ]
    }

    def create(FAQ faqInstance) {
        if (!faqInstance) {
            faqInstance = new FAQ()
        }
        faqInstance.language = faqInstance.language ?: Language.findByIsoCode("eng")

        def subscribers = cmsManagerKeyService.listSubscribers()
        def questionAndAnswerList = mediaItemsService.getPublisherItemsByType(QuestionAndAnswer)

        respond faqInstance, model: [
            subscribers          : subscribers,
            questionAndAnswerList: questionAndAnswerList
        ]
    }

    @Transactional
    def save(FAQ faqInstance) {
        if (!faqInstance) {
            notFound()
            return
        }

        faqInstance = mediaItemsService.updateItemAndSubscriber(faqInstance, params.long('subscriberId'))

        if (faqInstance.hasErrors()) {
            flash.errors = faqInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            def subscribers = cmsManagerKeyService.listSubscribers()
            respond faqInstance, view: 'create', model: [subscribers : subscribers,
                                                         questionAndAnswerList: mediaItemsService.getPublisherItemsByType(QuestionAndAnswer),
                                                         selectedQuestionAndAnswerList: faqInstance?.questionAndAnswers]
            return
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'faqInstance.label', default: 'FAQ'), faqInstance.name])
                redirect faqInstance
            }
            '*' { respond faqInstance, [status: CREATED] }
        }
    }

    def edit(FAQ faqInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond faqInstance, model: [
                questionAndAnswerList        : mediaItemsService.getPublisherItemsByType(QuestionAndAnswer),
                selectedQuestionAndAnswerList: faqInstance?.questionAndAnswers,
                subscribers                  : subscribers,
                currentSubscriber            : cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(faqInstance)?.subscriberId)
        ]
    }

    @Transactional
    def update(FAQ faqInstance) {
        if (!faqInstance) {
            notFound()
            return
        }

        faqInstance = mediaItemsService.updateItemAndSubscriber(faqInstance, params.long('subscriberId'))
        if (faqInstance.hasErrors()) {
            flash.errors = faqInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action: "edit", id: params.id
            return
        }

        faqInstance.questionAndAnswers.clear()
        params.list('questionAndAnswers').collect{ it as Long }.each{
            faqInstance.addToQuestionAndAnswers(QuestionAndAnswer.load(it))
        }

        faqInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'FAQ.label', default: 'FAQ'), [faqInstance.name]])
                redirect faqInstance
            }
            '*' { respond faqInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(FAQ faqInstance) {
        if (faqInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(faqInstance)
        if (featuredItem) {
            featuredItem.delete()
        }

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(faqInstance, true)
        mediaItemsService.delete(faqInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'FAQ.label', default: 'FAQ'), [faqInstance.name]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'faqInstance.label', default: 'FAQ'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
