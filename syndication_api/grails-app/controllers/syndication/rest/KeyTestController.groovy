/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


package syndication.rest

import grails.converters.JSON
import grails.util.Holders

class KeyTestController {
    def authorizationService

    def index() {
        def url = Holders.config.API_SERVER_URL + request.forwardURI[request.contextPath.size()..-1]
        def dateHeader = request.getHeader("date")

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
            response.sendError(400, "Not Authorized")
            return
        }
        response.sendError(200, "Key is Authorized!")
    }
}