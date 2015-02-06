package com.ctacorp.tinyurl.tools

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class TestController {
    def authorizationService

    def index() {
        render authorizationService.amIAuthorized()
    }
}
