package com.ctacorp.syndication.storefront.authentication

import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by sgates on 6/16/15.
 */
@TestFor(LogoutController)
class LogoutControllerSpec extends Specification {
    def "the user goes to /logout triggering the index action, it should redirect to logout"() {
        when: "index() is called"
            controller.index()
        then:
            response.redirectedUrl == "/j_spring_security_logout"
    }
}