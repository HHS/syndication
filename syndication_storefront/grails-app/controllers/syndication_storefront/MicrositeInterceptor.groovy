package syndication_storefront

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.microsite.MicrositeRegistration


class MicrositeInterceptor {

    def springSecurityService

    MicrositeInterceptor(){
        match(controller: 'microsite', action: '*')
    }

    boolean before() {
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_STOREFRONT_USER"){
            def registration = MicrositeRegistration.findByUser(User.get(springSecurityService.currentUser.id))
            if(!registration){
                redirect controller: "micrositeRegistration", action: "registration"
            }else if(registration && !registration.verified){
                redirect controller: "micrositeRegistration", action: "alreadyRegistered"
            }
        }
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
