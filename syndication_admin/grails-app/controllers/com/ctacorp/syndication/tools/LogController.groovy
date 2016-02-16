package com.ctacorp.syndication.tools

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class LogController {

    def index() {
    }

    static final String home = System.getProperty('user.home')
    static final File adminErrorLog = new File("$home/syndicationLogs/admin/errors.log")
    static final File adminInfoLog = new File("$home/syndicationLogs/admin/details.log")
    static final File apiErrorLog = new File("$home/syndicationLogs/api/errors.log")
    static final File apiInfoLog = new File("$home/syndicationLogs/api/details.log")
    static final File cmsErrorLog = new File("$home/syndicationLogs/cms/errors.log")
    static final File cmsInfoLog = new File("$home/syndicationLogs/cms/details.log")
    static final File storefrontErrorLog = new File("$home/syndicationLogs/storefront/errors.log")
    static final File storefrontInfoLog = new File("$home/syndicationLogs/storefront/details.log")
    static final File tinyErrorLog = new File("$home/syndicationLogs/tiny/errors.log")
    static final File tinyInfoLog = new File("$home/syndicationLogs/tiny/details.log")
    static final File tagErrorLog = new File("$home/syndicationLogs/tag/errors.log")
    static final File tagInfoLog = new File("$home/syndicationLogs/tag/details.log")

    def adminErrorLog(){
        response.contentType = "application/json"
        render([logData:loadFile(adminErrorLog)] as JSON)
    }

    def adminInfoLog(){
        response.contentType = "application/json"
        render([logData:loadFile(adminInfoLog)] as JSON)
    }

    def apiErrorLog(){
        response.contentType = "application/json"
        render([logData:loadFile(apiErrorLog)] as JSON)
    }

    def apiInfoLog(){
        response.contentType = "application/json"
        render([logData:loadFile(apiInfoLog)] as JSON)
    }

    def cmsLog(){
        response.contentType = "application/json"
        render([logData:loadFile(cmsErrorLog)] as JSON)
    }

    def cmsApiKeyLog(){
        response.contentType = "application/json"
        render([logData:loadFile(cmsInfoLog)] as JSON)
    }

    def storefrontErrorLog(){
        response.contentType = "application/json"
        render([logData:loadFile(storefrontErrorLog)] as JSON)
    }

    def storefrontInfoLog(){
        response.contentType = "application/json"
        render([logData:loadFile(storefrontInfoLog)] as JSON)
    }

    def tinyErrorLog(){
        response.contentType = "application/json"
        render([logData:loadFile(tinyErrorLog)] as JSON)
    }

    def tinyInfoLog(){
        response.contentType = "application/json"
        render([logData:loadFile(tinyInfoLog)] as JSON)
    }

    def tagErrorLog(){
        response.contentType = "application/json"
        render([logData:loadFile(tagErrorLog)] as JSON)
    }

    def tagInfoLog(){
        response.contentType = "application/json"
        render([logData:loadFile(tagInfoLog)] as JSON)
    }

    def logDownload(){
        response.setContentType("application/octet-stream")

        if(params.file && this."${params.file}"){
            response.setHeader("Content-disposition", "attachment;filename=\"${params.file}.log\"")
            response.outputStream << this."${params.file}".bytes
        } else{
            response.outputStream << "No file found (${params.file})!!".bytes
        }
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
