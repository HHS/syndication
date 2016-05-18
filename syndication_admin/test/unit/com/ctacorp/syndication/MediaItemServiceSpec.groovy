package com.ctacorp.syndication

import com.ctacorp.syndication.audit.SystemEvent
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.contentextraction.ContentRetrievalService
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.jobs.DelayedTinyUrlJob
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.QuestionAndAnswer
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.PDF
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification



/**
 * Created by nburk on 6/16/15.
 */
@TestFor(MediaItemsService)
@Mock([User, UserRole, Role, MediaItem, SystemEvent, Html, Video, Image, Infographic, QuestionAndAnswer,Collection, Tweet, PDF, MediaItemSubscriber, Campaign, MediaPreview, MediaThumbnail, CachedContent, FlaggedMedia, ExtendedAttribute])
class MediaItemServiceSpec extends Specification {

    def contentRetrievalService = Mock(ContentRetrievalService)
    def cmsManagerKeyService = Mock(CmsManagerKeyService)
    def mediaRollbackCalled = false
    def subscriberRollbackCalled = false
    def extendedAttributeService = Mock(ExtendedAttributeService)
    def tinyJob = Mock(DelayedTinyUrlJob)

    def setup(){
        User user = new User(name:"admin", username: "test@example.com", enabled: true, password: "SomerandomPass1").save()
        Role role = new Role(authority: "ROLE_ADMIN").save()
        UserRole.create user, role, true
        service.springSecurityService = [currentUser:User.get(1)]

        DelayedTinyUrlJob.metaClass.'static'.schedule = {Date date, Map map -> return null}

        service.extendedAttributeService = extendedAttributeService
        service.contentRetrievalService = contentRetrievalService
        service.cmsManagerKeyService = cmsManagerKeyService
        MediaItem.metaClass.'static'.facetedSearch = { Map params ->
            if(MediaItem.findBySourceUrl(params.sourceUrl)){
//                LinkedHashMap.metaClass.count = {}
            } else {
                LinkedHashMap.metaClass.count = {0}
            }
            return MediaItem.findBySourceUrl(params.sourceUrl) ?: [count:{0}]
        }


        //withTransaction mocking
        MediaItem.metaClass.'static'.withTransaction = {Closure callable ->
            callable.call([setRollbackOnly:{mediaRollbackCalled = true}])
        }

        MediaItemSubscriber.metaClass.'static'.withTransaction = {Closure callable ->
            callable.call([setRollbackOnly:{subscriberRollbackCalled = true}])
        }
    }

    Map populateValidParams(params = [:]) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["language"] = new Language()
        params["source"] = new Source()
        params
    }

    //------------------ updateItemAndSubscriber - save/updating a mediaItem --------------
    def "test that updateItemAndSubscriber saves a mediaItem with valid subscriber"() {
        setup: "create mediaItem"
            MediaItem mi = new MediaItem(populateValidParams())

        when: "the method is called to save the item"
            def countSubs = MediaItemSubscriber.count()
            service.updateItemAndSubscriber(mi, 15)

        then: "the mediaItem should be saved and associated to a subscriber"
            countSubs < MediaItemSubscriber.count()
            mi.id != null
            !mediaRollbackCalled
            !subscriberRollbackCalled

        when: "the mediaItem gets updated"
            countSubs = MediaItemSubscriber.count()
            service.updateItemAndSubscriber(mi, 15)

        then: "the mediaItem should be updated with no errors and saved to the same subscriber"
            countSubs == MediaItemSubscriber.count()
            mi.id != null
            !mediaRollbackCalled
            !subscriberRollbackCalled
    }

    def "test that updateItemAndSubscriber handles subscriberIds correctly"() {
        setup:"create mediaItem"
            MediaItem mi = new MediaItem(populateValidParams())

        when:"the method is called with an invalid subscriberId"
            service.updateItemAndSubscriber(mi, null)

        then:"transaction should be rolled back with the correct error on the mediaItem instance"
            mediaRollbackCalled
            subscriberRollbackCalled

    }

    def "test invalid mediaItems are rolledbacked"(){
        setup:"create mediaItem"
            MediaItem mi1 = new MediaItem(populateValidParams())
            mi1.save(flush:true)
            MediaItem mi2 = new MediaItem(populateValidParams())

        when:"the method is called with an invalid subscriberId"
            service.updateItemAndSubscriber(mi2, 1)

        then:"transaction should be rolled back with the correct error on the mediaItem instance"
            mediaRollbackCalled
            subscriberRollbackCalled
    }

    def "html items with invalid markup in the sourceUrl get rollbacked"(){
        setup:"create mediaItem"
            MediaItem mi1 = new Html(populateValidParams())

        when:"the method is called with a html mediaItem and invalid source url"
            service.updateItemAndSubscriber(mi1, 1)

        then:"transaction should be rolled back because of content extraction error"
            1 * service.contentRetrievalService.getContentAndMd5Hashcode(mi1.sourceUrl)
            mediaRollbackCalled
            subscriberRollbackCalled
    }

    def "saving a mediaItem works properly when saving/updating as a publisher"(){
        setup: "change current user to a publisher role"
            User user = new User(name:"admin", username: "test2@example.com", enabled: true, password: "SomerandomPass1", subscriberId: 1).save()
            Role role = new Role(authority: "ROLE_PUBLISHER").save()
            UserRole.create user, role, true
            service.springSecurityService = [currentUser:User.get(2)]
            MediaItem mi = new MediaItem(populateValidParams())

        when: "saving a mediaItem as a publisher with a valid subscriberId"
            def countSubs = MediaItemSubscriber.count()
            service.updateItemAndSubscriber(mi, null)

        then:"mediaItemSubscriber should be created and saved along with mediaItem"
            countSubs < MediaItemSubscriber.count()
            mi.id != null
            !mediaRollbackCalled
            !subscriberRollbackCalled

        when: "the mediaItem gets updated"
            countSubs = MediaItemSubscriber.count()
            service.updateItemAndSubscriber(mi, null)

        then: "the mediaItem should be updated with no errors and saved to the same subscriber"
            countSubs == MediaItemSubscriber.count()
            mi.id != null
            !mediaRollbackCalled
            !subscriberRollbackCalled
    }

