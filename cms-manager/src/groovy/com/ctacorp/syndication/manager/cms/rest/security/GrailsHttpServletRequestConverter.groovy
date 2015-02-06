package com.ctacorp.syndication.manager.cms.rest.security

import grails.util.Holders
import org.bouncycastle.util.encoders.Hex

import javax.servlet.http.HttpServletRequest
import java.security.MessageDigest

class GrailsHttpServletRequestConverter extends AuthorizationRequestConverter {

    static grailsApplication
    String contextPath

    static {
        grailsApplication = Holders.grailsApplication
    }

    GrailsHttpServletRequestConverter() {
        this.contextPath = Holders.servletContext.contextPath
    }

    @Override
    String getAuthorizationHeader() {
        def httpServletRequest = getObjectToConvert() as HttpServletRequest
        httpServletRequest.getHeader('Authorization')
    }

    @Override
    String getDateHeader() {
        def request = getObjectToConvert() as HttpServletRequest
        return request.getHeader('date')
    }

    @Override
    String getContentTypeHeader() {
        def request = getObjectToConvert() as HttpServletRequest
        return request.getHeader('content-type')
    }

    @Override
    String getContentLengthHeader() {
        def request = getObjectToConvert() as HttpServletRequest
        return request.getHeader('content-length')
    }

    @Override
    String getDataMd5() {
        def request = getObjectToConvert() as HttpServletRequest
        def data = request.inputStream.bytes
        def digest = MessageDigest.getInstance("MD5", "BC")
        new String(Hex.encode(digest.digest(data)))
    }

    @SuppressWarnings("GrUnresolvedAccess")
    @Override
    String getUrl() {
        def request = getObjectToConvert() as HttpServletRequest
        return grailsApplication.config.grails.serverURL + request.forwardURI[request.contextPath.size()..-1]
    }

    @SuppressWarnings("GrUnresolvedAccess")
    @Override
    String getHttpMethod() {
        def request = getObjectToConvert() as HttpServletRequest
        request.method
    }

    @Override
    boolean canConvert() {
        return getObjectToConvert() instanceof HttpServletRequest
    }
}
