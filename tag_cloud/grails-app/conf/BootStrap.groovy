/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import grails.util.Environment
import tagcloud.domain.Tag
import tagcloud.domain.TagType

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        log.info ("*** Tag Cloud is running in --> ${Environment.current} <-- mode. ***")
        initUsers()

        initRequiredData()

        if (grailsApplication.config.tagCloud.generateTestData) {
            initTestData()
        }

        String systemRunningMessage = """
==========================================
| -> Tag Cloud Ready.                    |
=========================================="""
        log.info systemRunningMessage
    }

    private initTestData() {
        def words = grailsApplication.mainContext.getResource("/words.txt").getFile().readLines()
        Random ran = new Random()

        1000.times{
            def tag = new Tag(name: words[ran.nextInt(words.size())], type: TagType.load(ran.nextInt(3)+1), language: Language.load(ran.nextBoolean()?1:400))
            if(!tag.save()){
                println tag.errors
            }
        }
    }

    private void initUsers() {
        String adminUsername = grailsApplication.config.springsecurity.tagCloud.adminUsername

        if (User.findByUsername(adminUsername)) {
            return
        }

        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)

        String intialAdminPassword = grailsApplication.config.springsecurity.tagCloud.initialAdminPassword

        def adminUser = new User(name: "TagCloud Administrator", username: adminUsername, enabled: true, password: intialAdminPassword)
        adminUser.save(flush: true)

        UserRole.create adminUser, adminRole, true
    }

    private void initRequiredData() {
        seedLanguages()

        TagType.findOrSaveByNameAndDescription("General", "Default tag type for general purpose tagging.")
        TagType.findOrSaveByNameAndDescription("Topic", "Classification.")
        TagType.findOrSaveByNameAndDescription("Audience", "Targeted group of people.")

        assert TagType.count() >= 3
    }

    def seedLanguages() {
        Language.seedDatabaseWithLanguages()
        def eng = Language.findByIsoCode("eng")
        eng.isActive = true
        eng.save(flush:true)
        def spanish = Language.findByIsoCode("spa")
        spanish.isActive = true
        spanish.save(flush:true)
        assert Language.count() >= 450
    }

    def destroy = {
    }
}