// -------------- DELETE ---------------
    def "A mediaItem gets deleted along with all of its dependencies"() {
        setup:"create mediaItems and dependencies to delete"
            MediaItem mi = new MediaItem(populateValidParams())
            def campaign = new Campaign([name:"Alergy", source:new Source(), startDate:new Date(),mediaItems: [mi]]); campaign.save()
            mi.campaigns = [campaign]
            mi.save(flush:true)
            new Collection(populateValidParams() + [mediaItems: [mi]]).save()
            def user = User.get(1); user.likes = [mi]; user.save()
            new MediaPreview([mediaItem: mi, imageData:[0, 0, 0, 0, 0] as byte[]]).save()
            new MediaThumbnail([mediaItem: mi, imageData:[0, 0, 0, 0, 0] as byte[]]).save()
            new CachedContent([mediaItem:mi, content: "content"]).save()
            new FlaggedMedia([mediaItem: mi,message:"message", failureType: FlaggedMedia.FailureType.NO_CONTENT]).save()
            new MediaItemSubscriber([mediaItem: mi,subscriberId: 1]).save(flush:true)

        when:"calling delete"
            def previewCount = MediaPreview.count()
            def thumbnailCount = MediaThumbnail.count()
            def itemCount = MediaItem.count()
            def cachedCount = CachedContent.count()
            def flaggedCount = FlaggedMedia.count()
            def subscriberCount = MediaItemSubscriber.count()
            service.delete(mi.id)

        then:"the item should be gone"
            Collection.list().mediaItems[0] == [] as Set<MediaItem>
            campaign.mediaItems == [] as Set<MediaItem>
            previewCount > MediaPreview.count()
            thumbnailCount > MediaThumbnail.count()
            cachedCount > CachedContent.count()
            flaggedCount > FlaggedMedia.count()
            subscriberCount > MediaItemSubscriber.count()
            itemCount > MediaItem.count()
            user.likes == [] as Set<MediaItem>
    }

//    Add extended Attribute
        def "test adding a invalid extended attribute"() {
            setup:"create mediaItem to then add an extended attribute onto"
                MediaItem mi = new MediaItem(populateValidParams())
                def ea = new ExtendedAttribute([name:"valid name", url:"http://www.example.com", mediaItem: mi])

            when:"add extended attribute is called with null attribute"
                def response = service.addExtendedAttribute(mi, null)

            then:"the response should be a string explaining why its not valid"
                response == "attribute doesn't exist"

            when:"add extended attribute is called with null attribute"
                response = service.addExtendedAttribute(mi, ea)

            then:"the response should be a string explaining why it snot valid"
                response == "Enter a Attribute and Value"
        }

        def "test adding a valid extended attribute"() {
            setup:"create mediaItem to then add an extended attribute onto"
                MediaItem mi = new MediaItem(populateValidParams())
                def ea = new ExtendedAttribute([name:"valid name", value:15, url:"http://www.example.com", mediaItem: mi])
                def ea2 = new ExtendedAttribute([name:"valid name", value:15, url:"http://www.example.com", mediaItem: mi])
                service.extendedAttributeService.metaClass.getUpdateInformation = {ExtendedAttribute att ->
                    def name = att.name
                    def atts = ExtendedAttribute.findAllByMediaItem(mi)
                    for (it in atts) {
                        if (it.name.trim().equalsIgnoreCase(name.trim())) {
                            it.value = att.value
                            return it
                        }
                    }
                    return null
                }

            when:"add extended attribute is called with null attribute"
                def response = service.addExtendedAttribute(mi, ea)

            then:"the response should be a string explaining why its not valid"
                response == null

            when:"add extended attribute is called with null attribute"
                response = service.addExtendedAttribute(mi, ea2)

            then:"the response should be a string explaining why its not valid"
                response == "Attribute already created"

        }

}