package com.ctacorp.syndication.authentication

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.context.request.RequestContextHolder


/**
 * Created by nburk on 12/22/14.
 */
class CustomUserDetailsService implements GrailsUserDetailsService {
    def cmsManagerKeyService
    def grailsApplication

    static final String UNAUTHORIZED_MESSAGE = "Sorry, your account is no longer authorized. Please contact syndicationadmin@hhs.gov to get support."
    static final String INVALID_AGREEMENT_MESSAGE = "Sorry, your account is no longer authorized. Please contact syndicationadmin@hhs.gov to get support."

    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User.withTransaction { status ->
            User user = User.findByUsername(username)
            if(!user){
                throw new CustomAuthenticationException(UNAUTHORIZED_MESSAGE)
            }
            if (user.authorities.contains(Role.findByAuthority("ROLE_PUBLISHER"))){
                if(!cmsManagerKeyService.getSubscriberById(user.subscriberId)){
                    RequestContextHolder.currentRequestAttributes().setAttribute("publisherFailure", UNAUTHORIZED_MESSAGE, 1)
                    throw new CustomAuthenticationException(UNAUTHORIZED_MESSAGE)
                } else if(!cmsManagerKeyService.getSubscriberById(user.subscriberId as String)?.keyAgreement){
                    RequestContextHolder.currentRequestAttributes().setAttribute("publisherFailure", INVALID_AGREEMENT_MESSAGE, 1)
                    throw new CustomAuthenticationException(INVALID_AGREEMENT_MESSAGE)
                }

            }
            def authorities = user.authorities.collect {new GrantedAuthorityImpl(it.authority)}

            return new GrailsUser(user.username, user.password, user.enabled, !user.accountExpired,
                    !user.passwordExpired, !user.accountLocked,
                    authorities ?: NO_ROLES, user.id)
        }
    }
}
