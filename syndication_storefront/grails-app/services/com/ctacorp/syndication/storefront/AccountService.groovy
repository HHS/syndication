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
    def mailService
    def groovyPageRenderer

    def passwordReset(email, renderer) throws Exception{
        //Find the referenced user
        User user = User.findByUsername(email)
        if(!user){
            throw new UsernameNotFoundException("User ${email} not found")
        }

        if (user.getAuthorities().contains(Role.findByAuthority("ROLE_ADMIN"))){
            log.error "A password reset was attempted for an admin account ID: ${user.id}"
            return
        }

        //Generate a new random password
        def randomPassword = PasswordGenerator.randomPassword
        user.password = randomPassword
        //Update user account with new rand password
        if(user.save(flush: true, failOnError: true)){
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

        passwd.each{ letter ->
            if (letter in PASSWORD_POLICY.upper){
                u = true
            }
            if (letter in PASSWORD_POLICY.lower){
                l = true
            }
            if (letter in PASSWORD_POLICY.numbers){
                n = true
            }
        }
        return [valid:u && l && n, uppercaseValid:u, lowercaseValid:l, numberValid:n]
    }

    private static final PASSWORD_POLICY = [
            "upper" : ('A'..'Z').toList(),
            "lower" : ('a'..'z').toList(),
            "numbers" : ('0'..'9').toList()
    ]

    @Transactional
    def accountUnlockEmail(String email, String body, String userHash){
        User user = User.findByUsername(email)
        if(!user){
            throw new UsernameNotFoundException("User ${email} not found")
        }

//        String stringToHash = email + System.nanoTime()
//        String userHash = stringToHash.encodeAsMD5();
//        String body = groovyPageRenderer.render(template:'/login/accountLockedEmail', model:[verification:userHash, id:user.id])

        use (TimeCategory) {
            user.codeExpirationDate = new Date() + 1.day
        }
        user.unlockCode = userHash
//        user.save(flush:true)

        //Update user account with new rand password
        if(user.save(flush: true, failOnError: true)){
            //if the save worked, send an email
            mailService.sendMail{
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
