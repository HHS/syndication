package com.ctacorp.syndication.jobs

/**
 * Created by utank on 5/22/17.
 */
class ContentIndexJob {

    def elasticsearchService

    static triggers = { }

    def execute(ignore) {
        log.info 'Starting content re-index job...'
        elasticsearchService.fullReindex()
    }
}
