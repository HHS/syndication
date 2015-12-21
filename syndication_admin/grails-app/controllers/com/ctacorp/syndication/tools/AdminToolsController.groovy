package com.ctacorp.syndication.tools

import com.ctacorp.syndication.jobs.HashResetJob
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.Tweet
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class AdminToolsController {
    def tinyUrlService
    def mediaItemsService
    def twitterService

    def index() {
    }

    def updateMissingTinyUrls() {
        def updatedMappings = tinyUrlService.updateItemsWithoutMappings()
        String statusMessage = ""
        updatedMappings.each {
            statusMessage += "${it.id} - ${it.targetUrl}<br/>"
        }
        if (statusMessage) {
            flash.message = "Mappings Added:<br/>" + statusMessage
        } else {
            flash.message = "No mapping updates needed!"
        }
        render view: "index"
    }

    def resetAllHashes(Boolean restrictToDomain, String domain) {
        HashResetJob.triggerNow(restrictToDomain: restrictToDomain, domain: domain)
        flash.message = "Hash reset job has been triggered, it may take minutes to hours to complete"
        redirect action: 'index'
    }

    def inspectTweet(Long tweetId) {
        def tweetData = twitterService.getTweet(tweetId)
        if (!tweetData) {
            flash.error = "the tweet Id " + tweetId + " does not exist."
            render view: 'index'
            return
        }
        render view: 'index', model: [tweetData: tweetData, tweetId: tweetId]
    }
}
