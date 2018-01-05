package cms.manager


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AuthorizationInterceptor)
class AuthorizationInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test authorization interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"authorization")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
