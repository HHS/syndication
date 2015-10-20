package com.ctacorp.syndication.audit

class SearchQuery {
    String query
    Long searchCount = 0L
    Date lastUpdated        //represents date created and Not modified

    static mapping = {
        version false
        autoTimestamp false
    }

    static constraints = {
        query maxSize: 255, nullable: false
        lastUpdated bindable: true
    }
}
