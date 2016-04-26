package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.PDF
import grails.plugin.aws.AWSCredentialsHolder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import solr.operations.SolrIndexingService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PDFController)
@Mock([PDF, MediaItemSubscriber, FeaturedMedia])
class PDFControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def solrIndexingService = Mock(SolrIndexingService)
    def tagService = Mock(TagService)

    def setup(){
        controller.mediaItemsService = mediaItemsService
        controller.mediaItemsService.metaClass.updateItemAndSubscriber = {PDF pdf, subId ->if(pdf.save(flush:true)){return pdf} else{return pdf}}
        controller.aws = [s3:{ -> [on:{String bucket -> [delete: {String path ->[]}]}]}]
        controller.metaClass.static.addToBucket = {PDF pdfInstance, String path -> []}
        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.solrIndexingService = solrIndexingService
        controller.tagService = tagService
        Collection.metaClass.static.findAll = {String query, java.util.Collection image  -> []}
        request.contentType = FORM_CONTENT_TYPE
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "Test the index action returns the correct model"() {
        setup:""
            populateValidParams(params)
            new PDF(params).save(flush:true)

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            1 * controller.mediaItemsService.getIndexResponse(params, PDF) >> {[mediaItemList:PDF.list(), mediaItemInstanceCount: PDF.count()]}
            model.PDFInstanceList
            model.pdfInstanceCount == 1
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.PDFInstance != null
            1 * controller.cmsManagerKeyService.listSubscribers()
    }

    void "Test the save action correctly persists an instance"() {
        setup:""
            populateValidParams(params)
            def pdf = new PDF(params).save(flush:true)

        when:"The save action is executed with an invalid instance"
            def invalidPDF = new PDF()
            request.method = 'POST'
            controller.save(invalidPDF)

        then:"The create view is rendered again with the correct model"
            model.PDFInstance != null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            controller.save(pdf)

        then:"A redirect is issued to the show action"
            1 * controller.solrIndexingService.inputMediaItem(pdf)
            response.redirectedUrl == '/PDF/show/1'
            controller.flash.message != null
            PDF.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        setup:""
            populateValidParams(params)
            def pdf = new PDF(params).save(flush:true)
            params.languageId = "1"
            params.tagTypeId = "1"

        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            1 * controller.tagService.getTagInfoForMediaShowViews(null, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
            response.status == 404

        when:"A domain instance is passed to the show action"
            response.reset()
            controller.show(pdf)

        then:"A model is populated containing the domain instance"
            model.pdfInstance == pdf
            1 * controller.tagService.getTagInfoForMediaShowViews(pdf, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
            model.tags == []
            model.languages == []
            model.tagTypes == []
            model.languageId == "1"
            model.tagTypeId == "1"
            model.selectedLanguage == []
            model.selectedTagType == []
            model.collections == []
            model.apiBaseUrl == grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def pdf = new PDF(params)
            controller.edit(pdf)

        then:"A model is populated containing the domain instance"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.PDFInstance == pdf
    }
    void "Test the update action performs an update on a valid domain instance"() {
        setup:""
        populateValidParams(params)
        def pdf = new PDF(params).save(flush:true)

        when:"Update is called for a domain instance that doesn't exist"
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/PDF/index'
        flash.message != null


        when:"An invalid domain instance is passed to the update action"
        response.reset()
        def invalidPDF = new PDF()
        controller.update(invalidPDF)

        then:"The edit view is rendered again with the invalid instance"
        response.redirectedUrl == '/PDF/edit'
        flash.errors != null

        when:"A valid domain instance is passed to the update action"
        response.reset()
        controller.update(pdf)

        then:"A redirect is issues to the show action"
        1 * controller.solrIndexingService.inputMediaItem(pdf)
        response.redirectedUrl == "/PDF/show/$pdf.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if its null"() {
        setup:
            populateValidParams(params)
            PDF pdf = null

        when:"The delete action is called for a null instance"
            request.method = 'POST'
            controller.delete(pdf)

        then:"A 404 is returned"
            response.redirectedUrl == '/PDF/index'
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        setup:
            populateValidParams(params)
            PDF pdf = new PDF(params).save(flush: true)

        when:"A domain instance is created"
            request.method = 'POST'
            response.reset()

        then:"It exists"
            PDF.count() == 1

        when:"The domain instance is passed to the delete action"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(pdf)

        then:"The instance is deleted"
            1 * mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(pdf,true)
            1 * controller.solrIndexingService.removeMediaItem(pdf)
            1 * controller.mediaItemsService.delete(pdf.id)
            response.redirectedUrl == '/PDF/index'
            flash.message != null
    }
}
