package syndication_admin
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import com.ctacorp.syndication.contact.EmailContact
import com.ctacorp.syndication.authentication.*
import grails.util.Environment
import grails.util.Holders
import org.apache.catalina.core.ApplicationContext

class BootStrap {
    def grailsApplication
    def queueService
    def mediaPreviewThumbnailJobService
    def config = Holders.config

    def init = { servletContext ->
        log.info ("Admin is running in --> ${Environment.current} <-- mode.")
        initUsers()

//        System.setProperty("jsse.enableSNIExtension", "false");

//        initContacts()

//        createScratchDirectories()
        String systemRunningMessage = """
==========================================
| -> Syndication Admin Ready.            |
=========================================="""
        log.info systemRunningMessage
    }

    def initContacts(){
        EmailContact admin = EmailContact.findByEmailIlike("syndicationadmin@hhs.gov")
        if(!admin){
            admin = new EmailContact(email: "syndicationadmin@hhs.gov", name: "Syndication Admin")
            admin.save()
        }
    }

    def destroy = {
    }

    private createScratchDirectories(){
        //The API should be creating this folder, and it should exist anyway because that's where google keys go
        //But just in case the admin is run by itself, this will ensure it's created.
        File scratch = new File(config.syndication.scratch.root)
        if(!scratch.exists()){
            scratch.mkdir()
            log.info "No scratch directory found so creating one: ${scratch}"
        }
    }

    @SuppressWarnings("GroovyUnusedAssignment")
    private void initUsers() {
        def adminRole      = Role.findOrSaveByAuthority('ROLE_ADMIN')
        def managerRole    = Role.findOrSaveByAuthority('ROLE_MANAGER')
        def storefrontRole = Role.findOrSaveByAuthority('ROLE_STOREFRONT_USER')
        def publisherRole  = Role.findOrSaveByAuthority('ROLE_PUBLISHER')

        assert adminRole && storefrontRole && publisherRole

        if(Environment.getCurrent() == Environment.DEVELOPMENT){
            if(!User.findByUsername("publisher@example.com")){
                def publisher = new User(username: "publisher@example.com",
                        enabled: true,
                        password: config.springsecurity.syndicationAdmin.initialAdminPassword,
                        subscriberId:config.testPublisherSubscriberKey).save(flush:true)
                UserRole.create publisher, publisherRole
            }
        }

        //noinspection GroovyAssignabilityCheck
        String adminUserName = config.SYNDICATION_ADMIN_USERNAME
        if (User.findByUsername(adminUserName)) {
            return
        }

        String initialPassword = config.SYNDICATION_ADMIN_PASSWORD

        def adminUser = new User(username: adminUserName, enabled: true, password: initialPassword)
        adminUser.save(flush: true)

        UserRole.create adminUser, adminRole, true
    }
}