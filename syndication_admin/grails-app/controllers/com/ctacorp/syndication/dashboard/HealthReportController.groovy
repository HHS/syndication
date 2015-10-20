package com.ctacorp.syndication.dashboard

import com.ctacorp.syndication.MediaItemSubscriber
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.health.FlaggedMedia
import com.ctacorp.syndication.jobs.MediaValidationJob
import com.ctacorp.syndication.media.MediaItem
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

        [flaggedMediaItems:flaggedMediaItems,totalCount:flaggedMediaItems.totalCount] << getStabilityStats()
    }

    def emailTest(){
        def flaggedMediaItems = getFlaggedItems(false)
        render view:"emailReport", model:[flaggedMediaItems:flaggedMediaItems, totalCount:flaggedMediaItems.totalCount]
        return
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
        def ignoredItems = getFlaggedItems(true)

        [flaggedMediaItems:ignoredItems, totalCount:ignoredItems.totalCount] << getStabilityStats()
    }

    @Transactional
    def ignoreFlaggedMedia(FlaggedMedia fm){
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

    def getFlaggedItems(boolean ignored){
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
                order params.sort ?:"dateFlagged", params.order ?: "desc"
            }
            eq 'ignored', ignored
        }
    }

    private Map getStabilityStats(){
        def flaggedMediaItems = getFlaggedItems(false)
        def ignoredItems = getFlaggedItems(true)
        def totalItems = MediaItem.count()
        if(totalItems > 0){
            def stableItems = totalItems - flaggedMediaItems.totalCount - ignoredItems.totalCount
            def percentStable = Math.round(100 * (1 - (flaggedMediaItems.totalCount + ignoredItems.totalCount) / totalItems) as Float)
            def percentIgnored = Math.round(100 * (ignoredItems.totalCount / totalItems) as Float)
            def percentFlagged = Math.round(100 * (flaggedMediaItems.totalCount / totalItems) as Float)
            if(percentIgnored < 1){
                percentIgnored = 1
            }
            if(percentFlagged < 1){
                percentFlagged = 1
            }
            if(percentStable + percentIgnored + percentFlagged > 100 && percentIgnored > 0 && percentFlagged > 0) {
                percentStable --
            } else if(percentStable + percentIgnored + percentFlagged < 100 && percentIgnored >= 1 && percentFlagged >= 1){
                percentStable ++
            }
            [
                    percentStable:percentStable,
                    percentIgnored:percentIgnored,
                    percentFlagged:percentFlagged,
                    stableItems: stableItems,
                    flaggedCount:flaggedMediaItems.totalCount,
                    ignoredCount:ignoredItems.totalCount
            ]
        } else {
            [:]
        }

    }
}
