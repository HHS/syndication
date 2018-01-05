package com.ctacorp.syndication.manager.cms.utils.exception

class RestDeliveryException extends Exception {

    RestDeliveryException(String message) {
        super(message)
    }

    RestDeliveryException(String message, Throwable cause) {
        super(message, cause)
    }
}
