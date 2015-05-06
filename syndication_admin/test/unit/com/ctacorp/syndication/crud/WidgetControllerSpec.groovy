package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.Widget
import com.ctacorp.syndication.media.Collection
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import solr.operations.SolrIndexingService
import spock.lang.Specification

@TestFor(WidgetController)
@Mock([Widget, MediaItemSubscriber, FeaturedMedia])
class WidgetControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def solrIndexingService = Mock(SolrIndexingService)
    def tagService = Mock(TagService)

    def setup(){
        controller.mediaItemsService = mediaItemsService
        controller.mediaItemsService.metaClass.updateItemAndSubscriber = {widget, subId ->if(widget.save(flush:true)){return null} else{return "errors"}}

        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.solrIndexingService = solrIndexingService
        controller.tagService = tagService
        Collection.metaClass.static.findAll = {String query, java.util.Collection widget  -> []}
        request.contentType = FORM_CONTENT_TYPE
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
        //widget required attributes
        params["height"] = 500
        params["width"] = 500
        params["code"] = "widget code"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            1 * controller.mediaItemsService.getIndexResponse(params, Widget) >> {[mediaItemList:Widget.list(), mediaItemInstanceCount: Widget.count()]}
            !model.widgetInstanceList
            model.widgetInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.widgetInstance!= null
            1 * controller.cmsManagerKeyService.listSubscribers()
    }

    void "Test the save action correctly persists an instance"() {
        setup:""
            populateValidParams(params)
            def widget = new Widget(params).save(flush:true)

        when:"The save action is executed with an invalid instance"
            def invalidWidget = new Widget()
            request.method = 'POST'
            controller.save(invalidWidget)

        then:"The create view is rendered again with the correct model"
            view == 'create'
            model.widgetInstance != null

        when:"save is executed"
            controller.save(widget)

        then:"A redirect is issued to the show action"
            1 * controller.solrIndexingService.inputMediaItem(widget)
            response.redirectedUrl == '/widget/show/1'
            controller.flash.message != null
            Widget.count() == 1

    }

    void "Test that the show action returns the correct model"() {
        setup:""
            populateValidParams(params)
            def widget = new Widget(params).save(flush:true)
            params.languageId = "1"
            params.tagTypeId = "1"

        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            1 * controller.tagService.getTagInfoForMediaShowViews(null, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
            response.status == 404

        when:"A domain instance is passed to the show action"
            controller.show(widget)

        then:"A model is populated containing the domain instance"
            model.widgetInstance == widget
            1 * controller.tagService.getTagInfoForMediaShowViews(widget, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
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
            def widget = new Widget(params)
            controller.edit(widget)

        then:"A model is populated containing the domain instance"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.widgetInstance == widget
    }

    void "Test the update action performs an update on a valid domain instance"() {
        setup:""
            populateValidParams(params)
            def widget = new Widget(params).save(flush:true)

        when:"Update is called for a domain instance that doesn't exist"
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/widget/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def invalidWidget = new Widget()
            controller.update(invalidWidget)

        then:"The edit view is rendered again with the invalid instance"
            response.redirectedUrl == '/widget/edit'
            flash.errors != null

        when:"A valid domain instance is passed to the update action"
            response.reset()
            controller.update(widget)

        then:"A redirect is issues to the show action"
            1 * controller.solrIndexingService.inputMediaItem(widget)
            response.redirectedUrl == "/widget/show/$widget.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.method = 'POST'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/widget/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def widget = new Widget(params).save(flush: true)

        then:"It exists"
            Widget.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(widget)

        then:"The instance is deleted"
            1 * mediaItemsService.removeMediaItemsFromUserMediaLists(widget,true)
            1 * controller.solrIndexingService.removeMediaItem(widget)
            1 * controller.mediaItemsService.delete(widget.id)
            response.redirectedUrl == '/widget/index'
            flash.message != null

    }
}
