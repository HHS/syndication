package cms.manager


import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SecurityInterceptor)
class SecurityInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }
    def filteredUri = '/api/v1/**'

    void "Test security interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(uri:filteredUri)

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
