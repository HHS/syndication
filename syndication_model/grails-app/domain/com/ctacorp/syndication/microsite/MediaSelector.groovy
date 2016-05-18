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
        TAG("Tag"),
        COLLECTION("Collection"),
        SOURCE("Source"),
        USER_MEDIA_LIST("My List"),
        CAMPAIGN("Campaign"),
        MOST_POPULAR("Most Popular"),
        MOST_RECENT("Most Recent")

        String name

        SelectorType(String name){
            this.name = name
        }
    }

    enum DisplayStyle{
        List("List"), THUMBNAIL_GRID("Thumbnail Grid"), FULL_CONTENT("Full Content")

        String name

        DisplayStyle(String name){
            this.name = name
        }
    }
}
