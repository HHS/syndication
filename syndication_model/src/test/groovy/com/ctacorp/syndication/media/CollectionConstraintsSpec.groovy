package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import com.ctacorp.syndication.media.Collection

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Build(Collection)
class CollectionConstraintsSpec extends Specification {

    void "valid instance should be valid"() {
        given: "A valid collection instance"
            def collectionInstance = Collection.build()

        when: "the instance is validated"
            collectionInstance.validate()

        then: "the instance should not have any errors"
            collectionInstance.errors.errorCount == 0
    }
}
