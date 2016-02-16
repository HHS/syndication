package syndicationadmin

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.media.MediaItem


class ContentOwnershipFilters {
    def mediaItemsService
    def campaignService

    def filters = {
        all(controller: 'collection|html|image|infographic|tweet|video', action: 'show|edit|update|delete') {
            before = {
                if(!mediaItemsService.ifPublisherValid(MediaItem.get(params.id))){
                    response.sendError(404)
                    return false
                }
            }
        }
        all(controller: 'campaign', action: 'show|edit|update|delete'){
            before = {
                if(!campaignService.publisherValid(Campaign.get(params.id))){
                    response.sendError(404)
                    return false
                }
            }
        }
    }
}
