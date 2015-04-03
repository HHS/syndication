
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
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.Source
import syndication.api.ApiResponse
import syndication.api.Message
import syndication.api.Meta
import syndication.api.Pagination

@API(swaggerDataPath = "/resources", description = "Global Search", modelRefs = [ApiResponse, Meta, Pagination, Message, MediaItem, Language, Source], modelExtensions = [
    @ModelExtension(id = "Resources", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results", attributes = [@PropertyAttribute(type = "array", typeRef = "ResourceLists", required = true)]),
    ], removeProperties = ["results"])
], models = [
    @Model(id = "ResourceLists", properties = [
        @ModelProperty(propertyName = "alternateImages", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "audios", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "campaigns", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "collections", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "htmls", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "images", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "infographics", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "languages", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "socialMedias", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "sources", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "tags", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "tagTypes", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "videos", attributes = [@PropertyAttribute(type = "ResourceList")]),
        @ModelProperty(propertyName = "widgets", attributes = [@PropertyAttribute(type = "ResourceList")])
    ]),
    @Model(id = "ResourceList", properties = [
        @ModelProperty(propertyName = "items", attributes = [@PropertyAttribute(type = "array", typeRef = "Resource", required = true)]),
        @ModelProperty(propertyName = "mediaType", attributes = [@PropertyAttribute(type = "string", required = true)])
    ]),
    @Model(id = "Resource", properties = [
        @ModelProperty(propertyName = "id", attributes = [@PropertyAttribute(type = "integer", format = "int64")]),
        @ModelProperty(propertyName = "name", attributes = [@PropertyAttribute(type = "string", required = true)])
    ])
])
class ResourcesController {
    def resourcesService

    static responseFormats = ['json']

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @APIResource(path="/resources.json", description = "Global search", operations = [
        @Operation(httpMethod="GET", notes="Returns the list of Resources matching the search query 'q'.<p>The search query 'q' is a Lucene query.<br>The syntax for a Lucene query can be found <a href=\"http://lucene.apache.org/core/2_9_4/queryparsersyntax.html\">here</a>.", nickname="getResources", type = "Resources", summary = "Get Resources by search query", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid search query"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="q", type="string", description="The search query supplied by the user", required=true, paramType = "query")
        ])
    ])
    def index() {
        def searchResults = resourcesService.globalSearch(params)

        if (!searchResults || searchResults.total == []) {
            params.total = 0
            params.countOverRide = 0
            params.totalPageOverride = 0
        } else {
            params.total = searchResults.total
            params.countOverRide = searchResults.total
            params.totalPageOverride = 1
        }

        if(!searchResults){
            respond ApiResponse.get200Response([]).autoFill(params)
            return
        }

        respond ApiResponse.get200Response([searchResults]).autoFill(params)
    }
}
