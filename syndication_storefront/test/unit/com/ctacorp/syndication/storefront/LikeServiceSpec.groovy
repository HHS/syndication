package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LikeService)
@Mock([User, MediaItem,User, Html])
class LikeServiceSpec extends Specification {



    def setup() {
        service.springSecurityService = [getCurrentUser:{User.get(1)}]
    }

    def cleanup() {
    }

    void "test something"() {
    }

    def "Invalid like Media id"() {
        given:"invalid mediaItem id"
            long id = -5
        new User(username: "user@example.com", password: "ABC123def").save(flush:true)

        when:"the likeMedia method is called"
            def result1 = service.likeMedia(id)
            def result2 = service.undoLikeMedia(id)
        then:"should return null"
            result1 == null
            result2 == null
    }

    def "valid likeMedia requeset"() {
        given:"valid mediaItem"
            MediaItem mi = new MediaItem(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user@example.com", password: "ABC123def", likes: []).save(flush:true)

        when:"the likeMedia method is called"
            def result = service.likeMedia(mi.id)

        then:"the item should have one like"
            User.get(1).likes == [mi] as HashSet
    }

    def "valid unlike media request with no likes yet"() {
        given:"Valid mediaItem with no likes from the user"
            new MediaItem(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user@example.com", password: "ABC123def",likes:[]).save(flush:true)

        when:"the unlikeMedia method is called"
            def result = service.undoLikeMedia(1)
        then:"get back a null object"
            result == null
    }

    def "valid unlike media request with likes"(){
        given:"valid media item with likes from the user"
            MediaItem mi = new MediaItem(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user@example.com", password: "ABC123def",likes: [mi]).save(flush:true)
        when:"undolikes method is called"
            def result = service.undoLikeMedia(1)
        then:"mediaItem should have no likes"
            User.get(1).likes == [] as HashSet
    }

    def "getLikeCount for mediaItems"() {
        given:"valid mediaItems with like counts"
            MediaItem mi1 = new MediaItem(name:"Some Item1", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
            MediaItem mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/fdfdfdfdf", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user1@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)
            new User(username: "user2@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)

        when:"getLikeCount is called"
            def count1 = service.getLikeCount(mi1.id)
            def count2 = service.getLikeCount(mi2.id)
        then:"then the counts should be correct"
            count1 == 2
            count2 == 0
    }

    def "Get all media like info"() {
        given:"valid mediaItems exist with like counts"
            MediaItem mi1 = new MediaItem(name:"Some Item1", sourceUrl:"http://www.example.com/1", language: new Language(), source: new Source()).save(flush:true)
            MediaItem mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/2", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user1@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)
            new User(username: "user2@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)
        when:"getAllMediaLikeInfo is called"
            def results = service.getAllMediaLikeInfo([mi1, mi2])

        then:
            results.'1' == [likeCount:2, alreadyLiked:false]
            results.'2' == [likeCount:0, alreadyLiked:false]
    }

    def "Get all media like info from Json"() {
        given:"valid mediaItems exist with like counts"
            MediaItem mi1 = new MediaItem(name:"Some Item1", sourceUrl:"http://www.example.com/1", language: new Language(), source: new Source()).save(flush:true)
            MediaItem mi2 = new MediaItem(name:"Some Item2", sourceUrl:"http://www.example.com/2", language: new Language(), source: new Source()).save(flush:true)
            new User(username: "user1@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)
            new User(username: "user2@example.com", password: "ABC123def",likes: [mi1]).save(flush:true)

        when:"getAllMediaLikeInfo is called"
            def results = service.getAllMediaLikeInfoFromJson([mi1, mi2])

        then:
            results.'1' == [likeCount:2, alreadyLiked:false]
            results.'2' == [likeCount:0, alreadyLiked:false]
    }

}
