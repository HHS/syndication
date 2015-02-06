
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.social

import static org.springframework.http.HttpStatus.*

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class SocialMediaAccountController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SocialMediaAccount.list(params), model:[socialMediaAccountInstanceCount: SocialMediaAccount.count()]
    }

    def show(SocialMediaAccount socialMediaAccountInstance) {
        respond socialMediaAccountInstance
    }

    def create() {
        respond new SocialMediaAccount(params)
    }

    @Transactional
    def save(SocialMediaAccount socialMediaAccountInstance) {
        if (socialMediaAccountInstance == null) {
            notFound()
            return
        }

        if (socialMediaAccountInstance.hasErrors()) {
            respond socialMediaAccountInstance.errors, view:'create'
            return
        }

        socialMediaAccountInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'socialMediaAccountInstance.label', default: 'SocialMediaAccount'), socialMediaAccountInstance.id])
                redirect socialMediaAccountInstance
            }
            '*' { respond socialMediaAccountInstance, [status: CREATED] }
        }
    }

    def edit(SocialMediaAccount socialMediaAccountInstance) {
        respond socialMediaAccountInstance
    }

    @Transactional
    def update(SocialMediaAccount socialMediaAccountInstance) {
        if (socialMediaAccountInstance == null) {
            notFound()
            return
        }

        if (socialMediaAccountInstance.hasErrors()) {
            respond socialMediaAccountInstance.errors, view:'edit'
            return
        }

        socialMediaAccountInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'SocialMediaAccount.label', default: 'SocialMediaAccount'), socialMediaAccountInstance.id])
                redirect socialMediaAccountInstance
            }
            '*'{ respond socialMediaAccountInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(SocialMediaAccount socialMediaAccountInstance) {

        if (socialMediaAccountInstance == null) {
            notFound()
            return
        }

        socialMediaAccountInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'SocialMediaAccount.label', default: 'SocialMediaAccount'), socialMediaAccountInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'socialMediaAccountInstance.label', default: 'SocialMediaAccount'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
