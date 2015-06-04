package com.ctacorp.syndication

import com.ctacorp.syndication.util.PasswordGenerator
import grails.transaction.Transactional

@Transactional
class PasswordService {
    static transactional = false

    def validatePassword(String passwd, String passwdRepeat) {
        boolean u = false //uppercase
        boolean l = false //lowercase
        boolean n = false //number
        boolean repeat = false

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
        if(passwd == passwdRepeat){
            repeat = true
        }
        return validationMessage([valid:u && l && n && repeat, uppercaseValid:u, lowercaseValid:l, numberValid:n, passwordsMatch:repeat])
    }

    def getRandomPassword(){
        PasswordGenerator.getRandomPassword()
    }

    def validationMessage(passwordValidation){
        if(!passwordValidation.valid){
            if(!passwordValidation.uppercaseValid){
                return "Your password is missing an uppercase letter"
            }
            if(!passwordValidation.lowercaseValid){
                return "Your password is missing a lowercase letter"
            }
            if(!passwordValidation.numberValid){
                return "Your password is missing a number"
            }
            if(!passwordValidation.passwordMatch){
                return "Your passwords do not match"
            }
        }
    }

    private static final PASSWORD_POLICY = [
            "upper" : ('A'..'Z').toList(),
            "lower" : ('a'..'z').toList(),
            "numbers" : ('0'..'9').toList()
    ]
}