package com.ctacorp.syndication.delivery.handler

import spock.lang.Specification

class ConfigTest extends Specification {

    def 'Config statically loads config'() {

        expect: 'PUBLISH_URL is not null'

        Config.PUBLISH_URL == 'http://localhost:8080/Syndication/api/v2/resources/media/htmls.json'

        and: 'SERVER_BASE_URL is not null'

        Config.SERVER_BASE_URL == 'http://localhost:9992'

        and: 'SERVER_BASE_URL is not null'

        Config.API_KEY_NAME == 'syndication_api_key'

        and: 'SERVER_BASE_URL is not null'

        Config.API_KEY_HEADER == 'Authorization'

        and: 'SERVER_BASE_URL is not null'

        Config.HTTP_CONNECTION_TIMEOUT == 30000

        and: 'SERVER_BASE_URL is not null'

        Config.HTTP_SOCKET_TIMEOUT == 30000
    }
}
