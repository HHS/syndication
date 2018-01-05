package tag_cloud


import grails.test.mixin.TestFor
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

    void "Test security interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"tags", action: 'save')
            withRequest(controller:"tags", action: 'update')
            withRequest(controller:"languages", action: 'delete')
            withRequest(controller:"languages", action: 'set')
            withRequest(controller:"tagTypes", action: 'tag')
            withRequest(controller:"tagTypes", action: 'findOrSaveTag')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
