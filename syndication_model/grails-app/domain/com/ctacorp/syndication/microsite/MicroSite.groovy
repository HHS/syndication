package com.ctacorp.syndication.microsite

import com.ctacorp.syndication.authentication.User

class MicroSite {
    User user
    String title
    URL logoUrl
    String footerText
    TemplateType templateType
    MediaSelector mediaArea1
    MediaSelector mediaArea2
    MediaSelector mediaArea3
    String footerLink1
    String footerLink2
    String footerLink3
    String footerLink4
    String footerLinkName1
    String footerLinkName2
    String footerLinkName3
    String footerLinkName4

    static constraints = {
        title           nullable: true
        footerText      nullable: true
        logoUrl         nullable: true
        templateType    nullable: false
        mediaArea1      nullable: true
        mediaArea2      nullable: true
        mediaArea3      nullable: true
        footerLink1     nullable: true
        footerLink2     nullable: true
        footerLink3     nullable: true
        footerLink4     nullable: true
        footerLinkName1 nullable: true
        footerLinkName2 nullable: true
        footerLinkName3 nullable: true
        footerLinkName4 nullable: true
    }

    enum TemplateType{
        GRID("Grid"),
        CAROUSEL("Carousel"),
        CLASSIC("Classic"),
        BLOG("Blog")

        String name

        TemplateType(String name){
            this.name = name
        }
    }
}
