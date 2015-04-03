/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import grails.transaction.NotTransactional
import grails.transaction.Transactional
import com.ctacorp.syndication.media.*
import com.ctacorp.solr.EntityType

@Transactional()
class ResourcesService {

    static transactional = false

    def tagsService
    def solrSearchService


    private static int getMax(params) {
        //TODO we need to define a global max somewhere, and load that here
        Math.min(params.int("max") ?: 20, 1000)
    }

    @Transactional(readOnly = true)
    def mediaSearch(params) {
        if (!params.q?.trim())
            return []

        def mediaItemType = EntityType.MEDIAITEM.toString()
        def fullSolrResultList = []

        try {
            def solrSearchResults = solrSearchService.search(params.q,EntityType.MEDIAITEM)
            fullSolrResultList = solrSearchResults.getResults().collect {
                it.id.replace("${mediaItemType}-", '')
            }
        } catch (ex) {
            log.info "failed on search ${ex}"
        }

        if (fullSolrResultList.isEmpty()) {
            return [list:[], listCount: 0]
        }

        def newList = fullSolrResultList
        def startIndex = params.offset ? params.offset as int : 0
        def endIndex = null
        if(params.max) {
            endIndex = startIndex + new Integer(params.max) - 1
            if(endIndex >= newList.size()){
                endIndex = newList.size()-1
            }
        }
        else {
            startIndex = null
            endIndex = null
        }

        if(startIndex!=null && endIndex!=null ) {
            newList = newList[startIndex..endIndex]
        }

        params.restrictToSet = newList.join(',')
        params['active'] = true

        def mediaItemsList = MediaItem.facetedSearch(params).list()
        def mediaItemsMap = mediaItemsList.collectEntries{[it.id, (it)]}
        def response = []

        newList.each {
            if(mediaItemsMap.containsKey(new Long(it))) {
                response << mediaItemsMap[new Long(it)]
            }
        }

        return [list:response, listCount: fullSolrResultList.size()]
    }

    @Transactional(readOnly = true)
    def globalSearch(params) {
        def total = 0
        if (!params.q?.trim()) {
            return []
        }

        def solrSearchResults
        try {
            solrSearchResults = solrSearchService.search(params.q ?: "")
        } catch (ex) {
            log.error "solr is down: ${ex.getMessage()}", ex
            return []
        }

        def mediaItemType = EntityType.MEDIAITEM.toString()
        def camaignType =EntityType.CAMPAIGN.toString()
        def sourceType = EntityType.SOURCE.toString()
        def tagType = EntityType.TAG.toString()
        def response = [:]
        def ids = []

        solrSearchResults.getResults().each { searchResult ->

            if(searchResult.entityType == mediaItemType) {
                ids << searchResult.id.replace("${mediaItemType}-", '')
            }
            if(searchResult.entityType == camaignType) {
                def item = [id: searchResult.id.replace("${camaignType}-", ''), name: searchResult.name]
                total = total + addGroupedMedia(response, "campaigns", item)
            }
            if(searchResult.entityType == sourceType) {
                def item = [id: searchResult.id.replace("${sourceType}-", ''), name: searchResult.name]
                total = total + addGroupedMedia(response, "sources", item)
            }
            if (searchResult.entityType == tagType) {
                def item = [id: searchResult.id.replace("${tagType}-", ''), name: searchResult.name]
                total = total + addGroupedMedia(response, "tags", item)
            }
         }

        /* get the results from solr and then return the objects from the ids */
        try {
            params.restrictToSet = ids.join(',')
            def limits = [max: params.max, offset: params.offset]
            params['active'] = true

            def mediaItemsMap = (ids.size() > 0) ? MediaItem.facetedSearch(params).list().collectEntries{[it.id, (it)]} : null

            ids.each {
                def searchResult = mediaItemsMap[new Long(it)]
                def item = [id: searchResult.id, name: searchResult.name]
                switch (searchResult) {
                    case Html: total = total + addGroupedMedia(response, "htmls", item, searchResult.getClass().simpleName); break;
                    case Image: total = total + addGroupedMedia(response, "images", item, searchResult.getClass().simpleName); break;
                    case Infographic: total = total + addGroupedMedia(response, "infographics", item, searchResult.getClass().simpleName); break;
                    case Audio: total = total + addGroupedMedia(response, "audio", item, searchResult.getClass().simpleName); break;
                    case Video: total = total + addGroupedMedia(response, "videos", item, searchResult.getClass().simpleName); break;
                    case Widget: total = total + addGroupedMedia(response, "widget", item, searchResult.getClass().simpleName); break;
                    case SocialMedia: total = total + addGroupedMedia(response, "socialMedia", searchResult.getClass().simpleName); break;
                    case Collection: total = total + addGroupedMedia(response,"collections",item,searchResult.getClass().simpleName); break;
                    default: log.error("Unsupported type in global search: ${item}")
                }
            }

        } catch (e) {
            log.error "solr is down: ${e.getMessage()}", e
        }
        if (response.isEmpty()) {
            return null
        }
        response.total = total
        response
    }

    @NotTransactional()
    private addGroupedMedia(Map groups, String group, itemToAdd, String mediaType = null) {
        def count = 0
        if (!groups[group]) {
            groups[group] = [items: []]
            if (mediaType) {
                groups[group].mediaType = mediaType
            }
        }
        if (!groups[group].items.contains(itemToAdd)) {
            groups[group].items << itemToAdd
            count++
        }
        return count
    }

}
