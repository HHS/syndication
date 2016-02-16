/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResponseFormatter
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResult
import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders

import javax.servlet.http.HttpServletRequest

class SecurityFilters {

    def filteredUri = Holders.config.apiKey.filteredUri
    def env = Environment.current

    def authorizationService
    def loggingService

    def filters = {

        someURIs(uri: filteredUri) {

            before = {

                setSubscriberRequestAttribute(request)

                if (env == Environment.PRODUCTION || Holders.config.syndication.cms.auth.enabled) {

                    try {

                        AuthorizationResult authorizationResult = authorizationService.authorize(request)

                        if (!authorizationResult.isAuthorized) {
                            def formattedResponse = AuthorizationResponseFormatter.formatResponse(authorizationResult)
                            render status: authorizationResult.httpStatus, contentType: 'application/json', text: formattedResponse
                            return false
                        }

                    } catch (e) {

                        def errorMessage = loggingService.logError('Unexpected error occurred when trying to authorize request', e)
                        def json = ([message: errorMessage] as JSON).toString(true)
                        render status: 500, contentType: 'application/json', text: json, encoding: "UTF-8"
                        return false
                    }

                } else {
                    log.info("Skipping auth check because the environment is ${env}")
                }

                return true
            }
        }

        someURIs(controller: 'authorization') {

            before = {

                setSubscriberRequestAttribute(request)
                String senderPublicKey = request.getAttribute('senderPublicKey')

                if (senderPublicKey == Subscriber.findByIsPrivileged(true).keyAgreement.entity2PublicKey) {
                    return true
                } else {
                    render status: 401, text: ([message: "Unauthorized: the sender's public key is not valid"] as JSON).toString(), encoding: "UTF-8", contentType: 'application/json'
                    return false
                }
            }
        }
    }

    private static void setSubscriberRequestAttribute(HttpServletRequest request) {

        def rawAuthHeader = request.getHeader("Authorization")
        def authHeader = ApiKeyUtils.getAuthHeader(rawAuthHeader, System.currentTimeMillis())
        request.setAttribute("senderPublicKey", authHeader?.senderPublicKey)
    }
}
