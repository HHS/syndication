package com.ctacorp.syndication.storefront.microsite

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.contact.EmailContact
import grails.plugin.springsecurity.annotation.Secured
import com.ctacorp.syndication.microsite.MicrositeRegistration
import grails.util.Holders

@Secured(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PUBLISHER', 'ROLE_STOREFRONT_USER'])
class MicrositeRegistrationController {

    def springSecurityService
    def mailService

    def registration() {

        render view:"registration", model:[registration:new MicrositeRegistration()]
    }

    def register(MicrositeRegistration registration){
        registration.user = User.get(springSecurityService.currentUser.id)
        registration.validate()
        if(registration.hasErrors()) {
            flash.errors = registration.errors
            render view: "registration", model:[registration: registration]
        }
        def mailRecipiants = EmailContact.list()?.email ?: "syndication@ctacorp.com"

        flash.message = "Mircosite Registration Sent!"
        registration.save()

        mailService.sendMail {
            multipart true
            async true
            to mailRecipiants
            subject "Microsite registration request"
            html g.render(template: 'registrationEmail', model:[userInstance: User.get(springSecurityService.currentUser.id), registration: registration, adminUrl:Holders.config.ADMIN_SERVER_URL])
        }

        redirect controller: "storefront", action:"index"
    }

    def alreadyRegistered() {
        render view:"viewRegistration", model:[registration:MicrositeRegistration.findByUser(User.get(springSecurityService.currentUser.id))]
    }

    def updateRegistration(MicrositeRegistration registration) {
        registration.validate()
        if(registration.hasErrors()) {
            flash.errors = registration.errors
            render view: "registration", model:[registration: registration]
        }

        flash.message = "Mircosite Registration has been updated!"

        registration.save(flush:true)
        redirect controller: "storefront", action:"index"
    }
}
