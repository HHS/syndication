
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class RoleController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def loggingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        respond Role.list(params), model: [instanceCount: Role.count()], view: 'index'
    }

    def show(Role role) {

        if (!role) {
            return notFound()
        }
        respond role, view: 'show'
    }

    def create() {
        respond new Role(params), view: 'create'
    }

    def edit(Role role) {
        respond role, view: 'edit'
    }

    @Transactional
    def update(Role role) {

        flash.clear()

        if (!role) {
            return notFound()
        }

        role.validate()

        if (role.hasErrors()) {
            respond role.errors, view: 'edit'
        } else {

            role.save(flush: true)

            flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label'), role.authority])
            redirect role
        }
    }

    @Transactional
    def delete(Role role) {

        flash.clear()

        if (!role) {
            return notFound()
        }

        def authority = role.authority

        if(Role.findByAuthority(authority)) {

            UserRole.findAllByRole(role).each {userRole ->
                userRole.delete(flush:true)
            }

            role.delete(flush:true)
        }

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label'), authority])
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def save() {

        flash.clear()

        def authority = params.authority as String

        if(Role.findByAuthority(authority)) {

            flash.message = message(code: 'default.exists.message', args: [message(code: 'role.label'), authority])
            redirect(action: "index", method: "GET")

        } else {

            def role = new Role(params)
            role.validate()

            if(role.hasErrors()) {
                respond role.errors, view: 'create'
            } else {

                role.save(flush:errors)
                flash.message = message(code: 'default.created.message', args: [message(code: 'role.label'), role.authority])
                redirect(action: "show", id: role.id)
            }
        }
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'role.label'), params.id])]
        redirect(action: "index", method: "GET")
    }
}
