package com.ctacorp.syndication.storefront.tools

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class StatusCheckController {

    @Secured(['permitAll'])
    def index() {
        if(params.callback) {
            render "${params.callback}(${([running: "roger"] as JSON)});"
        } else{
            render "${([running: "roger"] as JSON)}"
        }
    }
}
