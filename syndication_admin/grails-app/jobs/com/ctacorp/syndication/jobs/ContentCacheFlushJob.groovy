package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.exception.ContentUnretrievableException
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication_elasticsearch_plugin.ElasticsearchJob
import org.quartz.JobExecutionContext

class ContentCacheFlushJob {

    static final BATCH_SIZE = 50

    def contentRetrievalService
    def mediaPreviewThumbnailJobService
    def elasticsearchService

    static triggers = {}

    void execute(JobExecutionContext context) {

        int offset = 0


        while({

            int totalItemsFound = 0

            def mediaItems = []
            def cachedMediaItemIds = []

            CachedContent.withNewSession {

                mediaItems = MediaItem.list([offset: offset, max: BATCH_SIZE])
                def cachedContentList = cacheBatch(mediaItems)

                cachedMediaItemIds = cachedContentList.collect { it.mediaItem.id }.each { mediaItemId ->
                    mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(mediaItemId)
                }
            }

            if(cachedMediaItemIds) {
                ElasticsearchJob.triggerNow([command: ElasticsearchJob.BULK_REINDEX, mediaItemIds: cachedMediaItemIds])
            }

            totalItemsFound = mediaItems.size()
            offset += BATCH_SIZE
            totalItemsFound == BATCH_SIZE

        }()) continue
    }

    List<CachedContent> cacheBatch(List<MediaItem> mediaItems = []) {

        def cachedContentList = []

        mediaItems.each {

            try {

                def extractionResult = contentRetrievalService.extractSyndicatedContent(it.sourceUrl, [disableFailFast:true])
                String content = extractionResult.extractedContent

                if(content) {

                    CachedContent cached = CachedContent.findByMediaItem(it)

                    if(cached) {

                        if(cached.content != content) {

                            cached.content = content
                            cachedContentList << cached
                        }

                    } else {
                        
                        cached = new CachedContent(mediaItem: it, content: content)
                        cachedContentList << cached
                    }
                }

            } catch(ContentUnretrievableException e) {
                log.info("ContentUnretrievableException: ${e.getMessage()}")
            } catch(e) {
                log.error("error occurred when caching content for media item ${it.id}", e)
            }
        }

        cachedContentList.each { it.save() }
    }
}
