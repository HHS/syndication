package tinyurl_syndication


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
            withRequest(controller:'mediaMappingRest', action:'save')
            withRequest(controller:'mediaMappingRest', action:'update')
            withRequest(controller:'mediaMappingRest', action:'delete')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }

    /*void "Test InValid Authorization"(){
        when: "Authorization header is not set"
            withRequest(controller:'mediaMappingRest', action:'save')
            interceptor.request.addHeader("Authorization","")

        then:"status should be 400"
            response.status == 400
            !interceptor.before()
    }*/
}
