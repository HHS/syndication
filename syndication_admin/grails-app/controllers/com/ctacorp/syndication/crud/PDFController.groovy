package com.ctacorp.syndication.crud

import com.amazonaws.services.s3.model.ObjectMetadata
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.media.MediaItem
import grails.util.Holders
import com.amazonaws.services.s3.AmazonS3Client
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.impl.client.DefaultRedirectStrategy;

import java.security.MessageDigest

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

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class PDFController {

    def s3Client = new AmazonS3Client()
    def mediaItemsService
    def tagService
    def cmsManagerKeyService
    def springSecurityService
    def config = Holders.config

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    String getBucketName(){
         Holders.config.AWS_S3_BUCKET
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, PDF)
        respond indexResponse.mediaItemList, model: [pdfInstanceCount: indexResponse.mediaItemInstanceCount, mediaType:"PDF"]
    }

    def show(PDF pdfInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(pdfInstance, params)

        if(!pdfInstance){
            render status: NOT_FOUND
            return
        }

        render view:'show', model:[ pdfInstance     :pdfInstance,
                                    tags            :tagData.tags,
                                    languages       :tagData.languages,
                                    tagTypes        :tagData.tagTypes,
                                    languageId      :params.languageId,
                                    tagTypeId       :params.tagTypeId,
                                    selectedLanguage:tagData.selectedLanguage,
                                    selectedTagType :tagData.selectedTagType,
                                    collections     :Collection.findAll("from Collection where ? in elements(mediaItems)", [pdfInstance]),
                                    apiBaseUrl      :config?.API_SERVER_URL + config?.SYNDICATION_APIPATH,
                                    subscriber      :cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(pdfInstance)?.subscriberId)
        ]
    }

    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        PDF pdf = new PDF(params)
        pdf.language = Language.findByIsoCode("eng")
        respond pdf, model: [subscribers:subscribers]
    }

    @Transactional
    def save(PDF pdfInstance) {
        if (pdfInstance == null) {
            notFound()
            return
        }

        pdfInstance =  mediaItemsService.updateItemAndSubscriber(pdfInstance, params.long('subscriberId'))
        if(pdfInstance.hasErrors()){
            flash.errors = pdfInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond pdfInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers() ]
            return
        }

        try{
            addToBucket(pdfInstance, null)
        } catch(FileNotFoundException e){
            MediaItem.withTransaction {status ->
                //more explicit for testing purposes
                status.setRollbackOnly()
            }
            flash.errors = [[message:"A PDF file could not be found at " + pdfInstance.sourceUrl]]
            redirect action:'create', model:[subscribers:cmsManagerKeyService.listSubscribers() ], params:params
            return
        }

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pdfInstance.label', default: 'PDF'), [pdfInstance.name]])
                redirect pdfInstance
            }
            '*' { respond pdfInstance, [status: CREATED] }
        }
    }

    def edit(PDF pdfInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond pdfInstance, model: [
                pdfInstance: pdfInstance,
                subscribers:subscribers,
                currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(pdfInstance)?.subscriberId)
        ]
    }

    @Transactional
    def update(PDF pdfInstance) {
        if (pdfInstance == null) {
            notFound()
            return
        }
        String oldSourceUrl = ""
        def dirty = pdfInstance.isDirty("sourceUrl") || isMissing(pdfInstance.sourceUrl)
        if(dirty) {
            oldSourceUrl = pdfInstance.getPersistentValue("sourceUrl")
        }

        pdfInstance =  mediaItemsService.updateItemAndSubscriber(pdfInstance, params.long('subscriberId'))
        if(pdfInstance.hasErrors()){
            flash.errors = pdfInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:'edit', id:params.id
            return
        }

        if (dirty) {
            try{
                addToBucket(pdfInstance, oldSourceUrl)
            } catch(FileNotFoundException e){
                MediaItem.withTransaction {status ->
                    //more explicit for testing purposes
                    status.setRollbackOnly()
                }
                flash.errors = [[message:"A PDF file could not be found at " + pdfInstance.sourceUrl]]
                redirect action:'edit', id:params.id
                return
            }
        }

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

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(pdfInstance, true)
        deleteFromBucket(pdfInstance.sourceUrl)
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

    def addToBucket(PDF pdfInstance, String oldSourceUrl) {

        if(oldSourceUrl) {
            deleteFromBucket(oldSourceUrl)
        }

        def stream = null

        try {

            def url = new URL(pdfInstance.sourceUrl)
            stream = url.openStream()

            ObjectMetadata metadata = new ObjectMetadata()
            metadata.setContentType('application/pdf')

            s3Client.putObject(getBucketName(), getPdfKey(pdfInstance.sourceUrl), stream, metadata)

        } catch(e) {
            log.error "could not add pdf to bucket", e
        } finally {
            stream?.close()
        }
    }

    private isMissing(String sourceUrl) {
        !s3Client.doesObjectExist(getBucketName(), getPdfKey(sourceUrl))
    }

    private GString getPdfKey(String sourceUrl) {
        "pdf-files/${getUrlHash(sourceUrl)}.pdf"
    }

    private deleteFromBucket(String sourceUrl) {
        s3Client.deleteObject(getBucketName(), getPdfKey(sourceUrl))

    }

    private static String getUrlHash(String sourceUrl) {
        MessageDigest.getInstance("MD5").digest(sourceUrl.bytes).encodeHex().toString()
    }
}
