
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.render

import grails.rest.render.*
import grails.converters.*
import org.codehaus.groovy.grails.web.mime.MimeType
import syndication.api.ApiResponse

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 12/19/13
 * Time: 4:01 PM
 */
class ApiResponseRenderer extends AbstractRenderer<ApiResponse>{
    ApiResponseRenderer() {
        super(ApiResponse, [MimeType.JSON, MimeType.TEXT_JSON] as MimeType[])
    }

    void render(ApiResponse object, RenderContext context) {
        String charset = ";charset=UTF-8"
        JSON json = object as JSON
        json.encoding = "UTF-8"
        Writer writer = context.writer

        if(object.callback){
            context.contentType = "application/javascript${charset}"
            writer.write "${object.callback}(${json});"
            writer.flush()
        } else{
            context.contentType = MimeType.JSON.name + charset
            json.render(writer)
        }
    }
}
