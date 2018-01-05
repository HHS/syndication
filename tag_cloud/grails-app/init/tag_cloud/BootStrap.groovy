package tag_cloud

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import grails.util.Environment
import grails.util.Holders
import tagcloud.domain.Tag
import tagcloud.domain.TagType

class BootStrap {

    def grailsApplication
    def config = Holders.config

    def init = { servletContext ->
        log.info ("*** Tag Cloud is running in --> ${Environment.current} <-- mode. ***")
        initUsers()

        initRequiredData()

        if (Holders.config.TAG_CLOUD_GENERATETESTDATA == true) {
            println "test value: " + Holders.config.TAG_CLOUD_GENERATETESTDATA
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

        2000.times{
            def tag = new Tag(name: words[ran.nextInt(words.size())], type: TagType.load(ran.nextInt(3)+1), language: Language.load(ran.nextBoolean()?1:400))
            if(!tag.save()){
                println tag.errors
            }
        }
    }

    private void initUsers() {
        String adminUsername = config.TAG_CLOUD_ADMIN_USERNAME

        if (User.findByUsername(adminUsername)) {
            return
        }

        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)

        String intialAdminPassword = config.TAG_CLOUD_ADMIN_PASSWORD

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
