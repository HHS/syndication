package syndication


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ResponseRendererInterceptor)
class ResponseRendererInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test responseRenderer interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"media")
            withRequest(controller: 'tags')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
