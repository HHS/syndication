package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.media.Collection

import com.ctacorp.syndication.media.PDF
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class PDFController {

    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, PDF)
        respond indexResponse.mediaItemList, model: [pdfInstanceCount: indexResponse.mediaItemInstanceCount]
    }

    def show(PDF pdfInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(pdfInstance, params)

        respond pdfInstance, model:[tags:tagData.tags,
                                      languages:tagData.languages,
                                      tagTypes:tagData.tagTypes,
                                      languageId:params.languageId,
                                      tagTypeId:params.tagTypeId,
                                      selectedLanguage:tagData.selectedLanguage,
                                      selectedTagType:tagData.selectedTagType,
                                      collections: Collection.findAll("from Collection where ? in elements(mediaItems)", [pdfInstance]),
                                      apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new PDF(params), model: [subscribers:subscribers]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def save(PDF pdfInstance) {
        if (pdfInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(pdfInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            respond pdfInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers() ]
            return
        }

        solrIndexingService.inputMediaItem(pdfInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pdfInstance.label', default: 'PDF'), [pdfInstance.name]])
                redirect pdfInstance
            }
            '*' { respond pdfInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_USER'])
    def edit(PDF pdfInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond pdfInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(pdfInstance)?.subscriberId)]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
    @Transactional
    def update(PDF pdfInstance) {
        if (pdfInstance == null) {
            notFound()
            return
        }

        def status =  mediaItemsService.updateItemAndSubscriber(pdfInstance, params.long('subscriberId'))
        if(status){
            flash.errors = status
            redirect action:'edit', id:params.id
            return
        }

        solrIndexingService.inputMediaItem(pdfInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'PDF.label', default: 'PDF'), [pdfInstance.name]])
                redirect pdfInstance
            }
            '*'{ respond pdfInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    @Transactional
    def delete(PDF pdfInstance) {
        if (pdfInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(pdfInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeMediaItemsFromUserMediaLists(pdfInstance, true)
        solrIndexingService.removeMediaItem(pdfInstance)
        mediaItemsService.delete(pdfInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'PDF.label', default: 'PDF'), [pdfInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pdfInstance.label', default: 'PDF'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
