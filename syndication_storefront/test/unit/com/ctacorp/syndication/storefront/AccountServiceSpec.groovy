package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification
import spock.lang.Unroll


/**
 * Created by sgates on 3/25/15.
 */
@TestFor(AccountService)
@Mock([User, Role, UserRole])
class AccountServiceSpec extends Specification {
    def setup(){
        Role adminRole = new Role(authority: "ROLE_ADMIN").save(flush:true)
        Role userRole = new Role(authority: "ROLE_USER").save(flush:true)
        User adminUser = new User(username: "admin@example.com", password: "password").save(flush:true)
        User user = new User(username: "user@example.com", password: "password").save(flush:true)
        UserRole adminUserRole = UserRole.create(adminUser, adminRole, true)
        UserRole userUserRole = UserRole.create(user, userRole, true)

        assert Role.count() == 2
        assert User.count() == 2
        assert UserRole.count() == 2

        service.metaClass.sendMail = { println "Email Sent!" }
    }

    def "passwordReset should throw exception if no email is provided"() {
        when: "when the passwordReset method is called with null inputs"
            service.passwordReset(null, null)
        then: "an exception should be thrown"
            thrown(UsernameNotFoundException)
    }

    def "passwordReset should throw exception on unknown email address"() {
        given: "an unknown email address"
            String unknownEmailAddress = "unkown@example.com"
        when: "the reset method is called"
            service.passwordReset(unknownEmailAddress, null)
        then: "an exception should be thrown"
            thrown(UsernameNotFoundException)
    }

    def "if user is an admin, they should not be allowed to reset passwords"() {
        given: "an admin email address"
            String adminEmailAddress = "admin@example.com"
        when: "the passwordReset method is called"
            boolean result = service.passwordReset(adminEmailAddress, null)
        then: "the reset should be false"
            !result
    }

    def "a regular user should be allowed to reset their password"() {
        given: "a regular user"
            String userEmailAddress = "user@example.com"
        when: "passwordReset is called"
            boolean result = service.passwordReset(userEmailAddress, null)
        then: "the password email should be sent"
            //TODO can't verify this at the moment, maybe greenmail can run in unit test phase?
        and: "the result should be true"
            result
    }

    @Unroll
    def "validatePassword should be invalid for passwords not meeting standards"() {
        expect:
            def result = service.validatePassword(badPassword)
            result.valid == valid
            result.uppercaseValid == uppercaseValid
            result.lowercaseValid == lowercaseValid
            result.numberValid == numberValid
        where:
            badPassword |   valid   |   uppercaseValid  |   lowercaseValid  |   numberValid
            "a"         |   false   |   false           |   true            |   false
            "A"         |   false   |   true            |   false           |   false
            "1"         |   false   |   false           |   false           |   true
            "aA"        |   false   |   true            |   true            |   false
            "1a"        |   false   |   false           |   true            |   true
            "1A"        |   false   |   true            |   false           |   true
    }

    @Unroll
    def "validatePassword should be valid for passwords meeting standards"() {
        expect:
            def result = service.validatePassword(goodPassword)
            result.valid == valid
            result.uppercaseValid == uppercaseValid
            result.lowercaseValid == lowercaseValid
            result.numberValid == numberValid
        where:
            goodPassword |   valid   |   uppercaseValid  |   lowercaseValid  |   numberValid
            "1Ab"        |   true    |   true            |   true            |   true
    }

    def "accountUnlockEmail should throw exception if no email is provided"() {
        when: "when the passwordReset method is called with null inputs"
            service.accountUnlockEmail(null, null, null)
        then: "an exception should be thrown"
            thrown(UsernameNotFoundException)
    }

    def "accountUnlockEmail should throw exception on unknown email address"() {
        given: "an unknown email address"
            String unknownEmailAddress = "unkown@example.com"
        when: "the reset method is called"
            service.accountUnlockEmail(unknownEmailAddress, null, null)
        then: "an exception should be thrown"
            thrown(UsernameNotFoundException)
    }

    def "accountUnlockEmail should send unlock email to known users"() {
        given: "a known user"
            String knownUserEmailAddress = "user@example.com"
        when: "accountUnlockEmail is called"
            def result = service.accountUnlockEmail(knownUserEmailAddress, null, "someHash")
        then: "the user should have an unlockCode and a code expiration date"
            def user = User.findByUsername(knownUserEmailAddress)
            user
            user.codeExpirationDate
            user.unlockCode
        and: "unlock code should be the same as the hash we passed in"
            user.unlockCode == "someHash"
        and: "the codeExpirationDate should be 1 day in the future"
            user.codeExpirationDate > new Date()
            user.codeExpirationDate < new Date()+2
    }
}