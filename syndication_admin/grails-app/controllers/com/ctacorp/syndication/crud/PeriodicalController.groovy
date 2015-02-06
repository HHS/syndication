package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Collection
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Periodical
import com.ctacorp.syndication.authentication.UserRole
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder

import static org.springframework.http.HttpStatus.*
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

    @Secured(['ROLE_ADMIN'])
    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new Periodical(params), model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(Periodical periodicalInstance) {
        if (periodicalInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(periodicalInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:'create', params:params
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

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_USER'])
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

        def status =  mediaItemsService.updateItemAndSubscriber(periodicalInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:'edit', id:periodicalInstance.id
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
        
        mediaItemsService.removeMediaItemsFromUserMediaLists(periodicalInstance)
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
