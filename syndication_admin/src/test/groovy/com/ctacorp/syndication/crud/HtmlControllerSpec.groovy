package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import spock.lang.*



@TestFor(HtmlController)
@Mock([Html, MediaItemSubscriber, FeaturedMedia, Language])
@Build (Html)
class HtmlControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def tagService = Mock(TagService)


    def setup(){
        //all mediaItems
        controller.mediaItemsService = mediaItemsService
        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.tagService = tagService
        Collection.metaClass.static.findAll = {String query, java.util.Collection html  -> []}
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

    void "index action returns the correct model"() {

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        1 * controller.mediaItemsService.getIndexResponse(params, Html) >> {[mediaItemList:Html.list(), mediaItemInstanceCount: Html.count()]}
        !model.htmlList
        model.htmlInstanceCount == 0
    }

    void "create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        1 * controller.cmsManagerKeyService.listSubscribers()
        model.html!= null
    }

    void "save action correctly persists an instance"() {

        setup:
        controller.mediaItemsService = [updateItemAndSubscriber: {MediaItem mediaItem, Long subscriberId -> mediaItem.save(flush:true); mediaItem} ]
        populateValidParams(params)
        def html = Html.build()

        when:"The save action is executed with an invalid instance"
        request.method = 'POST'
        Html invalidHtml = new Html()
        controller.save(invalidHtml)

        then:"The create view is rendered again with the correct model"
        model.html!= null
        view == 'create'


        when:"The save action is executed with a valid instance"
        response.reset()
        controller.save(html)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/html/show/1'
        controller.flash.message != null
        Html.count() == 1
    }

    void "show action returns the correct model"() {
        setup:
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
        model.html == html
    }

    void "edit action returns the correct model"() {
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
        model.html == html
    }

    void "update action performs an update on a valid domain instance"() {
        setup:
        controller.mediaItemsService = [updateItemAndSubscriber: {MediaItem mediaItem, Long subscriberId -> mediaItem.save(flush:true); mediaItem} ]
        populateValidParams(params)
        def html = Html.build()

        when:"Update is called for a domain instance that doesn't exist"
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/html/index'
        flash.message != null


        when:"An invalid domain instance is passed to the update action"
        response.reset()
        Html invalidHtml = new Html()
        controller.update(invalidHtml)

        then:"The edit view is rendered again with the invalid instance"
        response.redirectedUrl == '/html/edit'
        flash.errors != null

        when:"A valid domain instance is passed to the update action"
        response.reset()
        controller.update(html)

        then:"A redirect is issues to the show action"
        response.redirectedUrl == "/html/show/$html.id"
        flash.message != null
    }

    void "delete action deletes an instance if it exists"() {
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
        1 * controller.mediaItemsService.delete(html.id)
        response.redirectedUrl == '/html/index'
        flash.message != null
    }
}
