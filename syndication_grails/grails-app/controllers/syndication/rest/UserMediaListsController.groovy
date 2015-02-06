/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.rest

import com.ctacorp.grails.swagger.annotations.API
import com.ctacorp.grails.swagger.annotations.APIResource
import com.ctacorp.grails.swagger.annotations.Model
import com.ctacorp.grails.swagger.annotations.ModelExtension
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.Operation
import com.ctacorp.grails.swagger.annotations.Parameter
import com.ctacorp.grails.swagger.annotations.PropertyAttribute
import com.ctacorp.grails.swagger.annotations.ResponseMessage
import com.ctacorp.syndication.AlternateImage
import com.ctacorp.syndication.Audio
import com.ctacorp.syndication.Collection
import com.ctacorp.syndication.Html
import com.ctacorp.syndication.Image
import com.ctacorp.syndication.Infographic
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.Video
import com.ctacorp.syndication.Widget
import com.ctacorp.syndication.storefront.UserMediaList
import syndication.api.ApiResponse
import syndication.api.Message
import syndication.api.Meta
import syndication.api.Pagination

@API(swaggerDataPath = "/userMediaLists", description = "Get media from user generated lists.", modelExtensions = [
    @ModelExtension(id="MediaItems", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type="array", typeRef="MediaItem", required = true)]),
    ], removeProperties = ["results"]),
    @ModelExtension(id="Ratings", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type="array", typeRef="Likes", required = true)]),
    ], removeProperties = ["results"]),
    @ModelExtension(id="EmbedCode", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type="array", typeRef="Snippet", required = true)]),
    ], removeProperties = ["results"]),
    @ModelExtension(id="SyndicatedMediaItems", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type="array", typeRef="SyndicatedMediaItem", required = true)]),
    ], removeProperties = ["results"]),
        @ModelExtension(id="YoutubeMetadata", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results",    attributes = [@PropertyAttribute(type="array", typeRef="Map", required = true)]),
    ], removeProperties = ["results"])
    ], models = [
    @Model(id="Likes", properties = [
        @ModelProperty(propertyName = "likes",      attributes = [@PropertyAttribute(type="integer", format = "int32", required = true)])
    ]),
    @Model(id="Snippet", properties = [
        @ModelProperty(propertyName = "snippet",    attributes = [@PropertyAttribute(type="string", required = true)])
    ]),
    @Model(id="SyndicatedMediaItem", properties = [
        @ModelProperty(propertyName = "id",                     attributes = [@PropertyAttribute(type="integer", format = "int64")]),
        @ModelProperty(propertyName = "name",                   attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "content",                attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "contentEncoding",        attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "description",            attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "mediaType",              attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "text",                   attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "contentType",            attributes = [@PropertyAttribute(type="string")])
    ])
    ],
    modelRefs = [
        ApiResponse,
        MediaItem,
        Meta,
        Pagination,
        Message,
        Collection,
        AlternateImage,
        Audio,
        Html,
        Image,
        Infographic,
        Language,
        Source,
        SocialMedia,
        Video,
        Widget
    ]
)
class UserMediaListsController {
    static responseFormats = ['json']

    @APIResource(path="/resources/userMediaLists/{id}.json", description = "Get a specific user media list.", operations = [
        @Operation(httpMethod="GET", notes="Get a specific user media list by 'id'.", nickname="getUserMediaList", type = "MediaItems", summary = "Get UserMediaList by ID", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the record to look up", required=true, paramType = "path")
        ])
    ])
    def show(Long id){
        def userMediaList = UserMediaList.get(id)
        if(!userMediaList){
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }

        def items = userMediaList.mediaItems ?: []
        respond ApiResponse.get200Response(items).autoFill(params)
    }
}
