
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
import com.ctacorp.syndication.AlternateImage
import com.ctacorp.syndication.Audio
import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.Html
import com.ctacorp.syndication.Image
import com.ctacorp.syndication.Infographic
import com.ctacorp.syndication.Language
import com.ctacorp.syndication.MediaItem
import com.ctacorp.syndication.SocialMedia
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.Video
import com.ctacorp.syndication.Widget
import syndication.api.Meta
import syndication.api.Pagination
import syndication.api.Message
import syndication.api.ApiResponse

@API(swaggerDataPath = "/campaigns", description = "Information about campaigns", modelExtensions = [
    @ModelExtension(id="MediaItems", model="ApiResponse"),
    @ModelExtension(id="Campaigns", model = "ApiResponse", addProperties = [
        @ModelProperty(propertyName = "results", attributes = [@PropertyAttribute(type = "array", typeRef="Campaign", required = true)]),
    ], removeProperties = ["results"])
], modelRefs = [
        AlternateImage,
        Audio,
        Campaign,
        com.ctacorp.syndication.Collection,
        Html,
        Image,
        Infographic,
        Language,
        Source,
        MediaItem,
        SocialMedia,
        Video,
        Widget,
        ApiResponse,
        Meta,
        Pagination,
        Message
])
class CampaignsController {
    static allowedMethods = [
        list: 'GET',
        save: 'POST',
        generateCloud: 'GET'
    ]

    static responseFormats = ['json']

    static defaultAction = "list"

    def apiResponseBuilderService
    def campaignsService

    @APIResource(path = "/resources/campaigns/{id}.json", description = "Information about a specific campaign", operations = [
        @Operation(httpMethod = "GET", notes="Returns the Campaign identified by the 'id'.", nickname="getCampaignById", type = "Campaigns", summary = "Get Campaign by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id", type = "integer", format = "int64", description = "The id of the record to look up", required = true, paramType = "path")
        ])
    ])
    def show(Campaign campaignInstance) {
        if(!campaignInstance){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified campaign could not be found")
            return
        }
        respond ApiResponse.get200Response([campaignInstance]).autoFill(params)
    }

    @APIResource(path = "/resources/campaigns.json", description = "Media Listings for a specif campaign", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of Campaigns.", nickname="getCampaigns", type = "Campaigns", summary = "Get Campaigns", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "max",    type = "integer", format="int32", description="The maximum number of records to return",                  required=false, paramType = "query"),
            @Parameter(name = "offset", type = "integer", format="int32", description="The offset of the records set to return for pagination",   required=false, paramType = "query"),
            @Parameter(name = "sort",   type = "string",                  description = "* Set of fields to sort the records by.",                required = false, paramType = "query")
        ])
    ])
    def list() {
        def campaignInstanceList = campaignsService.listCampaigns(params)
        params.total = campaignInstanceList.totalCount
        respond ApiResponse.get200Response(campaignInstanceList).autoFill(params)
    }

    @APIResource(path = "/resources/campaigns/{id}/media.json", description = "Campaign Listings", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of MediaItems for the Campaign identified by the 'id'.", nickname="getMediaByCampaignId", type = "MediaItems", summary = "Get MediaItems by Campaign ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id",     type = "integer", format="int64", description = "The id of the campaign to find media items for", required = true, paramType = "path"),
            @Parameter(name = "max",    type = "integer", format="int32", description="The maximum number of records to return", required=false, paramType = "query"),
            @Parameter(name = "offset", type = "integer", format="int32", description="The offset of the records set to return for pagination", required=false, paramType = "query"),
            @Parameter(name = "sort",   type = "string",                  description = "The name of the property to which sorting will be applied", required = false, paramType = "query")
        ])
    ])
    def listMediaForCampaign(Long id) {
        def mediaItemInstanceList = campaignsService.listMediaItemsForCampaign(id, params)
        if(!mediaItemInstanceList){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified campaign could not be found")
            return
        }
        params.total = mediaItemInstanceList.totalCount
        params.maxOverride = true
        respond ApiResponse.get200Response(mediaItemInstanceList).autoFill(params)
    }
}
