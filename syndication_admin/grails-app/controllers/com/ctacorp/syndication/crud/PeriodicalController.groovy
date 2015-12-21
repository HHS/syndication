package com.ctacorp.syndication.crud

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.media.Periodical
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder

import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
class PeriodicalController {
    def mediaItemsService
    def tagService
    def solrIndexingService
    def jobService
    def springSecurityService
    def cmsManagerKeyService

    RestBuilder rest = new RestBuilder()

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Periodical)
        respond indexResponse.mediaItemList, model: [periodicalInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(Periodical periodicalInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(periodicalInstance, params)
        respond periodicalInstance,   model: [  tags              : tagData?.tags,
                                                languages         : tagData?.languages,
                                                tagTypes          : tagData?.tagTypes,
                                                languageId        : params.languageId,
                                                tagTypeId         : params.tagTypeId,
                                                selectedLanguage  : tagData?.selectedLanguage,
                                                selectedTagType   : tagData?.selectedTagType,
                                                collections: Collection.findAll("from Collection where ? in elements(mediaItems)", [periodicalInstance]),
                                                apiBaseUrl        : grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    def urlTest(String sourceUrl){
        redirect controller: "mediaTestPreview", action: "index", params:[sourceUrl:sourceUrl]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        Periodical periodical = new Periodical(params)
        periodical.language = Language.findByIsoCode("eng")
        respond periodical, model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def save(Periodical periodicalInstance) {
        if (periodicalInstance == null) {
            notFound()
            return
        }

        periodicalInstance =  mediaItemsService.updateItemAndSubscriber(periodicalInstance, params.long('subscriberId'))
        if(periodicalInstance.hasErrors()){
            flash.errors = periodicalInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond periodicalInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        jobService.solrUpdate10SecondDelay(periodicalInstance.id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'periodical.label', default: 'Periodical'), periodicalInstance.id])
                redirect periodicalInstance
            }
            '*' { respond periodicalInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    def edit(Periodical periodicalInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond periodicalInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(periodicalInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(Periodical periodicalInstance) {
        if (periodicalInstance == null) {
            notFound()
            return
        }

        periodicalInstance =  mediaItemsService.updateItemAndSubscriber(periodicalInstance, params.long('subscriberId'))
        if(periodicalInstance.hasErrors()){
            flash.errors = periodicalInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:"edit", id:params.id
            return
        }

        jobService.solrUpdate10SecondDelay(periodicalInstance.id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Periodical.label', default: 'Periodical'), periodicalInstance.id])
                redirect periodicalInstance
            }
            '*'{ respond periodicalInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(Periodical periodicalInstance) {
        if (periodicalInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(periodicalInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(periodicalInstance, true)
        solrIndexingService.removeMediaItem(periodicalInstance)
        mediaItemsService.delete(periodicalInstance.id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Periodical.label', default: 'Periodical'), periodicalInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'periodical.label', default: 'Periodical'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
