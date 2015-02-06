package com.ctacorp.syndication.manager.cms.rest.security

import grails.util.Holders
import groovy.json.JsonBuilder

class AuthorizationResponseFormatter {

    static def includeAuthorizationFailureDetails = Holders.config.apiKey.includeAuthorizationFailureDetails

    static String formatResponse(AuthorizationResult authResult) {

        def jsonBuilder = new JsonBuilder()

        if(includeAuthorizationFailureDetails) {

            jsonBuilder (
                message: authResult.errorMessage,
                senderPublicKey: authResult.keyAgreement?.entity2PublicKey,
                signingString: authResult.signingString,
                senderHash: authResult.authHeader?.senderHash,
                calculatedHash: authResult.calculatedHash,
                uid: authResult.logFileId
            )

        } else {

            jsonBuilder (
                    message: authResult.errorMessage
            )
        }

        jsonBuilder.toPrettyString()
    }
}
