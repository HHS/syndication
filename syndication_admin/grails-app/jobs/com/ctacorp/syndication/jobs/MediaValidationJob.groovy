package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.health.FlaggedMedia
import grails.util.Holders

/**
 * Created by nburk on 11/18/14.
 */
class MediaValidationJob {
    static triggers = {
        cron name: 'mediaValidationTrigger', cronExpression: "0 0 0 ? * SAT" //Every Saturday
    }

    def group = "MediaValidation"
    def description = "Validates all media Items at midnight"
    def mediaValidationService
    def mediaItemsService
    def mailService
    def grailsApplication

    def execute(context){
        if(Holders.config.syndication.disableHealthReportEmail){ //disable health report emails in staging
            return
        }
        def mailRecipiants = null
        def subscriberId = context.mergedJobDataMap.get('subscriberId')
        def flaggedItems = null
        log.info "Media validation job beginning execution."
        if(subscriberId){
            mediaValidationService.fullHealthScan(subscriberId)
            mailRecipiants =  User.findAllBySubscriberId(subscriberId as Long).username
            flaggedItems = mediaValidationService.getPublisherFlaggedMedia(subscriberId)
        } else {
            mediaValidationService.fullHealthScan()
            mailRecipiants = grailsApplication.config.SyndicationAdmin.healthReportEmailAddresses ?: "syndication@ctacorp.com"
            flaggedItems = mediaValidationService.getFlaggedMedia()
        }
        
        log.info "flagged media items: \n${flaggedItems}"

        mailService.sendMail {
            bcc mailRecipiants
            subject "Health Report"
            body( view:"/healthReport/emailReport",
                  model:[flaggedMediaItems:flaggedItems])
        }
    }
}