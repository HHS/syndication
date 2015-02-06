package com.ctacorp.syndication.delivery.handler

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import groovy.json.JsonSlurper
import groovy.util.logging.Log

import java.util.logging.Level

@Log
class AuthHeader {

    static final String KEY_AGREEMENT_FILENAME = 'syndication_key_agreement.json'
    static final String KEY_AGREEMENT_ENCRYPTED_FILENAME = 'syndication_key_agreement.dat'
    static final String PRIVATE_KEY = 'syndication.pem'
    static final String USER_HOME = Config.USER_HOME

    static authHeaderGenerator

    static {

        def keyAgreementText = null

        def encryptedKeyAgreement = new File(USER_HOME + File.separator + KEY_AGREEMENT_ENCRYPTED_FILENAME)
        def privateKey = new File(USER_HOME + File.separator + '.ssh' + File.separator + PRIVATE_KEY)

        try {

            if(!encryptedKeyAgreement.exists()) {
                log.warning("external key agreement (${encryptedKeyAgreement.path}) not found")
            }

            if(!privateKey.exists()) {
                log.warning("private key (${privateKey.path}) not found")
            }

            if(encryptedKeyAgreement.exists() && privateKey.exists()) {

                def command = "openssl smime -decrypt -in ${encryptedKeyAgreement.path} -binary -inform DEM -inkey ${privateKey.path}"

                def proc = command.execute()
                proc.waitFor()
                if(proc.exitValue() == 0) {
                    keyAgreementText = proc.in.text
                }
            }

        } catch (exception) {
            log.log(Level.SEVERE, "Encountered an error when trying to load external key agreement (${encryptedKeyAgreement.path})", exception)
        }

        if(!keyAgreementText) {
            log.info("Using internal key agreement as '${USER_HOME + File.separator + KEY_AGREEMENT_ENCRYPTED_FILENAME}' was not found or could not be loaded")
            keyAgreementText = Thread.currentThread().getContextClassLoader().getResourceAsStream(KEY_AGREEMENT_FILENAME).text
        }

        def jsonKeyAgreement = new JsonSlurper().parseText(keyAgreementText)

        AuthorizationHeaderGenerator.KeyAgreement keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()
        keyAgreement.secret = jsonKeyAgreement['Secret Key']
        keyAgreement.publicKey = jsonKeyAgreement['Public Key']
        keyAgreement.privateKey = jsonKeyAgreement['Private Key']

        authHeaderGenerator = new AuthorizationHeaderGenerator(Config.API_KEY_NAME as String, keyAgreement)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    def create(content) {

        def headers = [:]

        // Set the required headers to compute the API key
        headers['Referer'] = Config.SERVER_BASE_URL
        headers['Date'] = new Date() as String
        headers['Content-Type'] = 'application/json'
        headers['Content-Length'] = content.bytes.length as String

        def apiKeyValue = authHeaderGenerator.getApiKeyHeaderValue(headers, Config.PUBLISH_URL, 'POST', content)
        headers[Config.API_KEY_HEADER] = apiKeyValue
        log.info("Authorization header value is '${apiKeyValue}'")

        // Remove the content type and content-length headers as RestClient will set these
        headers.remove("Content-Type")
        headers.remove("Content-Length")

        return headers
    }
}
