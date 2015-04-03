

/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.authentication

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER", "ROLE_BASIC", "ROLE_STATS", "ROLE_PUBLISHER"])
class UserController {
    def userService
    def springSecurityService
    def cmsManagerKeyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        params.sort = params.sort ?: "user.id"
        return userService.indexResponse(params)
    }

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    def show(User userInstance) {
        boolean allowDelete = userInstance.id != springSecurityService.currentUser.id

        respond userInstance, model: [allowDelete: allowDelete]
    }
    
    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    def create() {
        def roles = Role.list()
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER"){
            roles = Role.findAllByAuthorityInList(userService.getManagersAuthorityRoles())
        }
        
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new User(params), model: [subscribers:subscribers, roles:roles, currentRoleId: params?.authority]
    }

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    @Transactional
    def save(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }
        
        if (userInstance.hasErrors() || (Role.get(params.authority).authority == "ROLE_PUBLISHER" && !params.subscriberId)) {
            flash.errors = []
            flash.errors = userInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            if(Role.get(params.authority).authority == "ROLE_PUBLISHER" && !params.subscriberId){flash.errors << [message:"Select a Key"]}
            redirect action: 'create', params:params, model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        if(params.passwordVerify != params.password){
            flash.error = "Both passwords mush match!"
            redirect action: 'create', params:params, model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        userService.saveUserAndRole(userInstance, params.long('authority'))

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userInstance.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: CREATED] }
        }
    }

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    def edit(User userInstance) {
        def roles = Role.list()
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER"){
            roles = Role.findAllByAuthorityInList(userService.getManagersAuthorityRoles())
        }
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond userInstance, model: [subscribers:subscribers, currentSubscriber:subscribers.find{it.id as Long == userInstance?.subscriberId}?.id, roles: roles, currentRoleId:userInstance?.authorities?.getAt(0)?.id, fake:userInstance?.username]
    }

    def editMyAccount(){
        render view: "editMyAccount", model:[userInstance: springSecurityService.currentUser]
    }

    @Transactional
    def updateMyAccount(User userInstance){
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            flash.errors = userInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            respond userInstance.errors, view: 'editMyAccount'
            return
        }
        if(params.passwordVerify != params.password){
            flash.error = "Both passwords mush match!"
            userInstance.discard()
            respond userInstance.errors, view: 'editMyAccount'
            return
        }

        userInstance.save(flush:true)

        flash.message = "Your account has been successfully updated!"
        redirect controller:"dashboard", action: "syndDash"
    }

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    @Transactional
    def update(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors() || (Role.get(params.authority).authority == "ROLE_PUBLISHER" && !params.subscriberId)) {
            flash.errors = []
            flash.errors = userInstance.errors.allErrors.collect{[message:g.message([error : it])]}
            if(Role.get(params.authority).authority == "ROLE_PUBLISHER" && !params.subscriberId){flash.errors << [message:"Select a Key"]}
            userInstance.discard()
            redirect action:'edit', id:userInstance.id, model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        if(params.passwordVerify != params.password){
            flash.error = "Both passwords mush match!"
            respond userInstance.errors, view: 'edit', model: [subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        userService.saveUserAndRole(userInstance, params.long('authority'))

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    @Secured(["ROLE_ADMIN", "ROLE_MANAGER"])
    @Transactional
    def delete(User userInstance) {

        if (userInstance == null) {
            notFound()
            return
        }

        userService.deleteUser(userInstance)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userInstance.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
