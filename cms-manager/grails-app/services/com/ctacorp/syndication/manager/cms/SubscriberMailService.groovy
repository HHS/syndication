/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */

package com.ctacorp.syndication.manager.cms

import grails.util.Holders
import org.springframework.context.i18n.LocaleContextHolder

import javax.annotation.PostConstruct

class SubscriberMailService {

    static transactional = false

    def messageSource
    def emailService

    def accountHelpEmail
    def serverUrl

    @PostConstruct
    void init() {
        accountHelpEmail = Holders.config.springsecurity.cmsManager.account.help.email
        serverUrl = Holders.config.grails.serverURL
    }

    void sendSubscriberKeyAgreement(Subscriber subscriber) {

        def attachmentName = "${subscriber.name}.json"
        def attachment = subscriber.keyAgreement.jsonExport

        def subject = messageSource.getMessage('email.send.key.agreement.subject', [] as Object[], LocaleContextHolder.locale)
        def body = messageSource.getMessage('email.send.key.agreement.body', [attachmentName] as Object[], LocaleContextHolder.locale)

        emailService.sendMessageWithAttachment(subscriber.email, subject, body, attachmentName, 'application/json', attachment.bytes)
    }

    void sendSubscriberDelete(String email) {

        def subject = messageSource.getMessage('email.send.subscriber.delete.subject', [serverUrl] as String[], LocaleContextHolder.locale)
        def body = messageSource.getMessage('email.send.subscriber.delete.body', [accountHelpEmail] as String[], LocaleContextHolder.locale)

        emailService.sendMessage(email, subject, body)
    }
}
