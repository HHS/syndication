package com.ctacorp.syndication.tools

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.jobs.HashResetJob
import com.ctacorp.syndication.media.Collection
import com.ctacorp.syndication.media.MediaItem
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured(["ROLE_ADMIN"])
class AdminToolsController {
    def tinyUrlService
    def mediaItemsService
    def twitterService
    def remoteCacheService

    static allowedMethods = [ sendMessages:"POST"]

    def index() {
    }

    def massMessage(){
        params
    }

    def sendMessages(){
        if(!params.subject || !params.body){
            flash.error = "You must provide both a subject and a body"
            redirect action: "massMessage", params:[subject:params.subject, body:params.body, whichUsers:params.whichUsers]
            return
        }

        def sentToWhom

        switch(params.whichUsers){
            case 'all':
                sentToWhom = "all users";
                getUserList("ANY").each{ user ->
                    sendMessage(user.username, params.subject, params.body, params.boolean('htmlMessage'))
                }
                break
            case 'store':
                sentToWhom = "all storefront users"
                getUserList("ROLE_STOREFRONT_USER").each{ user ->
                    sendMessage(user.username, params.subject, params.body, params.boolean('htmlMessage'))
                }
                break;
            case 'pub':
                sentToWhom = "all publishers"
                getUserList("ROLE_PUBLISHER").each{ user ->
                    sendMessage(user.username, params.subject, params.body, params.boolean('htmlMessage'))
                }
                break;
            case 'man':
                getUserList("ROLE_MANAGER").each{ user ->
                    sendMessage(user.username, params.subject, params.body, params.boolean('htmlMessage'))
                }
                sentToWhom = "all managers"
                break
            case 'admin':
                getUserList("ROLE_ADMIN").each{ user ->
                    sendMessage(user.username, params.subject, params.body, params.boolean('htmlMessage'))
                }
                sentToWhom = "all admins"
                break;
            default:
                sentToWhom = "Unknown recipient: ${params.whichUsers}"
                flash.error = "Unknown recipient: ${sentToWhom}"
                redirect action: "massMessage", params:[subject:params.subject, body:params.body, whichUsers:params.whichUsers]
                return
        }

        flash.message = "Message sent to ${sentToWhom}"
        redirect action: "massMessage"
    }

    private sendMessage(recip, sub, bod, isHtml=false){
        println "Sending email to ${recip}, subject ${sub}, body: ${bod}, isHtml?: ${isHtml}"
        if(isHtml){
            sendMail{
                to "${recip}"
                subject sub
                html bod
            }
        } else{
            sendMail{
                to "${recip}"
                subject sub
                body bod
            }
        }
    }

    @Transactional
    public getUserList(role){
        if(role == "ANY") {
           return User.list()
        }
        UserRole.findAllByRole(Role.findByAuthority(role)).user
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

    def duplicateFinder(){
//        params.max = Math.min(params.int('max') ?: 100, 20000)
        params.sort = "id"
        params.order = params.order ?: "DESC"
        println "params: " + params
        def mediaList = MediaItem.list(params)

        def alreadyChecked = []
        def urlMap = [:]
        def dupes = []
        mediaList.each{ mi ->
            log.info("Checking ${mi.id} for duplicates")
            println "urlMapping: " + urlMap[mi.sourceUrl]
            if(!urlMap[mi.sourceUrl.toLowerCase()]){
                urlMap[mi.sourceUrl.toLowerCase()] = mi.id
            }else {
                //handle duplicate
                def found = MediaItem.findAllBySourceUrl(mi.sourceUrl, [sort: 'id', order: 'ASC'])
                if (found.size() > 1) {
                    log.info("Duplicate Found!! ${found}")
                    dupes << [
                            oldest: found[0],
                            dupes: found[1..-1]
                    ]
                }
            }
        }
        render view:'duplicateFinder', model:[duplicates:dupes, total:MediaItem.count()]
    }

    def addLoginDateToAllUsers(){
        User.list().each{ user ->
            User.withTransaction {
                if(!user.lastLogin) {
                    user.lastLogin = new Date()
                    if (!user.save()) {
                        log.error "Couldn't save user: ${user.id} ${user.username}"
                    }
                }
            }
        }
        flash.message = "Dates added to all users without an existing date. Check logs for errors."
        redirect action: "index"
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

    def updateSourceUrlHash() {
        log.info("start cleaning up incorrect collection source urls")
        Collection.list().each{
            if(!(it.sourceUrl ==~ /^https:\/\/www.example.com\/collection\/.*/)){
                it.sourceUrl = "https://www.example.com/collection/${System.nanoTime()}"
                it.save()
            }
        }
        log.info("finished cleaning collection source urls")
        log.info("------------------------begin adding source url md5 hashes----------------------")
        MediaItem.list().each{
            if(MediaItem.findAllBySourceUrl(it.sourceUrl).size() == 1) {
                it.validate()
                log.info("updated sourceUrlHash for media id: " + it.id)
                it.save()
            }

        }
        log.info("------------------------finished adding source url md5 hashes-----------------------")
        flash.message = " Updated!!! "
        render view:"index"
    }
}
