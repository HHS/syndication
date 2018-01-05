package syndication_admin

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.MediaItem


class HealthReportInterceptor {

    def springSecurityService
    HealthReportInterceptor(){
        match(controller: 'healthReport', action: 'checkMediaItem|ignoreFlaggedMedia|unignoreFlaggedMedia')
    }
    boolean before() {
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            if(!MediaItemSubscriber.findAllByMediaItemAndSubscriberId(MediaItem.get(params.long("id")), springSecurityService.currentUser.subscriberId)){
                response.sendError(404)
                return false
            }
        }
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
