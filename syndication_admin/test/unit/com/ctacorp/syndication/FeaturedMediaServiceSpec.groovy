package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FeaturedMediaService)
@Mock([MediaItem, MediaItemSubscriber, FeaturedMedia])
class FeaturedMediaServiceSpec extends Specification {

    def setup() {
        FeaturedMedia.metaClass.'static'.where = {Closure closure ->
            [
            deleteAll:{
            FeaturedMedia.get(1).delete()
            FeaturedMedia.get(2).delete()
            FeaturedMedia.get(3).delete()
            }
            ]
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

    void "feature a media item"() {
        setup:"a void mediaItem"
            def mi = new MediaItem(populateValidParams()).save(flush:true)

        when:"feature media is called"
            def featuredItem = service.featureMedia(mi.id)

        then:"then a new featured item should be created"
            featuredItem.id == 1 as Long

        when:"the same item tries to be featured twice"
            featuredItem = service.featureMedia(mi.id)

        then:"then a new featuredItem is not created"
            featuredItem.id == 1 as Long

    }

    void "feature a media item that isn't valid"() {
        when:"feature media is called"
            def featuredItem = service.featureMedia(null)

        then:"then a new featured item should be created"
            featuredItem == null

    }

    def "unfeatureMedia should delete the featuredMedia item"() {
        setup:"a void mediaItem"
            def mi = new MediaItem(populateValidParams()).save(flush:true)

        when:"unfeatureMedia is called with an invalid item"
            def featuredItem = service.featureMedia(15)

        then:"null is returned"
            featuredItem == null

        when:"feature media is called"
            featuredItem = service.featureMedia(mi.id)

        then:"then a new featured item should be created"
            featuredItem.id == 1 as Long

        when:"feature media is called"
            def count = FeaturedMedia.count()
            service.unfeatureMedia(mi.id)

        then:"then a new featured item should be created"
            count > FeaturedMedia.count()

        when:"unfeaturing an item that isn't featured"
            featuredItem = service.unfeatureMedia(mi.id)

        then:"null is returned"
            featuredItem == null

    }

    def "clear deletes all featuredMedia items"() {
        setup:"multiple featured media items"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def mi2 = new MediaItem(populateValidParams()).save(flush:true)
            def mi3 = new MediaItem(populateValidParams()).save(flush:true)
            new FeaturedMedia([mediaItem:mi]).save()
            new FeaturedMedia([mediaItem:mi2]).save()
            new FeaturedMedia([mediaItem:mi3]).save()

        when:"clear is called"
            FeaturedMedia.count() == 3
            service.clear()

        then:"there should be featuredMedia Items"
            FeaturedMedia.count() == 0
    }

    def "listFeatured should return a list of mediaItems"() {
        setup:"multiple featured media items"
            def mi = new MediaItem(populateValidParams()).save(flush:true)
            def mi2 = new MediaItem(populateValidParams())
            mi2.sourceUrl = "http://www.example.com/2"
                    mi2.save(flush:true)
            def mi3 = new MediaItem(populateValidParams())
                mi3.sourceUrl = "mi3.sourceUrl = http://www.example.com/3"
                mi3.save(flush: true)
            new FeaturedMedia([mediaItem:mi]).save()
            new FeaturedMedia([mediaItem:mi2]).save()
            new FeaturedMedia([mediaItem:mi3]).save()

        when:"listFeatured is called"
            def response = service.listFeatured()

        then:"a list of mediaItems should be returned"
            response == [mi, mi2, mi3]
    }

}
