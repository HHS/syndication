
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import grails.plugin.springsecurity.annotation.Secured

import com.ctacorp.syndication.Audio
import com.ctacorp.syndication.Collection
import com.ctacorp.syndication.Html
import com.ctacorp.syndication.Image
import com.ctacorp.syndication.Infographic
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.Periodical
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Video
import com.ctacorp.syndication.Widget

import static org.springframework.http.HttpStatus.*

import com.ctacorp.syndication.ExtendedAttribute
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class ExtendedAttributeController {

    def extendedAttributeService
    def tagService
    def mediaItemsService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ExtendedAttribute.list(params), model: [extendedAttributeInstanceCount: ExtendedAttribute.count()]
    }

    def show(ExtendedAttribute extendedAttributeInstance) {
        if(!extendedAttributeService.ifPublisherValid(extendedAttributeInstance)){
            response.sendError(404)
            return
        }
        respond extendedAttributeInstance
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def create() {
        respond new ExtendedAttribute(params)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    @Transactional
    def save(ExtendedAttribute extendedAttributeInstance) {
        if (extendedAttributeInstance == null) {
            notFound()
            return
        }

        if (extendedAttributeInstance.hasErrors()) {
            flash.errors = extendedAttributeInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            redirect action: 'create', params:params
            return
        }

        def isDuplicateItem = extendedAttributeService.getUpdateInformation(extendedAttributeInstance)
        if (!isDuplicateItem) {
            extendedAttributeInstance.save flush: true
        } else {
            this.update(isDuplicateItem)
            return
        }
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'extendedAttributeInstance.label', default: 'Extended Attribute'), [extendedAttributeInstance.name]])
                redirect extendedAttributeInstance
            }
            '*' { respond extendedAttributeInstance, [status: CREATED] }
        }
    }

    def edit(ExtendedAttribute extendedAttributeInstance) {
        if(!extendedAttributeService.ifPublisherValid(extendedAttributeInstance)){
            response.sendError(404)
            return
        }

        respond extendedAttributeInstance, model:[user:springSecurityService.currentUser]
    }

    @Transactional
    def update(ExtendedAttribute extendedAttributeInstance) {
        if(!extendedAttributeService.ifPublisherValid(extendedAttributeInstance)){
            response.sendError(404)
            return
        }
        
        if (extendedAttributeInstance == null) {
            notFound()
            return
        }
        
        if (extendedAttributeInstance.hasErrors()) {
            flash.errors = extendedAttributeInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            redirect action: 'edit', id:extendedAttributeInstance.id, params:[mediaId:params.mediaId]
            return
        }
        
        extendedAttributeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ExtendedAttribute.label', default: 'Extended Attribute'), [extendedAttributeInstance.name]])
                redirect action:'show', id:extendedAttributeInstance.id, params:[mediaId:params.mediaId]
            }
            '*' { respond extendedAttributeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(ExtendedAttribute extendedAttributeInstance) {
        if(!extendedAttributeService.ifPublisherValid(extendedAttributeInstance)){
            response.sendError(404)
            return
        }

        if (extendedAttributeInstance == null) {
            notFound()
            return
        }

        extendedAttributeInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ExtendedAttribute.label', default: 'Extended Attribute'), [extendedAttributeInstance.name]])
                if(params.mediaId){
                    redirect controller:'mediaItem', action:'show', id:params.long('mediaId')
                } else{
                    redirect action: "index", method: "GET"
                }

            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'extendedAttributeInstance.label', default: 'Extended Attribute'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    @Transactional
    def addAttribute(ExtendedAttribute attributeInstance){
        if(!extendedAttributeService.ifPublisherValid(attributeInstance)){
            response.sendError(404)
            return
        }

        MediaItem mi = MediaItem.findById(params.mediaItem)
        attributeInstance.mediaItem = mi
        String message = mediaItemsService.addExtendedAttribute(mi, attributeInstance)

        flash.error = message
        if(!message){
            flash.message = "Attribute added"
        }

        switch (mi) {
            case Audio: redirect controller: "audio", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Collection: redirect controller: "collection", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Html: redirect controller: "html", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Image: redirect controller: "image", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Infographic: redirect controller: "infographic", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Periodical: redirect controller: "periodical", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case SocialMedia: redirect controller: "socialMedia", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Video: redirect controller: "video", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
            case Widget: redirect controller: "widget", action: "edit", id: mi.id, params: [languageId: params.languageId, tagTypeId: params.tagTypeId]; break
        }
    }
}
