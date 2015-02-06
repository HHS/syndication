package com.ctacorp.syndication.delivery.handler

import groovy.util.logging.Log

@Log
class Config {

    static final String USER_HOME = System.getenv("HOME")
    static final String CONFIG_FILENAME = 'syndication_delivery_handler_config.groovy'

    static String PUBLISH_URL
    static String SERVER_BASE_URL

    static final String API_KEY_NAME
    static final String API_KEY_HEADER

    static final Integer HTTP_CONNECTION_TIMEOUT
    static final Integer HTTP_SOCKET_TIMEOUT

    static {

        def configText = null

        try {
            configText = new File(USER_HOME + File.separator + CONFIG_FILENAME).text
        } catch (FileNotFoundException ignored) {
        }

        if(!configText) {
            log.info("Using internal config as '${USER_HOME + File.separator + CONFIG_FILENAME}' was not found")
            configText = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILENAME).text
        }

        def config = new ConfigSlurper().parse(configText)

        PUBLISH_URL = config.publish?.url ?: ""
        SERVER_BASE_URL = config.server?.base?.url ?: ""
        API_KEY_NAME = config?.api?.key?.name ?: ""
        API_KEY_HEADER = config?.api?.key?.header ?: ""
        HTTP_CONNECTION_TIMEOUT = (config?.http?.connection?.timeout ?: 30) * 1000
        HTTP_SOCKET_TIMEOUT = (config?.http?.socket?.timeout ?: 30) * 1000

        log.info("PUBLISH_URL is: ${PUBLISH_URL}")
        log.info("SERVER_BASE_URL is: ${SERVER_BASE_URL}")
        log.info("API_KEY_NAME is: ${API_KEY_NAME}")
        log.info("API_KEY_HEADER is: ${API_KEY_HEADER}")
        log.info("HTTP_CONNECTION_TIMEOUT is: ${HTTP_CONNECTION_TIMEOUT}")
        log.info("HTTP_SOCKET_TIMEOUT is: ${HTTP_SOCKET_TIMEOUT}")
    }
}
