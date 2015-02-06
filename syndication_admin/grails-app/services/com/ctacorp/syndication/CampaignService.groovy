package com.ctacorp.syndication

import com.ctacorp.syndication.authentication.UserRole
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
        def errors = []
        campaign.validate()
        if (campaign.hasErrors()) {
            def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib');
            errors = campaign.errors.allErrors.collect{[message:g.message([error : it])]}
            return errors
        }
        campaign.save(flush:true)

        def campaignSubscriber = CampaignSubscriber.findByCampaign(campaign)

        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            if(!springSecurityService.currentUser.subscriberId){
                errors << [message:"You do not have a valid subscriber. For help contact " + grailsApplication.config.grails.mail.default.from]
                log.error("The publisher " + springSecurityService.currentUser.name)
            }
            if(campaignSubscriber){
                campaignSubscriber.subscriberId = springSecurityService.currentUser.subscriberId
            } else {
                campaignSubscriber = new CampaignSubscriber([campaign:campaign,subscriberId:springSecurityService.currentUser.subscriberId])
            }
            campaignSubscriber.save(flush: true)
        } else if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_ADMIN" && subscriberId) {
            if(campaignSubscriber){
                campaignSubscriber.subscriberId = subscriberId as Long
            } else {
                campaignSubscriber = new CampaignSubscriber([campaign: campaign,subscriberId:subscriberId as Long])
            }
            campaignSubscriber.save(flush: true)
        }

        if(errors != []){
            transactionStatus.setRollbackOnly()
            return errors
        }

        return null
    }
}
