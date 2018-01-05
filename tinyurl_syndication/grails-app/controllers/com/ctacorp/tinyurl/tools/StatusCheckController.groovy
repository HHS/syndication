package com.ctacorp.tinyurl.tools

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class StatusCheckController {

    def index() {
        render contentType: 'application/json', text: "${params.callback}(${([running:"roger"] as JSON)});"
    }
}
