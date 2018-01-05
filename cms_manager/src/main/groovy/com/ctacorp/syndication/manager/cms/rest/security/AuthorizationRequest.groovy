package com.ctacorp.syndication.manager.cms.rest.security

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import grails.validation.Validateable

class AuthorizationRequest implements Validateable {

    String authorizationHeader
    String dateHeader
    String contentTypeHeader
    String contentLengthHeader
    String url
    String httpMethod
    String dataMd5

    static constraints = {
        authorizationHeader nullable: false
        dateHeader nullable: false
        contentTypeHeader nullable: true
        contentLengthHeader nullable: true
        dataMd5 nullable: true
        url nullable: false
        httpMethod nullable: false
    }

    @Override
    String toString() {
        ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
    }
}
