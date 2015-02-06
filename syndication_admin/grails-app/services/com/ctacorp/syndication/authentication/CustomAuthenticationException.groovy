package com.ctacorp.syndication.authentication

import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * Created by nburk on 12/22/14.
 */
class CustomAuthenticationException extends UsernameNotFoundException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
