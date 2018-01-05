package com.ctacorp.tagcloud.constraint

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import tagcloud.domain.ContentItem


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/8/14
 * Time: 10:44 PM
 */
@Mock(ContentItem)
@Build(ContentItem)
@TestFor(ContentItem)
class ContentItemConstraintsSpec extends Specification {
    void "a valid instance should be valid"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "the instance is validated"
            instance_ContentItem.validate()

        then: "there should be no errors"
            instance_ContentItem.errors.errorCount == 0
    }

    void "nullable fields should be nullable"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "nullable fields are set to null"
            instance_ContentItem.externalUID = null
            instance_ContentItem.syndicationId = null

        then: "the instance should validate"
            instance_ContentItem.validate() == true

        and: "There should be no errors"
            instance_ContentItem.errors.errorCount == 0

    }

    void "non nullable fields shouldn't be allowed"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "non nullable fields are nulled out"
            instance_ContentItem.url = null

        then: "the instance shouldn't validate"
            instance_ContentItem.validate() == false

        and: "there should be 1 errors"
            instance_ContentItem.errors.errorCount == 1

        and: "the errors should be nullable"
            instance_ContentItem.errors.fieldError.code == "nullable"
    }

    void "blank fields shouldn't be allowed"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "url and externalUID fields are blanked"
            instance_ContentItem.externalUID = ""
            instance_ContentItem.url = ""

        then: "the instance shouldn't validate"
            instance_ContentItem.validate() == false

        and: "there should be 2 errors"
            instance_ContentItem.errors.errorCount == 2

        and: "the errors should be blank"
            instance_ContentItem.errors["url"].code == "blank"
            instance_ContentItem.errors["externalUID"].code == "blank"

    }

    void "invalid urls shouldn't be allowed"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "the url is set to something other than a url"
            instance_ContentItem.url = "not a url"

        then: "the instance should not validate"
            instance_ContentItem.validate() == false

        and: "there should be 1 errors"
            instance_ContentItem.errors.errorCount == 1

        and: "the errors should be url"
            instance_ContentItem.errors["url"].code == "url.invalid"
    }

    void "URLs that are too long shouldn't be allowed"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem = ContentItem.build()

        when: "url is too big"
            instance_ContentItem.url = "http://www.${'a'*1986}.com"

        then: "the instance should be invalid"
            instance_ContentItem.validate() == false

        and: "there should be 1 errors"
            instance_ContentItem.errors.errorCount == 1

        and: "the errors should be maxSize"
                instance_ContentItem.errors["url"].code == "maxSize.exceeded"
    }

    void "unique fields should be unique"(){
        given: "a valid ContentItem instance"
            def instance_ContentItem1 = ContentItem.build()
            def instance_ContentItem2 = ContentItem.build()

        when: "two instances share a syndication id"
            instance_ContentItem1.syndicationId = 1
            instance_ContentItem1.save(flush:true)
            instance_ContentItem2.syndicationId = 1

        then: "the second instance shouldn't validate"
            instance_ContentItem2.validate() != true

        and: "there should be 1 errors"
            instance_ContentItem2.errors.errorCount == 1

        and: "the errors should be unique"
            instance_ContentItem2.errors["syndicationId"].code == "unique"
    }
}