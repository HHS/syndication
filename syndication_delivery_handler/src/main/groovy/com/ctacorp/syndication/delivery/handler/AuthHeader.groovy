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

        def keys = new JsonSlurper().parseText(keyAgreementText)
        def keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement(secret: keys['Secret Key'], publicKey: keys['Public Key'])

        authHeaderGenerator = new AuthorizationHeaderGenerator(Config.API_KEY_NAME as String, keyAgreement)
    }

    def create(String content) {

        def headers = [:]

        // Set the required headers to compute the API key
        headers.Date = new Date() as String
        headers.'Content-Type' = 'application/json; charset=utf-8'
        headers.'Content-Length' = content.getBytes('UTF-8').length as String

        def apiKeyHeader = authHeaderGenerator.getApiKeyHeaderValue(headers, Config.PUBLISH_URL, 'POST', content)
        headers[Config.API_KEY_HEADER] = apiKeyHeader
        log.info("Authorization header is '${apiKeyHeader}'")

        headers
    }
}
