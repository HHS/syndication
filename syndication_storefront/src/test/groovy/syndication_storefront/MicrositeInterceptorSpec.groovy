package syndication_storefront


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MicrositeInterceptor)
class MicrositeInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test microsite interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"microsite")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
