
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
import com.ctacorp.syndication.ExtendedAttribute
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.Source
import grails.transaction.NotTransactional
import syndication.api.ApiResponse
import syndication.api.Message
import syndication.api.Meta
import syndication.api.Pagination

@API(swaggerDataPath = "/tags", description = "Information about tags", modelRefs = [ApiResponse, Meta, Pagination, Message, MediaItem, ExtendedAttribute, Language, Source], modelExtensions = [
    @ModelExtension(id="MediaItems", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="MediaItem", required = true)]),
    ],  removeProperties = ["results"]),
    @ModelExtension(id="TagTypes", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="TagType", required = true)]),
    ],  removeProperties = ["results"]),
    @ModelExtension(id="Tags", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="Tag", required = true)]),
    ],  removeProperties = ["results"]),
    @ModelExtension(id="TagLanguages", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type = "array", typeRef="TagLanguage", required = true)]),
    ],  removeProperties = ["results"])
], models = [
    @Model(id="Tag", properties = [
        @ModelProperty(propertyName = "id",             attributes = [@PropertyAttribute(type = "integer", format="int64", required = true)]),
        @ModelProperty(propertyName = "name",           attributes = [@PropertyAttribute(type = "string",  required = true)]),
        @ModelProperty(propertyName = "language",       attributes = [@PropertyAttribute(type = "Language", required = true)]),
        @ModelProperty(propertyName = "type",           attributes = [@PropertyAttribute(type = "TagType", required = true)])

    ]),
    @Model(id="TagType", properties = [
        @ModelProperty(propertyName = "id",             attributes = [@PropertyAttribute(type = "integer",    format="int64", required = true)]),
        @ModelProperty(propertyName = "name",           attributes = [@PropertyAttribute(type = "string",   required = true)]),
        @ModelProperty(propertyName = "description",    attributes = [@PropertyAttribute(type = "string")])
    ]),
    @Model(id="TagLanguage", properties = [
        @ModelProperty(propertyName = "id",             attributes = [@PropertyAttribute(type = "integer",  format="int64", required = true)]),
        @ModelProperty(propertyName = "name",           attributes = [@PropertyAttribute(type = "string",   required = true)]),
        @ModelProperty(propertyName = "isoCode",        attributes = [@PropertyAttribute(type = "string",   required = true)])
    ])
])
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

    @APIResource(path="/resources/tags/{id}.{format}", description = "Information about a specific tag", operations = [
        @Operation(httpMethod="GET", notes="Returns the Tag identified by the 'id' in the specified 'format'.", nickname="getTagById", type = "Tags", summary = "Get Tag by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the record to look up", required=true, paramType = "path")
        ])
    ])
    def show(Long id) {
        def result = tagsService.show(id, params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        respond ApiResponse.get200Response([result]).autoFill(params)
    }

    @NotTransactional
    def tagSave() {
        def result = tagsService.tagMediaItemByName(params)

        respond ApiResponse.get200Response(result).autoFill(params)
    }

    @APIResource(path="/resources/tags.{format}", description="List of Tags", operations=[
        @Operation(httpMethod = "GET", notes="Returns the list of Tags matching the specified query parameters in the specified 'format'.", nickname="getTags", type = "Tags", summary="Get Tags", responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "sort",          type = "string", description = "The name of the property to which sorting will be applied", required = false, paramType = "query"),
            @Parameter(name = "max",           type="integer", format="int32",    description = "The maximum number of records to return",                   required = false, paramType = "query"),
            @Parameter(name = "offset",        type="integer", format="int32",    description = "Return records starting at the offset index.",              required = false, paramType = "query"),
            @Parameter(name = "name",          type = "string", description = "Return tags[s] matching the supplied name",                 required = false, paramType = "query"),
            @Parameter(name = "nameContains",  type = "string", description = "Return tags which contain the supplied partial name.",      required = false, paramType = "query"),
            @Parameter(name = "mediaId",       type="integer", format="int64",   description = "Return tags associated with the supplied media id.",        required = false, paramType = "query"),
            @Parameter(name = "typeId",        type="integer", format="int64",   description = "Return tags belonging to the supplied tag type id.",        required = false, paramType = "query"),
            @Parameter(name = "typeName",      type = "string", description = "Return tags belonging to the supplied tag type name.",      required = false, paramType = "query")
        ])
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
        respond ApiResponse.get200Response(tags).autoFill(params)
    }

    @APIResource(path = "/resources/tags/{id}/related.{format}", description = "Information about related tags to a specific tag", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of Tags related to the Tag identified by the 'id' in the specified format.", nickname="getRelatedTagsById", type = "Tags", summary = "Get related Tags by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id",       type="integer",   format="int64",    description="The id of the tag to look up",                                 required=true, paramType = "path"),
            @Parameter(name = "max",      type="integer",   format="int32",     description="The maximum number of records to return",                      required=false, paramType = "query"),
            @Parameter(name = "offset",   type="integer",   format="int32",     description="The offset of the records set to return for pagination",       required=false, paramType = "query"),
            @Parameter(name = "sort",     type = "string",  description = "The name of the property to which sorting will be applied",  required = false, paramType = "query")
        ])
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

    @APIResource(path="/resources/tags/{id}/media.{format}", description="MediaItem", operations=[
        @Operation(httpMethod="GET", notes="Returns the list of MediaItems associated with the Tag identified by the 'id'.", nickname="getMediaByTagId", type = "MediaItems", summary = "Get MediaItems for Tag", responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id", type="integer", format="int64", description = "The id of the record to look up", required = true, paramType = "path"),
            @Parameter(name = "max", type="integer", format="int32", description="The maximum number of records to return", required=false, paramType = "query"),
            @Parameter(name = "offset", type="integer", format="int32", description="The offset of the records set to return for pagination", required=false, paramType = "query"),
            @Parameter(name = "sort", type = "string", description = "The name of the property to which sorting will be applied", required = false, paramType = "query")

        ])
    ])
    def listMediaForTagId(){
        def result = tagsService.getMediaForTagId(params) ?: []

        params.total = result?.totalCount ?: 0
        params.maxOverride = true
        respond ApiResponse.get200Response(result).autoFill(params)
    }

    @APIResource(path="/resources/tags/tagTypes.{format}", description = "List of Types", operations=[
        @Operation(httpMethod = "GET", notes="Returns the list of TagTypes", nickname="getTagTypes", type = "TagTypes", summary="Get TagTypes", responseMessages=[
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ])
    ])
    def listTypes() {
        def result = tagsService.listTypes(params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = result.size()
        respond ApiResponse.get200Response(result).autoFill(params)
    }

    @APIResource(path="/resources/tags/tagLanguages.{format}", description = "List of Tag Languages", operations=[
        @Operation(httpMethod = "GET", notes="Returns the list of TagLanguages", nickname="getTagLanguages", type = "TagLanguages", summary="Get TagLanguages", responseMessages=[
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ])
    ])
    def listLanguages() {
        def result = tagsService.listLanguages(params)
        if(!result){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse()
            return
        }
        params.total = result.size()
        respond ApiResponse.get200Response(result).autoFill(params)
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
