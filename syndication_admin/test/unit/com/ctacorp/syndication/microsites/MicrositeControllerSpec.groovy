package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.microsite.FlaggedMicrosite
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.authentication.User

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MicrositeController)
@Mock([MicroSite, User, FlaggedMicrosite])
class MicrositeControllerSpec extends Specification {


    def setup() {

    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["templateType"] = MicroSite.TemplateType.BLOG
        params["user"] = new User()
        params["title"] = "microsite temp"
        return params
    }

    def cleanup() {
    }

    void "test that index returns the microsites"() {
        when:
            controller.index()
        then:
            view == null
            response.redirectedUrl == null
            status == 200
    }

    void "test that show redirects to microsite filter index if the microsite doesn't exists"(){
        when:
            controller.show(new MicroSite())
        then:
            response.redirectedUrl == "/micrositeFilter/index"
            flash.error != null
    }

    void "when show is called with a valid microsite its show view should be rendered"() {
        setup:
            populateValidParams(params)
            def mi = new MicroSite(params).save()
        when:
            controller.show(mi)
        then:
            view == '/microsite/show'
            model.micrositeInstance == mi
            model.apiBaseUrl == grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
    }

    void "test that delete redirects to the microsite filter index if the microsite doesn't exists"() {
        when:
            controller.delete(new MicroSite())
        then:
            response.redirectedUrl == "/micrositeFilter/index"
            flash.error != null
    }

    void "when delete is called with a valid microsite it should get deleted"() {
        setup:
            populateValidParams(params)
            def mi = new MicroSite(params).save()
        when:
            MicroSite.count() == 1
            controller.delete(mi)
        then:
            response.redirectedUrl == "/micrositeFilter/index"
            flash.message != null
            MicroSite.count() == 0
    }
}
