package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService
import com.ctacorp.syndication.JobService
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.Html
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import solr.operations.SolrIndexingService
import spock.lang.Specification

@TestFor(HtmlController)
@Mock([Html, MediaItemSubscriber, FeaturedMedia])
class HtmlControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def solrIndexingService = Mock(SolrIndexingService)
    def tagService = Mock(TagService)
    def jobService = Mock(JobService)

    def setup(){
        //all mediaItems
        controller.mediaItemsService = mediaItemsService
        controller.mediaItemsService.metaClass.updateItemAndSubscriber = {Html html, subId ->if(html.save(flush:true)){return html} else{return html}}

        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.solrIndexingService = solrIndexingService
        controller.tagService = tagService
        Collection.metaClass.static.findAll = {String query, java.util.Collection html  -> []}
        request.contentType = FORM_CONTENT_TYPE

        //html specific
        controller.jobService = jobService
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            1 * controller.mediaItemsService.getIndexResponse(params, Html) >> {[mediaItemList:Html.list(), mediaItemInstanceCount: Html.count()]}
            !model.htmlInstanceList
            model.htmlInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.htmlInstance!= null
    }

    void "Test the save action correctly persists an instance"() {
        setup:""
            populateValidParams(params)
            def html = new Html(params).save(flush:true)

        when:"The save action is executed with an invalid instance"
            def invalidHtml = new Html()
            request.method = 'POST'
            controller.save(invalidHtml)

        then:"The create view is rendered again with the correct model"
            model.htmlInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            controller.save(html)

        then:"A redirect is issued to the show action"
            1 * controller.jobService.solrUpdate10SecondDelay(html.id)
            response.redirectedUrl == '/html/show/1'
            controller.flash.message != null
            Html.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        setup:""
            populateValidParams(params)
            def html = new Html(params).save(flush:true)
            params.languageId = "1"
            params.tagTypeId = "1"

        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            1 * controller.tagService.getTagInfoForMediaShowViews(null, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
            response.status == 404

        when:"A domain instance is passed to the show action"
            controller.show(html)

        then:"A model is populated containing the domain instance"
            model.htmlInstance == html
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def html = new Html(params)
            controller.edit(html)

        then:"A model is populated containing the domain instance"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.htmlInstance == html
    }

    void "Test the update action performs an update on a valid domain instance"() {
        setup:""
            populateValidParams(params)
            def html = new Html(params).save(flush:true)
        when:"Update is called for a domain instance that doesn't exist"
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/html/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def invalidHtml = new Html()
            controller.update(invalidHtml)

        then:"The edit view is rendered again with the invalid instance"
            response.redirectedUrl == '/html/edit'
            flash.errors != null

        when:"A valid domain instance is passed to the update action"
            response.reset()
            controller.update(html)

        then:"A redirect is issues to the show action"
            1 * controller.jobService.solrUpdate10SecondDelay(html.id)
            response.redirectedUrl == "/html/show/$html.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.method = 'POST'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/html/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def html = new Html(params).save(flush: true)

        then:"It exists"
            Html.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(html)

        then:"The instance is deleted"
            1 * mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(html,true)
            1 * controller.solrIndexingService.removeMediaItem(html)
            1 * controller.mediaItemsService.delete(html.id)
            response.redirectedUrl == '/html/index'
            flash.message != null
    }
}
