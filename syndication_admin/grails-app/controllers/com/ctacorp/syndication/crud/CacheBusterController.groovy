package com.ctacorp.syndication.crud

import com.ctacorp.syndication.cache.CacheBuster
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class CacheBusterController {

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CacheBuster.list(params), model:[cacheBusterInstanceCount:CacheBuster.count()]
    }

    def create() {

        respond new CacheBuster()
    }

    def save(CacheBuster cacheBusterInstance) {
        if (cacheBusterInstance == null) {
            notFound()
            return
        }

        cacheBusterInstance.validate()
        if(cacheBusterInstance.hasErrors()){
            flash.errors = cacheBusterInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            render view: "create", model:[cacheBusterInstance:cacheBusterInstance]
            return
        }

        cacheBusterInstance.save()

        render view:"show", model:[cacheBusterInstance:cacheBusterInstance]
    }

    def show(CacheBuster cacheBusterInstance) {
        render view:"show", model:[cacheBusterInstance:cacheBusterInstance]
    }

    def edit(CacheBuster cacheBusterInstance) {
        render view:"edit", model:[cacheBusterInstance:cacheBusterInstance]
    }

    def update(CacheBuster cacheBusterInstance) {
        if (cacheBusterInstance == null) {
            notFound()
            return
        }

        cacheBusterInstance.validate()
        if(cacheBusterInstance.hasErrors()){
            flash.errors = cacheBusterInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            render view: "edit", model:[cacheBusterInstance:cacheBusterInstance]
            return
        }

        cacheBusterInstance.save()

        render view:"show", model:[cacheBusterInstance:cacheBusterInstance]
    }

    def delete(CacheBuster cacheBusterInstance) {

        if (cacheBusterInstance == null) {
            notFound()
            return
        }

        cacheBusterInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'cacheBuster.label', default: 'Cache Buster'), [cacheBusterInstance.domainName]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm{
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cacheBusterInstance.label', default: 'CacheBuster'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
