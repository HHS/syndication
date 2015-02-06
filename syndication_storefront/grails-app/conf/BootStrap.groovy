import com.ctacorp.syndication.authentication.Role
import grails.util.Environment

class BootStrap {
    def grailsApplication

    def init = { servletContext ->
        initRequiredRoles()
        log.info ("*** Storefront is running in --> ${Environment.current} <-- mode. ***")
        String systemRunningMessage = """
==========================================
| -> Syndication Storefront Ready.       |
==========================================\
"""
        log.info systemRunningMessage
    }
    def destroy = {
    }

    private initRequiredRoles(){
        Role.findOrSaveWhere(authority: "ROLE_STOREFRONT_USER")
    }
}
