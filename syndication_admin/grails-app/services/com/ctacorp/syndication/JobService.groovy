package com.ctacorp.syndication

import com.ctacorp.syndication.jobs.UpdateSolrIndexJob

class JobService {
    static transactional = false

    def solrUpdate10SecondDelay(Long id){
        if(id){
            UpdateSolrIndexJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: id])
        }
    }
}
