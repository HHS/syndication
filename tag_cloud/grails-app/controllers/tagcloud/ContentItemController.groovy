/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package tagcloud

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import tagcloud.domain.ContentItem

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured (['ROLE_ADMIN'])
class ContentItemController {
    def ContentItemService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ContentItem.list(params), model:[contentItemInstanceCount: ContentItem.count()]
    }

    def show(ContentItem contentItemInstance) {
        respond contentItemInstance
    }

    def create() {
        respond new ContentItem(params)
    }

    @Transactional
    def save(ContentItem contentItemInstance) {
        if (contentItemInstance == null) {
            notFound()
            return
        }

        if (contentItemInstance.hasErrors()) {
            respond contentItemInstance.errors, view:'create'
            return
        }

        contentItemInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'contentItemInstance.label', default: 'ContentItem'), contentItemInstance.id])
                redirect contentItemInstance
            }
            '*' { respond contentItemInstance, [status: CREATED] }
        }
    }

    def edit(ContentItem contentItemInstance) {
        respond contentItemInstance
    }

    @Transactional
    def update(ContentItem contentItemInstance) {
        if (contentItemInstance == null) {
            notFound()
            return
        }

        if (contentItemInstance.hasErrors()) {
            respond contentItemInstance.errors, view:'edit'
            return
        }

        contentItemInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ContentItem.label', default: 'ContentItem'), contentItemInstance.id])
                redirect contentItemInstance
            }
            '*'{ respond contentItemInstance, [status: OK] }
        }
    }

    @NotTransactional
    def delete(ContentItem contentItemInstance) {

        if (contentItemInstance == null) {
            notFound()
            return
        }

        contentItemService.delete(contentItemInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ContentItem.label', default: 'ContentItem'), contentItemInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'contentItemInstance.label', default: 'ContentItem'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
