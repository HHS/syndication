package com.ctacorp.syndication.storefront.tools

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(StatusCheckController)
class StatusCheckControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def "index action should reply with a json status"() {
        when: "when the index action is called"
            request.method = "GET"
            controller.index()
        then: "there should be a view rendered"
            response.text
        and: "it should be json"
            response.json
        and: "it should be running"
            response.json.running == "roger"
    }

    def "index action is called with a callback param, it should respond with jsonp"() {
        when: "when index action is called with callback param"
            params.callback = "banana"
            controller.index()
        then: "a response should be rendered"
            response.text
        and: "it should start with the callback name"
            response.text.startsWith("banana(")
        and: "end with a semicolon"
            response.text.endsWith(");")
    }
}
