/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.media.Audio
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Image
import com.ctacorp.syndication.media.Infographic
import com.ctacorp.syndication.media.PDF
import com.ctacorp.syndication.media.Periodical
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.media.Video
import com.ctacorp.syndication.media.Widget
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_PUBLISHER'])
class MediaItemController {
    def tagService
    def mediaItemsService
    def springSecurityService
    RestBuilder rest = new RestBuilder()

    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_PUBLISHER'])
    def show(Long id) {
        MediaItem mi = MediaItem.get(id)
        flash.error = flash.error
        flash.message = flash.message
        switch(mi) {
            case Audio: redirect controller:        "Audio",       action: 'show', id: id, model:[audioInstance:mi];        break;
            case Collection: redirect controller:   "Collection",  action: 'show', id: id, model:[collectionInstance:mi];   break;
            case Html: redirect controller:         "Html",        action: 'show', id: id, model:[htmlInstance:mi];         break;
            case Image: redirect controller:        "Image",       action: 'show', id: id, model:[imageInstance:mi];        break;
            case Infographic: redirect controller:  "Infographic", action: 'show', id: id, model:[infographicInstance:mi];  break;
            case PDF:         redirect controller:  "PDF",         action: 'show', id: id, model:[PDFInstance:mi];          break;
            case Periodical:  redirect controller:  "Periodical",  action: 'show', id: id, model:[periodicalInstance:mi];   break;
            case Tweet: redirect controller:        "Tweet",       action: 'show', id: id, model:[tweetInstance:mi];        break;
            case Video: redirect controller:        "Video",       action: 'show', id: id, model:[videoInstance:mi];        break;
            case Widget: redirect controller:       "Widget",      action: 'show', id: id, model:[widgetInstance:mi];       break;
        }
    }

    def edit(Long id) {
        MediaItem mi = MediaItem.get(id)
        flash.error = flash.error
        flash.message = flash.message
        switch(mi) {
            case Audio: redirect controller:        "Audio",       action: 'edit', id: id, model:[audioInstance:mi];        break;
            case Collection: redirect controller:   "Collection",  action: 'edit', id: id, model:[collectionInstance:mi];   break;
            case Html: redirect controller:         "Html",        action: 'edit', id: id, model:[htmlInstance:mi];         break;
            case Image: redirect controller:        "Image",       action: 'edit', id: id, model:[imageInstance:mi];        break;
            case Infographic: redirect controller:  "Infographic", action: 'edit', id: id, model:[infographicInstance:mi];  break;
            case PDF:         redirect controller:  "PDF",         action: 'edit', id: id, model:[PDFInstance:mi];          break;
            case Periodical:  redirect controller:  "Periodical",  action: 'edit', id: id, model:[periodicalInstance:mi];   break;
            case Tweet: redirect controller:        "Tweet",       action: 'edit', id: id, model:[tweetInstance:mi];        break;
            case Video: redirect controller:        "Video",       action: 'edit', id: id, model:[videoInstance:mi];        break;
            case Widget: redirect controller:       "Widget",      action: 'edit', id: id, model:[widgetInstance:mi];       break;
        }
    }

    // for token input searches
    def tokenMediaSearch(String q){
        response.contentType = "application/json"
        params.active = params.active ?: ""
        params.visibleInStorefront = params.visibleInStorefront ?: ""
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            if(q.isInteger() && publisherItems().contains(q.toInteger() as Long)){
                render MediaItem.findAllByIdLike(q.toInteger(), [max:20]).collect{ [id:it.id, name:"$it.name"] } as JSON
                return
            } else {
                render MediaItem.facetedSearch([restrictToSet:publisherItems().join(","), nameContains: "${q}", active:params.active, visibleInStorefront:params.visibleInStorefront]).list([max:20]).collect{ [id:it.id, name:"$it.name"] } as JSON
                return
            }
        }
        if(q.isInteger()){
            render MediaItem.findAllByIdLikeOrNameIlike(q.toInteger(), "%${q}%", [max:20]).collect{ [id:it.id, name:"$it.id - $it.name"] } as JSON
        } else {
            render MediaItem.facetedSearch([nameContains: "${q}", active:params.active, visibleInStorefront:params.visibleInStorefront]).list([max:20]).collect{ [id:it.id, name:"$it.name"] } as JSON
        }
    }

    def search(){
        params.max = params.max ?: 15
        def mediaItems = null
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            params.inList = publisherItems().join(",")
            if(params.inList){
                mediaItems = mediaItemsService.findMediaByAll(params)
            }

        } else {
            mediaItems = mediaItemsService.findMediaByAll(params)
        }

        render view:"search", model:[mediaItemInstanceList:mediaItems,
                                     mediaItems:"something",
                                     mediaItemInstanceCount:mediaItems?.totalCount ?: 0,
                                     title:params.title,
                                     id:params.id,
                                     url:params.url,
                                     languageList:Language.findAllByIsActive(true),
                                     language:params.language,
                                     mediaTypeList:mediaItemsService.getMediaTypes(),
                                     mediaType:params.mediaType]
    }

    @Secured(['ROLE_ADMIN'])
    def updateOwner(MediaItem mediaItem){
        def mediaItemSubscriber = MediaItemSubscriber.findByMediaItem(mediaItem)
        if(!params.keyAgreement){
            flash.error = "You did not select a keyAgreement"
            redirect action: 'edit', id: mediaItem.id
            return
        }
        if(mediaItemSubscriber){
            mediaItemSubscriber.subscriberId = params.keyAgreement as Long
        } else {
            mediaItemSubscriber = new MediaItemSubscriber([mediaItem:mediaItem,subscriberId:params.subscriberId as Long])
        }
        mediaItemSubscriber.save(flush: true)

        flash.message = "The Media Items owner has been updated."
        redirect action: 'show', id: mediaItem.id
    }

    def checkUrlContentType(){
        if(!params.sourceUrl){
            render "false"
            return
        }
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        def imageFormat = params.imageFormat?.toLowerCase()
        switch(params.initController){
            case "image":
            case "infographic":
                if(params.sourceUrl.contains("jpeg") || params.sourceUrl.contains("jpg") || params.sourceUrl.contains("png")){
                    if(params.sourceUrl.contains("png") && imageFormat == "png"){
                        render true
                        return
                    }
                    if((params.sourceUrl.contains("jpg") || params.sourceUrl.contains("jpeg")) && imageFormat == "jpg"){
                        render true
                        return
                    }
                }
                    render "false"
                    return
            case "PDF":
                if(params.sourceUrl.toLowerCase().contains("pdf")){
                    if(params.sourceUrl.toLowerCase().endsWith("pdf")){
                        render "true"
                        return
                    }
                }
        }
        render "false"
    }

    @Secured(['ROLE_ADMIN'])
    def resetHash(Long id){
        mediaItemsService.resetHash(id)
        redirect action: "show", id:id
    }
}
