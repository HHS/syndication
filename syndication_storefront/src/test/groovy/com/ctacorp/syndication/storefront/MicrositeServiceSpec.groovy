package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MicrositeService)
@Build([MicroSite,MediaSelector])
class MicrositeServiceSpec extends Specification {

    // def springSecurityService = Mock(SpringSecurityService)
    // def tagService = Mock(TagService)


    def setup() {
        //service.tagService = tagService
        User user = User.build()
        service.springSecurityService = [currentUser:user]

    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["templateType"] = MicroSite.TemplateType.BLOG
        params["user"] = new User()
        params["title"] = "microsite temp"
        params["pane1selectorType"] = params.pane1selectorType
        return params
    }

    def cleanup() {

    }

    void "save Build should save a valid microsite"() {
        setup:
            def params = populateValidParams([:])
            service.springSecurityService = [currentUser:[id:1]]
            service.tagService = [getMediaForTagId:{Long id, Map mediaArea -> []}]
        when:"saveBuild is called"
            def response = service.saveBuild(params, params.templateType)
        then:"the item should get saved"
            response.id != null
    }

    void "updateBuild should update a microsite"() {
        setup:
            def params = populateValidParams([:])
            def microSite = service.saveBuild(params, params.templateType)
            microSite.title == "microsite temp"
        when:"updateBuild is called with a new title"
            params.title = "updated title"
            def response = service.updateBuild(microSite, params)
        then:"the title should get updated"
            response.title == "updated title"
    }

}
