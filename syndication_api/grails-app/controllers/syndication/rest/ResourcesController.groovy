/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import com.ctacorp.grails.swagger.annotations.*
import com.ctacorp.syndication.api.ApiResponse
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'resources', description = 'Global Search')
class ResourcesController {
    def resourcesService

    static responseFormats = ['json']

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources.json', operations = [
            @Operation(method = GET, description = "Global search", summary = "Get Resources by search query", responses = [
                    @Response(code = 200, description = "\"Returns the list of Resources matching the search query 'q'.<p>The search query 'q' is a Lucene query.<br>The syntax for a Lucene query can be found <a href=\"http://lucene.apache.org/core/2_9_4/queryparsersyntax.html\">here</a>.\"",
                            schema = @DataSchema(title = 'ArrayOfResources',
                                    type = DataSchemaType.ARRAY,
                                    reference = '#/definitions/ResourceWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'q', type = ParameterType.STRING, description = 'The search query supplied by the user', required = true),
            ], tags = ['resources']),
    ])
    def index() {

        def searchResults = resourcesService.globalSearch(params)

        params.total = searchResults.remove 'total'
        params.countOverRide = searchResults.remove 'count'
        params.totalPageOverride = Math.ceil(params.total?.toInteger() / params.max?.toInteger())?.intValue()

        if (!searchResults) {
            respond ApiResponse.get200Response([]).autoFill(params)
            return
        }

        respond ApiResponse.get200Response([searchResults]).autoFill(params)
    }
}
