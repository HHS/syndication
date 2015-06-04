package com.ctacorp.syndication.delivery.handler

import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

class AuthHeaderTest extends Specification {

    def authHeader

    def setup() {

        def keyAgreementFile = new File(AuthHeader.USER_HOME + File.separator + AuthHeader.KEY_AGREEMENT_FILENAME)

        if(!keyAgreementFile.exists()) {
            keyAgreementFile.write(new ClassPathResource('syndication_key_agreement.json').inputStream.text)
        }

        authHeader = new AuthHeader()
    }

    def 'generate the required header fields'() {

        when: "generating the required headers for API key authentication"

        def headers = authHeader.create('spamburger!')

        then: "the headers should contain the required values"

        headers['Date'] != null
        headers['Content-Type'] == null
        headers['Content-Length'] == null
        headers['Referer'] == 'http://localhost:9992'
        (headers['Authorization'] as String).startsWith('syndication_api_key 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000=:')
    }
}
