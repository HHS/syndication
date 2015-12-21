
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.CmsManagerKeyService
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.TagService
import com.ctacorp.syndication.media.Audio
import com.ctacorp.syndication.media.Collection
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import solr.operations.SolrIndexingService
import spock.lang.Specification

@TestFor(AudioController)
@Mock([Audio, MediaItemSubscriber, FeaturedMedia])
class AudioControllerSpec extends Specification {

    def mediaItemsService = Mock(MediaItemsService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def solrIndexingService = Mock(SolrIndexingService)
    def tagService = Mock(TagService)

    def setup(){
        controller.mediaItemsService = mediaItemsService
        controller.mediaItemsService.metaClass.updateItemAndSubscriber = {Audio audio, subId ->if(audio?.save(flush:true)){return audio} else{return audio}}

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
        params["duration"] = 500
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
            controller.index()

        then: "The model is correct"
            1 * controller.mediaItemsService.getIndexResponse(params, Audio) >> {[mediaItemList:Audio.list(), mediaItemInstanceCount: Audio.count()]}
            !model.audioInstanceList
            model.audioInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
            controller.create()

        then: "The model is correctly created"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.audioInstance != null
    }

    void "Test the save action correctly persists an instance"() {
        setup:""
            request.method = 'POST'
            populateValidParams(params)
            params.subscriberId = 1
            def audio = new Audio(params).save(flush:true)

        when: "The save action is executed with an invalid instance"
            def invalidAudio = new Audio()
            controller.save(invalidAudio)

        then: "The create view is rendered again with the correct model"
            flash.errors != null
            view == 'create'
        model.audioInstance != null

        when: "The save action is executed with a valid instance"
            response.reset()
            controller.save(audio)

        then: "A redirect is issued to the show action"
            1 * controller.solrIndexingService.inputMediaItem(audio)
            response.redirectedUrl == '/audio/show/1'
            controller.flash.message != null
            Audio.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        setup:""
            populateValidParams(params)
            def audio = new Audio(params).save(flush:true)
            params.languageId = "1"
            params.tagTypeId = "1"

        when: "The show action is executed with a null domain"
            controller.show(null)

        then: "A 404 error is returned"
        1 * controller.tagService.getTagInfoForMediaShowViews(null, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
            response.status == 404

        when: "A domain instance is passed to the show action"
            controller.show(audio)

        then: "A model is populated containing the domain instance"
            model.audioInstance == audio
            1 * controller.tagService.getTagInfoForMediaShowViews(audio, params) >> {[tags:[],languages:[],tagTypes:[], selectedLanguage:[], selectedTagType:[]]}
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
        when: "The edit action is executed with a null domain"
            controller.edit(null)

        then: "A 404 error is returned"
            response.status == 404

        when: "A domain instance is passed to the edit action"
            populateValidParams(params)
            def audio = new Audio(params)
            controller.edit(audio)

        then: "A model is populated containing the domain instance"
            1 * controller.cmsManagerKeyService.listSubscribers()
            model.audioInstance == audio
    }

    void "Test the update action performs an update on a valid domain instance"() {
        setup:""
            request.method = 'PUT'
            populateValidParams(params)
            def audio = new Audio(params).save(flush:true)

        when: "Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then: "A 404 error is returned"
            response.redirectedUrl == '/audio/index'
            flash.message != null


        when: "An invalid domain instance is passed to the update action"
            response.reset()
            def invalidAudio = new Audio()
            controller.update(invalidAudio)

        then: "The edit view is rendered again with the invalid instance"
            response.redirectedUrl == '/audio/edit'
            flash.errors != null

        when: "A valid domain instance is passed to the update action"
            response.reset()
            controller.update(audio)

        then: "A redirect is issues to the show action"
            1 * controller.solrIndexingService.inputMediaItem(audio)
            response.redirectedUrl == "/audio/show/$audio.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        request.method = 'POST'
            controller.delete(null)

        then: "A 404 is returned"
            response.redirectedUrl == '/audio/index'
            flash.message != null

        when: "A domain instance is created"
            response.reset()
            populateValidParams(params)
            def audio = new Audio(params).save(flush: true)

        then: "It exists"
            Audio.count() == 1

        when: "The domain instance is passed to the delete action"
            controller.delete(audio)

        then: "The instance is deleted"
            1 * mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(audio,true)
            1 * controller.solrIndexingService.removeMediaItem(audio)
            1 * controller.mediaItemsService.delete(audio.id)
            response.redirectedUrl == '/audio/index'
            flash.message != null
    }
}
