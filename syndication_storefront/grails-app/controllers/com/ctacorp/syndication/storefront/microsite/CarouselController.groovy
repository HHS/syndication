package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_STOREFRONT_USER'])
class CarouselController {
    def tagService
    def micrositeService
    def micrositeFilterService

    def sort = [[name:"Alphabetically",value:"name"], [name:"Authored Date",value:"dateContentAuthored"], [name:"Published Date", value:"dateContentPublished"]]
    def order = [[name:"Ascending", value:"asc"],[name:"Descending", value:"desc"]]
    Closure displayStyles = {MediaSelector.DisplayStyle.values()}

    def index() {}

    def test(){}

    def create(){
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
            displayStyle:displayStyles()
        ]
    }

    def save(){
        def microSite = micrositeService.saveBuild(params, "CAROUSEL")
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

    @Secured(['permitAll'])
    def show(){
        def microSite = MicroSite.get(params.long("id"))
        if(FlaggedMicrosite.findByMicrosite(microSite) && !FlaggedMicrosite.findByMicrosite(microSite).ignored) {
            flash.error = "The Microsite is temporarily blocked."
            redirect controller: "storefront", action: "index"
        }

        def pane3MediaItems = micrositeService.getMediaItems(microSite.mediaArea3, 50)
        def pane3MediaItemContents = micrositeService.getMediaContents(pane3MediaItems)

        def pane2MediaItems = micrositeService.getMediaItems(microSite.mediaArea2, 50)
        def pane2MediaItemContents = micrositeService.getMediaContents(pane2MediaItems)

        def pane1MediaItems = micrositeService.getMediaItems(microSite.mediaArea1)?.id?.join(",") ?: []
        String carouselData = micrositeService.getMediaData(pane1MediaItems,[sort:microSite?.mediaArea1?.sortBy,order:microSite?.mediaArea1?.orderBy]) as JSON


        render view:"show", model:[
            microSite:microSite,
            carouselData:carouselData,
            pane1MediaItems:micrositeService.getMediaItems(microSite.mediaArea1),
            pane2MediaItems:micrositeService.getMediaItems(microSite.mediaArea2),
            pane3MediaItems:micrositeService.getMediaItems(microSite.mediaArea3),
            pane2MediaItemContents:pane2MediaItemContents,
            pane3MediaItemContents:pane3MediaItemContents,
            collection:params.collection,
            apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
        ]
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
