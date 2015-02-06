/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Audio
import com.ctacorp.syndication.Html
import com.ctacorp.syndication.Collection
import com.ctacorp.syndication.Image
import com.ctacorp.syndication.Infographic
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.Periodical
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Video
import com.ctacorp.syndication.Widget
import static org.springframework.http.HttpStatus.*
import com.ctacorp.syndication.AlternateImage
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class AlternateImageController {

    def alternativeImageService
    def tagService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AlternateImage.list(params), model:[alternateImageInstanceCount: AlternateImage.count()]
    }

    def show(AlternateImage alternateImageInstance) {
        if(!alternativeImageService.ifPublisherValid(alternateImageInstance)){
            response.sendError(404)
            return
        }

        respond alternateImageInstance
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def create() {
        respond new AlternateImage(params)
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    @Transactional
    def save(AlternateImage alternateImageInstance) {
        if (alternateImageInstance == null) {
            notFound()
            return
        }

        if (alternateImageInstance.hasErrors()) {
            flash.errors = alternateImageInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            redirect action:'create', params:params
            return
        }

        alternateImageInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'alternateImageInstance.label', default: 'Alternate Image'), alternateImageInstance.id])
                redirect alternateImageInstance
            }
            '*' { respond alternateImageInstance, [status: CREATED] }
        }
    }

    def edit(AlternateImage alternateImageInstance) {
        if(!alternativeImageService.ifPublisherValid(alternateImageInstance)){
            response.sendError(404)
            return
        }

        respond alternateImageInstance, model:[user:springSecurityService.currentUser]
    }

    @Transactional
    def update(AlternateImage alternateImageInstance) {
        if(!alternativeImageService.ifPublisherValid(alternateImageInstance)){
            response.sendError(404)
            return
        }

        if (alternateImageInstance == null) {
            notFound()
            return
        }

        if (alternateImageInstance.hasErrors()) {
            flash.errors = alternateImageInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            redirect action:'edit', id:alternateImageInstance.id, params:[mediaId:params.mediaId]
            return
        }

        alternateImageInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AlternateImage.label', default: 'Alternate Image'), alternateImageInstance.id])
                redirect action:'show', id:alternateImageInstance.id, params:[mediaId:params.mediaId]
            }
            '*'{ respond alternateImageInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AlternateImage alternateImageInstance) {
        if (alternateImageInstance == null) {
            notFound()
            return
        }

        if(!alternativeImageService.ifPublisherValid(alternateImageInstance)){
            response.sendError(404)
            return
        }

        alternateImageInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AlternateImage.label', default: 'Alternate Image'), alternateImageInstance.id])
                if(params.mediaId){
                    redirect controller:'mediaItem', action:'show', id:params.long('mediaId')
                } else{
                    redirect action: "index", method:"GET"
                }
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'alternateImageInstance.label', default: 'Alternate Image'), params.id])
                redirect controller: 'dashboard', action: "syndDash", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def addAlternateImage(AlternateImage alternateImageInstance){
        if(!alternativeImageService.ifPublisherValid(alternateImageInstance)){
            response.sendError(404)
            return
        }

        MediaItem mi = MediaItem.findById(params.mediaItem)
        String message = alternativeImageService.addAlternativeImage(alternateImageInstance)
        flash.error = message
        if (!message) {
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
