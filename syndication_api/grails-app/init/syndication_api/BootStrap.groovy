package syndication_api

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.marshal.LanguageMarshaller
import com.ctacorp.syndication.marshal.MediaTypeHolderMarshaller
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.testdata.TestDataPopulator
import grails.util.Environment
import grails.util.Holders

class BootStrap {
    def grailsApplication
    def urlService
    def tinyUrlService
    def tagsService
    def youtubeService
    def mediaService
    def systemEventService
    def queueService
    def config = Holders.config

    def init = { servletContext ->
        log.info ("*** API is running in --> ${Environment.current} <-- mode. ***")

        //Too many misconfigured ssl sites out there, disable SNI for ingestion
//        System.setProperty("jsse.enableSNIExtension", "false");

        //Previews and Caching
//        createScratchDirectories()

        // verify scratch directories
//        verifyDirectories()

        //Initial / test user accounts
//        initUsers()

        //Demo data stuff
//        TestDataPopulator td = new TestDataPopulator(grailsApplication: grailsApplication, youtubeService: youtubeService, tinyUrlService: tinyUrlService, names:new File(servletContext.getRealPath("/"), "names.txt").readLines())
//        if (config.syndication.loadExampleRealData) {
//            println "loading example data..."
//            if (MediaItem.count() == 0) {
//                td.seedRealExamples()
////                td.seedMicrositeData()
//                td.seedNihContent()
//            } else{
//                println "There are already ${MediaItem.count()} items in the system, skipping example data creation..."
//            }
//        } else{
//            println "no example data loaded."
//            td.seedSources()
//            td.seedLanguages()
//        }
//
//        if(config.syndication.generateFakeData){
//            td.generateRandomHtmlItems(config.syndication.fakeDataCount ?: 1000)
//        }

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
}
