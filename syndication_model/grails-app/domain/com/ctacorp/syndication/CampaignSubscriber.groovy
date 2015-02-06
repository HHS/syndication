package com.ctacorp.syndication

class CampaignSubscriber {

    Long subscriberId
    Campaign campaign

    static constraints = {
        subscriberId nullable: false, blank: false
        campaign nullable: false, blank: false
    }
}
