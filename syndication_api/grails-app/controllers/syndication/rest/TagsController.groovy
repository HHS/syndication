
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
import com.ctacorp.syndication.data.TagHolder
import com.ctacorp.syndication.marshal.MediaItemMarshaller
import com.ctacorp.syndication.media.MediaItem
import grails.transaction.NotTransactional
import grails.util.Holders
import grails.web.mime.MimeType
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Embedded
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'tags', description = 'Information about tags')
class TagsController {
    static allowedMethods = [
            list: 'GET',
            save: 'POST',
            listForTagId: 'GET',
            listForTagName: 'GET'
    ]

    static defaultAction = "list"

    static responseFormats = ['json']

    def tagsService
    def mediaService

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/tags/{id}.{format}', operations = [
            @Operation(method = GET, description = "Information about a specific tag", summary = "Get Tag by ID", responses = [
                    @Response(code = 200, description = "Returns the Tag identified by the 'id' in the specified 'format'.", schema = @DataSchema(title = 'ArrayOfTags', type = DataSchemaType.ARRAY, reference = '#/definitions/TagMarshallerWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['tags']),
    ])
    def show(Long id) {
        def result = tagsService.show(id, params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        respond ApiResponse.get200Response([result]).autoFill(params), view:"index"
    }

    @NotTransactional
    def tagSave() {
        def result = tagsService.tagMediaItemByName(params)

        respond ApiResponse.get200Response(result).autoFill(params)
    }

    @Path(path = '/resources/tags.{format}', operations = [
            @Operation(method = GET, description = "List of Tags", summary = "Get Tags", responses = [
                    @Response(code = 200, description = "Returns the list of Tags matching the specified query parameters in the specified 'format'.", schema = @DataSchema(title = 'ArrayOfTags', type = DataSchemaType.ARRAY, reference = '#/definitions/TagMarshallerWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
                    @Parameter(name = 'name', type = ParameterType.STRING, description = 'Return tags[s] matching the supplied name', required = false),
                    @Parameter(name = 'nameContains', type = ParameterType.STRING, description = 'Return tags which contain the supplied partial name.', required = false),
                    @Parameter(name = 'mediaId', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'Return tags associated with the supplied media id.', required = false),
                    @Parameter(name = 'typeId', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'Return tags belonging to the supplied tag type id.', required = false),
                    @Parameter(name = 'typeName', type = ParameterType.STRING, description = 'Return tags belonging to the supplied tag type name.', required = false),
            ], tags = ['tags']),
    ])
    def list() {
        params.includePaginationFields = true
        def result = tagsService.listTags(params)
        if(!result.tags) {
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        def tags = result.tags ?: []

        params.countOverRide = result.dataSize
        params.total = result.total
        respond ApiResponse.get200Response(tags).autoFill(params), view:"index"
    }

    @Path(path = '/resources/tags/{id}/related.{format}', operations = [
            @Operation(method = GET, description = "Information about related tags to a specific tag", summary = "Get related Tags by ID", responses = [
                    @Response(code = 200, description = "Returns the list of Tags related to the Tag identified by the 'id' in the specified format.", schema = @DataSchema(title = 'ArrayOfTags', type = DataSchemaType.ARRAY, reference = '#/definitions/TagMarshallerWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the tag to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
            ], tags = ['tags']),
    ])
    def relatedTags(Long id) {
        def tagList = tagsService.listRelatedTags(id, params)

        if(!tagList){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = tagList.totalCount
        respond ApiResponse.get200Response(tagList.tags).autoFill(params)
    }

    @Path(path = '/resources/tags/{id}/media.{format}', operations = [
            @Operation(method = GET, description = "MediaItem", summary = "Get MediaItems for Tag", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems associated with the Tag identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the tag to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
            ], tags = ['tags']),
    ])
    def listMediaForTagId(){
        def result = tagsService.getMediaForTagId(params) ?: []

        params.total = result?.totalCount ?: 0
        params.maxOverride = true
        def items = []
        result.each { MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
    }

    @Path(path = '/resources/tags/{id}/syndicate.{format}', operations = [
            @Operation(method = GET, description = "MediaItem", summary = "Get MediaItems for Tag", responses = [
                    @Response(code = 200, description = "Renders the list of MediaItems associated with the Tag identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.STRING)),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'displayMethod', type = ParameterType.STRING, description = 'Method used to render an html request. Accepts one: [mv, list, feed]', required = false),
            ], tags = ['tags']),
    ])
    def syndicate(Long id){
        String tagName
        if(!id || !(tagName = tagsService.getTagName(id))){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }

        params.controllerContext = { model ->
            g.render template: "/media/mediaViewer", model:model
        }
        String content = mediaService.renderMediaForTag(id, params)

        response.withFormat {
            html{
                render text: content, contentType: MimeType.HTML.name
            }
            json{
                def resp = new Embedded(id:id, content:content ,name: tagName, description: "Media tagged with '${tagName}'")
                respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
            }
        }
    }

    def embed(Long id){
        String tagName
        if(!id || !(tagName = tagsService.getTagName(id))){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        String renderedResponse
        String url = Holders.config.API_SERVER_URL + "/resources/tags/${id}"
        TagHolder tag = new TagHolder(id:id, name:tagName)
        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                renderedResponse = mediaService.renderMediaViewerSnippet(tag, params)
                break
            case "feed":
            case "list":
            default:
                if(params.flavor && params.flavor.toLowerCase() == "iframe") {
                    renderedResponse = mediaService.renderIframeSnippet(url, params)
                } else{
                    renderedResponse = mediaService.renderJSSnippet(url, tag, params)
                }
        }

        withFormat {
            html {
                render renderedResponse
                return
            }
            json {
                response.contentType = 'application/json'
                respond ApiResponse.get200Response([[snippet:renderedResponse]]).autoFill(params), view:"/mediaItem/embed"
                return
            }
        }
    }

    @Path(path = '/resources/tags/tagTypes.{format}', operations = [
            @Operation(method = GET, description = "List of Types", summary = "Get MediaItems for Tag", responses = [
                    @Response(code = 200, description = "Renders the list of MediaItems associated with the Tag identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfTagTypes', type = DataSchemaType.ARRAY, reference = '#/definitions/TagTypeMarshallerWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], tags = ['tags']),
    ])
    def listTypes() {
        def result = tagsService.listTypes(params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = result.size()
        respond ApiResponse.get200Response(result).autoFill(params), view:"types"
    }

    @Path(path = '/resources/tags/tagLanguages.{format}', operations = [
            @Operation(method = GET, description = "List of Tag Languages", summary = "Get TagLanguages", responses = [
                    @Response(code = 200, description = "Returns the list of TagLanguages", schema = @DataSchema(title = 'ArrayOfTagLanguages', type = DataSchemaType.ARRAY, reference = '#/definitions/TagLanguageMarshallerWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], tags = ['tags']),
    ])
    def listLanguages() {
        def result = tagsService.listLanguages(params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = result.size()
        respond ApiResponse.get200Response(result).autoFill(params), view:"languages"
    }

    def query(String q){
        def result = tagsService.query(q)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = result.size()
        respond ApiResponse.get200Response(result).autoFill(params)
    }
}
