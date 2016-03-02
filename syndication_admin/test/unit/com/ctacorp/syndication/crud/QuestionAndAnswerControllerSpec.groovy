package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.JobService
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.QuestionAndAnswer
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import solr.operations.SolrIndexingService
import spock.lang.Specification

@TestFor(QuestionAndAnswerController)
@Mock([QuestionAndAnswer, QuestionAndAnswer, MediaItemSubscriber, FeaturedMedia])
class QuestionAndAnswerControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def solrIndexingService = Mock(SolrIndexingService)
    def tagService = Mock(TagService)
    def jobService = Mock(JobService)

    def setup(){
        controller.mediaItemsService = mediaItemsService
        controller.mediaItemsService.metaClass.updateItemAndSubscriber = {QuestionAndAnswer qAndA, subId ->if(qAndA.save(flush:true)){return qAndA} else{return qAndA}}
        controller.cmsManagerKeyService = cmsManagerKeyService
        controller.solrIndexingService = solrIndexingService
        controller.tagService = tagService
        controller.jobService = jobService
        Collection.metaClass.static.findAll = { String query, java.util.Collection qAndA  -> []}

        request.contentType = FORM_CONTENT_TYPE
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["answer"] = 'some valid answer'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
    }

    void "index action returns the correct model"() {
        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            1 * controller.mediaItemsService.getIndexResponse(params, QuestionAndAnswer) >> {[mediaItemList:QuestionAndAnswer.list(), mediaItemInstanceCount: QuestionAndAnswer.count()]}
            !model.questionAndAnswerInstanceList
            model.questionAndAnswerInstanceCount == 0
    }

    void "create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.questionAndAnswerInstance
    }

    void "save action correctly persists an instance"() {
        setup:
            populateValidParams(params)
            def qAndA = new QuestionAndAnswer(params).save(flush:true)

        when:"The save action is executed with an invalid instance"
            def invalidQuestionAndAnswer = new QuestionAndAnswer()
            request.method = 'POST'
            controller.save(invalidQuestionAndAnswer)

        then:"The create view is rendered again with the correct model"
            model.questionAndAnswerInstance != null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            controller.save(qAndA)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/questionAndAnswer/show/1'
            controller.flash.message != null
            QuestionAndAnswer.count() == 1
    }

    void "show action returns the correct model"() {
        setup:
            populateValidParams(params)
            def qAndA = new QuestionAndAnswer(params).save(flush:true)

        when:"The show action is executed with a null domain"
            1 * controller.tagService.getTagInfoForMediaShowViews(null, params)
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            response.reset()
            1 * controller.tagService.getTagInfoForMediaShowViews(qAndA, params)
            controller.show(qAndA)

        then:"A model is populated containing the domain instance"
            model.questionAndAnswerInstance == qAndA
    }

    void "edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            response.reset()
            populateValidParams(params)
            def qAndA = new QuestionAndAnswer(params)
            controller.edit(qAndA)

        then:"A model is populated containing the domain instance"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.questionAndAnswerInstance == qAndA
    }

    void "update action performs an update on a valid domain instance"() {
        setup:
            populateValidParams(params)
            def qAndA = new QuestionAndAnswer(params).save(flush:true)

        when:"Update is called for a domain instance that doesn't exist"
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/questionAndAnswer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def invalidFaq = new QuestionAndAnswer()
            controller.update(invalidFaq)

        then:"The edit view is rendered again with the invalid instance"
            response.redirectedUrl == '/questionAndAnswer/edit'
            flash.errors != null

        when:"A valid domain instance is passed to the update action"
            response.reset()
            controller.update(qAndA)

        then:"A redirect is issues to the show action"
            1 * controller.jobService.solrUpdate10SecondDelay(qAndA.id)
            response.redirectedUrl == "/questionAndAnswer/show/$qAndA.id"
            flash.message != null
    }

    void "delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.method = 'POST'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/questionAndAnswer/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def qAndA = new QuestionAndAnswer(params).save(flush: true)

        then:"It exists"
            QuestionAndAnswer.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(qAndA)

        then:"The instance is deleted"
            1 * mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(qAndA,true)
            1 * controller.solrIndexingService.removeMediaItem(qAndA)
            1 * controller.mediaItemsService.delete(qAndA.id)
            response.redirectedUrl == '/questionAndAnswer/index'
            flash.message != null
    }
}