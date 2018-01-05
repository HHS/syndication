package com.ctacorp.syndication.manager.cms.rest.security

import com.ctacorp.syndication.manager.cms.KeyAgreement
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.apache.commons.logging.LogFactory

class AuthorizationResult {

    static def log = LogFactory.getLog(this)

    Boolean isAuthorized = false
    KeyAgreement keyAgreement
    AuthorizationHeader authHeader
    String dataMd5
    String calculatedHash
    String canonicalizedHeaders
    String canonicalizedResource
    String requestMethod
    String signingString
    String rawAuthHeader
    String logFileId
    String errorMessage
    Integer httpStatus

    @Override
    String toString() {
        ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
    }
}
