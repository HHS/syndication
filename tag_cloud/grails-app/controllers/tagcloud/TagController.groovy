/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package tagcloud

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND
import grails.plugin.springsecurity.annotation.Secured
import tagcloud.domain.Tag
import grails.transaction.Transactional

/* This controller is for non-rest calls */

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class TagController {
    def tagService
    def solrIndexingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Tag.list(params), model: [tagInstanceCount: Tag.count()]

    }

    def show(Tag tagInstance) {
        respond tagInstance
    }

    def create() {
        respond new Tag(params)
    }

    @Transactional
    def save(Tag tagInstance) {
        if (tagInstance == null) {
            notFound()
            return
        }

        if (tagInstance.hasErrors()) {
            respond tagInstance.errors, view: 'create'
            return
        }

        tagInstance.save flush: true
        solrIndexingService.inputTag("${tagInstance.id}", tagInstance.name)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tagInstance.label', default: 'Tag'), tagInstance.id])
                redirect tagInstance
            }
            '*' { respond tagInstance, [status: CREATED] }
        }
    }

    def edit(Tag tagInstance) {
        respond tagInstance
    }

    @Transactional
    def update(Long id) {
        Tag tagInstance = Tag.get(id)
        if (tagInstance) {
            tagInstance?.properties = new Tag(params).properties
        } else {
            notFound()
            return
        }

        tagInstance.validate()

        if (tagInstance.hasErrors()) {
            respond tagInstance.errors, view: 'edit'
            return
        }

        tagInstance.save flush: true
        solrIndexingService.inputTag(String.valueOf(tagInstance.id),tagInstance.name)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Tag.label', default: 'Tag'), tagInstance.id])
                redirect tagInstance
            }
            '*' { respond tagInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Tag tagInstance) {
        if (tagInstance == null) {
            notFound()
            return
        }

        tagInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Tag.label', default: 'Tag'), tagInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def tools() {}

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagInstance.label', default: 'Tag'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
