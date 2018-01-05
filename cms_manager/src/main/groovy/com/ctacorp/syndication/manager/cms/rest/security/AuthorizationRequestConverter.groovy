package com.ctacorp.syndication.manager.cms.rest.security

abstract class AuthorizationRequestConverter {

    private objectToConvert

    abstract String getAuthorizationHeader()
    abstract String getDateHeader()
    abstract String getContentTypeHeader()
    abstract String getContentLengthHeader()
    abstract String getDataMd5()
    abstract String getUrl()
    abstract String getHttpMethod()
    abstract boolean canConvert()

    def getObjectToConvert() {
        return objectToConvert
    }

    AuthorizationRequest convert(obj) {

        objectToConvert = obj

        if(canConvert()) {
            def authorizationRequest = new AuthorizationRequest()
            authorizationRequest.authorizationHeader = getAuthorizationHeader()
            authorizationRequest.dateHeader = getDateHeader()
            authorizationRequest.contentTypeHeader = getContentTypeHeader()
            authorizationRequest.contentLengthHeader = getContentLengthHeader()
            authorizationRequest.url = getUrl()
            authorizationRequest.httpMethod = getHttpMethod()
            authorizationRequest.dataMd5 = getDataMd5()
            return authorizationRequest
        }

        null
    }
}
