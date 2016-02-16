package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.microsite.FlaggedMicrosite
import com.ctacorp.syndication.microsite.MicroSite
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class MicrositeController {

    def index() {
        def userMicrosites = MicroSite.list()
        [
                userMicrosites:userMicrosites
        ]
    }

    def show(MicroSite microsite) {
        if(!microsite?.id){
            flash.error = "the microsite does not exist"
            redirect controller: "micrositeFilter", action: "index"
        }

        render view:"show", model:[
                micrositeInstance: microsite,
                apiBaseUrl:grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath
        ]
    }

    def delete(MicroSite microSite){
        if(!microSite?.id){
            flash.error = "Could not find microSite"
            redirect controller: "micrositeFilter", action:"index"
            return
        }
        def name = microSite.title
        def flaggedMicrosite = FlaggedMicrosite.findByMicrosite(microSite)
        if(flaggedMicrosite){
            flaggedMicrosite.delete()
        }
        microSite.delete(flush: true)
        flash.message = "Microsite '${name}' has been deleted!"

        redirect controller:"micrositeFilter", action: "index"
    }
}
