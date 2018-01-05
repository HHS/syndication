package syndication_admin


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HealthReportInterceptor)
class HealthReportInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test healthReport interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"healthReport", action: 'checkMediaItem')
            withRequest(controller:"healthReport", action: 'ignoreFlaggedMedia')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
