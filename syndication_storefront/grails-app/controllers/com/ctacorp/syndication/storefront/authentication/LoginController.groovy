package com.ctacorp.syndication.storefront.authentication

import com.ctacorp.syndication.authentication.User
import grails.converters.JSON
import org.springframework.security.core.userdetails.UsernameNotFoundException

import javax.servlet.http.HttpServletResponse

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import grails.transaction.Transactional

@Secured('permitAll')
class LoginController {
    def mailService
    def recaptchaService
    def accountService
    def passwordService
    def authenticationTrustResolver
    def springSecurityService
    def userService

    /**
     * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
     */
    def index(){
        if (springSecurityService.isLoggedIn()) {
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
        } else {
            redirect action: 'auth', params: params
        }
    }

    /**
     * Show the login page.
     */
    def auth(){
        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
            return
        }

        String view = 'auth'
        String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
        render view: view, model: [postUrl: postUrl, rememberMeParameter: config.rememberMe.parameter,'spring-security-redirect':params.'spring-security-redirect']
    }

    /**
     * The redirect action for Ajax requests.
     */
    def authAjax(){
        response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
        response.sendError HttpServletResponse.SC_UNAUTHORIZED
    }

    /**
     * Show denied page.
     */
    def denied(){
        if (springSecurityService.isLoggedIn() && authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
            // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
            redirect action: 'full', params: params
        }
        render view:'denied'
    }

    /**
     * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
     */
    def full(){
        def config = SpringSecurityUtils.securityConfig
        render view: 'auth', params: params, model: [
                hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
                postUrl: "${request.contextPath}${config.apf.filterProcessesUrl}"
        ]
    }

    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */
    def authfail(){
        String msg = 'An unhandled error has ooccurred'
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]

        if(exception) {
            switch (exception) {
                case AccountExpiredException: msg = g.message(code: "springSecurity.errors.login.expired")
                    break
                case CredentialsExpiredException: msg = g.message(code: "springSecurity.errors.login.passwordExpired")
                    break
                case DisabledException: msg = g.message(code: "springSecurity.errors.login.disabled")
                    break
                case LockedException: msg = g.message(code: "springSecurity.errors.login.locked")
                    break
                default: msg = g.message(code: "springSecurity.errors.login.fail")
            }
        }

        if (springSecurityService.isAjax(request)) {
            render([error: msg] as JSON)
        } else {
            flash.message = msg
            redirect action: 'auth', params: params
        }
    }

    /**
     * The Ajax success redirect url.
     */
    def ajaxSuccess(){
        render([success: true, username: springSecurityService.authentication.name] as JSON)
    }

    /**
     * The Ajax denied redirect url.
     */
    def ajaxDenied(){
        render([error: 'access denied'] as JSON)
    }

    /**
     * The forgot password screen
     * @return
     */
    def forgotPassword() {
        flash.message = null
        flash.errors = null
    }

    /**
     * Password reset feature
     * @return
     */
    def passwordReset() {
        flash.errors = []
        boolean success = false
        boolean validCaptcha = recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)

        //Only allow this to continue if they pass the captcha
        if (validCaptcha) {
            try {
                //passes in a closure to render a template because the render from a service doesn't work
                success = accountService.passwordReset(params.email)
            } catch (UsernameNotFoundException e) {
                flash.errors << [message:"E-mail ${params?.email} is not registered."]
                log.info("User not found: ${params.email}\n${e}")
                render view:'forgotPassword'
                return
            }
            if (success) {
                flash.message = "An email has been sent to your address with a new temporary password."
            }
            recaptchaService.cleanUp(session)

            redirect action: 'auth'
            return
        }
        flash.errors << [message:"Recaptcha verfication failed, please try again!"]
        render view: "forgotPassword", model: [redo:true, email:params.email]
    }

    def accountLocked(){
        flash.message = null
        flash.errors = null
    }

    //sends an email to the user
    def requestUnlock(){
        flash.errors = []
        boolean success = false
        boolean validCaptcha = recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)

        //Only allow this to continue if they pass the captcha
        if (validCaptcha) {
            try {
                User user = User.findByUsername(params.email)
                if(!user){
                    flash.errors << [message:"The E-mail '${params.email}' does not exist."]
                    render view:'accountLocked'
                    return
                }
                if(!user.accountLocked){
                    flash.errors << [message:"E-mail '${params.email}' is not locked."]
                    render view:'accountLocked'
                    return
                }
                String stringToHash = params.email + System.nanoTime()
                String userHash = stringToHash.encodeAsMD5();
                String htmlBody = g.render(template:'accountLockedEmail', model:[verification:userHash, id:User.findByUsername(params.email).id])

                success = accountService.accountUnlockEmail(params.email, htmlBody, userHash)
            } catch (UsernameNotFoundException e) {
                flash.errors << [message:"E-mail ${params.email} is not registered."]
                log.info("User not found: ${params.email}\n${e}")
                render view:'accountLocked'
                return
            }
            if (success) {
                flash.message = "An email has been sent to your address with a link to unlock your account."
            }
            recaptchaService.cleanUp(session)

            redirect action: 'auth'
            return
        }
        flash.errors << [message:"Recaptcha verfication failed, please try again!"]
        render view: "accountLocked", model: [redo:true, email:params.email]
    }

    //the action that gets called on the get request from the link that was sent to the user
    def unlockAccount(User user){
        flash.errors = []
        if(user?.unlockCode != params.verification || user?.codeExpirationDate < new Date()){
            flash.errors << [message:"Verification failed. please try again or contact syndicationadmin@hhs.gov"]
            redirect action:'auth'
            return
        }
        user.accountLocked = false
        user.codeExpirationDate = null
        user.unlockCode = null
        user.save(flush:true)
        flash.message = "Your account has been unlocked!"
        redirect action:'auth'
    }

    def register() {
        [userInstance: new User(params)]
    }

    @Secured(['ROLE_STOREFRONT_USER', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
    def authenticate() {
        redirect controller: "storefront", action: "index"
    }

    def cancel(){
        redirect controller:"storefront", action:"index"
    }

    /**
     * Create a new user account (with all required info)
     * @return
     */
    def createUserAccount() {
        boolean validCaptcha = recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)
        User userInstance

        if (!validCaptcha) {
            userInstance = new User(params)
            userInstance.errors.reject("Invalid Recaptcha", "Recaptcha verfication failed, please try again!")
            respond userInstance.errors, view: "register", model: [userInstance: userInstance]
            return
        }

        userInstance = userService.createUser(params)
        if(!userInstance?.id || userInstance.hasErrors()){
            render view:"register", model:[userInstance: userInstance]
            return
        }

        def greeting = userInstance.name ? "${userInstance.name}, your": "Your"
        flash.message =  greeting + " account has been created!"

        mailService.sendMail {
            multipart true
            async true
            to userInstance.username
            subject "HHS Syndication System - Your account has been successfully created!"
            html g.render(template: 'registrationEmail', model:[username: userInstance.username.encodeAsHTML()])
        }

        redirect action:'auth'
    }

    @Secured(['ROLE_STOREFRONT_USER', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
    def userAccount() {
        flash.errors = []
        def userInstance
        if (springSecurityService.isLoggedIn()) {
            userInstance = User.get(springSecurityService.principal.id)
        } else {
            flash.errors << "You are not signed in!"
            redirect action: "authenticate"
            return
        }
        respond userInstance
    }

    /**
     * Similar to the create account method, this method is for updating user information
     * @return
     */
    @Secured(['ROLE_STOREFRONT_USER', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER', 'ROLE_BASIC', 'ROLE_STATS', 'ROLE_PUBLISHER'])
    @Transactional
    def updateUserAccount() {
        User updatedUser = userService.updateUser(params)
        if(updatedUser.hasErrors() || !updatedUser.id) {
            render view: "userAccount", model: [userInstance: updatedUser]
            return
        }
        //Let the user know via email that their account details have changed
        mailService.sendMail {
            async true
            to updatedUser.username
            subject "HHS Syndication System - Your account details have been updated!"
            html """\
                            <h2>HHS Syndication Account Details</h2><br/>
                            <ul>
                                <li>Username: ${updatedUser.username.encodeAsHTML()}</li>
                                <li>Registration ID: ${updatedUser.id}</li>
                            </ul>
                        """
        }
        flash.message = "User account updated."
        redirect controller:"storefront", action: "index"
    }
}