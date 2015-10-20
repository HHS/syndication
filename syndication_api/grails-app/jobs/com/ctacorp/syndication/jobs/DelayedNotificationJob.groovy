package com.ctacorp.syndication.jobs

class DelayedNotificationJob {
    def queueService

    def execute(context) {
        def msg = context.mergedJobDataMap.get('msg')
        if(msg) {
            queueService.sendMessage(msg)
        }
    }
}