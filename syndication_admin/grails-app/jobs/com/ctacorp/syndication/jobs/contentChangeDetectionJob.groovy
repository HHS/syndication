package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 2/17/15.
 */
class contentChangeDetectionJob {
    static triggers = {
        cron name: 'mediaValidationTrigger', cronExpression: "0 0 0 ? * *" //Every night at midnight
    }
    
    def mediaItemsService

    def execute(){
        mediaItemsService.scanContentForUpdates()
    }
}
