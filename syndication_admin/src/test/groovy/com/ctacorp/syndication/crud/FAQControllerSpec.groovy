package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.media.FAQ
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.QuestionAndAnswer
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.buildtestdata.mixin.Build
import spock.lang.Specification

@TestFor(FAQController)
@Mock([FAQ, QuestionAndAnswer, MediaItemSubscriber, FeaturedMedia])
@Build (FAQ)
class FAQControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def tagService = Mock(TagService)

    def setup(){
        controller.mediaItemsService = mediaItemsService
        //controller.mediaItemsService.metaClass.updateItemAndSubscriber = {FAQ faq, subId ->if(faq.save(flush:true)){return faq} else{return faq}}
        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.tagService = tagService

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
        1 * controller.mediaItemsService.getIndexResponse(params, FAQ) >> {[mediaItemList:FAQ.list(), mediaItemInstanceCount: FAQ.count()]}
        !model.faqList
        model.faqInstanceCount == 0
    }

    void "create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        1 * controller.cmsManagerKeyService.listSubscribers()
        model.FAQ!= null
    }

    void "save action correctly persists an instance"() {
        setup:
        controller.mediaItemsService = [updateItemAndSubscriber: { MediaItem mediaItem, Long subscriberId -> mediaItem.save(flush:true); mediaItem}, getPublisherItemsByType: {Class qa } ]
        populateValidParams(params)
        //def faq = new FAQ(params).save(flush:true)
        def faq = FAQ.build()

        when:"The save action is executed with an invalid instance"
        FAQ invalidFaq = new FAQ()
        request.method = 'POST'
        controller.save(invalidFaq)

        then:"The create view is rendered again with the correct model"
        model.FAQ != null
        view == 'create'

        when:"The save action is executed with a valid instance"
        response.reset()
        controller.save(faq)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/FAQ/show/1'
        controller.flash.message != null
        FAQ.count() == 1
    }

    void "show action returns the correct model"() {
        setup:
        populateValidParams(params)
        def faq = new FAQ(params).save(flush:true)

        when:"The show action is executed with a null domain"
        1 * controller.tagService.getTagInfoForMediaShowViews(null, params)
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404

        when:"A domain instance is passed to the show action"
        response.reset()
        1 * controller.tagService.getTagInfoForMediaShowViews(faq, params)
        controller.show(faq)

        then:"A model is populated containing the domain instance"
        model.FAQ == faq
    }

    void "edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404

        when:"A domain instance is passed to the edit action"
        response.reset()
        populateValidParams(params)
        def faq = new FAQ(params)
        controller.edit(faq)

        then:"A model is populated containing the domain instance"
        1 * controller.cmsManagerKeyService.listSubscribers()
        model.FAQ == faq
    }

    void "update action performs an update on a valid domain instance"() {
        setup:
        controller.mediaItemsService = [updateItemAndSubscriber: {MediaItem mediaItem, Long subscriberId -> mediaItem.save(flush:true); mediaItem} ]
        populateValidParams(params)
        def faq = new FAQ(params)
        faq.questionAndAnswers = [new QuestionAndAnswer(answer:"Do all these tests pass. I hope")]
        //faq.save(flush:true)
        faq.build()

        when:"Update is called for a domain instance that doesn't exist"
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/FAQ/index'
        flash.message != null

        when:"An invalid domain instance is passed to the update action"
        response.reset()
        FAQ invalidFaq = new FAQ()
        controller.update(invalidFaq)

        then:"The edit view is rendered again with the invalid instance"
        response.redirectedUrl == '/FAQ/edit'
        flash.errors != null

        when:"A valid domain instance is passed to the update action"
        response.reset()
        controller.update(faq)

        then:"A redirect is issues to the show action"
        response.redirectedUrl == "/FAQ/show/$faq.id"
        flash.message != null
    }

    void "delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
        request.method = 'POST'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/FAQ/index'
        flash.message != null

        when:"A domain instance is created"
        response.reset()
        populateValidParams(params)
        def faq = new FAQ(params).save(flush: true)

        then:"It exists"
        FAQ.count() == 1

        when:"The domain instance is passed to the delete action"
        controller.delete(faq)

        then:"The instance is deleted"
        1 * mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(faq,true)
        1 * controller.mediaItemsService.delete(faq.id)
        response.redirectedUrl == '/FAQ/index'
        flash.message != null
    }
}
