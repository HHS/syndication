package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Html

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Html)
class HtmlConstraintsSpec extends Specification {

    void "valid instance should be valid"() {
        given: "A valid html instance"
            def htmlInstance = Html.build()

        when: "the instance is validated"
            htmlInstance.validate()

        then: "the instance should not have errors"
            htmlInstance.errors.errorCount == 0
    }
}
