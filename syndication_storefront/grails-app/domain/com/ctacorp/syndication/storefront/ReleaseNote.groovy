package com.ctacorp.syndication.storefront

class ReleaseNote {
    String releaseNoteText
    Date releaseDate
    Date dateCreated
    Date lastUpdated

    static mapping = {
        releaseNoteText type: 'text'
    }

    static constraints = {
        releaseNoteText maxSize: Integer.MAX_VALUE, nullable: false, blank: false
        releaseDate nullable: false
        dateCreated()
        lastUpdated()
    }
}
