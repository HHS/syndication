

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
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.data.SourceHolder
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.mime.MimeType
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Embedded
import com.ctacorp.syndication.api.Message
import com.ctacorp.syndication.api.Meta
import com.ctacorp.syndication.api.Pagination

@API(swaggerDataPath = "/sources", description = "Information about sources.", modelRefs = [Source, ApiResponse, Meta, Pagination, Message], modelExtensions = [
    @ModelExtension(id="Sources", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="Source", required = true)]),
    ], removeProperties = ["results"])
])
@Transactional(readOnly = true)
class SourcesController {
    static allowedMethods = [
        list: 'GET',
        save: 'POST'
    ]

    static responseFormats = ['json']

    static defaultAction = "list"

    def sourceService
    def mediaService

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @APIResource(path = "/resources/sources/{id}.json", description = "Information about a specific source.", operations = [
        @Operation(httpMethod = "GET", notes="Returns the Source identified by the 'id'.", nickname="getSourceById", type = "Sources", summary = "Get Source by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id", type="integer", format="int64", description = "The id of the source to look up", required = true, paramType = "path")
        ])
    ])
    def show(Long id) {
        def sourceInstance = Source.read(id)
        if(!sourceInstance){
            response.setStatus(400)
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        respond ApiResponse.get200Response([sourceInstance]).autoFill(params)
    }

    @APIResource(path = "/resources/sources.json", description = "Source Listings", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of Sources.", nickname="getSources", type = "Sources", summary = "Get Sources", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "max",    type="integer", format="int32",         description = "The maximum number of records to return",                required = false, paramType = "query"),
            @Parameter(name = "offset", type="integer", format="int32",         description = "The offset of the records set to return for pagination", required = false, paramType = "query"),
            @Parameter(name = "sort",   type = "string",    description = "* Set of fields to sort the records by.",                required = false, paramType = "query")
        ])
    ])
    def list() {
        def sourceList = sourceService.listSources(params)
        params.total = sourceList.totalCount
        respond ApiResponse.get200Response(sourceList).autoFill(params)
    }

    @APIResource(path="/resources/sources/{id}/syndicate.{format}", description="MediaItem", operations=[
            @Operation(httpMethod="GET", notes="Renders the list of MediaItems associated with the Source identified by the 'id'.", nickname="syndicate", type = "MediaItems", summary = "Get MediaItems for Source", responseMessages=[
                    @ResponseMessage(code = 400, description = "Bad Request"),
                    @ResponseMessage(code = 500, description = "Internal Server Error")
            ], parameters = [
                    @Parameter(name = "id",          type="integer", format="int64", description = "The id of the record to look up", required = true, paramType = "path"),
                    @Parameter(name="displayMethod", type="string",                  description="Method used to render an html request. Accepts one: [mv, list, feed]", required=false, paramType = "query")
            ])
    ])
    def syndicate(Long id){
        def sourceInstance = Source.read(id)
        if(!sourceInstance){
            response.setStatus(400)
            respond ApiResponse.get400NotFoundResponse()
            return
        }

        params.controllerContext = { model ->
            g.render template: "/media/mediaViewer", model:model
        }

        String content = mediaService.renderMediaForSource(sourceInstance, params)
        response.withFormat {
            html{
                render text: content, contentType: MimeType.HTML.name
            }
            json{
                def resp = new Embedded(id:id, content:content, name: sourceInstance.name, description: "Media belonging to '${sourceInstance.name}'")
                respond ApiResponse.get200Response([resp]).autoFill(params)
            }
        }
    }

    def embed(Long id){
        String sourceName
        if(!id || !(sourceName = Source.get(id))){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        String renderedResponse
        String url = grailsApplication.config.grails.serverURL + "/api/v2/resources/sources/${id}"
        SourceHolder sourceHolder = new SourceHolder([id:id, name:sourceName])
        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                renderedResponse = mediaService.renderMediaViewerSnippet(sourceHolder, params)
                break
            case "feed":
            case "list":
            default:
                if(params.flavor && params.flavor.toLowerCase() == "iframe") {
                    renderedResponse = mediaService.renderIframeSnippet(url, params)
                } else{
                    renderedResponse = mediaService.renderJSSnippet(url, sourceHolder, params)
                }
        }

        withFormat {
            html {
                render renderedResponse
                return
            }
            json {
                response.contentType = 'application/json'
                respond ApiResponse.get200Response([[snippet:renderedResponse]]).autoFill(params)
                return
            }
        }
    }
}
