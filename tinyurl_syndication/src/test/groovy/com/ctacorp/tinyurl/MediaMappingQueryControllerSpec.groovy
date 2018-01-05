package com.ctacorp.tinyurl

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MediaMappingQueryController)
@Build(MediaMapping)
class MediaMappingQueryControllerSpec extends Specification {

     void "Test invalid targeturl gives error"() {
        when:"Invalid targeturl gives"
            controller.getByTargetUrl('')
        then:"Return correct media"
            response.getText().contains('error')
     }

    void "Test targeturl gives correct media"() {
        setup:
            def mediaMapping = MediaMapping.build(targetUrl:'http://example.com')
        when:"getByTargetUrl is called"
            request.format = "json"
            controller.getByTargetUrl('http://example.com')
        then:"It should produce a valid json reponse"
            response.status == 200
            response.json
        and:"The response should contain the expected URL"
            response.json.targetUrl == "http://example.com"
    }

    void "Test invalid syndicationId gives error"() {
        when:"Invalid syndicationId gives"
            controller.getBySyndicationId(0)
        then:"Return correct media"
            response.getText().contains('error')
    }

    void "Test SyndicationId gives correct media"() {
        setup:
            def mediaMapping = MediaMapping.build(syndicationId:100)
        when:"getBySyndicationId is called"
            request.format = "json"
            controller.getBySyndicationId(100)
        then:"It should produce a valid json reponse"
            response.status == 200
            response.json
        and:"The response should contain the expected URL"
            response.json.syndicationId == 100
    }

    void "Test invalid tinyUrl gives error"() {
        setup:
            String.metaClass.decodeBase32 = {-> Long.parseLong('a', 32)}
        when:"Invalid Token gives"
            controller.getByTinyUrl(Holders.config.TINYURL_SERVER_URL + '/zzz')
        then:"Return correct media"
            response.getText().contains('error')
    }

    void "Test tinyurl gives correct media"() {
        setup:
            def mediaMappingToken = MediaMapping.build(targetUrl:'http://example.com')
            Long.metaClass.encodeAsBase32 = {-> toString(longValue(), 32)}
            String.metaClass.decodeBase32 = {-> Long.parseLong(mediaMappingToken.token, 32)}
        when:"getByToken is called"
            request.format = "json"
            controller.getByTinyUrl(Holders.config.TINYURL_SERVER_URL + '/' + mediaMappingToken.token)
        then:"It should produce a valid json reponse"
            response.status == 200
            response.json
        and:"The response should contain the expected URL"
            response.json.id == 1
    }

}
