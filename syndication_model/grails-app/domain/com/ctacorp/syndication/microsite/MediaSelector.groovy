package com.ctacorp.syndication.microsite

class MediaSelector {
    SelectorType selectorType
    Long selectionId
    String sort
    String order

    static constraints = {
        selectorType    nullable: false
        selectionId     nullable: true, min: 1L
        sort            nullable: true, blank: false
        order           nullable: true, blank: false
    }

    enum SelectorType{
        TAG, COLLECTION, SOURCE, USER_MEDIA_LIST, CAMPAIGN, MOST_POPULAR, MOST_RECENT
    }
}
