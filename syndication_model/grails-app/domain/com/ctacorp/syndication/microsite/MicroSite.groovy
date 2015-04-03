package com.ctacorp.syndication.microsite

class MicroSite {
    String title
    URL logoUrl
    String footerText
    TemplateType templateType
    MediaSelector mediaArea1
    MediaSelector mediaArea2
    MediaSelector mediaArea3
    URL footerLink1
    URL footerLink2
    URL footerLink3
    URL footerLink4

    static constraints = {
        title           nullable: false,    blank: false
        footerText      nullable: true,     blank:false
        logoUrl         nullable: true
        templateType    nullable: false
        mediaArea1      nullable: true
        mediaArea2      nullable: true
        mediaArea3      nullable: true
        footerLink1     nullable: true
        footerLink2     nullable: true
        footerLink3     nullable: true
        footerLink4     nullable: true
    }

    enum TemplateType{
        PINTEREST, CAROUSEL, CLASSIC
    }
}
