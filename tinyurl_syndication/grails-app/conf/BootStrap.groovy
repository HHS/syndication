import com.ctacorp.tinyurl.marshalling.MediaMappingMarshaller
import com.ctacorp.tinyurl.MediaMapping
import com.ctacorp.tinyurl.authentication.Role
import com.ctacorp.tinyurl.authentication.User
import com.ctacorp.tinyurl.authentication.UserRole
import grails.util.Environment

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        log.info ("*** TinyURL is running in --> ${Environment.current} <-- mode. ***")
        initUsers()
        initTestData()

        //Add base32 encoding/decoding
        Long.metaClass.encodeAsBase32 = {-> toString(longValue(), 32)}
        String.metaClass.decodeBase32 = {-> Long.parseLong("${value}", 32)}

        //Register custom marshallers
        new MediaMappingMarshaller()

        String systemRunningMessage = """
==========================================
| -> Tiny Url Ready.                     |
=========================================="""
        log.info systemRunningMessage
    }

    def destroy = {
    }

    private void initTestData(){
        if(!grailsApplication.config.tinyUrl.generateTestData){
            return
        }
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/CostsOfDrinking/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/hpvvaccinations/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/RabiesAndKids/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/PreventChickenpox/index.html").save(flush:true)
    }

    private void initUsers() {
        String adminUsername = grailsApplication.config.springsecurity.tinyUrl.adminUsername
        if (User.findByUsername(adminUsername)) {
            return
        }
        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)

        def adminUser = new User(username: grailsApplication.config.springsecurity.tinyUrl.adminUsername, enabled: true, password: grailsApplication.config.springsecurity.tinyUrl.initialAdminPassword)

        adminUser.save(flush: true)

        UserRole.create adminUser, adminRole, true
    }
}
