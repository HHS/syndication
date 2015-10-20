package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by nburk on 6/23/15.
 */
@TestFor(LanguageService)
@Mock([MediaItem, Language])
class LanguageServiceSpec extends Specification {

    Map populateValidParams(params = [:]) {
        assert params != null
        //mediaItem required attributes
        params["name"] = 'someValidName'
        params["sourceUrl"] = 'http://www.example.com/jhgfjhg'
        params["source"] = new Source()
        params
    }

    def "test if mediaItem exists using a specific language"() {
        setup:"valid mediaitem"
            def mi = new MediaItem(populateValidParams())
            Language languageInstance = new Language([name:"english", isoCode:"eng", isActive:true]).save(flush:true)
            mi.language = languageInstance
            mi.save()

        when:"media item exists is called "
            def response = service.mediaItemExists(languageInstance)

        then:"true should be returned"
            response
    }

    def "should return false when no media item is using the specified language"() {
        setup:"valid mediaitem"
            new MediaItem(populateValidParams()).save(flush: true)
            Language languageInstance = new Language([name:"spanish", isoCode:"spa", isActive:true]).save(flush: true)

        when:"media item exists is called "
            def response = service.mediaItemExists(languageInstance)

        then:"true should be returned"
            !response
    }

}