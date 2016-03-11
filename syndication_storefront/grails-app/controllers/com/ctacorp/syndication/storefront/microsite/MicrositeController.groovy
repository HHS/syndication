package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite.TemplateType
import com.ctacorp.syndication.storefront.UserMediaList
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_STOREFRONT_USER'])
class MicrositeController {
    def springSecurityService
    def mediaService
    def tagService
    def micrositeService

    def index() {
        def userMicrosites = MicroSite.findAllByUser(User.get(springSecurityService.currentUser.id))
        [
            featuredMedia: mediaService.getFeaturedMedia(max: 10),
            userMicrosites:userMicrosites
        ]
    }

    def selectNewMicrosite(){ }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_STOREFRONT_USER'])
    def show(MicroSite microSite){
        redirectToSpecificMicrosite(microSite, "show")
    }

    def edit(MicroSite microSite){
        redirectToSpecificMicrosite(microSite, "edit")
        return
    }

    def delete(MicroSite microSite){
        redirectToSpecificMicrosite(microSite, "delete")
        return
    }

    private redirectToSpecificMicrosite(MicroSite microSite, String action){
        switch(microSite.templateType){
            case TemplateType.BLOG:
                redirect controller: "blog", action: action, id:microSite.id, params:[showAdminControls:params.showAdminControls ?: 0]
                break
            case TemplateType.GRID:
                redirect controller: "grid", action: action, id:microSite.id, params:[showAdminControls:params.showAdminControls ?: 0]
                break
            case TemplateType.CAROUSEL:
                redirect controller: "carousel", action: action, id:microSite.id, params:[showAdminControls:params.showAdminControls ?: 0]
                break
            case TemplateType.CLASSIC:
                redirect controller: "classic", action: action, id:microSite.id, params:[showAdminControls:params.showAdminControls ?: 0]
                break
        }
    }

    def currentList(){
        switch(params.listType){
            case "USER_MEDIA_LIST":
                def userMediaLists = UserMediaList.findAllByUser(springSecurityService.currentUser as User)
                render template: "userMediaList",
                        model:[selectorType:"USER_MEDIA_LIST",
                               userMediaLists:userMediaLists,
                               area:params.mediaAreaValue,
                               currentUserMediaList:MediaSelector.get(params.int("mediaAreaId")).selectionId
                        ]
                break
            case "COLLECTION":
                def currentCollection = Collection.get(MediaSelector.get(params.int("mediaAreaId")).selectionId)
                render template: "collections", model:[
                        selectorType:"COLLECTION",
                        collections: Collection.findAllByLanguageAndActiveAndVisibleInStorefront(Language.get(currentCollection?.language.id ?: 1),true,true),
                        area:params.mediaAreaValue,
                        selectedLanguage:Collection.get(currentCollection.id).language.id,
                        languages:Language.findAllByIsActive(true),
                        currentCollection:currentCollection.id]
                break
            case "TAG":
                def tagTypes = tagService.getTagTypes()
                def languages = tagService.getAllActiveTagLanguages()
                def currentTag = tagService.getTag(params.int("listId"))

                def tagInfo = tagService.listTags([
                        max:1000,languageId:
                        currentTag.language.id ?: 1,
                        typeId: currentTag.type.id ?: 1])
                def tags = tagInfo?.tags

                render template: "tags", model:[
                        selectorType:"TAG",
                        tags: tags.sort{it.name},
                        area:params.mediaAreaValue,
                        selectedLanguage:currentTag.language.id ?: 1,
                        languages:languages, selectedTagType:currentTag.type.id ?: 1,
                        tagTypes:tagTypes, currentTag:currentTag]
                break
            case "SOURCE":
                render template: "sources", model:[
                                selectorType:"SOURCE",
                               sources:Source.list(),
                               area:params.mediaAreaValue,
                               currentSource:MediaSelector.get(params.int("mediaAreaId")).selectionId]
                break
            case "CAMPAIGN": render template: "campaigns", model:[selectorType:"CAMPAIGN", campaigns:Campaign.list(), area:params.mediaAreaValue, currentCampaign:MediaSelector.get(params.int("mediaAreaId")).selectionId]
                return
            default:
                render "No Media Source and list specified"
        }
    }

    def specificList(){
        switch(params.listType){
            case "USER_MEDIA_LIST":
                def userMediaLists = UserMediaList.findAllByUser(springSecurityService.currentUser as User)
                render template: "userMediaList", model:[selectorType:"USER_MEDIA_LIST", userMediaLists:userMediaLists, area:params.mediaAreaValue]
                break
            case "COLLECTION":
                    render template: "collections", model:[selectorType:"COLLECTION", collections: Collection.findAllByLanguageAndActiveAndVisibleInStorefront(Language.get(params.long("language") ?: 1),true,true), area:params.mediaAreaValue, selectedLanguage:params.long("language"), languages:Language.findAllByIsActive(true)]
                break
            case "TAG":
                def tagTypes = tagService.getTagTypes()
                def languages = tagService.getAllActiveTagLanguages()
                def tagInfo = tagService.listTags([max:1000,languageId: params.int("languageId"),typeId: params.int("typeId")])
                def tags = tagInfo?.tags

                render template: "tags", model:[selectorType:"TAG", tags: tags.sort{it.name}, area:params.mediaAreaValue, selectedLanguage:languages.find { "${it.id}" == "${params.languageId}" }?.id ?: 1, languages:languages, selectedTagType:tagTypes.find { "${it.id}" == "${params.typeId}" }?.id ?: 1, tagTypes:tagTypes, currentServerUrl:grailsApplication.config.grails.serverURL]
                break
            case "SOURCE": render template: "sources", model:[selectorType:"SOURCE", sources:Source.list(), area:params.mediaAreaValue]
                break
            case "CAMPAIGN": render template: "campaigns", model:[selectorType:"CAMPAIGN", campaigns:Campaign.list(), area:params.mediaAreaValue]
                return
        }
    }

    def searchTags(){
        def tagTypes = tagService.getTagTypes()
        def languages = tagService.getAllActiveTagLanguages()
        def tagInfo = tagService.listTags([max:1000,languageId: params.int("languageId") ?: 1,typeId: params.int("typeId") ?: 1, name:params.name])
        def tags = tagInfo?.tags

        render tags as JSON
    }

    def summary(){
        def selectorTypeItem = micrositeService.getSelectorTypeItem(params.listType,params.long("listId"))
        def listType = params.listType ? MediaSelector.SelectorType."${params.listType}" : null
        render template: "summary", model:[ajaxRequest:true, listType:listType, header:params.panelHeader, listId:params.listId, item:selectorTypeItem, sortBy:params.sortBy, orderBy:params.orderBy, displayStyle: params.displayStyle, sidePanel:params.boolean("sidePanel"), area:params.area]
    }

    def displayStyle(){
        def mediaIds = params.("mediaItems").split(',').collect{ it as Long }.findAll{
            MediaItem.get(it as Long).instanceOf(Video) == false}

        def count = mediaIds.size()
        int offset = params.int("offset")
        def mediaId = mediaIds[offset]

        def syndicateResponse = micrositeService.getMediaContents([MediaItem.get(mediaId)])
        render template:"fullContentPagination", model:[syndicateResponse:syndicateResponse[0], offset:params.int("offset"), count:count, panel:params.panel]
    }

    def blogGallery() {}

    def classicGallery() {}

    def carouselGallery() {}

    def gridGallery() {}

    def microsite() {}

    def templateBlog() {}

    def templateCarousel() {}

    def templateClassic() {}

    def templateGrid() {}

    def newTemplates() {}
}
