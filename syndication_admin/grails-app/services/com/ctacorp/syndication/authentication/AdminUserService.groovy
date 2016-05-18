package com.ctacorp.syndication.authentication

import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.storefront.UserMediaList
import grails.transaction.Transactional

@Transactional
class AdminUserService {
    def springSecurityService
    def micrositeService

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

    def saveUserAndRole(User userInstance, Long roleId) {
        Role role = Role.get(roleId)
        if(role.authority != "ROLE_PUBLISHER"){
            userInstance.subscriberId = null
        }
        userInstance.save flush:true
        UserRole.removeAll(userInstance, true)
        UserRole.create(userInstance, role, true)
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
}
