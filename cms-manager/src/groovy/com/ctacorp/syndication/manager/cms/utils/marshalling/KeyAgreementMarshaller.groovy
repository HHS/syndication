package com.ctacorp.syndication.manager.cms.utils.marshalling

import com.ctacorp.syndication.manager.cms.KeyAgreement
import grails.converters.JSON

class KeyAgreementMarshaller {

    KeyAgreementMarshaller() {

        JSON.registerObjectMarshaller(KeyAgreement) { KeyAgreement keyAgreement ->

            [
                    publicKey  : keyAgreement.entity2PublicKey,
                    dateCreated: keyAgreement.dateCreated
            ]
        }
    }
}
