package syndicationadmin

import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.authentication.User

class RoleFilterFilters {
    def springSecurityService
    def userService

    def filters = {
        all(controller: 'user', action: 'edit|show|update|delete') {
            before = {
                if (UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER") {
                    if (!userService.getManagersAuthorityRoles().contains(UserRole.findByUser(User.get(params.long("id"))).role.authority)) {
                        response.sendError(404)
                        return false
                    }
                }
                UserRole.findByUser(User.get(params.long("id"))).discard()
            }
        }
            
        all(controller: 'healthReport', action: 'checkMediaItem|ignoreFlaggedMedia|unignoreFlaggedMedia'){
            before = {
                if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
                    if(!MediaItemSubscriber.findAllByMediaItemAndSubscriberId(MediaItem.get(params.long("id")), springSecurityService.currentUser.subscriberId)){
                        response.sendError(404)
                        return false
                    }
                }
            }
        }
    }
}
