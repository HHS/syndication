package com.ctacorp.syndication.audit

class SearchQuery {
    String query
    Long searchCount = 0L
    Date lastUpdated

    static mapping = {
        version false
    }

    static constraints = {
        query maxSize: 255, nullable: false
    }
}
