package syndication_admin

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole


class UserRoleInterceptor {

    def springSecurityService
    def userService

    UserRoleInterceptor(){
        match(controller: 'user', action: 'edit|show|update|delete')
    }
    boolean before() {
        if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER") {
            if (!userService.getManagersAuthorityRoles().contains(UserRole.findByUser(User.get(params.long("id"))).role.authority)) {
                response.sendError(404)
                return false
            }
        }
        UserRole.findByUser(User.get(params.long("id")))?.discard()
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
