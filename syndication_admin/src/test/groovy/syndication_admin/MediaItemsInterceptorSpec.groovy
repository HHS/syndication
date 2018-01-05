package syndication_admin


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MediaItemsInterceptor)
class MediaItemsInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test mediaItems interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"collection", action:'show')
            withRequest(controller:"html", action:'edit')
            withRequest(controller:"image", action:'update')
            withRequest(controller:"infographic", action:'delete')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
