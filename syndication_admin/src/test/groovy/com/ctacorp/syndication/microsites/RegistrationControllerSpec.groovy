package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.microsite.MicrositeRegistration
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationController)
@Mock([MicrositeRegistration])
class RegistrationControllerSpec extends Specification {

    def registrationService = Mock(RegistrationService)

    def setup() {
        controller.registrationService = registrationService
    }

    def populateValidParams(params) {
        assert params != null
        //mediaItem required attributes
        params["organization"] = "ABC"
        params["description"] = "I love microsites"
        params["verified"] = false
        params["user"] = new User()
        return params
    }

    def cleanup() {
    }

    void "test that index returns a list of the microsite registration forms"() {
        setup:
            populateValidParams(params)
            def mr = new MicrositeRegistration(params).save()
        when:"index is called"
            def resp = controller.index(10)
        then:"a microsite registration list should be returned"
            resp.registrationList == MicrositeRegistration.list()
            resp.registrationCount == 1
            resp.max == 10
    }

    void "test that the show method takes you to the registration's show page"() {
        setup:
            populateValidParams(params)
            def mr = new MicrositeRegistration(params).save()
        when:"the show method is called"
            controller.show(mr)
        then:"the show view should be rendered"
            view == "/registration/show"
            model.registrationInstance == mr

    }

    void "test that calling update with an invalid instance returns you to the registration index"() {
        when:"calling update with invalid instance"
            controller.update(new MicrositeRegistration())
        then:"you should get redirected to the index"
            flash.error
            response.redirectedUrl == "/registration/index"
    }

    void "test that calling update changes the value of verified if change access exists"(){
        setup:
            populateValidParams(params)
            def mr = new MicrositeRegistration(params).save(flush:true)
            controller.registrationService = [changeAccess: {MicrositeRegistration mr2 -> mr2.verified = true; mr2}]

        when:"the update method is called"
            params.changeAccess = true
            controller.update(mr)
        then:"change access method should be called"
            mr.verified
            view == "/registration/show"
            model.registrationInstance == mr
    }

    void "test that delete deletes the registration instance"(){
        setup:
            populateValidParams(params)
            def mr = new MicrositeRegistration(params).save()
            def oldCount = MicrositeRegistration.count()
        when:"the delete method is called"
            controller.delete(mr)
        then:"there should be MicrositeRgistration instances"
            MicrositeRegistration.count() == 0
            oldCount == 1
            response.redirectedUrl == "/registration/index"
            flash.message != null
    }

    void "test that delete redirects to index with an invalid instance"() {
        when:"the delete method is called"
            controller.delete(new MicrositeRegistration())
        then:"there should be a flash error and redirect to index"
            response.redirectedUrl == "/registration/index"
            flash.error != null
            flash.message == null
    }
}
