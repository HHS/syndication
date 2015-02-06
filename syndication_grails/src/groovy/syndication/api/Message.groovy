
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.api

import com.ctacorp.grails.swagger.annotations.Model
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.PropertyAttribute
import com.ctacorp.syndication.MediaItem
import org.codehaus.groovy.grails.web.errors.GrailsWrappedRuntimeException
import org.springframework.validation.FieldError
import syndication.error.Error

@Model(id = "Message", properties = [
    @ModelProperty(propertyName = "errorMessage",   attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "errorDetail",    attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "errorCode",      attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "userMessage",    attributes = [@PropertyAttribute(type = "string", required = true)])
])
class Message {

    String errorMessage = ""    //High level message
    def    errorDetail  = ""    //Stack trace or field error where applicable
    String errorCode    = ""    //Global error code for things
    String userMessage  = "Please report problems to the system administrator and include the provided date/time: ${new Date()}."

    static Message getGeneric404Message(){
        defaultMessagesForError Error.RECORD_NOT_FOUND
    }

    static Message getGeneric500Message(){
        defaultMessagesForError Error.GENERIC_SERVER_ERROR
    }

    static Message get500MessageForException(GrailsWrappedRuntimeException exception){
        Message m = defaultMessagesForError Error.GENERIC_SERVER_ERROR
        m.errorDetail = exception?.getStackTraceText()
        m.errorMessage = exception?.getMessage()
        m
    }

    static Message get400MessageForException(GrailsWrappedRuntimeException exception){
        Message m = defaultMessagesForError Error.GENERIC_SERVER_ERROR
        m.errorDetail = exception.getStackTraceText()
        m.errorMessage = exception.getMessage()
        m
    }

    static Message getContentNotExtractableError(){
        defaultMessagesForError Error.CONTENT_NOT_EXTRACTABLE
    }

    static Message getInvalidResourceForTypeError(){
        defaultMessagesForError Error.INVALID_RESOURCE_FOR_MEDIA_TYPE
    }

    static Message getNotAuthorizedError(){
        defaultMessagesForError Error.NOT_AUTHORIZED
    }

    static Message getFieldConstraintError(FieldError fieldError){
        Error error = Error.FIELD_CONSTRAINT_VIOLATION
        new Message(
            errorCode: error.code,
            errorMessage: error.name,
            errorDetail: ErrorDetail.getErrorDetail(fieldError),
            userMessage: error.message
        )
    }

    static List<Message> getFieldConstraintErrors(MediaItem mi){
        List<Message> messages = [] as List<Message>
        mi.errors.fieldErrors.each { FieldError fieldError ->
            messages << getFieldConstraintError(fieldError)
        }
        messages
    }

    static Message userMessage(String userMessage){
        new Message(userMessage: userMessage)
    }

    private static Message defaultMessagesForError(Error error){
        new Message(
                errorCode: error.code,
                errorMessage: error.name,
                userMessage: error.message
        )
    }

    def autoFill(params){
        throw new Exception("Method not Implemented")
    }
}
