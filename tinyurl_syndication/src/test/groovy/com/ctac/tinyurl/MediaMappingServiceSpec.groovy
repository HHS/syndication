package com.ctac.tinyurl

import com.ctacorp.tinyurl.MediaMapping
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(MediaMappingService)
@Build(MediaMapping)
@Mock(MediaMapping)
class MediaMappingServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    void "Save media"(){
        setup:
            def mediamapping = new MediaMapping([syndicationId:1, targetUrl:'http://www.google.com'])
            def mediamapping1 = new MediaMapping([syndicationId:1, targetUrl:'http://www.google.co.in'])

        when:"pass valid instance"
            def instance = service.saveMediaMapping(mediamapping)

        then:"should return saved media mapping instance"
            instance.id == 1 as Long

        when: "pass existing instance"
            def instance2 = service.saveMediaMapping(mediamapping1)

        then:"should return same media mapping instance"
            instance2.id == 1 as Long

    }


    void "Check bulkMapping in MediaMappingService with empty list"() {
        when: "list is empty"
            def response = service.bulkMapping([])
        then: "It should return blank map"
            response == []

    }

    void "Check bulkMapping in MediaMappingService with non-empty list"() {
        when: "list is not empty"
            def response = service.bulkMapping([[syndicationId: 1, guid:1, targetUrl:"http://www.hhs.gov"], [syndicationId:2, guid:2, targetUrl:"http://www.usa.gov"]])
        then: "It should return blank map"
            response.size() == 2


    }
}
