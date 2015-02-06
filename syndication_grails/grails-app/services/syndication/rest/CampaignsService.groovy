
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import grails.transaction.Transactional
import com.ctacorp.syndication.*

@Transactional(readOnly = true)
class CampaignsService {
    def apiResponseBuilderService
    def errorHandlingService
    def mediaService

    def listCampaigns(params) {
        params.max = getMax(params)
        if(params.id){
            return Campaign.get(params.id)
        }
        String sort = params.sort
        params.sort = null
        return Campaign.multiSort(sort).list(params)
    }

    def getTotal(params){
        def newP = [:] + params
        newP.max = Integer.MAX_VALUE
        newP.offset = 0
        Campaign.list(newP).size()
    }

    def listMediaItemsForCampaign(Long campaignId, params){
        if(!MediaItem.load(campaignId)){
            return null
        }
        mediaService.listMediaItemsForCampaign(campaignId, params)
    }

    private static int getMax(params){
        Math.min(params.int("max") ?: 20, 1000)
    }
}
