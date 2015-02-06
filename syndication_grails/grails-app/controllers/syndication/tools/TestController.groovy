
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.tools

import com.ctacorp.syndication.Html
import com.ctacorp.syndication.Image
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.Video
import grails.plugin.springsecurity.annotation.Secured

//@Secured('ROLE_ADMIN')
class TestController {
    def mqService
    def likeService
    def mediaService
    def solrIndexingService
    def grailsApplication

    def index() {
        flash.message = null
    }

    def poster(){
        println params
        render params
        render "<br/><hr/><br/>"
        render "<ul>"
        def headers = request.headerNames
        while(headers.hasMoreElements()){
            def h = headers.nextElement()
            render "<li>${h} : ${request.getHeader(h)}</li>"
            println "${h} : ${request.getHeader(h)}"
        }
        render "</ul>"

        render "<p>Body json-----------------------</p>"
        println "Body json-----------------------"
        println request?.JSON
        render request?.JSON
    }

    def sendUpdate(Long mediaId){
        println params
        MediaItem mediaItem = MediaItem.get(mediaId)
        println mediaItem?.name
        mqService.sendUpdateNotification(mediaItem)
        flash.message = "Sent update for: ${mediaItem.name} (${mediaItem.id})"
        render view:'index'
    }

    def updateAllMedia() {
        solrIndexingService.inputAllMedia()
        render "Successfully published all media items on solr"
    }
    def updateSolrOnPublish() {
        def htmlItem = new Html(sourceUrl: "http://www.cdc.gov/features/worldimmunizationweek/",
                name: "Noooo",
                language: Language.findByName("French"),
                source: Source.findByAcronym("NCI"),
                dateContentReviewed: new Date().clearTime()+300)

        mediaService.saveHtml(htmlItem, [name:"blah"])

        def videoItem = new Video(sourceUrl: "http://www.youtube.com/watch?v=sDHhRUzgx2k",
                name: "What If I dont like shotssss",
                language: Language.findByName("English"),
                source: Source.findByAcronym("CDC"),
                dateContentReviewed: new Date().clearTime()+300)
        mediaService.saveVideo(videoItem)

        def imageItem = new Image(sourceUrl: "http://www.cdc.gov/media/subtopic/library/LabsScientists/Frieden.jpg",
                name: "Scientist Picture - Friedmannnn",
                language: Language.findByName("English"),
                source: Source.findByAcronym("CDC"),
                dateContentReviewed: new Date().clearTime()+300,
                width: 23,
                height: 43,
                imageFormat: "jpeg",
                altText: "Scientist picture")
        mediaService.saveImage(imageItem)

        render "Published"
    }

    def highest(){
        likeService.mostPopular(params).each{
            render "${likeService.likeCountForMedia(it.id)} - ${it.name} <br/>"
        }
        render "done"

//        render likeService.mostPopular()
    }

    def fiveHundred(){
        response.sendError(500)
        return
    }

    def threeOTwo(){
        redirect action: "redirContent"
    }

    def redirContent(){
        render "<div class='syndicate'>This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! This is some content! </div>"
        return
    }

    def shortContent(){
        render "<div class='syndicate'>This is some content!</div>"
        return
    }
}
