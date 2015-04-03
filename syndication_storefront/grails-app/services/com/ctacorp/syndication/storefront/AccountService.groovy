package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.authentication.Role
import com.ctacorp.syndication.util.PasswordGenerator
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import groovy.time.TimeCategory
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Transactional
class AccountService {
    def grailsApplication

    private static final PASSWORD_POLICY = [
            "upper"  : ('A'..'Z').toList(),
            "lower"  : ('a'..'z').toList(),
            "numbers": ('0'..'9').toList()
    ]

    def passwordReset(email, renderer) throws Exception {
        if(!email){
            throw new UsernameNotFoundException("Provided username was null!")
        }
        //Find the referenced user
        User user = User.findByUsername(email)
        if (!user) {
            throw new UsernameNotFoundException("User ${email} not found")
        }

        if (user.getAuthorities().contains(Role.findByAuthority("ROLE_ADMIN"))) {
            log.error "A password reset was attempted for an admin account ID: ${user.id}"
            return false
        }

        //Generate a new random password
        def randomPassword = PasswordGenerator.randomPassword
        user.password = randomPassword
        //Update user account with new rand password
        if (user.save(flush: true, failOnError: true)) {
            //if the save worked, send an email
            sendMail {
                async true
                to user.username
                subject "Your password has been reset"
                html renderer(randomPassword)
            }
            return true
        }
        return false
    }

    @NotTransactional
    def validatePassword(String passwd) {
        boolean u = false //uppercase
        boolean l = false //lowercase
        boolean n = false //number

        passwd.each { letter ->
            if (letter in PASSWORD_POLICY.upper) {
                u = true
            }
            if (letter in PASSWORD_POLICY.lower) {
                l = true
            }
            if (letter in PASSWORD_POLICY.numbers) {
                n = true
            }
        }
        return [valid: u && l && n, uppercaseValid: u, lowercaseValid: l, numberValid: n]
    }

    @Transactional
    def accountUnlockEmail(String email, String body, String userHash) {
        if(!email){
            throw new UsernameNotFoundException("Provided username was null!")
        }
        User user = User.findByUsername(email)
        if (!user) {
            throw new UsernameNotFoundException("User ${email} not found")
        }

        user.codeExpirationDate = new Date() + 1
        user.unlockCode = userHash

        //Update user account with new rand password
        if (user.save(flush: true, failOnError: true)) {
            //if the save worked, send an email
            sendMail {
                multipart true
                async true
                to user.username
                subject "Unlock Storefront Account"
                html body
            }
            return true
        }
        return false
    }
}
