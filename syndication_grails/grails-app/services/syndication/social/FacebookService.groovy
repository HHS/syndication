
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.social

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.social.SocialMediaAccount
import grails.plugins.rest.client.RestBuilder
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import javax.annotation.PostConstruct

//@Transactional
class FacebookService {
//    def grailsApplication
//
//    RestBuilder rest = new RestBuilder()
//    String accessToken
//
//    @PostConstruct
//    void init() {
//        accessToken = "${grailsApplication.config.syndication.facebookAccessToken}|${grailsApplication.config.syndication.facebookSecret}"
//    }
//
//    @Transactional(readOnly = true)
//    def listSocialMediaAccounts(params = [:]){
//        SocialMediaAccount.list(params)
//    }
//
//    @Transactional(readOnly = true)
//    def scanAndImport() {
//        log.info("Scanning Facebook Accounts for new Posts")
//        def accounts = SocialMediaAccount.list()
//        accounts.each { account ->
//            def rawData = getFeedItemsFromLast24Hours(account.accountName)
//            rawData.data.each{ feedItem ->
//                saveFacebookPost(feedItem)
//            }
//        }
//    }
//
//    @Transactional(readOnly = true)
//    def findFacebookItem(String id){
//        return SocialMedia.findByExternalGuid(id)
//    }
//
//    def saveFacebookPost(item){
//        if(findFacebookItem(item.id)){ return } //already imported
//
//        Date dateContentAuthored = Date.parse("YYYY-MM-dd'T'HH:mm:ss'+'0000", item.created_time)
//        Date dateContentUpdated = Date.parse("YYYY-MM-dd'T'HH:mm:ss'+'0000", item.updated_time)
//
//        SocialMedia sm = new SocialMedia(
//                socialMediaType:"Facebook",
//                externalGuid: item.id,
//                language:Language.findByIsoCode("eng"),
//                source: Source.findByAcronym("HHS"),
//                dateContentAuthored:dateContentAuthored,
//                dateContentUpdated:dateContentUpdated,
//                sourceUrl:"http://localhost:8080/Syndication/api/v2/resources/media/ID_HERE/content",
//                name: "${item.from.name} - ${dateUpdated}",
//                description: item.message
//        )
//        if(!sm.save()){
//            log.error sm.errors
//            log.error "Couldn't save facebook item: ${item}"
//            return null
//        } else{
//            sm.sourceUrl = sm.sourceUrl.replace("/media/ID_HERE", "/media/${sm.id}")
//            if(!sm.save()){
//                log.error sm.errors
//                log.error "Couldn't update facebook URL with id: ${item}"
//                return null
//            }
//        }
//        return sm.id
//    }
//
//    @NotTransactional
//    def getFeedItemsFromLast24Hours(String accountName) {
//        String url = "https://graph.facebook.com/${accountName}/feed"
//        int yesterday = ((int)System.currentTimeMillis()/1000)-60*60*24
//        String query = "?access_token=${accessToken}&since=${yesterday}"
//        try {
//            return rest.get(url+query) { accept "application/json" }.json
//        } catch (e) {
//            log.error "Tag Service could not be reached."
//        }
//    }
//
//    @NotTransactional
//    def getFacebookDetails(long id){
//        SocialMedia sm = SocialMedia.get(id)
//        String fbId = sm.externalGuid
//        if(sm){
//            String url = "https://graph.facebook.com/${fbId}"
//            String query = "?access_token=${accessToken}"
//            try {
//                return rest.get(url+query) { accept "application/json" }.json
//            } catch (e) {
//                log.error "Tag Service could not be reached."
//            }
//        } else{
//            log.error "item ${id} not found"
//        }
//    }
}
