/*
Copyright (c) 2014,Â Health and Human Services - Web Communications (ASPA)â€¨All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.Language
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class LanguageController {
    static allowedMethods = [index: "GET", setActive: "GET", setInactive: "GET"]
    def languageService

    def index() {
        def activeTagLanguageList = Language.findAllByIsActive(true)
        respond Language.list(sort:"name", order:"asc"), model: [activeTagLanguageInstanceList: activeTagLanguageList]
    }

    @Transactional
    def setActive(Long languageId) {
        if (languageId == null) {
            notFound()
            return
        }

        Language languageInstance = Language.get(languageId)
        if (!languageInstance) {
            notFound()
            return
        }
        languageInstance.isActive = true
        languageInstance.save flush: true

        flash.message = "The Language: ${languageInstance} has been activated."
        redirect action: "index"
    }

    @Transactional
    def setInactive(Long languageId) {
        if (languageId == null) {
            notFound()
            return
        }

        Language languageInstance = Language.get(languageId)
        if (!languageInstance) {
            notFound()
            return
        }
        def mediaExists = languageService.mediaItemExists(languageInstance)
        if (mediaExists) { //this is currently an error (language is in use)
            languageInUse()
            return
        }
        languageInstance.isActive = false
        languageInstance.save flush:true

        flash.message = "The Language: ${languageInstance} has been deactivated."
        redirect action: "index"
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'language.label', default: 'Language'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    protected void languageInUse() {
        request.withFormat {
            form multipartForm {
                flash.message = "Sorry! Languages currently used by media items cannot be disabled."
                redirect action: "index", method: "GET"
            }
        }
    }
}
