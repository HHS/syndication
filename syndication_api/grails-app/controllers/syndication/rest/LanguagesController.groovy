
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import com.ctacorp.syndication.api.ApiResponse
import grails.transaction.Transactional
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.grails.swagger.annotations.*
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'languages', description = 'Information about languages')
@Transactional(readOnly = true)
class LanguagesController {
    static allowedMethods = [
        list: 'GET',
        save: 'POST'
    ]

    static responseFormats = ['json']
    static defaultAction = "list"
    def languagesService

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/languages/{id}.json', operations = [
            @Operation(method = GET, description = "Information about a specific language", summary = "Get Language by ID", responses = [
                    @Response(code = 200, description = "Returns the Language identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfLanguages', type = DataSchemaType.ARRAY, reference = '#/definitions/LanguageWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the language to look up', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['languages']),
    ])
    def show(Language languageInstance) {
        if(!languageInstance){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified record could not be found")
            return
        }
        respond ApiResponse.get200Response([languageInstance]).autoFill(params), view:"index"
    }

    @Path(path = '/resources/languages.json', operations = [
            @Operation(method = GET, description = "Language Listings", summary = "Get Languages", responses = [
                    @Response(code = 200, description = "Returns the list Languages.", schema = @DataSchema(title = 'ArrayOfLanguages', type = DataSchemaType.ARRAY, reference = '#/definitions/LanguageWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
            ], tags = ['languages']),
    ])
    def list() {
        def languageList = languagesService.listLanguages(params)
        params.total = languageList.totalCount
        respond ApiResponse.get200Response(languageList).autoFill(params), view:"index"
    }
}
