package com.ctacorp.syndication.tools

import com.ctacorp.syndication.jobs.HashResetJob
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.media.Tweet
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class AdminToolsController {
    def tinyUrlService
    def mediaItemsService
    def twitterService
    def remoteCacheService

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

    def flushAllCaches(){
        remoteCacheService.flushRemoteCache()
        flash.message = "Remote Cache Flush requested"
        redirect action: 'index'
    }

    def findDuplicates(){
        def allMedia = MediaItem.list()
        def alreadyChecked = []
        def dupes = []
        allMedia.each{ mi ->
            log.info("Checking ${mi.id} for duplicates")
            if(!(mi.id in alreadyChecked)) {
                def found = MediaItem.findAllBySourceUrl(mi.sourceUrl, [sort: 'id', order: 'ASC'])
                if (found.size() > 1) {
                    log.info("Duplicate Found!! ${found}")
                    dupes << [
                            oldest: found[0],
                            dupes: found[1..-1]
                    ]
                }
                found.each{
                    alreadyChecked << it.id
                }
            }
        }
        render view:'index', model:[duplicates:dupes]
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

    def downloadDatabaseAsJsonFile(){
        def allItems = MediaItem.list()

        def allData = []
        allItems.each{ MediaItem mi ->
            def data = [
                    name:mi.name,
                    sourceUrl:mi.sourceUrl,
                    sourceId:mi.sourceId,
                    languageId:mi.languageId,
                    description:mi.description,
                    mediaType:mi.getClass().simpleName.toLowerCase()
            ]
            allData << data
        }

        response.setContentType("APPLICATION/OCTET-STREAM")
        response.setHeader("Content-Disposition", "Attachment;Filename=\"SyndicationData_${new Date().format('yyyy_mm_dd-HH:mm')}.json\"")

        def outputStream = response.getOutputStream()
        outputStream << ((allData as JSON).toString(params.boolean('pretty') ?: false))
        outputStream.flush()
        outputStream.close()
    }
}
