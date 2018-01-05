package com.ctacorp.syndication.authorization

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.buildtestdata.mixin.Build
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
//        setup:"setup generator"
//            def generator = [hashData:"abc"]
//            service.generator = generator
//        expect:"not null"
//            service.hashBody("abc")
    }
}