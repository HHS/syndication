package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.audit.SearchQuery
import groovy.sql.Sql

/**
 * Created by nburk on 7/28/15.
 * adds all the rows up from the previous day into one row for each media item.
 * in order to cut down on the number of rows in the SearchQuery table.
 */
class SearchQueryJob {
    static triggers = {
        cron name: 'searchQueryMinify', cronExpression: "0 0 0 ? * *" //Every night at midnight
    }

    def mediaItemsService
    def dataSource

    def execute(){
        log.info "Initiated Daily SearchQuery Minify Job"
        def sql = new Sql(dataSource)
        SearchQuery.withTransaction{ status ->
            def searches = sql.rows("SELECT SUM(search_count) AS searchCount,query,last_updated AS lastUpdated FROM search_query WHERE last_updated = ${new Date().clearTime() - 2} GROUP BY query")
            try{
                def tempSearchObjects = searches as SearchQuery[]
                SearchQuery.executeUpdate("DELETE SearchQuery s WHERE last_updated = :startDate",[startDate:new Date().clearTime() - 2])

                tempSearchObjects.each{query ->
                    if(!query.save(flush:true)){
                        status.setRollbackOnly()
                    }
                }
            } catch(e){
                log.error(e)
                log.error("could not properly combine SearchQuery rows for the date of : " + new Date().clearTime() - 2)
            }
        }
    }
}
