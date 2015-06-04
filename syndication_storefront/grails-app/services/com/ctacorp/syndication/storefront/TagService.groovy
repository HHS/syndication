package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
import com.google.common.cache.CacheBuilder
import com.google.common.cache.Cache
import com.google.common.cache.CacheLoader
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit

@Transactional
class TagService {
    def grailsApplication
    String serverAddress
    RestBuilder rest = new RestBuilder()
    Cache cache;

    @PostConstruct
    void init() {
        serverAddress = grailsApplication.config.tagCloud.serverAddress
        cache = CacheBuilder.newBuilder().
                expireAfterWrite(30, TimeUnit.SECONDS).
                maximumSize(500).
                build(new CacheLoader<Long, Collection>() {
                    public Collection load(Long key) throws Exception {
                        restGet("${serverAddress}/tags/getTagsForSyndicationId.json?syndicationId=${key}") ?: []
                    }
                })
    }

    def getTag(Long tagId){
        restGet("${serverAddress}/tags/show/${tagId}.json") ?: []
    }
    
    def getTagsForMediaId(Long id) {
        cache.get(id)
    }

    def getAllActiveTagLanguages() {
        restGet("${serverAddress}/languages/getActive.json") ?: []
    }

    def getTagsByType(String type){
        restGet("${serverAddress}/tags/getTagsByTypeName.json?typeName=${type}") ?: []
    }

    def listTags(params = [:]) {
        params.languageId = params.languageId ?: 1
        params.typeId = params.typeId ?: 1
        def offset = params.offset ?: 0
        def max = params.max ?: 10
        def sort = params.sort ?: "id"
        params.name = params.name ?: ""
        if (params.order?.toLowerCase() == "desc") {
            sort = "-${sort}"
        }
        restGet("${serverAddress}/tags.json?languageId=${params.languageId}&typeId=${params.typeId}&includePaginationFields=1&offset=${offset}&max=${max}&sort=${sort}&nameContains=${params.name}")
    }

    def getTagTypes(params = [:]) {
        def include = params.includePaginationFields ?: 0
        def offset = params.offset ?: 0
        def max = params.max ?: 10
        def sort = params.sort ?: "id"
        if (params.order?.toLowerCase() == "desc") {
            sort = "-${sort}"
        }
        restGet("${serverAddress}/tagTypes/index.json?offset=${offset}&max=${max}&sort=${sort}&includePaginationFields=${include}") ?: []
    }

    def getMediaForTagId(Long id, params) {
        def contentList = restGet("${serverAddress}/content/getContentForTagId.json", [tagId:id])
        if(!contentList) { return [] }

        MediaItem.facetedSearch([restrictToSet:contentList*.syndicationId.join(",")]).list(params)
    }

    private restGet(String url, params = null) {
        try {
            rest.get(url + aggregateParams(params)) { accept "application/json" }.json
        } catch (e) {
            log.warn "Tag Service could not be reached."
        }
    }

    private String aggregateParams(p) {
        if (!p) {
            return ""
        }
        String query = "?"
        p.each { key, value ->
            if (!(key == "controller" || key == "action" || key == "format")) {
                query += key.encodeAsURL() + "=" + value.encodeAsURL() + "&"
            }
        }
        if (query == "?") {
            return ""
        }

        query[0..-1 - 1] //remove trailing &
    }
}
