package com.ctacorp.syndication.authorization

import grails.test.mixin.TestFor
import spock.lang.Specification
import syndication.authorization.AuthorizationService


/**
 * Created by sgates on 5/29/15.
 */
@TestFor(AuthorizationService)
class AuthorizationServiceSpec extends Specification {
    def setup() {
        service.init()
    }

    def "test init"(){
        expect:"not null"
        service.hashBody("abc")
    }
}