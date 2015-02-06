package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.jobs.MediaValidationJob
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.NotTransactional
import grails.transaction.Transactional

@Secured(["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_PUBLISHER"])
@Transactional(readOnly = true)
class HealthReportController {
    def mediaValidationService
    def springSecurityService

    def index(Integer max) {
        setDefaultParams(max)
        def flaggedMediaItems = getFlaggedItems(false)
        [flaggedMediaItems:flaggedMediaItems, totalCount:flaggedMediaItems.totalCount]
    }

    @NotTransactional
    def checkAllMedia(){
        if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
            MediaValidationJob.triggerNow([subscriberId:springSecurityService.currentUser.subscriberId])
        } else {
            MediaValidationJob.triggerNow()
        }

        flash.message = "Health report re-scan has started, it may take several hours to complete."
        redirect action: "index"
        return
    }

    @NotTransactional
    def checkMediaItem(Long id){
        mediaValidationService.rescanItem(id)
        flash.message = "Item has been rescanned. If the problem has not been resolved or if a new error was encountered, then the flag will not be removmed."
        redirect action: "index"
        return
    }

    def ignored(Integer max) {
        setDefaultParams(max)
        def flaggedMediaItems = getFlaggedItems(true)
        [flaggedMediaItems:flaggedMediaItems, totalCount:flaggedMediaItems.totalCount]
    }

    @Transactional
    def ignoreFlaggedMedia(FlaggedMedia fm){
        println "getting here"
        if(fm){
            fm.ignored = true
            fm.save(flush:true)
            flash.message = "${fm.mediaItem.name} errors are now being ignored."
        } else{
            flash.error = "The item you selected cannot be found. Perhaps someone deleted it while you were working on it?"
        }
        redirect action: "index"
        return
    }

    @Transactional
    def unignoreFlaggedMedia(FlaggedMedia fm){
        if(fm){
            fm.ignored = false
            fm.save(flush:true)
            flash.message = "${fm.mediaItem.name} errors are now being flagged."
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
    
    private getFlaggedItems(boolean ignored){
        def cri = FlaggedMedia.createCriteria()

        cri.list(max:params.max, offset:params.offset){
            if(UserRole.findByUser(springSecurityService.currentUser).role.authority == "ROLE_PUBLISHER"){
                def publishersFlaggedItems = MediaItemSubscriber.findAllBySubscriberId(springSecurityService.currentUser.subscriberId).mediaItem
                if(publishersFlaggedItems){
                    "in" ('mediaItem', publishersFlaggedItems)
                }
            }
            
            if(params.sort == "mediaItem.name"){
                mediaItem{
                    order('name', params.order)
                }
            } else{
                order params.sort, params.order
            }
            eq 'ignored', ignored
        }
    }
}
