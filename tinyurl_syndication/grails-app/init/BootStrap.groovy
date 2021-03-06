import com.ctacorp.tinyurl.marshalling.MediaMappingMarshaller
import com.ctacorp.tinyurl.MediaMapping
import com.ctacorp.tinyurl.authentication.Role
import com.ctacorp.tinyurl.authentication.User
import com.ctacorp.tinyurl.authentication.UserRole
import grails.util.Environment
import grails.util.Holders

class BootStrap {
    final boolean generateTestData = false
    def config = Holders.config

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
        if(!generateTestData){
            return
        }
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/CostsOfDrinking/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/hpvvaccinations/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/RabiesAndKids/index.html").save()
        new MediaMapping(targetUrl: "http://www.cdc.gov/features/PreventChickenpox/index.html").save(flush:true)
    }

    private void initUsers() {
        String adminUsername = config.TINYURL_ADMIN_USERNAME
        String adminPassword = config.TINYURL_ADMIN_PASSWORD
        User adminUser = User.findByUsername(adminUsername)
        if(!adminUser){
            adminUser = new User(username: adminUsername, enabled: true, password: adminPassword)
            adminUser.save(flush:true)
        }

        def adminRole = Role.findOrSaveByAuthority('ROLE_ADMIN')

        def adminUserRole = UserRole.findByUserAndRole(adminUser, adminRole)
        if(!adminUserRole){
            UserRole.create adminUser, adminRole, true
        }
    }
}
