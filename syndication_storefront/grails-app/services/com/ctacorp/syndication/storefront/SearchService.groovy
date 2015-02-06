package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.MediaItem
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class SearchService {
    def grailsApplication
    RestBuilder rest = new RestBuilder()

    def mediaSearch(String query, params = [:]) {
        //Replace with solr search plugin once it is ready
        String restString = grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath + "/resources/media/searchResults?q=${query}"

        def response = rest.get(restString).json

        def ids = response?.results*.id.join(",")
        params.inList = ids
        MediaItem.facetedSearch(params)
    }
}
