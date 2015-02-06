
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import grails.transaction.Transactional
import com.ctacorp.syndication.*
import syndication.api.ApiResponse
import syndication.api.Meta
import syndication.api.Pagination
import syndication.api.Message
import com.ctacorp.grails.swagger.annotations.*

@API(swaggerDataPath = "/languages", description = "Information about languages", modelRefs = [Language, ApiResponse, Meta, Pagination, Message], modelExtensions = [
    @ModelExtension(id="Languages", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="Language", required = true)]),
    ], removeProperties = ["results"])
])
@Transactional(readOnly = true)
class LanguagesController {
    static allowedMethods = [
        list: 'GET',
        save: 'POST'
    ]

    static responseFormats = ['json']
    static defaultAction = "list"
    def languagesService

    @APIResource(path = "/resources/languages/{id}.json", description = "Information about a specific language", operations = [
        @Operation(httpMethod = "GET", notes="Returns the Language identified by the 'id'.", nickname="getLanguageById", type = "Languages", summary = "Get Language by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id", type = "integer", format = "int64", description = "The id of the language to look up", required = true, paramType = "path")
        ])
    ])
    def show(Language languageInstance) {
        if(!languageInstance){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified record could not be found")
            return
        }
        respond ApiResponse.get200Response([languageInstance]).autoFill(params)
    }

    @APIResource(path = "/resources/languages.json", description = "Language Listings", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list Languages.", nickname="getLanguages", type = "Languages", summary = "Get Languages", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "max",    type = "integer", format = "int32", description="The maximum number of records to return",                required = false, paramType = "query"),
            @Parameter(name = "offset", type = "integer", format = "int32", description="The offset of the records set to return for pagination", required = false, paramType = "query"),
            @Parameter(name = "sort",   type = "string",                    description = "* Set of fields to sort the records by.",              required = false, paramType = "query")
        ])
    ])
    def list() {
        def languageList = languagesService.listLanguages(params)
        params.total = languageList.totalCount
        respond ApiResponse.get200Response(languageList).autoFill(params)
    }
}
