package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.mq.QueueService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
class DelayedNotificationJobSpec extends Specification {

    def queueService = Mock(QueueService)

    def setup() {
    }

    def cleanup() {
    }

    void "test the execute"() {
        setup:""
            def job = new DelayedNotificationJob()
            def queCalled = false
            job.queueService = queueService
            job.queueService.metaClass.sendMessage = {String msg -> queCalled = true}

        when:"the job is executed"
            def message = [mergedJobDataMap: new DataMap()]
           job.execute(message)

        then:"the proper methods are called"
            queCalled
    }

     class DataMap{
         def get(String msg){
                return "hello"
         }
    }
}

