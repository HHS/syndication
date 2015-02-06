package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItem
import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import grails.util.Holders

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class MediaService {

    RestBuilder rest
    def config
    def grailsApplication

    @PostConstruct
    def init(){
        rest = new RestBuilder()
        config = Holders.config
    }

    def getFeaturedMedia(params = [:]){
        def featured = FeaturedMedia.where{
            mediaItem{
                active == true
                visibleInStorefront == true
            }
        }.list(params)

        featured*.mediaItem
    }

    def listNewestMedia(params){
        params.sort = "-id"
        params['visibleInStorefront'] = true
        params['active'] = true
        params['languageName'] = "english"
        MediaItem.facetedSearch(params).list([max:params.max, offset:params.offset])
    }

    def findMediaByAll(params){
        MediaItem.facetedSearch([
                nameContains:params.title.replace('%', '\\%'),
                languageName:params.language,
                sourceName:params.source,
                sourceUrlContains:params.domain,
                mediaTypes:params.mediaType,
                visibleInStorefront:true,
                active:true,
                restrictToSet: params.topicItems
        ]).list(max:params.max, offset:params.offset)
    }

    def mediaItemSolrSearch(String searchQuery, params=[:]){
        def offset = params.offset ?: 0
        def max = params.max ?: 10
        def resp = null
        try{
            resp = rest.get(grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
                    + "/resources/media/searchResults.json?q=${searchQuery}&max=${max}&offset=${offset}").json
            def idList = resp.results.id
            if (idList.isEmpty()) {
                return []
            }
            def mediaItemsList = MediaItem.facetedSearch(restrictToSet:idList.join(','), active:true, visibleInStorefront:true).list()
            def mediaItemsMap = mediaItemsList.collectEntries{[it.id, (it)]}
            def finalSearchResult = []
            idList.each {
                if(mediaItemsMap.containsKey(new Long(it))) {
                    finalSearchResult.add(mediaItemsMap[new Long(it)])
                }
            }
            resp.results = finalSearchResult
            resp.totalCount = resp.meta.pagination.total
        }catch(e){
            log.error("Could not make rest call to the API from the mediaService.")
        }

        resp
    }

     def getMediaTypes() {
        def mediaTypes = []
        grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.MediaItem") {
                def simpleName = it.clazz.simpleName
                if(simpleName == "SocialMedia"){
                    mediaTypes << "Social Media"
                }else{
                    mediaTypes << simpleName
                }
            }
        }
        mediaTypes.sort()
    }
}