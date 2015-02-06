package com.ctacorp.syndication.manager.cms.rest.security

import grails.validation.Validateable

@Validateable
class AuthorizationRequest {

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
}
