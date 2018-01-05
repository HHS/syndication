
/*
Copyright (c) 2014,Â Health and Human Services - Web Communications (ASPA)
â€¨All rights reserved.

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
import tagcloud.domain.TagType
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured (['ROLE_ADMIN'])
class TagTypeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TagType.list(params), model: [tagTypeInstanceCount: TagType.count()]
    }

    def show(TagType tagTypeInstance) {
        respond tagTypeInstance
    }

    def create() {
        respond new TagType(params)
    }

    @Transactional
    def save(TagType tagTypeInstance) {
        if (tagTypeInstance == null) {
            notFound()
            return
        }

        if (tagTypeInstance.hasErrors()) {
            respond tagTypeInstance.errors, view: 'create'
            return
        }

        tagTypeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tagTypeInstance.label', default: 'TagType'), tagTypeInstance.id])
                redirect tagTypeInstance
            }
            '*' { respond tagTypeInstance, [status: CREATED] }
        }
    }

    def edit(TagType tagTypeInstance) {
        respond tagTypeInstance
    }

    @Transactional
    def update(TagType tagTypeInstance) {
        if (tagTypeInstance == null) {
            notFound()
            return
        }

        if (tagTypeInstance.hasErrors()) {
            respond tagTypeInstance.errors, view: 'edit'
            return
        }

        tagTypeInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'TagType.label', default: 'Tag Type'), tagTypeInstance.id])
                redirect tagTypeInstance
            }
            '*' { respond tagTypeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(TagType tagTypeInstance) {

        if (tagTypeInstance == null) {
            notFound()
            return
        }

        tagTypeInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'TagType.label', default: 'Tag Type'), tagTypeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagTypeInstance.label', default: 'Tag Type'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
