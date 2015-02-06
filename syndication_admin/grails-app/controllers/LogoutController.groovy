import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class LogoutController {

    /**
     * Index action. Redirects to the Spring security logout uri.
     */
    def index = {
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }
}
