package syndication.audit

import com.ctacorp.syndication.audit.SystemEvent
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SystemEventService)
@Mock(SystemEvent)
class SystemEventServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    // systemStarted()---------------------------------------------------------
    void "systemStarted() should return a SystemEvent object"() {
        when: "systemStarted() is called"
        SystemEvent se = service.systemStarted()
        then: "se should not be null"
        se
    }

    void "systemStarted() should return the correct type of event"() {
        when: "systemStarted() is called"
        SystemEvent se = service.systemStarted()
        then: "se type should be 'system_start'"
        se.type == "system_start"
    }

    void "systemStarted() should save a SystemEvent object"() {
        when: "systemStarted() is called"
        SystemEvent se = service.systemStarted()
        then: "se should have an id"
        se.id
    }

    // systemShutdown()---------------------------------------------------------
    void "systemShutdown() should return a SystemEvent object"() {
        when: "systemShutdown() is called"
        SystemEvent se = service.systemShutdown()
        then: "se should not be null"
        se
    }

    void "systemShutdown() should return the correct type of event"() {
        when: "systemShutdown() is called"
        SystemEvent se = service.systemShutdown()
        then: "se type should be 'system_stop'"
        se.type == "system_stop"
    }

    void "systemShutdown() should save a SystemEvent object"() {
        when: "systemShutdown() is called"
        SystemEvent se = service.systemShutdown()
        then: "se should have an id"
        se.id
    }
}
