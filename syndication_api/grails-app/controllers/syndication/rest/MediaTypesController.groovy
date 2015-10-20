
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import com.ctacorp.grails.swagger.annotations.*
import grails.transaction.Transactional
import grails.util.Holders
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Message
import com.ctacorp.syndication.api.Meta
import com.ctacorp.syndication.api.Pagination
import com.ctacorp.syndication.marshal.MediaTypeHolder

@API(swaggerDataPath = "/mediaTypes", description = "Information about media types", modelRefs = [ApiResponse, Meta, Pagination, Message], models = [
    @Model(id="MediaType", properties = [
        @ModelProperty(propertyName = "name",         attributes = [@PropertyAttribute(type = "string", required = true)]),
        @ModelProperty(propertyName = "description",  attributes = [@PropertyAttribute(type = "string", required = true)])
    ])], modelExtensions = [
      @ModelExtension(id="MediaTypes", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="MediaType", required = true)]),
    ], removeProperties = ["results"])
])
@Transactional(readOnly = true)
class MediaTypesController {
    def apiResponseBuilderService

    static responseFormats = ['json']

    static allowedMethods = [ list: 'GET']
    static defaultAction = "list"

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @APIResource(path = "/resources/mediaTypes.{format}", description = "Information about media types", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of available MediaTypes.", nickname="getMediaTypes", type = "MediaTypes", summary = "Get MediaTypes", responseMessages = [
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ])
    ])
    def list() {
        def types = getMediaTypes().sort{ it.name }
        params.sort = "name"
        params.order = "ASC"
        params.max = types.size()
        respond ApiResponse.get200Response(types).autoFill(params)
    }

    private getMediaTypes() {
        def mediaTypes = []

        Holders.grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.media.MediaItem") {
                mediaTypes << new MediaTypeHolder(name:it.clazz.simpleName, description: g.message(code: "syndication.mediaType.${it.clazz.simpleName}.description"))
            }
        }

        mediaTypes
    }
}
