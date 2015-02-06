package com.ctacorp.tinyurl.tools

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AppInfoController {

    def index() {}
}
