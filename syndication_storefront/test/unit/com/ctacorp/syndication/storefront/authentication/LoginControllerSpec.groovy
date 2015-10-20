package com.ctacorp.syndication.storefront.authentication

import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.UserRole
import com.ctacorp.syndication.commons.testing.DSLTester
import grails.buildtestdata.mixin.Build
import grails.plugin.mail.MailService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Environment
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification


/**
 * Created by sgates on 6/16/15.
 */

@TestFor(LoginController)
@Mock([User, UserRole, Role])
@Build([User, UserRole, Role])
class LoginControllerSpec extends Specification {
    def "when user is not logged in, they should be redirected to auth"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{false}]
        when: "index is called with no login"
            controller.index()
        then: "user should be redirected to auth page"
            response.redirectedUrl == "/login/auth"
    }

    def "when user is logged in, they should be sent to default target"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{true}]
        when: "index is called with a valid login"
            controller.index()
        then: "user should be redirected to the root"
            response.redirectedUrl == "/"
    }

    def "when a user is already logged in, auth should redirect"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{true}]
        when: "the auth action is called"
            controller.auth()
        then: "the user should be redirected to the root"
            response.redirectedUrl == "/"
    }

    def "when a user isn't logged in, auth should send them to login page"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{false}]
        when: "the auth action is called"
            controller.auth()
        then: "the user should be redirected to the root"
            view == "/login/auth"
    }

    def "authAjax should return a 401"() {
        expect: "response code should be 401"
            controller.authAjax() == null
            response.status == 401
    }

    def "when a user is logged in and is rememberMe, should be redirected to full"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{true}]
            controller.authenticationTrustResolver = [isRememberMe:{true}]
        when: "the auth action is called"
            controller.denied()
        then: "the user should be redirected to the root"
            response.redirectedUrl == "/login/full"

    }

    def "when a user is logged in and not rememberMe, should be shown denied"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn:{true}]
            controller.authenticationTrustResolver = [isRememberMe:{true}]
        when: "the auth action is called"
            controller.denied()
        then: "the view should be null"
            view == "/login/denied"
    }

    def "full() should render the auth view"() {
        given: "a mocked springSecurityService"
            controller.authenticationTrustResolver = [isRememberMe:{false}]
        when: "the auth action is called"
            controller.full()
        then: "the view should be null"
            view == "/login/auth"
    }

    def "authFail should show an error message in flash if there is an exception in session"() {
        given: "no ajax"
            controller.springSecurityService = [isAjax:{false}]

        when: "account is expired"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new AccountExpiredException("expired")
            controller.authfail()
        then: "we get the expired flash message"
            flash.message == "springSecurity.errors.login.expired"

        when: "credentials expired"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new CredentialsExpiredException("expired")
            response.reset()
            controller.authfail()
        then: "we get credentials expired flash message"
            flash.message == "springSecurity.errors.login.passwordExpired"

        when: "account is disabled"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new DisabledException("expired")
            response.reset()
            controller.authfail()
        then: "we get the disabled flash message"
            flash.message == "springSecurity.errors.login.disabled"

        when: "account is locked"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new LockedException("expired")
            response.reset()
            controller.authfail()
        then: "we get the locked flashed message"
            flash.message == "springSecurity.errors.login.locked"

        when: "the error is of an unknown type"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new Object()
            response.reset()
            controller.authfail()
        then: "we get the general login fail message"
            flash.message == "springSecurity.errors.login.fail"

        when: "the request is ajax"
            controller.springSecurityService = [isAjax:{true}]
            session["SPRING_SECURITY_LAST_EXCEPTION"] = new Object()
            response.reset()
            controller.authfail()
        then: "we get a json response"
            response.json
        and: "the json response contains the expected error"
            response.json.error == "springSecurity.errors.login.fail"

        when: "there is no exception"
            session["SPRING_SECURITY_LAST_EXCEPTION"] = null
        then: "the message should the default"
            flash.message == "springSecurity.errors.login.fail"
    }

    def "ajaxSuccess should return an ajax success message"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [authentication:[name:"bob"]]
        when: "the ajaxSuccess action is called"
            controller.ajaxSuccess()
        then: "the response should be json"
            response.json
        and: "it should be a success"
            response.json.success
    }

    def "ajaxDenied should return an ajax denied message"() {
        when: "the ajaxDenied action is called"
            controller.ajaxDenied()
        then: "the response should be json"
            response.json
        and: "it should be an error"
            response.json.error == "access denied"
    }

    def "forgotPassword should null the message and errors"() {
        given: "Errors and messages in the flash"
            flash.errors = [1,2,3]
            flash.message = "oh my god"
        when: "forgotPassword is called"
            controller.forgotPassword()
        then: "the flash errors and flash message should be null"
            !flash.errors
            !flash.message
    }

    def "passwordReset should show error on invalid captcha"() {
        given: "a mocked recaptchaService"
            controller.recaptchaService = [verifyAnswer:{session, adr, params -> false}]
        when: "passwordReset action is called"
            controller.passwordReset()
        then: "there should be flash errors"
            flash.errors == [[message:"Recaptcha verfication failed, please try again!"]]
        and: "the view should be forgotPassword"
            view == "/login/forgotPassword"
    }

    def "passwordReset should error if username isn't found"() {
        given: "a mocked set of services"
            controller.recaptchaService = [verifyAnswer:{session, adr, params -> true}, cleanUp:{null}]
            controller.accountService = [passwordReset:{email -> throw new UsernameNotFoundException("not found")}]
            params.email = "a@b.com"
        when: "passwordReset action is called"
            controller.passwordReset()
        then: "there should be flash errors"
            flash.errors == [[message:"E-mail ${params.email} is not registered."]]
        and: "the view should be forgotPassword"
            view == "/login/forgotPassword"
    }

    def "valid recaptcha should allow user to reset password"() {
        given: "a mocked recaptchaService and accountService"
            controller.recaptchaService = [verifyAnswer:{session, adr, params -> true}, cleanUp:{session -> true}]
            controller.accountService = [passwordReset:{email -> true}]
        when: "passwordReset action is called"
            controller.passwordReset()
        then: "the view should be forgotPassword"
            response.redirectedUrl == "/login/auth"
    }

    def "unlockAccount should unlock a user's account if all conditions are correct"() {
        given: "a user with a locked account"
            User u = User.build()
            u.accountLocked = true
            u.unlockCode = "abc"
            u.codeExpirationDate = new Date() + 1
            u.save()

            params.verification = u.unlockCode
        when: "unlockAccount is called for this user"
            controller.unlockAccount(u)
            u = User.read(1)
        then: "the account should be unlocked"
            ! u.accountLocked
            u.codeExpirationDate == null
            u.unlockCode == null
        and: "we should be directed to auth to login"
            response.redirectedUrl == "/login/auth"
    }

    def "unlockAccount should fail if unlock code doesn't match"() {
        given: "a user with a locked account"
            User u = User.build()
            u.accountLocked = true
            u.unlockCode = "abc"
            u.codeExpirationDate = new Date() + 1
            u.save()

            params.verification = "def"
        when: "unlockAccount is called for this user"
            controller.unlockAccount(u)
            u = User.read(1)
        then: "the account should still be locked"
            u.accountLocked
            u.codeExpirationDate
            u.unlockCode
        and: "we should be directed to auth to login"
            response.redirectedUrl == "/login/auth"
    }

    def "unlockAccount should fail if expirationDate is in the past"() {
        given: "a user with a locked account"
            User u = User.build()
            u.accountLocked = true
            u.unlockCode = "abc"
            u.codeExpirationDate = new Date() - 1
            u.save()

            params.verification = u.unlockCode
        when: "unlockAccount is called for this user"
            controller.unlockAccount(u)
            u = User.read(1)
        then: "the account should still be locked"
            u.accountLocked
            u.codeExpirationDate
            u.unlockCode
        and: "we should be directed to auth to login"
            response.redirectedUrl == "/login/auth"
    }

    def "register should create a new user instance"() {
        when: "register is called"
            def model = controller.register()
        then: "there should be a model with a user instance"
            model
            model.userInstance
    }

    def "authenticate should redirect to storefront contoller"() {
        when: "authenticate is called"
            controller.authenticate()
        then: "we should be redirected to storefront"
        if(Environment.current != Environment.DEVELOPMENT){
            response.redirectedUrl == "/storefront/index"
        } else {
            response.redirectedUrl == "/"
        }

    }

    def "cancel should redirect to storefront contoller"() {
        when: "authenticate is called"
            controller.cancel()
        then: "we should be redirected to storefront"
        if(Environment.current != Environment.DEVELOPMENT) {
            response.redirectedUrl == "/storefront/index"
        } else {
            response.redirectedUrl == "/"
        }
    }

    def "createUserAccount should show errors if there are any on post"() {
        given: "a set of mocked services"
            controller.passwordService = [validatePassword:{password, passwordRepeat -> [valid:false, passwordValidationMessage:"Password is bad"]}]
            controller.recaptchaService = [verifyAnswer:{ a,b,c -> true}]
            controller.userService = [createUser:{params ->
                User user = User.build()
                user.username = null
                user.validate()
                user
            }]
        when: "createUserAccount is called"
            controller.createUserAccount()
        then: "we should see the register view"
            view == "/login/register"
        and: "there should be a model returned"
            model
            model.userInstance
        and: "the model should have errors"
            !model.userInstance.validate()

            model.userInstance.errors
    }

    def "accountLocked should reset flash"() {
        given: "flash set to something"
            flash.message = "some message"
            flash.errors = ["some errors"]
        when: "accountLocked is called"
            controller.accountLocked()
        then: "the flash should get reset"
            !flash.message
            !flash.errors
    }

    def "createUserAccount should go back to register view if captcha is invalid"() {
        given: "an invalid captcha"
            controller.recaptchaService = [verifyAnswer:{ a,b,c -> false}]
        when: "createUserAccount is called"
            controller.createUserAccount()
        then: "view should be register"
            view == "register"
    }

    def "createUserAccount should direct to auth action if user is created"() {
        given: "a set of mocked services"
            controller.passwordService = [validatePassword:{password, passwordRepeat -> [valid:true]}]
            controller.recaptchaService = [verifyAnswer:{session, adr, params -> true}]
            def mailServiceMock = DSLTester.mock(MailService, 'sendMail', [])
            controller.mailService = new MailService()
            controller.userService = [createUser:{params ->
                User user = User.build()
                user
            }]
        when: "createUserAccount is called"
            controller.createUserAccount()
        then: "we should be redirected to auth page"
            response.redirectedUrl == "/login/auth"
        and: "an email should have been sent"
            mailServiceMock.result
    }

    def "updateUserAccount should render userAccount view when updates are invalid"() {
        given: "bad update information"
            controller.userService = [updateUser:{params ->
                User u = User.build()
                u.errors.rejectValue("username", "nullable")
                u
            }]
        when: "updateUserAccount is called"
            controller.updateUserAccount()
        then: "the view should be userAccount"
            view == "/login/userAccount"
    }

    def "updateUserAccount should render userAccount view when instance doesn't save"() {
        given: "an instance without an id"
            controller.userService = [updateUser:{params ->
                User u = User.build()
                u.id = null
                u
            }]
        when: "updateUserAccount is called"
            controller.updateUserAccount()
        then: "the view should be userAccount"
            view == "/login/userAccount"
    }

    def "updateUserAccount should redirect to index page on successful update"() {
        given: "a valid user instance"
            controller.userService = [updateUser:{params ->
                User u = User.build()
                u.save(flush: true)
            }]
            def mock = DSLTester.mock(MailService, 'sendMail', [])
            controller.mailService = new MailService()
        when: "updateUserAccount is called"
            controller.updateUserAccount()
        then: "the user should be redirected to the storefront index page"
            response.redirectedUrl == "/storefront/index"
    }

    def "updateUserAccount should send email"() {
        given: "a mocked mail service"
            controller.userService = [updateUser:{params ->
                User u = User.build()
                u.save(flush: true)
            }]
            def mock = DSLTester.mock(MailService, 'sendMail', [])
            controller.mailService = new MailService()
        when: "updateUserAccount is called"
            controller.updateUserAccount()
        then: "the email details should be correct"
            mock.result.find{it.name == "to"}.args[0] == "a@b.com"
            mock.result.find{it.name == "async"}.args[0] == true
            mock.result.find{it.name == "subject"}.args[0] == "HHS Syndication System - Your account details have been updated!"
            mock.result.find{it.name == "html"}.args[0].contains("a@b.com") //email
            mock.result.find{it.name == "html"}.args[0].contains("1")       //userid
    }

    def "userAccount should redirect to auth if not logged in"() {
        given: "a mocked springSecurityService"
            controller.springSecurityService = [isLoggedIn: { false }]
        when: "userAccount() is called"
            controller.userAccount()
        then: "user should be redirected to the authenticate action"
            response.redirectedUrl == "/login/authenticate"
            flash.errors[0] == "You are not signed in!"
    }

    def "userAccount should return userInstance if logged in"() {
        given: "a mocked springSecurityService"
            User user = User.build()
            controller.springSecurityService = [isLoggedIn:{true}, principal:[id:user.id]]
        when: "userAccount() is called"
            controller.userAccount()
        then: "a model should be returned"
            model.userInstance
    }

    def "requestUnlock should fail if captcha is invalid"() {
        given: "a bad captcha"
            controller.recaptchaService = [verifyAnswer:{a,b,c-> false}]
        when: "requestUnlock is called"
            controller.requestUnlock()
        then: "there should be a flash error"
            flash.errors == [[message:"Recaptcha verfication failed, please try again!"]]
        and: "and we should be redirected to the accountLocked view"
            view == "/login/accountLocked"
    }

    def "requestUnlock should render accountLocked view when a user not found exception occurs"() {
        given: "mocked services"
            controller.accountService = [accountUnlockEmail:{a, b, c-> throw new UsernameNotFoundException("not found")}]
            controller.recaptchaService = [verifyAnswer:{a,b,c-> true}]
            params.email = "a@b.com"
            User user = User.build(username:params.email, accountLocked:true)
        when: "requestUnlock is called"
            controller.requestUnlock()
        then: "the view should be accountLocked"
            view == "/login/accountLocked"
        and: "there should be flash errors"
            flash.errors == [[message:"E-mail a@b.com is not registered."]]
    }

    def "requestUnlock should redirect to auth if successful"() {
        given: "mocked services"
            controller.accountService = [accountUnlockEmail:{a, b, c-> true}]
            controller.recaptchaService = [verifyAnswer:{a,b,c-> true}, cleanUp:{}]
            params.email = "a@b.com"
            User user = User.build(username:params.email, accountLocked:true)
        when: "requestUnlock is called"
            controller.requestUnlock()
        then: "the view should be accountLocked"
            response.redirectUrl == "/login/auth"
        and: "there should be flash errors"
            flash.message == "An email has been sent to your address with a link to unlock your account."
    }

    def "requestUnlock should render accountLocked view if user isn't found"() {
        given: "mocked services"
            controller.accountService = [accountUnlockEmail:{a, b, c-> true}]
            controller.recaptchaService = [verifyAnswer:{a,b,c-> true}, cleanUp:{}]
            params.email = "a@b.com"
        when: "requestUnlock is called"
            controller.requestUnlock()
        then: "the view should be accountLocked"
            view == "/login/accountLocked"
        and: "there should be flash errors"
            flash.errors == [[message:"The E-mail '${params.email}' does not exist."]]
    }

    def "requestUnlock should redirect to accountLocked account isn't locked"() {
        given: "mocked services"
            controller.accountService = [accountUnlockEmail:{a, b, c-> true}]
            controller.recaptchaService = [verifyAnswer:{a,b,c-> true}, cleanUp:{}]
            params.email = "a@b.com"
            User user = User.build(username:params.email, accountLocked:false)
        when: "requestUnlock is called"
            controller.requestUnlock()
        then: "the view should be accountLocked"
            view == "/login/accountLocked"
        and: "there should be flash errors"
            flash.errors == [[message:"E-mail '${params.email}' is not locked."]]
    }
}