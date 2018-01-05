package syndication_admin

import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.CampaignService
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.MediaItem
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CampaignInterceptor)
@Mock(Campaign)

class CampaignInterceptorSpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["id"] = '1'

    }

    void "Test campaign interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller: 'campaign', action: 'show')
            withRequest(controller: 'campaign', action: 'edit')
            withRequest(controller: 'campaign', action: 'update')
            withRequest(controller: 'campaign', action: 'delete')

        then:"The interceptor does match"
            interceptor.doesMatch()
    }

    void "Test ValidPublisher"(){
        setup:
            def campaignService = Mock(CampaignService)
            campaignService.publisherValid(_) >> true
            interceptor.campaignService = campaignService
        when:"Given invalid Campaign id"
            interceptor.before()

        then:"returns 404 status"
            1 * interceptor.campaignService.publisherValid(_)
    }
}
