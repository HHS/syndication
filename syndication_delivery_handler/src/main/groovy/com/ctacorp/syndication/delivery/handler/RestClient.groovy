package com.ctacorp.syndication.delivery.handler

import groovy.json.JsonSlurper;

class RestClient {

    def post(String content, Map<String,String> headers) {

        headers.Referer = Config.SERVER_BASE_URL

        def connection = new URL(Config.PUBLISH_URL).openConnection() as HttpURLConnection
        connection.setReadTimeout(Config.HTTP_SOCKET_TIMEOUT)
        connection.setConnectTimeout(Config.HTTP_CONNECTION_TIMEOUT)
        connection.setRequestMethod('POST')
        connection.setDoOutput(true)

        headers.entrySet().each { entry ->
            connection.setRequestProperty(entry.key, entry.value)
        }

        def output = new OutputStreamWriter(connection.getOutputStream(), 'UTF-8')
        output.write(content)
        output.close()

        def input = connection.getInputStream()
        def responseText = connection.getInputStream()?.getText('UTF-8')

        def response = new JsonSlurper().parseText(responseText)
        def status = connection.getResponseCode()

        input.close()

        [status, response]
    }
}