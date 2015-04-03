package com.ctacorp.syndication.audit

import grails.transaction.Transactional

@Transactional
class QueryAuditService {

    def log(String queryString) {
        String cleanedString = queryString.toLowerCase().trim()
        SearchQuery searchQuery = SearchQuery.findByQueryIlike(cleanedString)
        if(searchQuery){
            searchQuery.searchCount++
        } else{
            searchQuery = new SearchQuery(query: cleanedString, searchCount: 1L)
        }
        searchQuery.save(flush: true)
    }

    def getMostPopular(Date within = new Date()-30, int max = 10){
        SearchQuery.findAllByLastUpdatedGreaterThan(within, [sort:"searchCount", order: "DESC", max: max])
    }

    def getMostPopularAllTime(){
        getMostPopular(new Date()-36500)
    }
}