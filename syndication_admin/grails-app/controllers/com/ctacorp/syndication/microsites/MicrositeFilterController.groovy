package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.jobs.MicrositeValidationJob
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class MicrositeFilterController {

    def micrositeFilterService

    def index(Integer max) {
        setDefaultParams(max)
        def flaggedMicrosites = FlaggedMicrosite.findAllByIgnored(false)

        [flaggedMicrosites:flaggedMicrosites,totalCount:FlaggedMicrosite.count()]
    }

    def ignored(Integer max) {
        setDefaultParams(max)
        def flaggedMicrosites = FlaggedMicrosite.findAllByIgnored(true)

        [flaggedMicrosites:flaggedMicrosites,totalCount:FlaggedMicrosite.count()]
    }

    @Transactional
    def ignoreFlaggedMicrosite(FlaggedMicrosite fm){
        if(fm){
            fm.ignored = true
            fm.save(flush:true)
            flash.message = "${fm.microsite.title} language is now being ignored."
        } else{
            flash.error = "The item you selected cannot be found. Perhaps someone deleted it while you were working on it?"
        }

        redirect action: "index"
        return
    }

    @Transactional
    def unignoreFlaggedMicrosite(FlaggedMicrosite fm){
        if(fm){
            fm.ignored = false
            fm.save(flush:true)
            flash.message = "${fm.microsite.title} errors are now being flagged."
        } else{
            flash.error = "The item you selected cannot be found. Perhaps someone deleted it while you were working on it?"
        }
        redirect action: "ignored"
        return
    }

    private void setDefaultParams(max){
        params.max = Math.min(max ?: 10, 10)
        params.sort = params.sort ?: "dateFlagged"
        params.order = params.order ?: "DESC"
    }

    def checkAllMicrosites(){
        MicrositeValidationJob.triggerNow()
        flash.message = "Microsite report re-scan has started, it may take several hours to complete."
        redirect action:"index"
    }

    def checkMicrosite(Long id){
        micrositeFilterService.rescanItem(id)
        flash.message = "Microsite has been rescanned. If the problem has not been resolved or if a new error was encountered, then the flag will not be removed."
        redirect action: "index"
        return
    }
}
