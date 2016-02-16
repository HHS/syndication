package com.ctacorp.syndication.media

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created by sgates on 12/29/15.
 */
@TestFor(QuestionAndAnswer)
@Build([QuestionAndAnswer])
@Mock([QuestionAndAnswer])
class QuestionAndAnswerConstraintsSpec extends Specification {
    def setup(){
        mockForConstraintsTests(QuestionAndAnswer)
    }

    def "a valid instance should be valid"() {
        given: "a valid questionAndAnswer instance"
            def questionAndAnswerInstance = QuestionAndAnswer.build()
        when: "instance is validated"
            def valid = questionAndAnswerInstance.validate()
        then: "the instance should be valid"
            valid
    }

    def "non-nullable fields shouldn't be allowed"() {
        given: "an instance with nulled fields"
            def questionAndAnswerInstance = QuestionAndAnswer.build()
            questionAndAnswerInstance.answer = null
        when: "the instance is validated"
            def valid = questionAndAnswerInstance.validate()
        then: "it should be invalid"
            ! valid
        and: "there should be 1 errors"
            questionAndAnswerInstance.errors.errorCount == 1
    }

    def "blank fields shouldn't be allowed"() {
        given: "an instance with blank fields"
            def questionAndAnswerInstance = QuestionAndAnswer.build()
            questionAndAnswerInstance.answer = ""
        when: "the instance is validated"
            def valid = questionAndAnswerInstance.validate()
        then: "it should be invalid"
            ! valid
        and: "there should be 1 errors"
            questionAndAnswerInstance.errors.errorCount == 1
        and: "the errors should be blank"
            questionAndAnswerInstance.errors["answer"] == "blank"
    }

    def "fields that are too long shouldn't be allowed"() {
        given: "an instance with long fields"
            def questionAndAnswerInstance = QuestionAndAnswer.build()
            questionAndAnswerInstance.answer = "a"*2001
        when: "the instance is validated"
            def valid = questionAndAnswerInstance.validate()
        then: "it should be invalid"
            ! valid
        and: "there should be 1 errors"
            questionAndAnswerInstance.errors.errorCount == 1
        and: "the errors should be blank"
            questionAndAnswerInstance.errors["answer"] == "maxSize"
    }

    def "fields that are under the length limit should be allowed"() {
        given:"an instance with valid length fields"
            def questionAndAnswerInstance = QuestionAndAnswer.build()
            questionAndAnswerInstance.answer = "a"*2000
        when: "the instance is validated"
            def valid = questionAndAnswerInstance.validate()
        then: "it should be valid"
            valid
        and: "there should be no errors"
            !questionAndAnswerInstance.hasErrors()
    }
}