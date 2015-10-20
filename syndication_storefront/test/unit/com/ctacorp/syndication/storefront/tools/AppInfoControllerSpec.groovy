package com.ctacorp.syndication.storefront.tools

import grails.test.mixin.TestFor
import grails.util.Environment
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AppInfoController)
class AppInfoControllerSpec extends Specification {

    def setup() {
    }

    def "meta data should be loaded"() {
        when: "when index is called"
            request.method = "GET"
            def resp = controller.index()
        then: "metaData should be returned in the model"
            resp.metaData
        and: "it should contain the expected fields"
            resp.metaData.app.buildHash
            resp.metaData.app.lastGitCommitDate
            resp.metaData.app.buildDate
    }

    def "meta data should be loaded differently in production environment"(){
        given: "a production environment"
            Environment.metaClass.static.getCurrent = { Environment.PRODUCTION }
        when: "index is called"
            request.method = "GET"
            def resp = controller.index()
        then: "the resp should be null, because the meta data shouldn't exist in this location during test"
            !resp
    }
}
