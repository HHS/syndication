package com.ctac.tinyurl

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional

import javax.annotation.PostConstruct

@Transactional
class AuthorizationService {

    static transactional = false

    def grailsApplication

    private AuthorizationHeaderGenerator generator
    private AuthorizationHeaderGenerator.KeyAgreement keyAgreement
    private RestBuilder rest

    @PostConstruct
    void init() {
        String publicKey = grailsApplication.config.cmsManager.publicKey
        String privateKey = grailsApplication.config.cmsManager.privateKey
        String secret = grailsApplication.config.cmsManager.secret

        if (privateKey && publicKey && secret) {
            rest = new RestBuilder()
            keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()

            keyAgreement.setPrivateKey(privateKey)
            keyAgreement.setPublicKey(publicKey)
            keyAgreement.setSecret(secret)

            generator = new AuthorizationHeaderGenerator("syndication_api_key", keyAgreement)
        }
    }

    String hashBody(String body){
        generator.hashData(body)
    }

    boolean checkAuthorization(Map thirdPartyRequest) {
        def authorizationRequest = (thirdPartyRequest as JSON).toString()
        log.debug "I am requesting:\n ${authorizationRequest}"

        String date = new Date().toString()
        String requestUrl = grailsApplication.config.cmsManager.serverUrl + grailsApplication.config.cmsManager.verifyAuthPath
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([
            date: date,
            "content-type": "application/json",
            "content-length": authorizationRequest.bytes.size() as String
        ],
            requestUrl, "POST", authorizationRequest)

        def resp = rest.post(requestUrl) {
            header 'Date', date
            header 'Authorization', apiKeyHeaderValue

            json thirdPartyRequest
        }

        resp.status == 204
    }

    boolean amIAuthorized() {
        String date = new Date().toString()
        String requestUrl = grailsApplication.config.cmsManager.serverUrl + grailsApplication.config.cmsManager.selfAuthPath
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([date: date], requestUrl, "GET", null)
        def resp = rest.get(requestUrl) {
            header 'Date', date
            header 'Authorization', apiKeyHeaderValue
        }

        resp.json.isSecure as Boolean
    }
}
