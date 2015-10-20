package com.ctacorp.syndication

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PasswordService)
class PasswordServiceSpec extends Specification {
    def "validate valid password"() {
        given:"valid passwords"
            String pass1 = "Password1"
            String pass2 = "1asSword"
            String pass3 = "jdkjfD0000"

        when:"validate password is called"
            def valid1 = service.validatePassword(pass1,pass1)
            def valid2 = service.validatePassword(pass2,pass2)
            def valid3 = service.validatePassword(pass3,pass3)
        then:"they should all be true"
            valid1.valid
            valid2.valid
            valid3.valid
    }

    def "validate inValid password"() {
        given:"valid passwords"
            String pass1 = "assword1"
            String pass2 = "Password"
            String pass3 = "jdkjfc0000"

        when:"validate password is called"
            def valid1 = service.validatePassword(pass1,pass1)
            def valid2 = service.validatePassword(pass2,pass2)
            def valid3 = service.validatePassword(pass3,pass3)
        then:"they should all be true"
            valid1.validationMessage == "Your password is missing an uppercase letter"
            valid2.validationMessage == "Your password is missing a number"
            valid3.validationMessage == "Your password is missing an uppercase letter"
    }
}
