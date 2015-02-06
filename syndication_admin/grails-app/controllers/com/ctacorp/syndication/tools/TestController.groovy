package com.ctacorp.syndication.tools

import com.ctacorp.syndication.Html
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.health.FlaggedMedia
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class TestController {
    def mediaValidationService
    def contentRetrievalService
    def contentCacheService

    def index() {}

    def flagItems(){
        Random ran = new Random()
        int max = MediaItem.count()
        10.times{
            def mi = MediaItem.get(ran.nextInt(max)+1)
            if(mi){
                def failureType = FlaggedMedia.FailureType.values()[ran.nextInt(FlaggedMedia.FailureType.values().size())]
                new FlaggedMedia(
                        mediaItem: mi,
                        dateFlagged: new Date()-ran.nextInt(7),
                        message: message(code:"healthReport.failure.${failureType}"),
                        ignored: ran.nextBoolean(),
                        failureType: failureType
                ).save(flush:true)
            }
        }
        flash.message = "Media Items Flagged"
        render view:'index'
    }

    def testHealth(MediaItem mi){
        def report = mediaValidationService.performValidation(mi?.id)
        render view: "index", model:[healthReport:report]
        return
    }

    def allHealthReports(){
        def reports = []
        MediaItem.list().each{ mi ->
            reports << mediaValidationService.performValidation(mi?.id)
        }
        render view: "index", model:[healthReports:reports]
    }

    def cacheSomeData(){
//        render "<ul>"
//        Html.list().each{
//            render "<li>${it}</li>"
//        }
//        render "</ul>"

//        CachedContent.where{
//            id > 0L
//        }.deleteAll()
//
//        render "<ul>"
//        long start = System.currentTimeMillis()
//        Html.list().each{
//            def cached = CachedContent.findByMediaItem(it)
//            if(!cached){
//                try {
//                    String content = contentRetrievalService.extractSyndicatedContent(it.sourceUrl)
//                    CachedContent cc = new CachedContent(mediaItem: it, content: content)
//                    cc.save(flush: true)
//                    if(cc.hasErrors()){
//                        println cc.errors
//                    }
//                    println cc.id
//                    cached = cc
//                }catch(e){
//                    e.printStackTrace()
//                }
//            }
//            render "<li>${cached?.content}</li>"
//        }
//        render "</ul>"
//        println System.currentTimeMillis() - start

        Html h = Html.last()
        contentCacheService.get(h)
        contentCacheService.flush(h)
        contentCacheService.get(h)
        render h
    }
}