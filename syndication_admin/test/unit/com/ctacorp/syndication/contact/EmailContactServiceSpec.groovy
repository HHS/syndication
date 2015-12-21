package com.ctacorp.syndication.contact

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(EmailContactService)
@Mock([EmailContact])
class EmailContactServiceSpec extends Specification {

    def setup() {
        def c1 = new EmailContact(name:"some guy", email:"someEmail@example.com").save(flush:true)
        def c2 = new EmailContact(name:"another guy", email:"anotherGuy@example.com").save(flush:true)
        def c3 = new EmailContact(name:"some girl", email:"someGirl@example.com").save(flush:true)
    }

    def "sanity check"() {
        expect: "there should be 3 existing contacts"
            EmailContact.count() == 3
    }

    def "saveEmailContact should persist a valid instance"(){
        given: "a valid EmailContact instance"
            EmailContact ec = new EmailContact(name:"myName", email:"myEmail@example.com")
        when: "save is called"
            ec = service.saveEmailContact(ec)
        then: "ec should have an id"
            ec.id
        and: "the count should be 1 higher"
            EmailContact.count() == 4
    }

    def "saveEmailContact should not persist and invalid instance and should return errors"() {
        given: "an invalid instance"
            EmailContact ec = new EmailContact()
        when: "save is called"
            ec = service.saveEmailContact(ec)
        then: "the instance should not have an ID"
            !ec.id
        and: "there should still be only 3 contacts"
            EmailContact.count() == 3
        and: "there should be errors in the instance"
            ec.hasErrors()
    }

    def "getEmailContact should return the expected instance"() {
        when: "the getEmailContact method is called with the expected id"
            EmailContact ec = service.getEmailContact(1L)
        then: "instances should be returned when asked for"
            ec.id == 1L
        and: "the name should be the expected one"
            ec.name == "some guy"
        and: "the email should match"
            ec.email == "someEmail@example.com"
    }

    def "getEmailContact should return null when no instance is found"(){
        when: "the getEmailContact method is called with an invalid id"
            EmailContact ec = service.getEmailContact(1000)
        then: "no instance should be returned"
            !ec
    }

    def "search should find instances by name"() {
        given: "the name of a contact"
            def name = "some guy"
        when: "search is called"
            def results = service.search(name)
        then: "it should find the expected record"
            EmailContact.read(1) in results
    }

    def "search should find instances by partial name"() {
        given: "the partial name of a contact"
            def name = "some"
        when: "search is called"
            def results = service.search(name)
        then: "it should find the expected record"
            EmailContact.read(1) in results
    }

    def "search should find instances by email"() {
        given: "the email of a contact"
            def email = "someEmail@example.com"
        when: "search is called"
            def results = service.search(email)
        then: "it should find the expected record"
            EmailContact.read(1) in results
    }

    def "search should find instances by partial email"() {
        given: "the partial email of a contact"
            def email = "Guy"
        when: "search is called"
            def results = service.search(email)
        then: "it should find the expected record"
            EmailContact.read(2) in results
    }
}
