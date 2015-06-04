package com.ctacorp.syndication.storefront.tools

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Environment

@Secured(['ROLE_ADMIN'])
class AppInfoController {

    def index() {
        def metaData
        if(Environment.current == Environment.PRODUCTION){
            metaData = new ConfigSlurper().parse(new File(grailsApplication.parentContext.servletContext.getRealPath("/WEB-INF/MetaData.groovy")).toURI().toURL())
        } else{
            metaData = new ConfigSlurper().parse(new File("web-app/WEB-INF/MetaData.groovy").toURI().toURL())
        }

        [metaData:metaData]
    }
}