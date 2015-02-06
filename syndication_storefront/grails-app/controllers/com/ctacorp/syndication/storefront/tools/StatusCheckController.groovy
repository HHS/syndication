package com.ctacorp.syndication.storefront.tools

import grails.converters.JSON

class StatusCheckController {

    def index() {
        render "${params.callback}(${([running:"roger"] as JSON)});"
    }
}
