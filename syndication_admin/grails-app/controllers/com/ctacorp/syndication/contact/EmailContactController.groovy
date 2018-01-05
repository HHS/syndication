package com.ctacorp.syndication.contact

import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class EmailContactController {
    def emailContactService

    def index() {
        params.sort = params.sort ?: "name"
        params.max = Math.min(params.int('max') ?: 20, 100)
        def emailContactInstanceList = EmailContact.list(params)
        [emailContactInstanceList: emailContactInstanceList, emailContactInstanceCount:EmailContact.count()]
    }

    def create() {
        [emailContact: new EmailContact()]
    }

    def show(EmailContact ec) {
        [emailContactInstance: ec]
    }

    def edit(EmailContact ec){
        [emailContactInstance: ec]
    }

    def sendTestEmail(EmailContact ec){
        sendMail{
            to "${ec.email}"
            subject "This is a test email"
            body "If you are receiving this message, it is because someone has initiated a 'test email' "+
            "check from the admin console of the HHS Syndication System (https://syndicationadmin.digitalmedia.hhs.gov). "+
            "You can safely ignore this message, but if you feel it was sent in error, please contact syndicationadmin@hhs.gov "+
            "and ask to be removed from the list."
        }
        flash.message = "Test email sent to ${ec.email}"
        render view: "show", model: [emailContactInstance: ec]
    }

    def save(EmailContact ec) {
        ec = emailContactService.saveEmailContact(ec)
        if(ec.hasErrors()){
            respond ec.errors, view:'create'
            return
        }
        render view: "show", model: [emailContactInstance: ec]
    }

    def update(EmailContact ec){
        ec = emailContactService.saveEmailContact(ec)
        if(ec.hasErrors()){
            respond ec.errors, view:'create'
            return
        }

        flash.message = "Email Contact \"${ec.name}\" updated successfully."
        render view: "show", model: [emailContactInstance: ec]
    }

    def delete(EmailContact ec) {
        if (ec == null) {
            notFound()
            return
        }

        emailContactService.delete(ec)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'EmailContact.label', default: 'Email Contact'), ec.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'EmailContact.label', default: 'Email Contact'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
