/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCcDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.ctacorp.syndication.*
import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.authentication.*
import com.ctacorp.syndication.storefront.UserMediaList
import grails.util.Environment
import grails.util.Holders
import com.ctacorp.syndication.marshal.*
import com.ctacorp.syndication.testdata.TestDataPopulator

class BootStrap {
    def grailsApplication
    def urlService
    def tinyUrlService
    def tagsService
    def youtubeService
    def mediaService
    def systemEventService
    def queueService
    def mediaPreviewThumbnailJobService
    def config = Holders.config

    def init = { servletContext ->
        log.info ("*** API is running in --> ${Environment.current} <-- mode. ***")

        //Too many misconfigured ssl sites out there, disable SNI for ingestion
        System.setProperty("jsse.enableSNIExtension", "false");

        //Previews and Caching
        createScratchDirectories()

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
                td.seedMicrositeData()
                td.seedNihContent()
            }
        } else{
            td.seedSources()
            td.seedLanguages()
        }

        if(config.syndication.generateFakeData){
            td.generateRandomHtmlItems(config.syndication.fakeDataCount ?: 1000)
        }

        MediaItemChangeListener.initialize(grailsApplication, queueService, mediaPreviewThumbnailJobService)

        //initial admins UserMediaList
        if (MediaItem.count() == 0) {
            initUserMediaList()
        }
        systemEventService.systemStarted()

        String systemRunningMessage = """
==========================================
| -> Syndication API Ready.              |
=========================================="""
        log.info systemRunningMessage
    }

    def destroy = {
        systemEventService.systemShutdown()
    }

    private void initUsers() {
        def adminRole = Role.findOrSaveByAuthority('ROLE_ADMIN')
        def userRole = Role.findOrSaveByAuthority('ROLE_USER')
        def storefrontRole = Role.findOrSaveByAuthority('ROLE_STOREFRONT_USER')

        String adminUsername = config.springsecurity.syndication.adminUsername
        if (User.findByUsername(adminUsername)) { return }

        String initialAdminPassword = config.springsecurity.syndication.initialAdminPassword
        def adminUser = new User(name: "Syndication Administrator", username: adminUsername, enabled: true, password: initialAdminPassword)

        if(!adminUser.save(flush: true)) {
            println "Could not save the user ${adminUsername} due to the following errors: ${adminUser.errors}"
        }

        UserRole.create adminUser, adminRole, true
    }
    
    private void initUserMediaList(){
        def adminUser = User.findByName("Syndication Administrator")
        new UserMediaList(name: "My List", description:"Default list", user: adminUser,mediaItems: MediaItem.findAllByIdInList((20..30))).save(flush:true)
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
        new File("${rootPath}/preview/tmp").mkdirs()

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
        verifyDirectory(new File("${rootPath}/preview/tmp"))
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
            new VideoMarshaller(),
            new SourceMarshaller(),
            new CollectionMarshaller(),
            new CampaignMarshaller(),
            new LanguageMarshaller(),
            new AlternateImageMarshaller(),
            new MediaTypeHolderMarshaller(),
            new AudioMarshaller(),
            new TweetMarshaller(),
            new PDFMarshaller(),
            new PeriodicalMarshaller(),
            new WidgetMarshaller()
        ]

        def services = [
                urlService: urlService,
                tinyUrlService: tinyUrlService,
                tagsService: tagsService,
                mediaService: mediaService,
                youtubeService: youtubeService
        ]

        marshallers.each { marshaller ->
            marshaller.services = services
        }
    }
}
