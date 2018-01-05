package syndication_api


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AddHeaderInterceptor)
class AddHeaderInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test addHeader interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"addHeader")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
