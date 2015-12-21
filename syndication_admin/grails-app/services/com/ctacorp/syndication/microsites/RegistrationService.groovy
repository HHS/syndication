package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.microsite.MicrositeRegistration
import grails.transaction.Transactional

@Transactional
class RegistrationService {

    def changeAccess(MicrositeRegistration registrationInstance) {
        if(registrationInstance.verified){
            registrationInstance.verified = false
        } else {
            registrationInstance.verified = true
        }
        registrationInstance
    }
}
