package com.ctacorp.syndication.contact

import grails.transaction.Transactional

@Transactional
class EmailContactService {

    EmailContact saveEmailContact(EmailContact ec) {
        ec.save()
        ec
    }

    @Transactional(readOnly = true)
    EmailContact getEmailContact(Long id){
        EmailContact.get(id)
    }

    def search(String query){
        def contacts = EmailContact.withCriteria{
            or{
                ilike 'name', "%${query}%"
                ilike 'email', "%${query}%"
            }
        }

        contacts
    }

    def delete(EmailContact ec){
        println "id: ${ec.id}"
        ec.delete()
    }
}
