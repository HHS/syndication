package com.ctacorp.syndication.delivery.handler

import spock.lang.Specification

class AuthHeaderTest extends Specification {

    def authHeaders = new AuthHeader()

    def 'generate the required header fields'() {

        when: "generating the required headers for API key authentication"

        def headers = authHeaders.create('spamburger!')

        then: "the headers should contain the required values"

        headers['Date'] != null
        headers['Content-Type'] == null
        headers['Content-Length'] == null
        headers['Referer'] == 'http://localhost:9992'
        (headers['Authorization'] as String).startsWith('syndication_api_key 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000=:')
    }
}
