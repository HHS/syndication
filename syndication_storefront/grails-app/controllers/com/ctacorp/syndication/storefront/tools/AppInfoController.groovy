package com.ctacorp.syndication.storefront.tools

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Environment

@Secured(['ROLE_ADMIN'])
class AppInfoController {

    def index() {
        def configFile
        if(Environment.getCurrent() == Environment.PRODUCTION){
            String filePath = grailsApplication.parentContext.servletContext.getRealPath("/WEB-INF/MetaData.groovy")
            if(!filePath){
                return
            }
            configFile = new File(filePath)?.toURI()?.toURL()
        } else{
            configFile = new File("web-app/WEB-INF/MetaData.groovy")?.toURI()?.toURL()
        }

        def metaData = new ConfigSlurper().parse(configFile)
        [metaData:metaData]
    }
}