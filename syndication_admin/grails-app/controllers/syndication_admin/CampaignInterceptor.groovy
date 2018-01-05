package syndication_admin

import com.ctacorp.syndication.Campaign

class CampaignInterceptor {

    def campaignService

    CampaignInterceptor() {
        match(controller: 'campaign', action: 'show|edit|update|delete')
    }

    boolean before() {
        if(!campaignService.publisherValid(Campaign.get(params.id))){
            response.sendError(404)
            return false
        }
        return true
    }
}
