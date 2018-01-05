

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
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.data.SourceHolder
import grails.transaction.Transactional
import grails.util.Holders
import grails.web.mime.MimeType
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Embedded
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'sources', description = 'Information about tags')
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
    def config = Holders.config

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/sources/{id}.json', operations = [
            @Operation(method = GET, description = "Information about a specific source.", summary = "Get Source by ID", responses = [
                    @Response(code = 200, description = "Returns the Source identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfSources', type = DataSchemaType.ARRAY, reference = '#/definitions/SourceWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the source to look up', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['sources']),
    ])
    def show(Long id) {
        def sourceInstance = Source.read(id)
        if(!sourceInstance){
            response.setStatus(400)
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        respond ApiResponse.get200Response([sourceInstance]).autoFill(params), view:"index"
    }

    @Path(path = '/resources/sources.json', operations = [
            @Operation(method = GET, description = "Source Listings", summary = "Get Sources", responses = [
                    @Response(code = 200, description = "Returns the list of Sources.", schema = @DataSchema(title = 'ArrayOfSources', type = DataSchemaType.ARRAY, reference = '#/definitions/SourceWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
            ], tags = ['sources']),
    ])
    def list() {
        def sourceList = sourceService.listSources(params)
        params.total = sourceList.totalCount
        respond ApiResponse.get200Response(sourceList).autoFill(params), view:"index"
    }

    @Path(path = '/resources/sources/{id}/syndicate.{format}', operations = [
            @Operation(method = GET, description = "MediaItem", summary = "Get MediaItems for Source", responses = [
                    @Response(code = 200, description = "Renders the list of MediaItems associated with the Source identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfSyndicatedItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'displayMethod', type = ParameterType.STRING, description = 'Method used to render an html request. Accepts one: [mv, list, feed]', required = false),
            ], tags = ['sources']),
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
                respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
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
        String url = config.API_SERVER_URL + "/resources/sources/${id}"
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
