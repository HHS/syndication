/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.authentication.UserRole
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
class MediaItemController {

    def tagService
    def mediaItemsService
    def springSecurityService
    def cmsManagerKeyService
    def elasticsearchService

    RestBuilder rest = new RestBuilder()

    def publisherItems = {MediaItemSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.mediaItem?.id}

    def show(Long id) {
        MediaItem mi = MediaItem.read(id)
        flash.error = flash.error
        flash.message = flash.message
        redirect controller: "${mi.getClass().simpleName}", action:"show", id:id, model:["${getInstanceName(mi.getClass().simpleName)}":mi], params:params
    }

    private String getInstanceName(String input){
        //special case for acronyms like PDF, FAQ, etc...
        def chars = input.chars
        if(chars[0].isUpperCase() && chars[1].isUpperCase()){
            return input
        }
        return input[0].toLowerCase() + input.substring(1)
    }

    @Secured(['ROLE_ADMIN'])
    def deleteAll(){
        def checks = params.findAll{
            it.key.startsWith("deleteChecked")
        }

        def deleted = []

        checks.each{
            if(it.value == "on"){
                def id = it.key.split("_")[1] as Long
                deleted << id
                mediaItemsService.delete(id)
            }
        }

        flash.message = "Deleted items with IDs: ${deleted}"

        if(params.whereToController) {
            if (params.whereToAction) {
                redirect controller: params.whereToController, action: params.whereToAction
                return
            } else {
                redirect controller: params.whereToController, action: 'index'
                return
            }
        }

        redirect controller:"dashboard", action: "syndDash"
    }

    def edit(Long id) {
        MediaItem mi = MediaItem.get(id)
        flash.error = flash.error
        flash.message = flash.message
        redirect controller: "${mi.getClass().simpleName}", action:"edit", id:id
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

            def search = MediaItem.withCriteria(params, {

                and {

                    or {
                        eq 'id', q.toLong()
                        like 'name', "%${q}%"
                    }

                    eq 'active', params.active?.toBoolean() ?: true
                    eq 'visibleInStorefront', params.visibleInStorefront?.toBoolean() ?: true
                }
            })

            render search.collect{ [id:it.id, name:"$it.name"] } as JSON

        } else {
            render MediaItem.facetedSearch([nameContains: "${q}", active:params.active, visibleInStorefront:params.visibleInStorefront]).list([max:20]).collect{ [id:it.id, name:"$it.name"] } as JSON
        }
    }

    def search() {

        if((params.title || params.fullText) && !params.id) {
            elasticsearch()
        } else {
            originalSearch()
        }
    }

    def elasticsearch() {

        def results = [total: 0, ids: []]
        def mediaItems = []
        def accessibleIds = []

        params.max = params.max ?: 15
        params.offset = params.offset ?: 0

        def isPublisher = UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"

        if(isPublisher){
            accessibleIds = publisherItems()
        } else {

            if(params.subscriberId) {
                accessibleIds = MediaItemSubscriber.findAllBySubscriberId(params.int("subscriberId") ?: 0).collect { it.id }.toList()
            }
        }

        def esParams = params.clone() as Map
        esParams.visibleInStorefront = false
        esParams.max = 10000
        esParams.offset = 0

        if(params.sort) {

            esParams.remove('sort')
            esParams.remove('order')

            results = elasticsearchService.searchForIds(esParams, accessibleIds)

            if(results.ids) {

                results.total = MediaItem.countByIdInList(results.ids)
                mediaItems = MediaItem.findAllByIdInList(results.ids, params)
            }

        } else {

            results = elasticsearchService.searchForIds(esParams, accessibleIds)

            if(results.ids) {

                results.total = MediaItem.countByIdInList(results.ids)

                def idsForQuery = results.ids.join(',')
                def foundIds = MediaItem.executeQuery("select m.id from MediaItem m where m.id in (${idsForQuery}) order by field (m.id,${idsForQuery})", [:], params)
                mediaItems = MediaItem.getAll(foundIds)
            }
        }

        render view:"search", model:[
                fullText:params.fullText,
                mediaItemInstanceList:mediaItems,
                mediaItems:"something",
                mediaItemInstanceCount: results.total,
                title:params.title,
                id:params.id,
                url:params.url,
                languageList:Language.findAllByIsActive(true),
                language:params.language,
                mediaTypeList:mediaItemsService.getMediaTypes(),
                mediaType:params.mediaType,
                subscriberList:cmsManagerKeyService.listSubscribers(),
                subscriberId:params.subscriberId,
                sourceList:Source.list(),
                sourceId:params.sourceId,
                createdBy:params.createdBy,
                max: params.max
        ]
    }

    def originalSearch(){
        params.max = params.max ?: 15
        def mediaItems = null
        def subscriberList = null
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            params.inList = publisherItems().join(",")
            if(params.inList){
                mediaItems = mediaItemsService.findMediaByAll(params)
            }

        } else {
            if(params.subscriberId){
                params.inList = MediaItemSubscriber?.findAllBySubscriberId(params.int("subscriberId") ?: 0)?.mediaItem?.id?.toString()?.replace("[","")?.replace("]","") ?: "-1"
            }
            mediaItems = mediaItemsService.findMediaByAll(params)
            subscriberList = cmsManagerKeyService.listSubscribers()
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
                                     mediaType:params.mediaType,
                                     subscriberList:subscriberList,
                                     subscriberId:params.subscriberId,
                                     sourceList:Source.list(),
                                     sourceId:params.sourceId,
                                     createdBy:params.createdBy,
                                     max: params.max
        ]
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
        flash.message = "Hash cleared for item ${id}"
        redirect action: "show", id:id
    }

    @Secured(['ROLE_ADMIN'])
    def resetDBCache(Long id){
        mediaItemsService.resetDBCache(id)
        flash.message = "Database cache reset for item ${id}"
        redirect action: "show", id:id
    }
}
