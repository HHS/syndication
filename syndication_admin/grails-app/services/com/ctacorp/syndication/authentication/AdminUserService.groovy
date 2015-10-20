package com.ctacorp.syndication.authentication

import grails.transaction.Transactional

@Transactional
class AdminUserService {
    def springSecurityService

    def deleteUser(User userInstance) {
        UserRole.removeAll(userInstance)

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
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER"){
            def userRoles = UserRole.createCriteria().list(params){
                def userRole = params.role
                if(!userRole || userRole.toLowerCase() == "any"){
                    role{
                        "in" 'authority', getManagersAuthorityRoles()
                    }
                } else {
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

            return [userInstanceCount: userRoles.getTotalCount(), userRoles:userRoles, currentRole: params.role]
        }

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
    
    def getManagersAuthorityRoles(){
        ["ROLE_USER", "ROLE_BASIC", "ROLE_STATS"]
    }
}
