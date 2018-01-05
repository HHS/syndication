package com.ctacorp.tagcloud.controller

import spock.lang.Specification
import tagcloud.TagTypesController
import tagcloud.domain.TagType

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class TagTypesControllerSpec extends Specification {

    def setup() {
        new TagType(name: "OtherType", description: "Some other tag type").save(flush: true)
    }

    def cleanup() {
    }

    def "(Sanity Check) there should be instances of required objects in the DB"() {
        expect: "There should be the expected number of each object type before we test"

            TagType.list().each{
                println "| ${it} ${it.id}"
            }

            TagType.list().size() == 4

            TagType.get(1).name == "General"
            TagType.get(1).description == "Default tag type for general purpose tagging."

            TagType.get(2).name == "Topic"
            TagType.get(2).description == "Classification."

            TagType.get(3).name == "Audience"
            TagType.get(3).description == "Targeted group of people."
    }

    def "listTagTypes should list the types of tags in the system"(){
        given: "a tagsController"
            TagTypesController controller = new TagTypesController()
        when: "index action is called"
            controller.request.method = "GET"
            controller.index()
        then: "there should be a list of tagTypes returned"
            def j = controller.response.json
            j != null
        and: "the list should contain our expected tag types"
            j*.name.containsAll(["OtherType", "Topic", "Audience", "General"])
    }
}
