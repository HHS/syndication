package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 2/23/16.
 */
class DelayedTaggingJob {

    def tagsService

    def execute(context) {
        def instanceId = context.mergedJobDataMap.get('mediaId')
        log.info("begin tag media job for mediaID ${instanceId}")
        def requestJson = context.mergedJobDataMap.get('requestJson')
        def methodName = context.mergedJobDataMap.get('methodName')

        switch(methodName){
            case("tagMedia"):tagsService.tagMedia(instanceId, requestJson)
                break;
            case("tagMediaItemByNames"):tagsService.tagMediaItemByNames(instanceId, requestJson)
                break;
            case("tagMediaItemByNamesAndLanguageAndType"):
                tagsService.tagMediaItemByNamesAndLanguageAndType(instanceId, requestJson)
                break;
        }

    }
}
