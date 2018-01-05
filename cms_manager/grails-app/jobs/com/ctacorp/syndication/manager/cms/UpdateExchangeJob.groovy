package com.ctacorp.syndication.manager.cms

/**
 * Created by nburk on 5/2/17.
 */
class UpdateExchangeJob {

    def authorizationService

    def execute(context) {
        def url = context.mergedJobDataMap.get('url')
        def message = context.mergedJobDataMap.get('message')
        authorizationService.post(url, message)
    }
}
