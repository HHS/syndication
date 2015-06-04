package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.UserMediaList
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class BlogController {
    
    def tagService
    def micrositeService

    def sort = ["name", "dateContentAuthored", "dateContentPublished"]
    def order = [[name:"ascending", value:"asc"],[name:"descending", value:"desc"]]
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

        redirect action: "show", id:microSite.id, params:[showAdminControls:true]
    }

    def edit(){
        def microSite = MicroSite.get(params.long("id"))
        def userMediaLists = UserMediaList.list()
        def collections = Collection.list()
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


        def pane3MediaItems = micrositeService.getMediaItems(microSite.mediaArea3, 50)
        def pane2MediaItems = micrositeService.getMediaItems(microSite.mediaArea2)
        def pane1MediaItems = micrositeService.getMediaItems(microSite.mediaArea1, 50)

        def blogs = micrositeService.getMediaContents(pane1MediaItems)

        render view:"show", model:[microSite: microSite,
                                        blogs:blogs,
                                        pane2MediaItems:pane2MediaItems,
                                        pane3MediaItems:pane3MediaItems,
                                        collection:params.collection,
                                        apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath]
    }

    def update(MicroSite microSite){
        if(!microSite?.id){
            flash.error = "Could not find microSite"
            redirect action:"edit"
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
        microSite.delete(flush: true)
        flash.message = "Microsite ${name} has been deleted!"

        redirect controller:"microsite", action: "index"
    }
}
