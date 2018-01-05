/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.tagcloud.constraint

import com.ctacorp.syndication.Language
import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import tagcloud.domain.Tag
import tagcloud.domain.TagType

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Tag)
@Mock(Tag)
@Build([Tag, TagType, Language])
class TagConstraintsSpec extends Specification {

    def cleanup() {
    }

    void "a valid instance should be valid"() {
        expect: "a valid instance should validate"
            Tag.build().validate() == true
    }

    void "invalid nulls shouldn't be allowed"(){
        given: "a valid instance"
            Tag tag = Tag.build()
        when: "non-nullable fields are set to null"
            tag.name = null
            tag.type = null
            tag.language = null
        then: "the instance shouldn't validate"
            tag.validate() == false
        and: "there should be three errors"
            tag.errors.errorCount == 3
        and: "the errors should be nullable for each field"
            tag.errors["name"].code == "nullable"
            tag.errors["type"].code == "nullable"
            tag.errors["language"].code == "nullable"
    }

    void "blank values shouldn't be allowed"(){
        given: "a valid Tag instance"
            def instance_Tag = Tag.build()

        when: "name is blanked out"
            instance_Tag.name = ""

        then: "the instance shouldn't validate"
            instance_Tag.validate() == false

        and: "there should be 1 errors"
            instance_Tag.errors.errorCount == 1

        and: "the errors should be blank"
            instance_Tag.errors["name"].code == "blank"
    }

    void "names should be unique"(){
        given: "a valid Tag instance"
            def instance_Tag = Tag.build()
            def instance_Tag2 = Tag.build()
            def tagType = TagType.build()
            def language = Language.build()

        when: "two instances have the same name"
            instance_Tag.name = "a"
            instance_Tag.type = tagType
            instance_Tag.language = language
            instance_Tag2.name = "a"
            instance_Tag2.type = tagType
            instance_Tag2.language = language
            instance_Tag.save(flush:true)
            instance_Tag2.save(flush:true)

        then: "the second instance shouldn't validate"
            instance_Tag2.validate() == false

        and: "there should be 1 errors"
            instance_Tag2.errors.errorCount == 1

        and: "the error should be unique"
            instance_Tag2.errors["name"].code == "unique"
    }

    void "names that are too long shouldn't be allowed"(){
        given: "a valid Tag instance"
            def instance_Tag = Tag.build()

        when: "name is too big"
            instance_Tag.name = "a"*256

        then: "the instance shouldn't validate"
            instance_Tag.validate() == false

        and: "there should be 1 errors"
            instance_Tag.errors.errorCount == 1

        and: "the errors should be maxSize"
            instance_Tag.errors["name"].code == "maxSize.exceeded"
    }

    void "Names that are the right size should be valid"(){
        given: "a valid Tag instance"
            def instance_Tag = Tag.build()

        when: "the name is under the size limit"
            instance_Tag.name = "a"*255

        then: "the instance should validate"
            instance_Tag.validate() == true

        and: "There should be no errors"
            instance_Tag.errors.errorCount == 0
    }
}