
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
import org.grails.web.errors.GrailsWrappedRuntimeException
import com.ctacorp.syndication.api.ApiResponse

class ErrorController {
    static responseFormats = ["JSON"]
    def errorService

    def fivehundred() {
        GrailsWrappedRuntimeException exception = request.exception

        if(exception?.getMessage()?.contains("could not resolve property:")){
            redirect action: fourhundred()
            return
        }

        def status = 500
        def exceptionResp = { ApiResponse.get500ResponseForException(exception) as JSON }

        try{
            (status,exceptionResp) = errorService.checkInternalError(request.parameterMap, exception)
        }catch(e){log.error(e.getMessage())}

        response.status = status
        response.contentType = "application/json"
        try{
            render exceptionResp()
            return
        } catch(e){
            log.error(e)
            e.printStackTrace()
        }
    }

    def fourhundred(){
        GrailsWrappedRuntimeException exception = request.exception
        def status = 400
        response.status = status
        response.contentType = "application/json"
        render ApiResponse.get400ResponseForException(exception).autoFill(params) as JSON
    }

    def unauthorized(){
        response.status = 400
        response.contentType = "application/json"
        render ApiResponse.get400NotAuthorizedResponse().autoFill(params) as JSON
    }
}