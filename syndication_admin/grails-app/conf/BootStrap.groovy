
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import com.ctacorp.syndication.MediaItemChangeListener
import com.ctacorp.syndication.authentication.*
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import grails.util.Environment

class BootStrap {
    def grailsApplication
    def queueService

    def init = { servletContext ->
        log.info ("Admin is running in --> ${Environment.current} <-- mode.")
        initUsers()

        MediaItemChangeListener.initialize(grailsApplication, queueService)

        warmUpMq()

        String systemRunningMessage = """
==========================================
| -> Syndication Admin Ready.            |
=========================================="""
        log.info systemRunningMessage
    }

    def destroy = {
    }

    private warmUpMq(){
        //First time a message is sent to MQ, it bombs. No idea why. Warming it up will prevent a failure later!
        queueService.sendMessage(new Message(messageType: MessageType.UPDATE))
    }

    @SuppressWarnings("GroovyUnusedAssignment")
    private void initUsers() {
        def adminRole      = Role.findOrSaveByAuthority('ROLE_ADMIN')
        def managerRole    = Role.findOrSaveByAuthority('ROLE_MANAGER')
        def statsRole      = Role.findOrSaveByAuthority('ROLE_STATS')
        def basicRole      = Role.findOrSaveByAuthority('ROLE_BASIC')
        def userRole       = Role.findOrSaveByAuthority('ROLE_USER')
        def storefrontRole = Role.findOrSaveByAuthority('ROLE_STOREFRONT_USER')
        def publisherRole  = Role.findOrSaveByAuthority('ROLE_PUBLISHER')

        //noinspection GroovyAssignabilityCheck
        String adminUserName = grailsApplication.config.springsecurity.syndicationAdmin.adminUsername
        if (User.findByUsername(adminUserName)) {
            return
        }

        String initialPassword = grailsApplication.config.springsecurity.syndicationAdmin.initialAdminPassword
        def adminUser = new User(username: adminUserName, enabled: true, password: initialPassword)
        adminUser.save(flush: true)

        UserRole.create adminUser, adminRole, true
    }
}