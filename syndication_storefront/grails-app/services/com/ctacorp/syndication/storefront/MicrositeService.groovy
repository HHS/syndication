package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.microsite.MediaSelector
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

@Transactional
class MicrositeService {
    
    def springSecurityService
    def tagService
    def grailsApplication
    def mediaService
    RestBuilder rest = new RestBuilder()

    def serviceMethod() {

    }

    def saveBuild(params, templateType){
        MicroSite microSite = new MicroSite()
        microSite.user = User.get(springSecurityService.currentUser.id)
        microSite.title = params.title
        microSite.logoUrl = params.logo ? new URL(params.logo): null
        microSite.footerText = params.footerText
        microSite.templateType = templateType
        microSite = updateMediaAreas(microSite, params)
        microSite.footerLink1 = params.footerLink1 ? new URL(params.footerLink1) : null
        microSite.footerLink2 = params.footerLink2 ? new URL(params.footerLink2) : null
        microSite.footerLink3 = params.footerLink3 ? new URL(params.footerLink3) : null
        microSite.footerLink4 = params.footerLink4 ? new URL(params.footerLink4) : null
        microSite.footerLinkName1 = params.footerLinkName1
        microSite.footerLinkName2 = params.footerLinkName2
        microSite.footerLinkName3 = params.footerLinkName3
        microSite.footerLinkName4 = params.footerLinkName4

        microSite.validate()

        if(microSite.hasErrors()){
            return microSite
        }

        microSite.mediaArea1?.save(flush:true)
        microSite.mediaArea2?.save(flush:true)
        microSite.mediaArea3?.save(flush:true)

        microSite.save(flush:true)
    }
    
    def updateBuild(MicroSite microSite, params){
        microSite.user = User.get(springSecurityService.currentUser.id)
        microSite.title = params.title
        microSite.logoUrl = params.logo ? new URL(params.logo): null
        microSite.footerText = params.footerText
        microSite = updateMediaAreas(microSite, params)
        microSite.footerLink1 = params.footerLink1 ? new URL(params.footerLink1) : null
        microSite.footerLink2 = params.footerLink2 ? new URL(params.footerLink2) : null
        microSite.footerLink3 = params.footerLink3 ? new URL(params.footerLink3) : null
        microSite.footerLink4 = params.footerLink4 ? new URL(params.footerLink4) : null
        microSite.footerLinkName1 = params.footerLinkName1
        microSite.footerLinkName2 = params.footerLinkName2
        microSite.footerLinkName3 = params.footerLinkName3
        microSite.footerLinkName4 = params.footerLinkName4

        microSite.mediaArea1?.save()
        microSite.mediaArea2?.save()
        microSite.mediaArea3?.save()

        microSite
    }

    def updateMediaAreas(MicroSite microSite, params){
        if(params.pane1selectorType) {
            if(microSite.mediaArea1){
                microSite.mediaArea1.properties = [selectorType: params.pane1selectorType ?: microSite?.mediaArea1?.selectorType, selectionId: params."pane1ListId", displayStyle:params.pane1DisplayStyle, sortBy: params.pane1Sort, orderBy: params.pane1Order, header: params.pane1Header, description: params.pane1description]
            }
            else{
                microSite.mediaArea1 = new MediaSelector([selectorType:params.pane1selectorType,selectionId:params."pane1ListId", displayStyle:params.pane1DisplayStyle, sortBy:params.pane1Sort,orderBy:params.pane1Order, header:params.pane1Header, description:params.pane1description])
                microSite.mediaArea1?.save(flush:true)
            }
        }
        if(params.pane2selectorType) {
            if (microSite.mediaArea2) {
                microSite.mediaArea2.properties = [selectorType: params.pane2selectorType ?: microSite?.mediaArea2?.selectorType, selectionId: params."pane2ListId", displayStyle:params.pane2DisplayStyle, sortBy: params.pane2Sort, orderBy: params.pane2Order, header: params.pane2Header, description: params.pane2description]
            } else {
                microSite.mediaArea2 = new MediaSelector([selectorType:params.pane2selectorType,selectionId:params."pane2ListId", displayStyle:params.pane2DisplayStyle, sortBy:params.pane2Sort,orderBy:params.pane2Order, header:params.pane2Header, description:params.pane2description])
                microSite.mediaArea2?.save(flush:true)
            }
        }
        if(params.pane3selectorType){
            if (microSite.mediaArea3) {
                microSite.mediaArea3.properties = [selectorType: params.pane3selectorType ?: microSite?.mediaArea3?.selectorType, selectionId: params."pane3ListId", displayStyle:params.pane3DisplayStyle, sortBy: params.pane3Sort, orderBy: params.pane3Order, header: params.pane3Header, description: params.pane3description]
            } else {
                microSite.mediaArea3 = new MediaSelector([selectorType:params.pane3selectorType,selectionId:params."pane3ListId", displayStyle:params.pane3DisplayStyle, sortBy:params.pane3Sort,orderBy:params.pane3Order, header:params.pane3Header, description:params.pane3description])
                microSite.mediaArea3?.save(flush:true)
            }
        }
        microSite
    }

