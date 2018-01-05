package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional
import grails.plugins.rest.client.RestBuilder
import groovy.time.TimeCategory
import grails.util.Holders
import grails.web.servlet.mvc.GrailsParameterMap

import javax.annotation.PostConstruct

@Transactional(readOnly = true)
class MediaService {

    RestBuilder rest = new RestBuilder()
    def config = Holders.config
    def grailsApplication
    def elasticsearchService

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

    def mediaItemElasticSearch(String searchQuery, params = [:]) {

        if(!searchQuery) {
            return []
        }

        def elasticsearchFoundIds

        try {
            elasticsearchFoundIds = elasticsearchService.search(searchQuery).collect { it._id }
        } catch(e) {
            log.error('error occurred during elasticsearch search', e)
        }

        if(!elasticsearchService) {
            return []
        }

        params.offset = params.offset ?: 0
        def mediaItems = MediaItem.facetedSearch(restrictToSet: elasticsearchFoundIds.join(','), active: true, visibleInStorefront: true, syndicationVisibleBeforeDate: new Date().toString()).list(max: params.max, offset: params.offset)
        mediaItems = bubbleExactFieldMatcheToTheTop(mediaItems, searchQuery, params.offset ?: "0")
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

    def bubbleExactFieldMatcheToTheTop(def mediaItems, String searchQuery, String offset) {
        def item = MediaItem.findBySourceUrlOrName(searchQuery,searchQuery)
        if(item && item.active && item.visibleInStorefront){
            if(mediaItems.id.contains(item.id) && offset == "0") {
                def index = mediaItems.findIndexOf{it.id==item.id}
                mediaItems.removeAt(index)
            }
            if(offset == "0"){
                mediaItems.add(0, item)
            }
        }
        mediaItems
    }
}