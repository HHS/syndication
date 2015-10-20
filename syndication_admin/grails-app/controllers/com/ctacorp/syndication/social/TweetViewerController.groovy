package com.ctacorp.syndication.social

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.Tweet
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_PUBLISHER'])
class TweetViewerController {
    def twitterService

    def index(Integer count, Boolean restrictToMedia, String accountName) {
        count = count ? Math.min(count, 100) : 10
        restrictToMedia ?: true
        def apiStatus = twitterService.getAPIRateLimitStatus("/application/rate_limit_status")
        def tweets

        if(!accountName){
            render view: "index", model: [status:apiStatus, message:"Please input a twitter account name to continue..."]
            return
        }
        if(restrictToMedia){
            tweets = twitterService.getMediaStatuses(accountName, count ?: 10)
        } else{
            tweets = twitterService.getStatuses(accountName, count ?: 10)
        }

        tweets.each{
            it.text = twitterService.linkifyUrls(it.text)
        }
        [tweets:tweets, status:apiStatus, restrictToMedia:restrictToMedia, count:count, accountName:accountName]
    }

    def rateLimitStatus(){
        [statuses:twitterService.getAPIRateLimitStatuses()]
    }

}