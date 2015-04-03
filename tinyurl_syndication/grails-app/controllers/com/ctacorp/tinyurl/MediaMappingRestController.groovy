package com.ctacorp.tinyurl

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

@Secured(['permitAll'])
class MediaMappingRestController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static defaultAction = "index"

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond MediaMapping.list(params), model:[mediaMappingInstanceCount: MediaMapping.count()]
    }

    def show(MediaMapping mediaMappingInstance) {
        respond mediaMappingInstance
    }

    @Transactional
    def save(MediaMapping mediaMappingInstance) {
        println "Mapping save for ${params}"
        if (mediaMappingInstance == null) {
            notFound()
            return
        }

        if(mediaMappingInstance.guid) {
            def existing = MediaMapping.findByGuid(mediaMappingInstance.guid)
            if(existing){
                respond existing, [status: CREATED]
                return
            }
        }

        if (mediaMappingInstance.hasErrors()) {
            log.error mediaMappingInstance.errors
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
