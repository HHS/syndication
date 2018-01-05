package com.ctacorp.syndication.storefront.mediaviewer

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.storefront.ApiService
import com.ctacorp.syndication.storefront.UserMediaList
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MediaViewerController)
@Mock([UserMediaList, MediaItem, User, Campaign])
@Build([MediaItem, Language, Source, Collection, Campaign])
class MediaViewerControllerSpec extends Specification {

    def setup() {
        Language eng = Language.build(isoCode:"eng")
        4.times{
            MediaItem.build(language: eng,sourceUrl: "http://www.example.com/${it}")
        }
        User usr = new User(name:"someName", username: "someEmail@example.com", password:"somePassword").save()
        assert usr
        assert MediaItem.count() == 4
    }

    def cleanup() {
    }

    def "when no mediaSource is specified, there should be a 400 error"() {
        when: "index action is called with no media source"
            params.id = 1L
            controller.index()
        then: "the response status should be 400"
            response.status == 400
    }

    def "when no id is specified, there should be a 400 error"() {
        when: "index action is called with no id"
            params.mediaSource = "collection"
            controller.index()
        then:
            response.status == 400
    }

    def "when no id or mediaSource is specified, there should be a 400 error"() {
        when: "index action is called with no id"
            controller.index()
        then:
            response.status == 400
    }

    def "tag media source should produce a mediaViewer for tags"() {
        given: "a set of tagged media and the tag media source"
            controller.tagService = [getMediaForTagId:{id, args -> MediaItem.list()}]
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            params.id = 1L
            params.mediaSource = "tag"
        when: "index is called for this tag/media"
            def model = controller.index()
        then: "a correct model should be returned"
            hasCorrectItemsInModel(model)
    }

    def "userMediaList media source should produce a mediaViewer for a userMediaList"() {
        given: "a userMediaList with items in it"
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            UserMediaList uml = new UserMediaList(name:"myList", mediaItems: [MediaItem.read(1), MediaItem.read(2), MediaItem.read(3), MediaItem.read(4)], description:"myList", user:User.read(1))
            uml.save(flush:true)
            params.id = 1L
            params.mediaSource = "usermedialist"
        when: "index is called for this userMediaList"
            def model = controller.index()
        then: "a correct model should be returned"
            hasCorrectItemsInModel(model)
    }

    def "collection media source should produce a mediaViewer for a collection"() {
        given: "a collection of media items"
            Collection col = Collection.build(mediaItems: MediaItem.list())
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            params.id = Collection.first().id
            params.mediaSource = "collection"
        when: "index is called for this collection"
            def model = controller.index()
        then: "a correct model should be returned"
            hasCorrectItemsInModel(model)
    }

    def "campaign media source should produce a mediaViewer for a campaign"() {
        given: "a campaign of media items"
            Campaign campaign = Campaign.build(mediaItems: MediaItem.list())
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            params.id = Campaign.first().id
            params.mediaSource = "campaign"
        when: "index is called for this campaign"
            def model = controller.index()
        then: "a correct model should be returned"
            hasCorrectItemsInModel(model)
    }

    def "source mediaSource should produce a mediaViewer for a campaign"() {
        given: "a source for media items"
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            Source src = Source.build()
            MediaItem.list().each{
                it.source = src
                it.save()
            }
            params.id = src.id
            params.mediaSource = "source"
        when: "index is called for this source"
            def model = controller.index()
        then: "a correct model should be returned"
            hasCorrectItemsInModel(model)
    }

    def "invalid mediaSource value should lead to 400 error"() {
        given: "an invalid source type"
            params.id = 1L
            params.mediaSource = "banana"
        when: "the index action is called"
            controller.index()
        then: "the response should be a 400"
            response.status == 400
            response.errorMessage.contains("Invalid media source")
    }

    def "large lists should be truncated to 20 items"() {
        given: "a collection of more than 20 items"
            40.times{ MediaItem.build(language: Language.read(1), source: Source.read(1)) }
            controller.apiService = [getSyndicatedContent:{Long id -> "content"}]
            Collection col = Collection.build(mediaItems: MediaItem.list())
            assert col.mediaItems.size() > 20
            params.id = col.id
            params.mediaSource = "collection"
        when: "index is called"
            def  model = controller.index()
        then: "the resulting model should have only 20 items"
            model.mediaList.size() == 20
    }

    void hasCorrectItemsInModel(model){
        assert model
        assert model.mediaList
        assert model.mediaList.size() == 4
        assert model.mediaList[0].meta
        assert model.mediaList[0].meta.name
    }
}