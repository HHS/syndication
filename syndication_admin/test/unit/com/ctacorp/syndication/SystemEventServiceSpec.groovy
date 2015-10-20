package com.ctacorp.syndication

import com.ctacorp.syndication.audit.SystemEvent
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SystemEventService)
@Mock([SystemEvent])
class SystemEventServiceSpec extends Specification {

    def recentListCalled = false
    def listEventsCalled = false

    def setup() {

        Math.metaClass.static.min = {int val1, int val2 ->  listEventsCalled = true;20}
        LinkedHashMap.metaClass.static.int = {String str -> 20}

        SystemEvent.metaClass.'static'.list = {Map map ->
            if(map.max == 10 && map.sort == 'dateCreated' && map.order == 'desc'){
                recentListCalled = true
            } else if(map != [max:20] as Map || !listEventsCalled) {
                listEventsCalled = false
            }
        }

        (1..15).each{
            new SystemEvent([name:"name${it}", type:"any", message:"explanation", messageDetails:"details"]).save()
        }
    }

    def cleanup() {
    }

    void "test list recent events"() {
        when:"listRecentEvents is called"
            service.listRecentEvents()

        then:"system.list should be called"
            recentListCalled

    }

    void "test list events"() {
        when:"listRecentEvents is called"
            service.listEvents([max:20])

        then:"system.list should be called"
            listEventsCalled

    }
}
