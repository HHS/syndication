package com.ctacorp.syndication.tools

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class LogController {

    def index() {
    }

    def adminErrorLog(){
        File adminErrorLog = new File("${System.getProperty('user.home')}/syndicationLogs/admin/errors.log")
        response.contentType = "application/json"
        render([logData:loadFile(adminErrorLog)] as JSON)
    }

    def adminInfoLog(){
        File adminInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/admin/details.log")
        response.contentType = "application/json"
        render([logData:loadFile(adminInfoLog)] as JSON)
    }

    def apiErrorLog(){
        File apiErrorLog = new File("${System.getProperty('user.home')}/syndicationLogs/api/errors.log")
        response.contentType = "application/json"
        render([logData:loadFile(apiErrorLog)] as JSON)
    }

    def apiInfoLog(){
        File apiInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/api/details.log")
        response.contentType = "application/json"
        render([logData:loadFile(apiInfoLog)] as JSON)
    }

    def cmsLog(){
        File apiInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/cms/cmsmanager.log")
        response.contentType = "application/json"
        render([logData:loadFile(apiInfoLog)] as JSON)
    }

    def cmsApiKeyLog(){
        File apiInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/cms/cmsmanager.apikey.log")
        response.contentType = "application/json"
        render([logData:loadFile(apiInfoLog)] as JSON)
    }

    def storefrontErrorLog(){
        File storefrontErrorLog = new File("${System.getProperty('user.home')}/syndicationLogs/storefront/errors.log")
        response.contentType = "application/json"
        render([logData:loadFile(storefrontErrorLog)] as JSON)
    }

    def storefrontInfoLog(){
        File storefrontInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/storefront/details.log")
        response.contentType = "application/json"
        render([logData:loadFile(storefrontInfoLog)] as JSON)
    }

    def tinyErrorLog(){
        println "tinyErrors"
        File tinyErrorLog = new File("${System.getProperty('user.home')}/syndicationLogs/tiny/errors.log")
        response.contentType = "application/json"
        render([logData:loadFile(tinyErrorLog)] as JSON)
    }

    def tinyInfoLog(){
        File tinyInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/tiny/details.log")
        response.contentType = "application/json"
        render([logData:loadFile(tinyInfoLog)] as JSON)
    }

    def tagErrorLog(){
        println "tag errors!"
        File tagErrorLog = new File("${System.getProperty('user.home')}/syndicationLogs/tag/errors.log")
        response.contentType = "application/json"
        render([logData:loadFile(tagErrorLog)] as JSON)
    }

    def tagInfoLog(){
        println "tagInfo"
        File tagInfoLog = new File("${System.getProperty('user.home')}/syndicationLogs/tag/details.log")
        println tagInfoLog.text
        response.contentType = "application/json"
        render([logData:loadFile(tagInfoLog)] as JSON)
    }


    //1MB default
    private String loadFile(File file, long max = 1024 * 1024){
        if(!file.exists()){
            return consoleWrap("No Log Found")
        }
        def output = new StringBuilder()
        file.withReader{ reader ->
            if(file.size() > max){
                reader.skip(file.size() - max)
            }
            String line = ""
            boolean even = false
            while((line = reader.readLine()) != null){
                output.append(consoleWrap(line, even))
                even = !even
            }
        }
        String compiledLog = output.toString()
        compiledLog ?: consoleWrap("No log messages at this time.")
    }

    private String consoleWrap(String input, boolean even = false){
        "<p class='consoleLine ${even ? 'even' : 'odd'}'>${input}</p>"
    }
}
