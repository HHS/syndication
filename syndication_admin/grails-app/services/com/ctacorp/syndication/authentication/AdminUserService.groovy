package com.ctacorp.syndication.authentication

import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.UserMediaList
import com.ctacorp.syndication.util.PasswordGenerator
import grails.transaction.Transactional
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Transactional
class AdminUserService {
    def springSecurityService
    def micrositeService
    def mailService
    def grailsApplication
    def groovyPageRenderer

    def updateUserLastLogin(){
        User currentUser = springSecurityService.getCurrentUser()
        currentUser?.lastLogin = new Date()
        if(!currentUser?.save()){
            log.error "Admin: Could not update user's last login date: ${currentUser} ${currentUser?.id}"
        }
    }

    def deleteUser(User userInstance) {
        UserRole.removeAll(userInstance)

        userInstance.likes = null
        UserMediaList.where{
            user == userInstance
        }.deleteAll()

        MicroSite.where{
            user == userInstance
        }.each{ microsite ->
            micrositeService.deleteMicrosite(microsite.id)
        }

        userInstance.delete flush: true
    }

    def saveUserAndRole(User user, Long roleId) {

        Role role = Role.get(roleId)

        if(role.authority != "ROLE_PUBLISHER"){
            user.subscriberId = null
        }

        user.save flush:true

        UserRole.withNewTransaction {
            UserRole.removeAll(user, true)
        }

        UserRole.create(user, role, true)
    }
    
    def indexResponse(params){
        def userRoles = UserRole.createCriteria().list(params){
            def userRole = params.role
            if(userRole && userRole.toLowerCase() != "any"){
                role{
                    eq 'authority', userRole
                }
            }

            user {
                if(params.search) {
                    switch (params.searchSelector) {
                        case "user.name": ilike 'name', "%${params.search}%"; break
                        case "user.username": ilike 'username', "%${params.search}%"; break;
                        case "user.both":
                            or{
                                ilike 'name', "%${params.search}%"
                                ilike 'username', "%${params.search}%"
                            }
                             break;
                        default: break;
                    }
                }
            }
        }
        [userInstanceCount: userRoles.getTotalCount(), userRoles:userRoles, currentRole: params.role]
    }

    def resetPassword(User user) {
        if (user.getAuthorities().contains(Role.findByAuthority("ROLE_ADMIN"))) {
            log.error "A password reset was attempted for an admin account ID: ${user.id}"
            return false
        }

        //Generate a new random password
        def randomPassword = PasswordGenerator.randomPassword
        user.password = randomPassword
        //Update user account with new rand password

        if (user.save(flush: true, failOnError: true)) {
            //if the save worked, send an email
            mailService.sendMail {
                async true
                to user.username
                subject "Your password has been reset"
                html groovyPageRenderer.render(template: "/login/passwordResetEmail", model:[randomPassword:randomPassword, user:user])
            }
            return true
        }
        false
    }

}
