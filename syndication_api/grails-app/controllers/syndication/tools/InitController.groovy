
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.tools

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.media.MediaItem
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class InitController {
    def tinyUrlService
    def tagsService
    def solrService
    def authorizationService
    def cmsManagerService

    def index() {

    }

    def tags(){
        flash.message = null
        flash.error = null
        println "Running tagInit"
        if(initTagService()){
            flash.message = "Tags created."
        } else{
            flash.error = "TagCloud server not found!"
        }
        render view: 'index'
    }

    def tiny(){
        flash.message = null
        flash.error = null
        println "Running tinyInit"
        if(initTinyUrlService()){
            flash.message = "Tiny URLs created."
        } else{
            flash.error = "tinyURL server not found!"
        }
        render view: 'index'
    }

    def seedSolr() {
        println "seeding solr "

        solrService.inputMediaItems()
        solrService.inputCampaigns()
        solrService.inputSources()
        println "solr is seeded"
    }

    private boolean ping(String serverAddress, String name){
        URL url = new URL(serverAddress);
        try{
            assert url.text
            log.info "${name} Server found"
            println "${name} Server found"
            return true
        } catch(e){
            println "${name} Server Not found!"
            return false
        }
    }

    private boolean initTinyUrlService(){
        if(ping("${grailsApplication.config.tinyUrl.serverAddress}${grailsApplication.config.syndication.tinyUrl.mappingBase}.json", "TinyURL")){
            MediaItem.list().each{ MediaItem mi->
                def tinyInfo = tinyUrlService.getMappingByMediaItemId(mi.id)
                if(tinyInfo.error){
                    println "Creating TinyURL for ${mi.sourceUrl}"
                    tinyUrlService.createMapping(mi.sourceUrl, mi.id, mi.externalGuid)
                }
            }
            return true
        } else{
            println "Can't reach server: ${grailsApplication.config.tinyUrl.serverAddress}${grailsApplication.config.syndication.tinyUrl.mappingBase}.json"
        }
        false
    }

    private boolean initTagService(){
        if(ping(grailsApplication.config.tagCloud.serverAddress + "/tags.json", "TagCloud")){
            MediaItem.list().each{ MediaItem mi->
                (ran.nextInt(3)+1).times{
                    def tag = randomTag()
                    println "Tagging ${mi.name} with ${tag}"
                    tagsService.tagMediaItemByName(mi.id, tag, 1, 1)
                }
            }
            return true
        }
        false
    }

    def initOwnership(){
        try{
            flash.message = "Ownership already exists"
            def publicKey = grailsApplication.config.cmsManager.publicKey
            def subscriber = cmsManagerService.getSubscriber(publicKey)
            def mediaItems = MediaItem.list()

            if(subscriber && !MediaItemSubscriber.list()){
                println "adding ownership to media Items"
                flash.message = "Ownership added!"
                mediaItems.each{mediaItem ->
                    new MediaItemSubscriber([mediaItem:mediaItem,subscriberId:subscriber.id as Long]).save(flush:true)
                }
            }

        } catch (e) {
            println "Can't connect to CMS Manager!"
        }
        render view: 'index'
    }

    Random ran = new Random()
    def tags = [
        'Smoking Use and Tobacco Facts',
        'Quitting Resources',
        'Cessation',
        'Secondhand Smoke',
        'Prevent Smoking and Tobacco Use',
        'Prevention',
        'Guidance, Regulations and Compliance',
        'Cessation',
        'Prevention',
        'Secondhand Smoke',
        'Research',
        'Policy'
    ]

    private String randomTag(){
        tags[ran.nextInt(tags.size())]
    }
}
