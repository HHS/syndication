package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_STOREFRONT_USER'])
class BlogController {
    
    def tagService
    def micrositeService
    def micrositeFilterService

    def sort = [[name:"Alphabetically",value:"name"], [name:"Authored Date",value:"dateContentAuthored"], [name:"Published Date", value:"dateContentPublished"]]
    def order = [[name:"Ascending", value:"asc"],[name:"Descending", value:"desc"]]
    Closure displayStyles = {MediaSelector.DisplayStyle.values()}

    def index() {}

    def create(){
        def userMediaLists = UserMediaList.list()

        [
            userMediaLists:userMediaLists,
            collections:Collection.list(),
            sources:Source.list(),
            tags:tagService.getTagsByType("General"),
            campaigns:Campaign.list(),
            sort:sort,
            order:order,
            displayStyle:displayStyles()
        ]
    }

    def save(){
        def microSite = micrositeService.saveBuild(params, "BLOG")
        if(microSite.hasErrors()){
            flash.errors = microSite.errors
            render view:"create", model:[
                                                userMediaLists:UserMediaList.list(),
                                                collections:Collection.list(),
                                                sources:Source.list(),
                                                tags:tagService.getTagsByType("General"),
                                                campaigns:Campaign.list(),
                                                sort:sort,
                                                order:order,
                                                displayStyle:displayStyles(),
                                                microSite: microSite
                                         ]
            return
        }
        micrositeFilterService.performValidation(microSite)

        redirect action: "show", id:microSite.id, params:[showAdminControls:true]
    }

    def edit(){
        def microSite = MicroSite.get(params.long("id"))
        def userMediaLists = UserMediaList.list()
        def collections = Collection.findAllByLanguageAndActiveAndVisibleInStorefront(Language.get(params.long("language") ?: 1),true,true)
        def sources = Source.list()
        def tags = tagService.getTagsByType("General")
        def campaigns = Campaign.list()
        [
            userMediaLists:userMediaLists,
            collections:collections,
            sources:sources,
            tags:tags,
            campaigns:campaigns,
            sort:sort,
            order:order,
            displayStyle:displayStyles(),
            microSite:microSite
        ]
    }

    @Secured(['permitAll'])
    def show(){
        def microSite = MicroSite.get(params.long("id"))
        if(FlaggedMicrosite.findByMicrosite(microSite) && !FlaggedMicrosite.findByMicrosite(microSite).ignored) {
            flash.error = "The Microsite is temporarily blocked."
            redirect controller: "storefront", action: "index"
        }
        def pane3MediaItems = micrositeService.getMediaItems(microSite.mediaArea3)
        def pane2MediaItems = micrositeService.getMediaItems(microSite.mediaArea2)
        def pane1MediaItems = micrositeService.getMediaItems(microSite.mediaArea1, 50)
        def maxBlogs = 5

        def blogs = micrositeService.getMediaContents(pane1MediaItems, 0, maxBlogs)

        render view:"show", model:[
                                        microSite: microSite,
                                        blogs:blogs,
                                        pane2MediaItems:pane2MediaItems,
                                        pane3MediaItems:pane3MediaItems,
                                        collection:params.collection,
                                        apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                                        blogOffset:0,
                                        maxBlogs:maxBlogs
        ]

    }

    @Secured(['permitAll'])
    def getMoreBlogs() {
        def microSite = MicroSite.get(params.long("id"))
        def pane1MediaItems = micrositeService.getMediaItems(microSite.mediaArea1, 50)
        def blogs = micrositeService.getMediaContents(pane1MediaItems, params.int("blogOffset"), params.int("maxBlogs"))

        if(!blogs) {
            render [:] as JSON
        }
        render template: "blogList", model:[blogs:blogs]
    }

    def update(MicroSite microSite){
        if(!microSite?.id){
            flash.error = "Could not find microSite"
            redirect controller: "microsite", action:"index"
            return
        }

        microSite = micrositeService.updateBuild(microSite, params)

        if(microSite.hasErrors()){
            flash.errors = microSite.errors
            render view:"edit", model: [userMediaLists:UserMediaList.list(),
                                        collections:Collection.list(),
                                        sources:Source.list(),
                                        tags:tagService.getTagsByType("General"),
                                        campaigns:Campaign.list(),
                                        sort:sort,
                                        order:order,
                                        displayStyle:displayStyles(),
                                        microSite:microSite]
            return
        }
        micrositeFilterService.validateOnUpdate(microSite)

        microSite.save(flush: true)
        flash.message = "microsite  Updated!"


        redirect action:"show", id:microSite.id, params:[showAdminControls:true]

    }

    def delete(MicroSite microSite){
        if(!microSite?.id){
            flash.error = "Could not find microSite"
            redirect controller: "microsite", action:"index"
            return
        }

        def name = microSite.title
        def flaggedMicrosite = FlaggedMicrosite.findByMicrosite(microSite)
        if(flaggedMicrosite){
            flaggedMicrosite.delete()
        }

            microSite.delete(flush: true)
            flash.message = "Microsite ${name} has been deleted!"

        redirect controller:"microsite", action: "index"
    }
}