    def getMediaItems(MediaSelector mediaArea, def max = 10, def offset = 0){
        def mediaItems = null
        switch(mediaArea?.selectorType){
            case MediaSelector.SelectorType.TAG:
                mediaItems = tagService.getMediaForTagId(mediaArea.selectionId, [sort:mediaArea.sortBy, order:mediaArea.orderBy,max:max, offset:offset])
                break
            case MediaSelector.SelectorType.COLLECTION: List<Long> mediaItemIds = Collection.get(mediaArea.selectionId)?.mediaItems?.id
                mediaItems = MediaItem.findAllByIdInList(mediaItemIds, [sort:mediaArea.sortBy, order:mediaArea.orderBy,max:max, offset:offset])
                break
            case MediaSelector.SelectorType.SOURCE: mediaItems = MediaItem.findAllBySource(Source.get(mediaArea.selectionId), [sort:mediaArea.sortBy, order:mediaArea.orderBy,max:max, offset:offset])
                break
            case MediaSelector.SelectorType.USER_MEDIA_LIST: List<Long> mediaItemIds = UserMediaList.get(mediaArea.selectionId)?.mediaItems?.id
                mediaItems = MediaItem.findAllByIdInList(mediaItemIds, [sort:mediaArea.sortBy, order:mediaArea.orderBy,max:max, offset:offset])
                break
            case MediaSelector.SelectorType.CAMPAIGN: List<Long> mediaItemIds = Campaign.get(mediaArea.selectionId)?.mediaItems?.id
                mediaItems = MediaItem.findAllByIdInList(mediaItemIds, [sort:mediaArea.sortBy, order:mediaArea.orderBy,max:max, offset:offset])
                break
        }
        mediaItems
    }
    
    def getSelectorTypeItem(type,Long itemId){
        switch(type){
            case "TAG":tagService.getTag(itemId)
                break
            case "COLLECTION": Collection.get(itemId)
                break
            case "SOURCE": Source.get(itemId)
                break
            case "USER_MEDIA_LIST": UserMediaList.get(itemId)
                break
            case "CAMPAIGN": Campaign.get(itemId)
                break
        }
    }
    
    def getMediaData(def mediaItemIds,params){
        def resp = null
        try {
            resp = rest.get(grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath + "/resources/media.json?max=10&restrictToSet=${mediaItemIds}&sort=${params.sort}&order=${params.order}")?.json
        }catch(e){
            log.error(e)
            println "error: " + e
        }
        resp
    }
    
    def getMediaContents(def mediaItems){
        def resp = []
        mediaItems.each{ item ->
            try {
                resp << rest.get(grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath + "/resources/media/${item.id}/syndicate.json?autoplay=false")
            }catch(e){
                log.error(e)
                println "error: " + e
            }
        }
        resp
    }

}
