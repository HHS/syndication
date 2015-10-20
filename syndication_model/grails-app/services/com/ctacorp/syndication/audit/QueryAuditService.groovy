package com.ctacorp.syndication.audit

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class QueryAuditService {
    def dataSource

    def log(String queryString) {
        String cleanedString = queryString.toLowerCase().trim()
        SearchQuery searchQuery = new SearchQuery(query: cleanedString, searchCount: 1L, lastUpdated: new Date().clearTime())
        searchQuery.save(flush: true)
    }

    def getMostPopular(Date within = new Date()-30, int max = 10){
        def sql = new Sql(dataSource)
        def responseRows = sql.rows("select SUM(search_count) AS searchCount,query,last_updated AS lastUpdated from search_query Where last_updated >= ${within.clearTime()} Group By query Order By searchCount DESC LIMIT ${max}")
        responseRows as SearchQuery[]
    }

    def getMostPopularAllTime(){
        getMostPopular(new Date()-36500)
    }
}