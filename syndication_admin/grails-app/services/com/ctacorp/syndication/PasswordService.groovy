package com.ctacorp.syndication

import com.ctacorp.syndication.util.PasswordGenerator
import grails.transaction.Transactional

@Transactional
class PasswordService {
    static transactional = false

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

    def getRandomPassword(){
        PasswordGenerator.getRandomPassword()
    }

    private static final PASSWORD_POLICY = [
            "upper" : ('A'..'Z').toList(),
            "lower" : ('a'..'z').toList(),
            "numbers" : ('0'..'9').toList()
    ]
}
