package tagcloud

import com.ctacorp.syndication.authentication.User
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class UserController {

    def springSecurityService

    def index() {
        User currentUser = springSecurityService.getCurrentUser()
        [currentUser:currentUser]
    }

    @Transactional
    def updateUser(User userInstance){
        if(userInstance.validate() && !userInstance.hasErrors()){
            userInstance.save(flush:true)
            flash.message = "User account updated"
            redirect action:"index"
        } else{
            render view:"index", model:[currentUser:userInstance]
        }
    }
}
