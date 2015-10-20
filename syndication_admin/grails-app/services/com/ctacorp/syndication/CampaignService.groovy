package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional

/**
 * Created by nburk on 1/12/15.
 */
@Transactional
class CampaignService {
    def springSecurityService
    def grailsApplication
    
    def publisherCampaigns(){
        CampaignSubscriber?.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)?.campaign?.id
    }

    def publisherValid(Campaign campaign){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER" && !publisherCampaigns()?.contains(campaign?.id)){
            return false
        }
        return true
    }
    
    def mediaItemBelongsToSubscriber(campaignInstance, mediaId){
        
        return !CampaignSubscriber.findByCampaign(campaignInstance) || MediaItemSubscriber.findAllByMediaItem(MediaItem.get(mediaId)).subscriberId.contains(CampaignSubscriber.findByCampaign(campaignInstance).subscriberId)
        
    }
    
    def updateCampaignAndSubscriber(Campaign campaign, subscriberId = null){
        def campaignSubscriber = null
        campaign.validate()

        if(campaign.id) {
            campaignSubscriber = CampaignSubscriber.findByCampaign(campaign)
        }
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            if(!springSecurityService.currentUser.subscriberId){
                campaign.errors.reject("Invalid Subscriber", "You do not have a valid subscriber. For help contact " + grailsApplication.config.grails.mail.default.from)
                log.error("The publisher " + springSecurityService.currentUser.name)
            }
            if(campaignSubscriber){
                campaignSubscriber.subscriberId = springSecurityService.currentUser.subscriberId
            } else {
                campaignSubscriber = new CampaignSubscriber([campaign:campaign,subscriberId:springSecurityService.currentUser.subscriberId])
            }
        } else if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_ADMIN") {
            if (!subscriberId) {
                campaign.errors.reject("SubscriberId invalid","You did not select a valid subscriber.")
                log.error("The publisher " + springSecurityService.currentUser.name)
            } else if(campaignSubscriber){
                campaignSubscriber.subscriberId = subscriberId as Long
            } else {
                campaignSubscriber = new CampaignSubscriber([campaign: campaign,subscriberId:subscriberId as Long])
            }
        }
        campaign.validate()

        if(campaign.hasErrors()){
            Campaign.withTransaction {status ->
                //more explicit for testing purposes
                status.setRollbackOnly()
            }
            CampaignSubscriber.withTransaction {status ->
                status.setRollbackOnly()
            }
//            transactionStatus.setRollbackOnly()
            return campaign
        }

        campaign.save(flush:true)
        campaignSubscriber?.save(flush: true)

        return campaign
    }
}
