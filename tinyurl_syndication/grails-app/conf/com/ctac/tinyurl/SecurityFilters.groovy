package com.ctac.tinyurl

import grails.converters.JSON

class SecurityFilters {
    def authorizationService
    def grailsApplication

    def filters = {
        all(controller:'mediaMappingRest', action:'save|update|delete') {
            before = {
                def url = grailsApplication.config.grails.serverURL + request.forwardURI[request.contextPath.size()..-1]

                def authHeaders  = [
                    authorizationHeader: request.getHeader("Authorization"),
                    dateHeader: request.getHeader("Date"),
                    contentTypeHeader: request.getHeader("content-type"),
                    contentLengthHeader: request.getHeader("Content-Length"),
                    url: url,
                    httpMethod: request.getMethod(),
                    dataMd5: authorizationService.hashBody(request.reader.text)
                ]

                boolean authorized = authorizationService.checkAuthorization(authHeaders)

                if(!authorized){
                    response.status = 400
                    response.contentType = "application.json"
                    render ([error:'not authorized'] as JSON)
                    println "not authorized"
                    return false
                }
                true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
