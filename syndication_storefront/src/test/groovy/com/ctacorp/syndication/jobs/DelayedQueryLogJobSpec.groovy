package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.audit.QueryAuditService
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class DelayedQueryLogJobSpec extends Specification {
    DelayedQueryLogJob job

    def setup(){
        job = new DelayedQueryLogJob()
        job.queryAuditService = Mock(QueryAuditService)
    }

    def "job should call the queryAuditService"() {
        given: "a queryString"
            String query = "This is a query"
        when: "the job is executed"
            def context = [mergedJobDataMap:[queryString:query]]
            job.execute(context)
        then: "the queryAuditService should be called with the supplied query"
            1 * job.queryAuditService.log(query)
    }

    def "queries bigger than 255 characters should get truncated"() {
        given: "a long query"
            String query = "a"*256
        when: "when the query is logged"
            def context = [mergedJobDataMap:[queryString:query]]
            job.execute(context)
        then: "the service should be called with a truncated query input"
            1 * job.queryAuditService.log("a"*255)
    }
}
