package com.ctacorp.syndication.socialmedia

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItemsService
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.commons.util.Hash
import com.ctacorp.syndication.commons.util.Util
import com.ctacorp.syndication.media.Tweet
import com.ctacorp.syndication.social.TwitterAccount
import grails.transaction.Transactional
import grails.util.Holders
import twitter4j.Paging
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.conf.ConfigurationBuilder

import javax.annotation.PostConstruct
import java.util.concurrent.Callable
import java.util.regex.Matcher
import java.util.regex.Pattern

@Transactional
class TwitterService {
    Twitter twitter
    def mediaItemsService
    def guavaCacheService
    def springSecurityService

    @PostConstruct
    def init(){
        def config = Holders.config
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false)
                .setOAuthConsumerKey(config.syndication.twitterConsumerKey)
                .setOAuthConsumerSecret(config.syndication.twitterConsumerSecret)
                .setOAuthAccessToken(config.syndication.twitterAccessToken)
                .setOAuthAccessTokenSecret(config.syndication.twitterAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    Status getTweet(Long tweetId) {
        twitter.showStatus(tweetId)
    }

    def saveTweet(Long tweetId, Long subscriberId = null) {
        String key = Hash.md5("${tweetId}")
        Status status = guavaCacheService.tweetCache.get(key, new Callable<Status>() {
            @Override
            public Status call(){
                twitter.showStatus(tweetId)
            }
        });
        saveTweet(status, subscriberId)
    }

    def saveTweet(Status status, Long subscriberId = null) {
        if(Tweet.findByTweetId(status.id)) {
            return Tweet.findByTweetId(status.id)
        } else {
            Tweet newTweet = new Tweet()
            //tweet specific fields
            def twitterAccount = TwitterAccount.findOrSaveByAccountName(status.user.screenName)
            newTweet.account = twitterAccount
            newTweet.tweetId = status.id
            newTweet.messageText = status.text
            newTweet.mediaUrl = status.mediaEntities?.mediaURLHttps[0] ?: null
            newTweet.tweetDate = status.createdAt

            //media item required fields
            newTweet.name = status.user.screenName + " Post on " + status.createdAt.format("EEE, MMM d, yyyy")
            newTweet.sourceUrl = "https://twitter.com/" + status.user.screenName + "/status/" + status.id
            newTweet.description = status.user.description
            newTweet.language = Language.findByName("English")
            newTweet.source = Source.findByAcronym("FDA")

            newTweet.validate()
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_ADMIN") {
                return mediaItemsService.updateItemAndSubscriber(newTweet, subscriberId)
//                return newTweet.save(flush:true)
            } else {
                return mediaItemsService.updateItemAndSubscriber(newTweet, null)
            }
        }

    }

    long getAccountID(String accountName){
        if(accountName.startsWith("@")){
            accountName = accountName[1..-1]
        }
        User user = null
        try{
            user = twitter.showUser(accountName)
        } catch(e) {
            log.error e
        }
        user?.getId() ?: -1
    }

    boolean isProtected(String accountName) {
        if(accountName.startsWith("@")){
            accountName = accountName[1..-1]
        }
        User user = null
        try{
            user = twitter.showUser(accountName)
        } catch(e) {
            log.error e
            return true
        }
        user?.isProtected()
    }

    def getStatuses(String username, int max=20){
        getStatuses(getAccountID(username), max)
    }

    def getStatuses(long user, int max) {
        if(user == -1) {
            return [error:"User does not exist"]
        }
        def page = 1
        String key = Hash.md5("${user}" + "${max}" + "${page}")
        Status[] timeline = guavaCacheService.tweetCache.get(key, new Callable<Status[]>() {
            @Override
            public Status[] call(){
                twitter.getUserTimeline(user, new Paging(page, max))
            }
        });
        timeline
    }

    def getMediaStatuses(String username, int max=20){
        getMediaStatuses(getAccountID(username), max)
    }

    def getMediaStatuses(long user, int max=20) {
        if(user == -1) {
            return [error:"User does not exist"]
        }
        def mediaStatuses = []
        for(int i=1; i<150; i++){
            String key = Hash.md5("${user}" + "${max}" + "${i}")
            def timeline = guavaCacheService.tweetCache.get(key, new Callable<Status[]>() {
                @Override
                public Status[] call(){
                    twitter.getUserTimeline(user, new Paging(i, 100))
                }
            });

            timeline.each {
                guavaCacheService.setTweetCache(Hash.md5("${it.id}"), it)
                if(it.mediaEntities){
                    mediaStatuses << it
                }
            }
            if(mediaStatuses.size() >= max || timeline.size() == 0){
                break
            }
        }
        mediaStatuses.size() > max ? mediaStatuses[0..max-1] : mediaStatuses
    }

    def linkifyUrls(String inputText){
        String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        Matcher matcher = Pattern.compile(urlRegex).matcher(inputText)
        String result = ""
        int lastIndex = 0
        while(matcher.find()){
            def prefix = "<a href='${matcher.group()}'>"
            def postfix = "</a>"
            result += inputText[lastIndex..matcher.start()-1] + prefix + inputText[matcher.start()..matcher.end()-1] + postfix
            lastIndex = matcher.end()
        }
        if(lastIndex < inputText.size()-1) {
            result += inputText[lastIndex..-1]
        }
        result
    }

    def getAPIRateLimitStatuses(){
        def statuses = []
        twitter.getRateLimitStatus().each{
            statuses << it
        }
        statuses.sort{it.key}
    }

    def getAPIRateLimitStatus(key){
        twitter.getRateLimitStatus()[key]
    }
}
