package com.ctacorp.syndication.jobs

/**
 * Created by nburk on 11/4/14.
 */
class ReindexMediaJob {
    static triggers = {}
    def solrIndexingService
    def tagService

    def execute(context){
        String type = context.mergedJobDataMap.get('type')
        switch(type){
            case "mediaItems": solrIndexingService.inputAllMedia(context.mergedJobDataMap.get('subscriberId'))
                break
            case "sources": solrIndexingService.inputAllSources()
                break
            case "campaigns": solrIndexingService.inputAllCampaigns(context.mergedJobDataMap.get('subscriberId'))
                break
            case "tags": def tags
                        def tagInfo = tagService.listAllTags(context.mergedJobDataMap.get('params'))
                        tags = tagInfo.tags
                        tags.each { tag ->
                            solrIndexingService.inputTag(String.valueOf(tag.id), tag.name)
                        }
                break
        }
    }
}
