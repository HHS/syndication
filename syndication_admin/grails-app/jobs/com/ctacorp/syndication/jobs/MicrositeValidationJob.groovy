package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 12/1/15.
 */
class MicrositeValidationJob {
    static triggers = {
        cron name: 'mediaValidationTrigger', cronExpression: "0 0 0 ? * *" //Every Night
    }

    def micrositeFilterService

    def execute(){
        micrositeFilterService.scanAllMicrosites()
    }
}
