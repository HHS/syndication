/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package syndication

import grails.converters.JSON
import grails.util.Environment
import syndication.api.ApiResponse

class SecurityFilters {
    def authorizationService
    def grailsApplication

    def filters = {
        if (Environment.getCurrent() == Environment.PRODUCTION || grailsApplication.config.syndication.cms.auth.enabled) {
            mediaPosts(controller: 'media', action: 'save*') {
                before = {
                    handleAuthRequest(request)
                }

                after = { Map model -> }
                afterView = { Exception e -> }
            }

            cache(controller:'cacheAccess', action:'*'){
                before = {
                    handleAuthRequest(request)
                }

                after = { Map model -> }
                afterView = { Exception e -> }
            }
        }
    }

    private boolean handleAuthRequest(request){
        def dateHeader = request.getHeader("date")
        boolean authorized = checkAuthorization(dateHeader, request)

        if (!authorized) {
            fail()
            return false
        }

        success(request)
        return true
    }

    private boolean success(request){
        log.info("Authentication succeeded for request ${request}")
    }

    private boolean fail(){
        response.status = 400
        response.contentType = "application.json"
        render ApiResponse.get400NotAuthorizedResponse().autoFill(params) as JSON
    }

    private boolean checkAuthorization(dateHeader, request){
        def url = grailsApplication.config.grails.serverURL + request.forwardURI[request.contextPath.size()..-1]

        def authHeaders = [
                authorizationHeader: request.getHeader("Authorization"),
                dateHeader         : dateHeader,
                contentTypeHeader  : request.getHeader("content-type"),
                contentLengthHeader: request.getHeader("Content-Length"),
                url                : url,
                httpMethod         : request.getMethod(),
                dataMd5            : authorizationService.hashBody(request.reader.text)
        ]

        def authorized = authorizationService.checkAuthorization(authHeaders)
        if(!authorized){
            log.error("Request (${request}) was not not authorized")
            log.error("Computed authHeaders were: \n${(authHeaders as JSON).toString(true)}")
        }

        authorized
    }
}