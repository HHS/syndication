package com.ctacorp.syndication.jobs

class DelayedQueryLogJob {
    def queryAuditService

    def execute(context) {
        String queryString = context.mergedJobDataMap.get('queryString')
        if(queryString.size() > 255){
            queryString = queryString[0..254]
        }
        queryAuditService.log(queryString)
    }
}
