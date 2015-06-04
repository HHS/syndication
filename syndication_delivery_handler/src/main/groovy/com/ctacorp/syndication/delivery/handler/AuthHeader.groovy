package com.ctacorp.syndication.delivery.handler
import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import groovy.json.JsonSlurper
import groovy.util.logging.Log

@Log
class AuthHeader {

    static final String KEY_AGREEMENT_FILENAME = 'syndication_key_agreement.json'
    static final String USER_HOME = Config.USER_HOME
    static final File KEY_AGREEMENT_FILE = new File(USER_HOME + File.separator + KEY_AGREEMENT_FILENAME)

    static authHeaderGenerator

    static {

        def keyAgreementText = {

            if (!KEY_AGREEMENT_FILE.exists()) {
                log.info("Using internal key agreement as '${USER_HOME + File.separator + KEY_AGREEMENT_FILENAME}' was not found or could not be loaded")
                return Thread.currentThread().getContextClassLoader().getResourceAsStream(KEY_AGREEMENT_FILENAME).text
            }

            KEY_AGREEMENT_FILE.text
        }()

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
