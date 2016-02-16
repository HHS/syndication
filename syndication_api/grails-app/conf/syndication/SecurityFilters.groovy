/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package syndication

import grails.converters.JSON
import grails.util.Environment
import com.ctacorp.syndication.api.ApiResponse

class SecurityFilters {
    def authorizationService
    def grailsApplication

    def filters = {
//        //log the header values for debugging
//        allTheStuff(controller:'*', action:'*'){
//            before = {
//                String headers = ""
//                request.getHeaderNames().each { name ->
//                    headers += "$name: ${request.getHeader(name)}"
//                }
//                log.debug headers
//            }
//        }
        addHeader(controller:'*', action:'*') {
            before={
                response.addHeader("Access-Control-Allow-Origin", "*")
            }
        }

        if (Environment.getCurrent() == Environment.PRODUCTION || grailsApplication.config.syndication.cms.auth.enabled) {
            mediaPosts(controller: 'media', action: 'save*') {
                before = {
                    handleAuthRequest(owner, request)
                }

                after = { Map model -> }
                afterView = { Exception e -> }
            }

            cache(controller:'cacheAccess', action:'*'){
                before = {
                    handleAuthRequest(owner, request)
                }

                after = { Map model -> }
                afterView = { Exception e -> }
            }
        }
    }

    private boolean handleAuthRequest(filter, request){
        boolean authorized = checkAuthorization(request)
        if (!authorized) {
            def url = "Requested URL ------------------\n${grailsApplication.config.grails.serverURL}${request.forwardURI[request.contextPath.size()..-1]}"
            String requestHeaders = "Headers --------------------\n"
            request.getHeaderNames().each { name ->
                requestHeaders += " -> ${name}:${request.getHeader(name)}\n"
            }
            def body = "Body ---------------------\n${request.reader.text}"
            log.info("Not Authorized: Request: \n${requestHeaders}\n${url}\n${body}")
            filter.redirect(controller: "error", action: "unauthorized")
            return false
        }

        success(request)
        true
    }

    private boolean success(request){
        try {
            log.info("Authentication succeeded for request: ${request.reader.text}")
        } catch(ignore){
            log.info("Authentication succeeded for request: ${request.forwardURI}")
        }
    }

    private boolean checkAuthorization(request){
        //Get the requested URL
        def url = grailsApplication.config.grails.serverURL + request.forwardURI[request.contextPath.size()..-1]
        log.debug "API: RequestURL: ${url}"

        def authHeaders = [
                authorizationHeader: request.getHeader("Authorization"),
                dateHeader         : request.getHeader("date"),
                contentTypeHeader  : request.getHeader("content-type"),
                contentLengthHeader: request.getHeader("Content-Length"),
                url                : url,
                httpMethod         : request.getMethod(),
                dataMd5            : authorizationService.hashBody(request.reader.text)
        ]

        log.debug "API: AuthHeaders as seen before authorization check: ${authHeaders}"

        def authorized = authorizationService.checkAuthorization(authHeaders)
        log.debug "API: Authorized?: ${authorized}"
        if(!authorized){
            log.error("Request was not not authorized")
            log.error("Computed authHeaders were: \n${(authHeaders as JSON).toString(true)}")
        }

        authorized
    }
}