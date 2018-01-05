package com.ctacorp.syndication.storefront

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import grails.util.Holders

@Transactional
class ApiService {
    def grailsApplication

    RestBuilder rest = new RestBuilder()

    def getSyndicatedContent(Long id) {
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        def resp = rest.get("${Holders.config.API_SERVER_URL}/api/v2/resources/media/${id}/syndicate.json?autoplay=false")
        resp.json.results[0].content
    }
}
