package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import groovy.time.TimeCategory
import grails.util.Holders
import com.ctacorp.solr.EntityType
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class MediaService {

    RestBuilder rest = new RestBuilder()
    def config = Holders.config
    def grailsApplication
    def solrSearchService

    def getFeaturedMedia(params = [:]) {
        def featured = FeaturedMedia.where {
            mediaItem {
                active == true
                visibleInStorefront == true
                dateSyndicationVisible <= new Date() || dateSyndicationVisible == null
            }
        }.list(params)

        featured*.mediaItem
    }

    def listNewestMedia(params) {
        params.max = params.max ?: 20
        params.offset = params.offset ?: 0
        params.sort = "-id"
        params.visibleInStorefront = true
        params.active = true
        params.languageName = "english"
        params.syndicationVisibleBeforeDate = new Date().toString()
        MediaItem.facetedSearch(params).list([max: params.max, offset: params.offset])
    }

    //Can't test this because case insensitive query is broken in test phase: https://jira.grails.org/browse/GRAILS-8841
    @Transactional
    def findMediaByAll(params) {
        MediaItem.facetedSearch([
                nameContains                : params.title?.replace('%', '\\%'),
                languageName                : params.language,
                sourceName                  : params.source,
                sourceUrlContains           : params.domain,
                mediaTypes                  : params.mediaType,
                visibleInStorefront         : true,
                syndicationVisibleBeforeDate: new Date().toString(),
                active                      : true,
                restrictToSet               : params.topicItems,
                createdByContains           : params.createdBy
        ]).list(max: params.max, offset: params.offset)
    }

    def mediaItemSolrSearch(String searchQuery, params = [:]) {
        def solrSearchResults
        try {
            solrSearchResults = solrSearchService.search(searchQuery ?: "", EntityType.MEDIAITEM)
        } catch (ex) {
            log.error "solr is down: ${ex.getMessage()}", ex
            return []
        }

        def mediaItemType = EntityType.MEDIAITEM.toString()
        def ids = []

        solrSearchResults.getResults().each { searchResult ->
            ids << searchResult.id.replace("${mediaItemType}-", '')
        }

        params.offset = params.offset ?: 0
        def mediaItems = MediaItem.facetedSearch(restrictToSet: ids.join(','), active: true, visibleInStorefront: true, syndicationVisibleBeforeDate: new Date().toString()).list(max: params.max, offset: params.offset)
        mediaItems = bubbleExactFieldMatcheToTheTop(mediaItems, searchQuery, params.offset ?: 0)
        mediaItems
    }

    def getMediaTypes() {
        def mediaTypes = []
        grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.media.MediaItem") {
                mediaTypes << [name:it.clazz.simpleName, id:it.clazz.simpleName]
            }
        }

        MediaItem.StructuredContentType.enumConstants.each{

            mediaTypes << [name:it.prettyName, id:it.name()]
        }

        mediaTypes.sort{ it.name }
    }

    def bubbleExactFieldMatcheToTheTop(def mediaItems, String searchQuery, def offset) {
        def item = MediaItem.findBySourceUrlOrName(searchQuery,searchQuery)
        if(item){
            if(mediaItems.id.contains(item.id) && offset) {
                def index = mediaItems.findIndexOf{it.id==item.id}
                mediaItems.removeAt(index)
            }
            if(offset == "0" || offset == 0){
                mediaItems.add(0, item)
            }
        }
        mediaItems
    }
}