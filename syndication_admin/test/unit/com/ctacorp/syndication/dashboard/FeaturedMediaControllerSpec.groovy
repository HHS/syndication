package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.FeaturedMediaService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(FeaturedMediaController)
@Mock([FeaturedMedia,MediaItem])
class FeaturedMediaControllerSpec extends Specification {

    def featuredMediaService = Mock(FeaturedMediaService)

    def setup() {
        controller.featuredMediaService = featuredMediaService
        new FeaturedMedia([mediaItem:new MediaItem([name:"media item 1"]).save(flush:true)]).save()
        new FeaturedMedia([mediaItem:new MediaItem([name:"media item 2"]).save(flush:true)]).save()
        new MediaItem([name:"media item 3"])
    }

    def cleanup() {
    }

    void "test something"() {
    }

    void "Test the index action returns the correct model"() {

        when:"The index is executed."
            def model = controller.index()

        then:"The model is correct"
            controller.featuredMediaService.listFeatured(params) >> [[id:1,name:"featuredMediaItem"]]
            model.featuredMedia == [[id:1,name:"featuredMediaItem"]] as List<MediaItem>
            model.featuredMediaForTokenInput != null
    }

    void "Test the featuredMediaItems handles the request"() {

        when:"the featuredMediaItems is called"
            controller.featureMediaItems("2,3")

        then:"the proper service methods should be called along with a redirect to index"
            1 * controller.featuredMediaService.listFeatured() >> [[id:1],[id:2]]
            1 * controller.featuredMediaService.unfeatureMedia(1)
            1 * controller.featuredMediaService.featureMedia(3)
            response.redirectedUrl == "/featuredMedia/index"

    }

    void "Test the featureItem handles the request"() {

        when:"the featureItem action is called with an already featured item"
            controller.featureItem(1)

        then:"it should let you know"
            controller.featuredMediaService.listFeatured() >> [[id:1]]
            flash.message == "The media item has been featured. <a href='/featuredMedia/index'>Click here for feature page</a>"
            response.redirectedUrl == "/mediaItem/show/1"

        when:"the featureItem action is called with an unfeatured item"
            response.reset()
            controller.featureItem(3)

        then:"the featuredMedia method should be called"
            controller.featuredMediaService.listFeatured() >> [[id:1]]
            1 * controller.featuredMediaService.featureMedia(3)
            response.redirectedUrl == "/mediaItem/show/3"
    }
}
