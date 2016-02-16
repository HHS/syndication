package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.social.TwitterStatusCollector

/**
 * Created by nburk on 1/8/16.
 */
class TwitterStatusCollectorJob {

    def twitterService

    static triggers = {
        cron name: 'twitterStatusCollectorTrigger', cronExpression: "0 0 0 ? * *" //Every Night
    }

    def execute(){
        log.info "Initiated Daily tweet auto importer job"
        def twitterStatusCollectors = TwitterStatusCollector.list()
        twitterStatusCollectors.each{ statusCollector ->
            twitterService.updateStatusCollection(statusCollector)
        }
    }
}
