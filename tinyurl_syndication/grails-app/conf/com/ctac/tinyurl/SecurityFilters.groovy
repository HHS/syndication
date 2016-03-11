package com.ctac.tinyurl

import grails.converters.JSON

class SecurityFilters {
    def authorizationService
    def grailsApplication

    def filters = {
        all(controller:'mediaMappingRest', action:'save|update|delete') {
            before = {

                if(request.getHeader("Authorization") != grailsApplication.config.syndication.internalAuthHeader){
                    response.status = 400
                    response.contentType = "application/json"
                    render ([error:'not authorized'] as JSON)
                    log.error "not authorized"
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
