package syndication


import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SecurityInterceptor)
class SecurityInterceptorSpec extends Specification {

    def setup() {
        Holders.config.putAt('SYNDICATION_CMS_AUTH_ENABLED', 'true')
    }

    def cleanup() {

    }

    void "Test security interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller: 'admin')
            withRequest(controller: 'cacheAccess')
            withRequest(controller: 'media', action: 'saveCollection')
            withRequest(controller: 'media', action: 'saveFAQ')
            withRequest(controller: 'media', action: 'saveHtml')
            withRequest(controller: 'media', action: 'saveImage')
            withRequest(controller: 'media', action: 'saveInfographic')
            withRequest(controller: 'media', action: 'savePDF')
            withRequest(controller: 'media', action: 'saveQuestionAndAnswer')
            withRequest(controller: 'media', action: 'saveTweet')
            withRequest(controller: 'media', action: 'saveVideo')

        then:"The interceptor does match"
            interceptor.doesMatch()

        when: "action is swaggerui and controller is admin"
            withRequest(controller: 'admin', action: "swaggerUi")

        then:
            !interceptor.doesMatch()
    }
}
