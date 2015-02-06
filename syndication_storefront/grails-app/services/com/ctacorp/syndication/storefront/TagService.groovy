package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.MediaItem
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

    def getTagsForMediaId(Long id) {
        cache.get(id)
    }

    def getTagsByType(String type){
        restGet("${serverAddress}/tags/getTagsByTypeName.json?typeName=${type}") ?: []
    }

    def getMediaForTagId(Long id, params) {
        params.tagId = id
        def contentList = restGet("${serverAddress}/content/getContentForTagId.json", params)
        if(!contentList) { return [] }

        MediaItem.facetedSearch([restrictToSet:contentList*.syndicationId.join(",")]).list()
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
