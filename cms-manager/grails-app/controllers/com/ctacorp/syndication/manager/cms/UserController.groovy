
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
class UserController {

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ["html"]

    def loggingService
    def springSecurityService
    def userSubscriberService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model: [instanceCount: User.count()], view: 'index'
    }

    def show(User user) {

        if (!user) {
            return notFound()
        }

        boolean allowDelete = user.id != springSecurityService.currentUser.id

        respond user, model: [allowDelete: allowDelete], view: 'show'
    }

    def create() {
        respond new User(params), view: 'create'
    }

    def edit(User user) {
        respond user, view: 'edit'
    }

    @Transactional
    def update(User user) {

        flash.clear()

        if (!user) {
            return notFound()
        }

        user.validate()

        if (user.hasErrors()) {
            respond user.errors, view: 'edit'
        } else {

            user.save(flush:true)

            def subscribers = {
                Subscriber.findAllByIdInList(params.subscribers as List)
            }()

            userSubscriberService.disassociateUserFromSubscribers(user)
            userSubscriberService.associateUserWithSubscribers(subscribers, user)

            def role = Role.findById(params.role as Long)
            if(role) {
                UserRole.findByUser(user).delete()
                UserRole.create(user, role, true)
            }

            flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label'), user.username])
            redirect user
        }
    }

    @Transactional
    def delete(User user) {

        flash.clear()

        if (!user) {
            return notFound()
        }

        def username = user.username

        if(User.findById(user.id)) {

            UserRole.findByUser(user)?.delete()
            userSubscriberService.disassociateUserFromSubscribers(user)

            user.delete(flush:true)
        }

        flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label'), username])
        redirect(action: "index", method: "GET")
    }

    @Transactional
    def save(User user) {

        flash.clear()

        if(User.findByUsername(user.username)) {
            flash.errors = [message(code: 'default.exists.message', args: [message(code: 'user.label'), user.username])]
            redirect(action: "index", method: "GET")
        } else {

            user.validate()

            if(user.hasErrors()) {
                respond user.errors, view: 'create'
            } else {

                user.save(flush:true)

                def subscribers = {
                    Subscriber.findAllByIdInList(params.subscribers as List)
                }()

                userSubscriberService.associateUserWithSubscribers(subscribers, user)

                def role = Role.findById(params.role as Long)
                if(role) {
                    UserRole.create(user, role, true)
                }

                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label'), user.username])
                redirect(action: "show", id: user.id)
            }
        }
    }

    def notFound() {
        flash.errors = [message(code: 'default.not.found.message', args: [message(code: 'user.label'), params.id])]
        redirect(action: "index", method: "GET")
    }
}
