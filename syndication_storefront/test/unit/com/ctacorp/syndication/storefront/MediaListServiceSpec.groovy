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
@TestFor(MediaListService)
@Mock([MediaItem, UserMediaList, User, Html, Language, Source])
class MediaListServiceSpec extends Specification {

    def setup() {
        User user = new User(username: "user@example.com", password: "password").save(flush:true)
        Html html = new Html(name:"Some Item", sourceUrl:"http://www.example.com/jhgfjhg", language: new Language(), source: new Source()).save(flush:true)
        Html html2 = new Html(name:"Another Item", sourceUrl:"http://www.example.com/sadfasf", language: new Language(), source: new Source()).save(flush:true)
        Html html3 = new Html(name:"Another Item again", sourceUrl:"http://www.example.com/asdfasf", language: new Language(), source: new Source()).save(flush:true)

        assert User.count() == 1
        assert MediaItem.count() == 3
    }

    def "addMediaToMediaList should fail on null inputs"() {
        when: "addMediaToMediaList is called with null inputs"
            def result = service.addMediaToMediaList(null, null)
        then: "the service should return false"
            !result
    }

    def "addMediaToMediaList should fail if nothing was added to a valid list"() {
        given: "A valid user media list"
            UserMediaList uml = new UserMediaList(name:"My List", description: "My awesome list!", user: User.load(1))
            uml.save(flush:true)
        when: "nothing is added to it"
            def result = service.addMediaToMediaList(null, uml.id)
        then: "The result should be false"
            !result
    }

    def "addMediaToMediaList should fail if something was added to a null list"() {
        given: "A valid mediaItem"
            MediaItem media = MediaItem.load(1)
        when: "nothing is added to it"
            def result = service.addMediaToMediaList(media.id, null)
        then: "The result should be false"
            !result
    }

    def "addMediaToMediaList should add valid items to a media list"() {
        given: "media items and a media list"
            UserMediaList uml = new UserMediaList(name:"My List", description: "My awesome list!", user: User.load(1))
            uml.save(flush:true)
            MediaItem media1 = MediaItem.load(1)
            MediaItem media2 = MediaItem.load(2)
            MediaItem media3 = MediaItem.load(3)
        when: "addMediaToMediaList is called"
            service.addMediaToMediaList(media1.id, uml.id,)
            service.addMediaToMediaList(media2.id, uml.id,)
            service.addMediaToMediaList(media3.id, uml.id,)
        then: "There should now be two items saved in the list"
            def loaded = UserMediaList.get(uml.id)
            uml.mediaItems.size() == 3
    }
}
