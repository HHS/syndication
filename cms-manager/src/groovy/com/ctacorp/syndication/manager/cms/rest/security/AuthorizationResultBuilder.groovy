package com.ctacorp.syndication.manager.cms.rest.security

import com.ctacorp.syndication.manager.cms.KeyAgreement
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class AuthorizationResultBuilder {

    def AuthorizationResult authorizationResult = new AuthorizationResult()

    private AuthorizationResultBuilder validateProperty(name, value) {
        if (!value) {
            throw new AuthorizationServiceException("value for property '${name}' was null or empty")
        }
        this
    }

    AuthorizationResultBuilder setAuthHeader(AuthorizationHeader authHeader) {
        authorizationResult.authHeader = authHeader
        validateProperty('authHeader', authHeader)
    }

    AuthorizationResultBuilder setKeyAgreement(KeyAgreement keyAgreement) {
        authorizationResult.keyAgreement = keyAgreement
        validateProperty('keyAgreement', keyAgreement)
    }

    AuthorizationResultBuilder setCalculatedHash(String calculatedHash) {
        authorizationResult.calculatedHash = calculatedHash
        validateProperty('calculatedHash', calculatedHash)
        if (authorizationResult.authHeader?.senderHash) {
            if (calculatedHash == authorizationResult.authHeader?.senderHash) {
                authorizationResult.isAuthorized = true
            }
        }
        this
    }

    AuthorizationResultBuilder setDataMd5(String dataMd5) {
        authorizationResult.dataMd5 = dataMd5
        validateProperty('dataMd5', dataMd5)
    }

    AuthorizationResultBuilder setCanonicalizedHeaders(String canonicalizedHeaders) {
        authorizationResult.canonicalizedHeaders = canonicalizedHeaders
        validateProperty('canonicalizedHeaders', canonicalizedHeaders)
    }

    AuthorizationResultBuilder setCanonicalizedResource(String canonicalizedResource) {
        authorizationResult.canonicalizedResource = canonicalizedResource
        validateProperty('canonicalizedResource', canonicalizedResource)
    }

    AuthorizationResultBuilder setRequestMethod(String requestMethod) {
        authorizationResult.requestMethod = requestMethod
        validateProperty('requestMethod', requestMethod)
    }

    AuthorizationResultBuilder setSigningString(String signingString) {
        authorizationResult.signingString = signingString
        validateProperty('signingString', signingString)
    }

    AuthorizationResultBuilder setRawAuthHeader(String rawAuthHeader) {
        authorizationResult.rawAuthHeader = rawAuthHeader
        validateProperty('rawAuthHeader', rawAuthHeader)
    }

    AuthorizationResultBuilder setLogFileId(logFileId) {
        authorizationResult.logFileId = logFileId as String
        validateProperty('logFileId', logFileId)
    }

    AuthorizationResultBuilder setErrorMessage(String errorMessage) {
        authorizationResult.errorMessage = errorMessage
        validateProperty('errorMessage', errorMessage)
    }

    AuthorizationResultBuilder setIsAuthorized(Boolean isAuthorized) {
        authorizationResult.isAuthorized = isAuthorized
        this
    }

    AuthorizationResultBuilder setHttpStatus(Integer httpStatus) {
        authorizationResult.httpStatus = httpStatus
        this
    }

    @Override
    String toString() {
        ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
    }
}
