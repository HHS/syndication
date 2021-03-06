
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.api

import com.ctacorp.syndication.media.MediaItem
import org.grails.web.errors.GrailsWrappedRuntimeException

class ApiResponse {

    Meta meta = new Meta()
    Collection results = []
    String callback = null

    //200s --------------------------------------------------------------------

    static ApiResponse get200Response(Collection results = []){
        ApiResponse response = new ApiResponse(results:results)
        response
    }

    static ApiResponse get200ResponseCustomUserMessage(String userMessage, Collection results){
        ApiResponse response = new ApiResponse(results:results)
        response.meta.addUserMessage(userMessage)
        response
    }

    static ApiResponse get200ResponseCustomUserMessage(String userMessage){
        ApiResponse response = new ApiResponse()
        response.meta.addUserMessage(userMessage)
        response
    }


    //400s ------------------------------------------------------------------

    static ApiResponse get400Response(){
        ApiResponse response = new ApiResponse()
        response.meta.status = 400
        response
    }

    static ApiResponse get400ContentNotExtractableErrorResponse(){
        default400ForMessage(Message.getContentNotExtractableError())
    }

    static ApiResponse get400ContentUnretrievableResponse(){
        default400ForMessage(Message.getContentUnretrievableMessage())
    }

    static ApiResponse  get400ResponseForException(GrailsWrappedRuntimeException exception){
        default400ForMessage(Message.get400MessageForException(exception))
    }

    static ApiResponse get400InvalidInstanceErrorResponse(MediaItem mi){
        default400ForMessages(Message.getFieldConstraintErrors(mi))
    }

    static ApiResponse get400InvalidField(){
        default400ForMessage(Message.getFieldConstraintError())
    }

    static ApiResponse get400InvalidResourceForTypeErrorResponse(){
        default400ForMessage(Message.getInvalidResourceForTypeError())
    }

    static ApiResponse get400NotAuthorizedResponse(){
        default400ForMessage(Message.getNotAuthorizedError())
    }

    static ApiResponse get400NotFoundResponse(){
        default400ForMessage(Message.getGeneric404Message())
    }

    static ApiResponse get400ResponseCustomMessage(Message message){
        default400ForMessage(message)
    }

    static ApiResponse get400ResponseCustomMessage(String message){
        default400ForMessage(Message.userMessage(message))
    }

    //500s -----------------------------------------------------------------

    static ApiResponse get500Response(){
        ApiResponse response = new ApiResponse()
        response.meta.status = 500
        response
    }

    static ApiResponse get500ResponseCustomMessage(Message message){
        default500ForMessage(message)
    }

    static ApiResponse  get500ResponseForException(GrailsWrappedRuntimeException exception){
        default500ForMessage(Message.get500MessageForException(exception))
    }

    private static ApiResponse default400ForMessage(Message message){
        ApiResponse response = new ApiResponse()
        response.meta.status = 400
        response.meta.setMessage(message)
        response
    }

    private static ApiResponse default400ForMessages(List<Message> messages){
        ApiResponse response = new ApiResponse()
        response.meta.status = 400
        response.meta.setMessages(messages)
        response
    }

    private static ApiResponse default500ForMessage(Message message){
        ApiResponse response = new ApiResponse()
        response.meta.status = 500
        response.meta.addMessage(message)
        response
    }

    ApiResponse autoFill(params){
        callback = params.callback
        meta.autoFill(params, results.size())
        this
    }

    Map generateMetaBlock() {
        [
                status:meta?.status,
                messages:meta?.generateMessagesBlock(),
                pagination:meta?.pagination?.generatePaginationBlock(),
        ]
    }

    String toString(){
        String ApiResponse = "Api Response:\n" +
                "callback:${callback}\n" +
                "meta:\n${meta}\n" +
                "results:"
        results.each{ r->
            ApiResponse += "  - ${r}\n"
        }

        ApiResponse
    }
}
