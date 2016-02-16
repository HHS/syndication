package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.audit.SearchQuery
import com.ctacorp.syndication.metric.MediaMetric
import groovy.sql.Sql

/**
 * Created by nburk on 8/24/15.
 * Finds rows that appear more than once for the same media_id and date and combines them into a single row.
 * The extra row tends to happen from a race condition where the metrics get incremented.
 */
class MediaMetricCleanupJob {
    static triggers = {
        cron name: 'mediaMetricCleanup', cronExpression: "0 0 0 ? * *" //Every night at midnight
    }

    def mediaItemsService
    def dataSource

    def execute() {
        log.info "Initiated Daily Media Metric Cleanup job"
        def sql = new Sql(dataSource)
        MediaMetric.withTransaction{ status ->
            try{
                def yesterday = new Date() - 1
                def duplicateMediaMetrics = sql.rows("SELECT *, COUNT(*) FROM media_metric WHERE media_metric.day <= :yesterday GROUP BY media_id, day HAVING COUNT(*) > 1",[yesterday:yesterday.format('yyyy-MM-dd')])
                duplicateMediaMetrics.each{ metric ->
                    def searches = sql.rows("SELECT SUM(api_view_count) AS apiViewCount,SUM(storefront_view_count) AS storefrontViewCount,media_id AS media,day AS day FROM media_metric WHERE day = ${metric.day} and media_id = ${metric.media_id}")

                    def tempSearchObjects = searches as MediaMetric[]
                    SearchQuery.executeUpdate("DELETE MediaMetric m WHERE m.day = :day AND m.media.id = :mediaId",[day:metric.day.clearTime(),mediaId:metric.media_id])
                    tempSearchObjects.each{query ->
                    if(!query.save()){
                        log.error "Could not save query for media item: ${metric.media_id} with date: ${metric.day}"
                        status.setRollbackOnly()
                    }
                    }
                }
            } catch(e){
                log.error(e)
                log.error("could not properly combine Media Metirc rows for the date of : " + (new Date() - 1).clearTime())
            }
        }
    }
}
