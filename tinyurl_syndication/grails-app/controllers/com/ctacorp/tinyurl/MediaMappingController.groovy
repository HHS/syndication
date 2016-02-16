package com.ctacorp.tinyurl

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class MediaMappingController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MediaMapping.list(params), model: [mediaMappingInstanceCount: MediaMapping.count()]
    }

    def show(MediaMapping mediaMappingInstance) {
        respond mediaMappingInstance
    }

    def create() {
        respond new MediaMapping(params)
    }

//    Delete massive amounts of duplicate tinyUrls if they exist
    @Transactional
    def prune() {
        def allItems = MediaMapping.list(sort: "id", order: "DESC")*.id
        println "starting Prune"
        def alreadyDeleted = []
        allItems.each { mi ->
            if(!(mi in alreadyDeleted)) {
                def flaggedForDeletion = []
                MediaMapping mm = MediaMapping.read(mi)
                if (mm) {
                    println mm.id
//                    render "<p>${mm.targetUrl}</p>"
                    def dupes = MediaMapping.findAllBySyndicationIdAndTargetUrl(mm.syndicationId, mm.targetUrl, [sort: "id", order: "DESC"])
                    if (dupes.size() > 1) {
//                    render "<ul>"
//                    dupes.each { dupe ->
//                        render "<li>${dupe.id} - ${dupe.targetUrl}</li>"
//                    }
                        flaggedForDeletion.addAll(dupes[0..-2]*.id)
//                    render "</ul>"
                    }
                    if (flaggedForDeletion) {
                        def beforeCount = MediaMapping.count()
                        MediaMapping.where {
                            id in flaggedForDeletion
                        }?.deleteAll()
                        alreadyDeleted.addAll(dupes*.id)
                        println "deleted ${flaggedForDeletion.size()} -- ${beforeCount} to ${MediaMapping.count()}"
                    }
                }
            }
        }
        println "finished prune"
        render "done"
    }

    @Transactional
    def save(MediaMapping mediaMappingInstance) {
        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        if (mediaMappingInstance.hasErrors()) {
            respond mediaMappingInstance.errors, view: 'create'
            return
        }

        mediaMappingInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'mediaMappingInstance.label', default: 'MediaMapping'), mediaMappingInstance.id])
                redirect mediaMappingInstance
            }
            '*' { respond mediaMappingInstance, [status: CREATED] }
        }
    }

    def edit(MediaMapping mediaMappingInstance) {
        respond mediaMappingInstance
    }

    @Transactional
    def update(MediaMapping mediaMappingInstance) {
        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        if (mediaMappingInstance.hasErrors()) {
            respond mediaMappingInstance.errors, view: 'edit'
            return
        }

        mediaMappingInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'MediaMapping.label', default: 'MediaMapping'), mediaMappingInstance.id])
                redirect mediaMappingInstance
            }
            '*' { respond mediaMappingInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(MediaMapping mediaMappingInstance) {

        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        mediaMappingInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'MediaMapping.label', default: 'MediaMapping'), mediaMappingInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'mediaMappingInstance.label', default: 'MediaMapping'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
