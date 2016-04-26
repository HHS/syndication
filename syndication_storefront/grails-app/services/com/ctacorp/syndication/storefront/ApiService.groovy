package com.ctacorp.syndication.storefront

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class ApiService {
    def grailsApplication

    RestBuilder rest = new RestBuilder()

    def getSyndicatedContent(Long id) {
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        def resp = rest.get("${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/${id}/syndicate.json?autoplay=false")
        resp.json.results[0].content
    }
}
