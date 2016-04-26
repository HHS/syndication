
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import com.ctacorp.syndication.Source
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.social.TwitterAccount
import grails.transaction.NotTransactional
import twitter4j.TwitterException

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.FeaturedMedia
import com.ctacorp.syndication.MediaItemSubscriber

import com.ctacorp.syndication.media.Tweet
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER'])
@Transactional
class TweetController {

    def mediaItemsService
    def tagService
    def solrIndexingService
    def cmsManagerKeyService
    def springSecurityService
    def twitterService

    static allowedMethods = [save: "POST", update: "PUT", delete: "POST"]

    @Transactional(readOnly = true)
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def indexResponse = mediaItemsService.getIndexResponse(params, Tweet)
        respond indexResponse.mediaItemList, model: [tweetInstanceCount: indexResponse.mediaItemInstanceCount, mediaType:"Tweet"]
    }

    @NotTransactional
    def importTweets(Integer count, Boolean restrictToMedia, String accountName){
        def sourceList = Source.list()
        count = count ? Math.min(count, 100) : 10
        restrictToMedia ?: true
        def subscribers = cmsManagerKeyService.listSubscribers()
        def apiStatus = twitterService.getAPIRateLimitStatus("/application/rate_limit_status")
        def tweets

        def twitterAccountList = null
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_ADMIN" || UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_MANAGER") {
            twitterAccountList = TwitterAccount.list()
        } else if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER") {
            twitterAccountList = TwitterAccount.findAllBySubscriberId(springSecurityService.currentUser.subscriberId)
        }

        if(!accountName){
            render view: "importTweets",
                    model: [
                        status:apiStatus,
                        message: twitterService.isProtected(accountName) ? "This twitter account is private." :"Please add a twitter account and select it to continue.",
                        twitterAccountList:twitterAccountList,
                        subscribers:subscribers,
                        sourceList:sourceList
                    ]
            return
        }

        tweets = restrictToMedia ? twitterService.getMediaStatuses(accountName, count ?: 10) : twitterService.getStatuses(accountName, count ?: 10)

        ArrayList<Map> modifiableStatuses = []
        //TODO this doesn't look like a good way to do this :/
        if(tweets instanceof LinkedHashMap) {
            flash.error = tweets.error
        } else {
            tweets.each{
                modifiableStatuses.add([text:twitterService.linkifyUrls(it.text), id:it.id, mediaEntities:[mediaURLHttps:[it.mediaEntities.mediaURLHttps[0]]],createdAt:it.createdAt])
            }
        }

        [
            tweets:modifiableStatuses,
            status:apiStatus,
            restrictToMedia:restrictToMedia,
            count:count,
            accountName:accountName,
            twitterAccountList:twitterAccountList,
            subscribers:subscribers,
            sourceList:sourceList
        ]
    }

    @NotTransactional
    def saveTweets(Long sourceId){
        flash.errors = []
        if(!sourceId){
            flash.errors << [message:"No source selected!"]
        }

        def authority = UserRole.findByUser(springSecurityService.currentUser).role.authority

        if(authority in ["ROLE_ADMIN", "ROLE_MANAGER"] && params.subscriberId == "") {
            flash.errors << [message:"No Subscriber Selected"]
        }

        def tweetIds = params.findAll{ it.key.startsWith("tweetId_") }.collect{it.key - "tweetId_"}

        if(!tweetIds) {
            flash.errors << [message:"No Tweets were selected for import."]
        }

        if(flash.errors) {
            redirect action:"importTweets", params:params
            return
        }

        flash.messages = []
        flash.errors = []

        try {
            tweetIds.each { tweetId ->
                if (Tweet.findByTweetId(tweetId as Long)) {
                    def mi = Tweet.findByTweetId(tweetId as Long)
                    flash.errors << [message: mi.name + " already exists. Contact an administrator if this is an issue."]
                } else {
                    def mediaItem = twitterService.saveTweet(tweetId as Long, sourceId, params.long("subscriberId"))
                    if (mediaItem.hasErrors()) {
                        flash.errors << [message: "twitter post Id: " + tweetId + " for twitter account: " + params.accountName + " has issues with its default configuration. Please Contact an administrator."]
                    } else {
                        solrIndexingService.inputMediaItem(mediaItem)
                        flash.messages << [message: mediaItem.name + " created!"]
                    }
                }

            }
        }catch(java.util.concurrent.ExecutionException e){
            log.error("Rate limit exceeded")
            flash.errors = []
            flash.errors << [message: "Twitter API Rate Limit exceeded. When too many requests are made to twitter too quickly, it exceeds a limit set by Twitter's API. Please wait a few minutes and try your import again - Twitter rate limits reset evey 15 minutes."]
        }catch(e){
            log.error ("Error importing tweets: ${e}")
        }
        redirect action:"index"
    }

    @NotTransactional
    def show(Tweet tweetInstance) {
        def tagData = tagService.getTagInfoForMediaShowViews(tweetInstance, params)

        respond tweetInstance, model:[      tags            :tagData.tags,
                                            languages       :tagData.languages,
                                            tagTypes        :tagData.tagTypes,
                                            languageId      :params.languageId,
                                            tagTypeId       :params.tagTypeId,
                                            selectedLanguage:tagData.selectedLanguage,
                                            selectedTagType :tagData.selectedTagType,
                                            collections     : Collection.findAll("from Collection where ? in elements(mediaItems)", [tweetInstance]),
                                            apiBaseUrl      :grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath,
                                            subscriber      :cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(tweetInstance)?.subscriberId)
        ]
    }

    def refreshTwitterMeta(Long id){
        twitterService.refreshMetaData(id)
        flash.message = "Twitter meta data has been reparsed and saved."
        redirect action:"show", params:[id:id]
    }

    def create() {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond new Tweet(params), model: [subscribers:subscribers]
    }

    def save(Tweet tweetInstance) {
        if (tweetInstance == null) {
            notFound()
            return
        }

        tweetInstance =  mediaItemsService.updateItemAndSubscriber(tweetInstance, params.long('subscriberId'))
        if(tweetInstance.hasErrors()){
            flash.errors = tweetInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            respond tweetInstance, view:'create', model:[subscribers:cmsManagerKeyService.listSubscribers()]
            return
        }

        solrIndexingService.inputMediaItem(tweetInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tweetInstance.label', default: 'Tweet'), [tweetInstance.name]])
                redirect tweetInstance
            }
            '*' { respond tweetInstance, [status: CREATED] }
        }
    }

    def edit(Tweet tweetInstance) {
        def subscribers = cmsManagerKeyService.listSubscribers()
        respond tweetInstance, model: [subscribers:subscribers, currentSubscriber:cmsManagerKeyService.getSubscriberById(MediaItemSubscriber.findByMediaItem(tweetInstance)?.subscriberId)]
    }

    def update(Tweet tweetInstance) {
        if (tweetInstance == null) {
            notFound()
            return
        }

        tweetInstance =  mediaItemsService.updateItemAndSubscriber(tweetInstance, params.long('subscriberId'))
        if(tweetInstance.hasErrors()){
            flash.errors = tweetInstance.errors.allErrors.collect { [message: g.message([error: it])] }
            redirect action:'edit', id:params.id
            return
        }

        solrIndexingService.inputMediaItem(tweetInstance)
        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Tweet.label', default: 'Tweet'), [tweetInstance.name]])
                redirect tweetInstance
            }
            '*'{ respond tweetInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_PUBLISHER'])
    def delete(Tweet tweetInstance) {
        if (tweetInstance == null) {
            notFound()
            return
        }

        def featuredItem = FeaturedMedia.findByMediaItem(tweetInstance)
        if(featuredItem){
            featuredItem.delete()
        }

        mediaItemsService.removeInvisibleMediaItemsFromUserMediaLists(tweetInstance, true)
        solrIndexingService.removeMediaItem(tweetInstance)
        mediaItemsService.delete(tweetInstance.id)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Tweet.label', default: 'Tweet'), [tweetInstance.name]])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tweetInstance.label', default: 'Tweet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def exceptionHandler(TwitterException e){
        flash.error = "Twitter Request rate limit exceeded. The rate limit will " +
                "reset in ${(e.rateLimitStatus.secondsUntilReset / 60) as int} minutes."
        [rateLimitStatus:e.rateLimitStatus]
    }
}
