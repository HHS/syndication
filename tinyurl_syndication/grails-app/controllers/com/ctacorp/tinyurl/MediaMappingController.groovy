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
        respond MediaMapping.list(params), model:[mediaMappingInstanceCount: MediaMapping.count()]
    }

    def show(MediaMapping mediaMappingInstance) {
        respond mediaMappingInstance
    }

    def create() {
        respond new MediaMapping(params)
    }

    //just for testing
//    @Transactional
//    def fill(){
//        1000.times{
//            new MediaMapping(targetUrl:"http://www.apple.com").save()
//        }
//        redirect action:"index"
//    }

    @Transactional
    def save(MediaMapping mediaMappingInstance) {
        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        if (mediaMappingInstance.hasErrors()) {
            respond mediaMappingInstance.errors, view:'create'
            return
        }

        mediaMappingInstance.save flush:true

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
            respond mediaMappingInstance.errors, view:'edit'
            return
        }

        mediaMappingInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'MediaMapping.label', default: 'MediaMapping'), mediaMappingInstance.id])
                redirect mediaMappingInstance
            }
            '*'{ respond mediaMappingInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(MediaMapping mediaMappingInstance) {

        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        mediaMappingInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'MediaMapping.label', default: 'MediaMapping'), mediaMappingInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'mediaMappingInstance.label', default: 'MediaMapping'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
