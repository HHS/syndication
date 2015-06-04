package com.ctacorp.syndication.microsite

class MediaSelector {
    SelectorType selectorType
    DisplayStyle displayStyle
    Long selectionId
    String sortBy
    String orderBy
    String header
    String description

    static constraints = {
        selectorType    nullable: false
        displayStyle    nullable: true
        selectionId     nullable: true, min: 1L
        sortBy            nullable: true, blank: false
        orderBy           nullable: true, blank: false
        header          nullable: true, blank: true
        description     nullable: true, blank: true
    }

    enum SelectorType{
        TAG, COLLECTION, SOURCE, USER_MEDIA_LIST, CAMPAIGN, MOST_POPULAR, MOST_RECENT
    }

    enum DisplayStyle{
        List("List"), THUMBNAIL_GRID("Thumbnail Grid"), FULL_CONTENT("Full Content")

        String name

        DisplayStyle(String name){
            this.name = name
        }
    }
}
