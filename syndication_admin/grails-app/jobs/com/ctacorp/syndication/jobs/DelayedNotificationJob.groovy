package com.ctacorp.syndication.jobs

class DelayedNotificationJob {
    def cmsService

    def execute(context) {
        def msg = context.mergedJobDataMap.get('msg')
        if(msg) {
            cmsService.sendMessage(msg)
        }
    }
}
