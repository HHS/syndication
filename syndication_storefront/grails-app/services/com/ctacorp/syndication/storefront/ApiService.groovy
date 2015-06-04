package com.ctacorp.syndication.storefront

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class ApiService {
    def grailsApplication

    RestBuilder rest = new RestBuilder()

    def getSyndicatedContent(Long id) {
        def resp = rest.get("${grailsApplication.config.syndication.contentExtraction.urlBase}/${id}/syndicate.json?autoplay=false")
        resp.json.results[0].content
    }
}
