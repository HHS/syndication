/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import org.springframework.context.i18n.LocaleContextHolder

class EmailSubscriptionMailService {

    static transactional = false

    def emailService
    def messageSource

    void sendSubscriptionUpdate(EmailSubscription emailSubscription, content) {

        def bodyKey = 'email.send.subscription.update.body'

        if (emailSubscription.attachContent) {
            bodyKey = 'email.send.subscription.update.body.attachment'
        }

        sendSubscription('email.send.subscription.update.subject', bodyKey, emailSubscription, content)
    }

    void sendSubscriptionCreate(EmailSubscription emailSubscription, content) {

        def bodyKey = 'email.send.subscription.create.body'

        if (emailSubscription.attachContent) {
            bodyKey = 'email.send.subscription.create.body.attachment'
        }

        sendSubscription('email.send.subscription.create.subject', bodyKey, emailSubscription, content)
    }

    void sendStorefrontDelete(EmailSubscription emailSubscription) {
        sendSubscription('email.send.storefront.delete.subject', 'email.send.storefront.delete.body', emailSubscription)
    }

    void sendSubscriptionDelete(EmailSubscription emailSubscription) {
        sendSubscription('email.send.subscription.delete.subject', 'email.send.subscription.delete.body', emailSubscription)
    }

    private void sendSubscription(String messageSubjectKey, String messageBodyKey, EmailSubscription emailSubscription, content = null) {

        def subject = messageSource.getMessage(messageSubjectKey, [emailSubscription.sourceUrl] as Object[], LocaleContextHolder.locale)

        if (emailSubscription.attachContent) {

            def attachmentName = "${emailSubscription.title}.html"

            def body = {
                if(subject.contains('delete')) {
                    messageSource.getMessage(messageBodyKey, [emailSubscription.sourceUrl] as Object[], LocaleContextHolder.locale)
                } else {
                    messageSource.getMessage(messageBodyKey, [attachmentName, emailSubscription.sourceUrl] as Object[], LocaleContextHolder.locale)
                }
            }()

            if(content) {
                emailService.sendMessageWithAttachment(emailSubscription.emailSubscriber.email, subject, body, attachmentName, 'text/html', content.bytes)
            } else {
                emailService.sendMessage(emailSubscription.emailSubscriber.email, subject, body)
            }

        } else {
            def body = messageSource.getMessage(messageBodyKey, [emailSubscription.sourceUrl] as Object[], LocaleContextHolder.locale)
            emailService.sendMessage(emailSubscription.emailSubscriber.email, subject, body)
        }
    }
}