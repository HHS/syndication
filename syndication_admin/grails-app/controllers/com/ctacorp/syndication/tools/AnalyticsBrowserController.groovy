package com.ctacorp.syndication.tools

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Holders

@Secured(["ROLE_ADMIN"])
class AnalyticsBrowserController {
    def config = Holders.config
    def googleAnalyticsService

    def index() {
        render view:"index", model:[accounts:googleAnalyticsService.getAccounts()]
    }

    def webProperties(String accountId, String accountName){
        [webProperties:googleAnalyticsService.getWebProperties(accountId), accountId:accountId, accountName:accountName]
    }

    def profiles(String accountId, String accountName, String webPropertyId, String webPropertyName){
        [profiles:googleAnalyticsService.getProfiles(accountId, webPropertyId), accountId: accountId, webPropertyId:webPropertyId, accountName:accountName, webPropertyName:webPropertyName]
    }

    def query(String accountId, String accountName, String webPropertyId, String webPropertyName, String profileName, String profileId){
        [profiles:googleAnalyticsService.getProfiles(accountId, webPropertyId), accountId: accountId, webPropertyId:webPropertyId, accountName:accountName, webPropertyName:webPropertyName, profileName:profileName, profileId:profileId]
    }

    def executeQuery(QueryData qd){
        render view:"query", model: [results:googleAnalyticsService.executeQuery(qd.profileId, qd.start, qd.end, qd.query), accountId: params.accountId, webPropertyId:params.webPropertyId, accountName:params.accountName, webPropertyName:params.webPropertyName, profileName:params.profileName, profileId:params.profileId, start:qd.start, end:qd.end]
    }
}


class QueryData{
    Date start
    Date end
    String profileId
    String query

    String toString(){
        "<>\nstart: ${start}\nend: ${end}\nprofileId: ${profileId}\nquery: ${query}"
    }
}