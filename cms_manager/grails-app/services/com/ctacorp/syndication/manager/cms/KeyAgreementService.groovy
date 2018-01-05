
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms


import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import grails.transaction.Transactional
import grails.util.Holders

@Transactional
class KeyAgreementService {

    static entityName = Holders.config.apiKey.entities.cmsManager as String

    void addKeyAgreementToSubscriber(Subscriber subscriber) {

		//TODO Just use the entity2 name
        def existingKeyAgreement = KeyAgreement.findByEntity1AndEntity2(entityName, subscriber.name)
        if (existingKeyAgreement) {
            existingKeyAgreement.delete(flush:true)
        }

        def params = ApiKeyUtils.newKeyAgreement(entityName, subscriber.name)
        def keyAgreement = new KeyAgreement(params).save(failOnError:true)

        subscriber.keyAgreement = keyAgreement
        subscriber.save(flush: true, failOnError: true)
    }

    def newKeyAgreement(String entityName) {

        def keyAgreement = ApiKeyUtils.newKeyAgreement(entityName, entityName)
        def agreement = new KeyAgreement(keyAgreement)
        agreement.save(flush: true)
        agreement
    }
}
