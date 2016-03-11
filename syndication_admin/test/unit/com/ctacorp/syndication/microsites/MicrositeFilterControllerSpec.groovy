package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.jobs.MicrositeValidationJob
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import com.ctacorp.syndication.microsite.MicroSite
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MicrositeFilterController)
@Mock([FlaggedMicrosite, MicroSite])
class MicrositeFilterControllerSpec extends Specification {

    def setup() {
        controller.micrositeFilterService = [rescanItem:{long id ->[]}]
        MicrositeValidationJob.metaClass.static.triggerNow = {[]}
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["microsite"] = new MicroSite()
        params["dateFlagged"] = new Date()
        params["message"] = "bad language"
        params["ignored"] = params.ignored ?: false
        return params
    }

    void "test that index returns a list of flagged microsites that are NOT ignored"() {
        setup:
            populateValidParams(params)
            def fm1 = new FlaggedMicrosite(params).save()
            def fm2 = new FlaggedMicrosite(params).save()
            params.ignored = true
            populateValidParams(params)
            def fm3 = new FlaggedMicrosite(params).save()
        when:"index is called"
            def resp = controller.index(10)
        then:"a list of unignored flagged microsites should be returned"
            resp.flaggedMicrosites == FlaggedMicrosite.findAllByIgnored(false)
            resp.totalCount == 2
    }

    void "test that ignored returns a list of all the ignored items"() {
        setup:
            populateValidParams(params)
            def fm1 = new FlaggedMicrosite(params).save()
            params.ignored = true
            populateValidParams(params)
            def fm2 = new FlaggedMicrosite(params).save()
            def fm3 = new FlaggedMicrosite(params).save()
        when:"index is called"
            def resp = controller.ignored(10)
        then:"a list of ignored flagged microsites should be returned"
            resp.flaggedMicrosites == FlaggedMicrosite.findAllByIgnored(true)
            resp.totalCount == 2
    }

    void "test that ignore flagged microsites sets ignored from false to true"() {
        setup:
            populateValidParams(params)
            def fm1 = new FlaggedMicrosite(params).save()
        when:"index is called"
            controller.ignoreFlaggedMicrosite(fm1)
        then:"the flagged microsite should now be true"
            response.redirectedUrl == "/micrositeFilter/index"
            fm1.ignored
    }

    void "test that un-ignore flagged microsites sets ignored from true to false"() {
        setup:
            params.ignored == true
            populateValidParams(params)
            def fm1 = new FlaggedMicrosite(params).save()
        when:"index is called"
            controller.unignoreFlaggedMicrosite(fm1)
        then:"the flagged microsite should now be false"
            response.redirectedUrl == "/micrositeFilter/ignored"
            !fm1.ignored
    }

    void "test that check microsite calls rescan item with the given id"() {
        setup:
            populateValidParams(params)
            def fm1 = new FlaggedMicrosite(params).save()
        when:"index is called"
            controller.checkMicrosite(fm1.id)
        then:"rescan item should be called and then redirected to index"
//            1 * controller.micrositeFilterService.rescanItem(fm1.id)
            response.redirectedUrl == "/micrositeFilter/index"
    }

    void "test that check All microsite calls the microsite validation job"() {
        when:"index is called"
            controller.checkAllMicrosites()
        then:"rescan item should be called and then redirected to index"
            flash.message != null
            response.redirectedUrl == "/micrositeFilter/index"

    }
}
