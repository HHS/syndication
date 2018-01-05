package com.ctac.tinyurl

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import grails.util.Holders

import javax.annotation.PostConstruct

@Transactional
class AuthorizationService {

    static transactional = false

    def config = Holders.config

    private AuthorizationHeaderGenerator generator
    private AuthorizationHeaderGenerator.KeyAgreement keyAgreement
    private RestBuilder rest

    @PostConstruct
    void init() {
        String publicKey = config.CMSMANAGER_PUBLICKEY
        String privateKey = config.CMSMANAGER_PRIVATEKEY
        String secret = config.CMSMANAGER_SECRET

        if (privateKey && publicKey && secret) {
            rest = new RestBuilder()
            rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
            keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()

            keyAgreement.setPublicKey(publicKey)
            keyAgreement.setSecret(secret)

            generator = new AuthorizationHeaderGenerator(Holders.config.apiKey.keyName ?: "syndication_api_key", keyAgreement)
        }
    }

    String hashBody(String body){
        generator.hashData(body)
    }

    boolean checkAuthorization(Map thirdPartyRequest) {
        def authorizationRequest = (thirdPartyRequest as JSON).toString()
        log.debug "I am requesting:\n ${authorizationRequest}"

        String date = new Date().toString()
        String requestUrl = config.CMSMANAGER_SERVER_URL + config.CMSMANAGER_VERIFYAUTHPATH
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([
            "Date": date,
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
        String requestUrl = config.CMSMANAGER_SERVER_URL + config.CMSMANAGER_SELFAUTHPATH
        String apiKeyHeaderValue = generator.getApiKeyHeaderValue([date: date], requestUrl, "GET", null)
        def resp = rest.get(requestUrl) {
            header 'Date', date
            header 'Authorization', apiKeyHeaderValue
        }

        resp.json.isSecure as Boolean
    }
}
