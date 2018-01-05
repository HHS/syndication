package syndication_admin


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserRoleInterceptor)
class UserRoleInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test userRole interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller: "user", action: 'show')
            withRequest(controller: 'user', action: 'edit')
            withRequest(controller: 'user', action: 'update')
            withRequest(controller: 'user', action: 'delete')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
