package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.microsite.MicroSite
import com.ctacorp.syndication.microsite.MicrositeRegistration
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
class RegistrationController {

    def registrationService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: "verified"

        [
            registrationList:MicrositeRegistration.list(params),
            registrationCount:MicrositeRegistration.count(),
            max:params.max

        ]
    }

    def show(MicrositeRegistration registrationInstance) {
        render view:"show", model:[registrationInstance:registrationInstance]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_MANAGER'])
    def update(MicrositeRegistration registrationInstance){
        if(!registrationInstance){
            flash.error = "Registration Instance doesn't exist."
        }

        if(params.changeAccess) {
            registrationInstance = registrationService.changeAccess(registrationInstance)
        }

        registrationInstance.save()

        render view:"show", model:[registrationInstance:registrationInstance]
    }

    def delete(MicrositeRegistration registrationInstance) {
        if(!registrationInstance){
            flash.error = "Registration Instance doesn't exist."
        }

        flash.message = "Registration Form Deleted."
        registrationInstance.delete()



        redirect action:"index"
    }
}
