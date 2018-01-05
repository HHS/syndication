package com.ctacorp.syndication.storefront.tools

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AppInfoController {

    def index() {
    }
}