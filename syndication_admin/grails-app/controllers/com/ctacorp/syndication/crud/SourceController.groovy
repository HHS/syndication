
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.MediaItem

import com.ctacorp.syndication.Source
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional(readOnly = true)
class SourceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def solrIndexingService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Source.list(params), model: [sourceInstanceCount: Source.count()]
    }

    def show(Source sourceInstance) {
        respond sourceInstance
    }

    def create() {
        respond new Source(params)
    }

    @Transactional
    def save(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        if (sourceInstance.hasErrors()) {
            flash.errors = sourceInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond sourceInstance.errors, view: 'create'
            return
        }

        sourceInstance.save flush: true
        solrIndexingService.inputSource(sourceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sourceInstance.label', default: 'Source'), [sourceInstance.name]])
                redirect sourceInstance
            }
            '*' { respond sourceInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def edit(Source sourceInstance) {
        respond sourceInstance
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    @Transactional
    def update(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        if (sourceInstance.hasErrors()) {
            flash.errors = sourceInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond sourceInstance.errors, view: 'edit'
            return
        }

        sourceInstance.save flush: true
        solrIndexingService.inputSource(sourceInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Source.label', default: 'Source'), [sourceInstance.name]])
                redirect sourceInstance
            }
            '*' { respond sourceInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    @Transactional
    def delete(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        if (MediaItem.findBySource(sourceInstance) != null){
            flash.error = "You may not delete this Source, it still has Media Items that reference it."
            redirect action:"show", id:sourceInstance.id
            return
        }

        solrIndexingService.removeSource(sourceInstance)
        sourceInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Source.label', default: 'Source'), [sourceInstance.name]])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'sourceInstance.label', default: 'Source'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
