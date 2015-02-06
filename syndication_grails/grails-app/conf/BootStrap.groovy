
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCcDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import clover.org.apache.commons.lang.SystemUtils
import com.ctacorp.syndication.*
import com.ctacorp.syndication.authentication.*
import com.ctacorp.syndication.storefront.UserMediaList
import grails.util.Environment
import grails.util.Holders
import org.codehaus.groovy.grails.commons.ApplicationAttributes
import syndication.marshal.*
import syndication.testdata.TestDataPopulator

class BootStrap {

    def grailsApplication
    def thumbnailService
    def tinyUrlService
    def tagsService
    def youtubeService
    def mediaService
    def systemEventService
    def solrService
    def queueService
    def config

    def init = { servletContext ->
        log.info ("*** API is running in --> ${Environment.current} <-- mode. ***")
        config = Holders.config

        //Previews and Caching
        createScratchDirectories()

        //verify that necessary programs are executable
        verifyPrograms()
        // verify scratch directories
        verifyDirectories()
        //Custom Object Marshalling
        initMarshallers()

        //Initial / test user accounts
        initUsers()

        //Demo data stuff
        TestDataPopulator td = new TestDataPopulator(grailsApplication: grailsApplication, youtubeService: youtubeService, tinyUrlService: tinyUrlService, names:new File(servletContext.getRealPath("/"), "names.txt").readLines())
        if (config.syndication.loadExampleRealData) {
            if (MediaItem.count() == 0) {
                td.seedRealExamples()
            }
        } else{
            td.seedSources()
            td.seedLanguages()
        }

        MediaItemChangeListener.initialize(grailsApplication, queueService)

        systemEventService.systemStarted()

        String systemRunningMessage = """
==========================================
| -> Syndication API Ready.              |
==========================================\
"""
        log.info systemRunningMessage
    }

    def destroy = {
        systemEventService.systemShutdown()
    }

    private void initUsers() {
        def adminRole = Role.findOrSaveByAuthority('ROLE_ADMIN')
        def userRole = Role.findOrSaveByAuthority('ROLE_USER')

        String adminUsername = config.springsecurity.syndication.adminUsername
        if (User.findByUsername(adminUsername)) { return }

        String initialAdminPassword = config.springsecurity.syndication.initialAdminPassword
        def adminUser = new User(name: "Syndication Administrator", username: adminUsername, enabled: true, password: initialAdminPassword)

        if(!adminUser.save(flush: true)) {
            println "Could not save the user ${adminUsername} due to the following errors: ${adminUser.errors}"
        }

        new UserMediaList(name: "My List", description:"Default list", user: adminUser).save(flush:true)

        UserRole.create adminUser, adminRole, true
    }

    private void createScratchDirectories() {
        //Local Storage stuff
        String rootPath = config.syndication.scratch.root
        File root = new File(rootPath)
        root.mkdirs()
        if (root.exists()) {
            log.info "Scratch Directory located at: ${rootPath}"
        } else {
            log.error "Root path was not found/created: ${rootPath}"
            System.exit(1)
        }

        new File("${rootPath}/preview/full").mkdirs()
        new File("${rootPath}/preview/thumbnail").mkdirs()
        new File("${rootPath}/preview/small").mkdirs()
        new File("${rootPath}/preview/medium").mkdirs()
        new File("${rootPath}/preview/large").mkdirs()
        new File("${rootPath}/preview/custom").mkdirs()

        new File("${rootPath}/html").mkdirs()
    }

    private verifyDirectories() {
        String rootPath = config.syndication.scratch.root

        verifyDirectory(new File("${rootPath}/preview/full"))
        verifyDirectory(new File("${rootPath}/preview/thumbnail"))
        verifyDirectory(new File("${rootPath}/preview/small"))
        verifyDirectory(new File("${rootPath}/preview/medium"))
        verifyDirectory(new File("${rootPath}/preview/large"))
        verifyDirectory(new File("${rootPath}/preview/custom"))
        verifyDirectory(new File("${rootPath}/html"))
    }

    private verifyDirectory(File f) {
        if (!(f.exists() && f.isDirectory())) {
            log.error(f.getAbsolutePath() + " does not exist")
            throw new Exception(f.getAbsolutePath() + " does not exist")
        }
    }

    private initMarshallers() {
        def marshallers = [
            new HtmlMarshaller(),
            new InfographicMarshaller(),
            new ImageMarshaller(),
            new VideoMarshaller(youtubeService:youtubeService),
            new SourceMarshaller(),
            new CollectionMarshaller(),
            new CampaignMarshaller(),
            new LanguageMarshaller(),
            new AlternateImageMarshaller(),
            new MediaTypeHolderMarshaller(),
            new AudioMarshaller(),
            new SocialMediaMarshaller(),
            new PeriodicalMarshaller()
        ]

        marshallers.each { marshaller ->
            marshaller.thumbnailService = thumbnailService
            marshaller.tinyUrlService = tinyUrlService
            marshaller.tagsService = tagsService
            marshaller.mediaService = mediaService
        }
    }

    private verifyProgram(String path, String fileName, String versionCall, String versionAnswer) {
        def programExists = true
        def programRunnable = true
        File executableFile = new File(path, fileName)
        if (!executableFile.exists()) {
            programExists = false
        } else {
            def process = "${executableFile.getAbsolutePath()} ${versionCall}".execute()
            String retVal = process.text
            programRunnable = retVal.contains(versionAnswer)
            process.destroy()
        }
        if (!(programExists && programRunnable)) {
            log.error("exiting on startup: ${fileName} cannot be found. Are you sure it's installed?")
            throw new Exception("Exiting on startup: ${fileName} cannot be found. Are you sure it's installed?")
        }
    }

    private verifyPrograms() {
        //Image Magick
        def path = config.imageMagick.location
        verifyProgram(path, "convert", "-version", "Version")

        //xvfb
        if (SystemUtils.IS_OS_LINUX) {
            path = config.xvfb.location
            verifyProgram(path, 'xvfb-run', '-help', 'Usage')
        }
        //cutycapt
        path = config.cutycapt.location
        if (SystemUtils.IS_OS_MAC) {
            verifyProgram(path, 'cutycapt', '--help', 'help') //note the capitalization
        } else {
            def command = "${config.xvfb.location}/xvfb-run  ${config.cutycapt.location}/cutycapt --version"
            def process = command.execute()
            String retval = process.text
            process.destroy()
            def programRunnable = retval.contains("Usage")
            if (!programRunnable) {
                log.error("exiting on startup: convert does not execute, is cutycapt installed?")
                throw new Exception("exiting on startup: convert does not execute, is cutycapt installed?")
            }
        }
    }
}
