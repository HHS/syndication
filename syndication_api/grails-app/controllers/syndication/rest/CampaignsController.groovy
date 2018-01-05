
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
import com.ctacorp.syndication.api.Embedded
import com.ctacorp.syndication.data.CampaignHolder
import com.ctacorp.syndication.Campaign
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.marshal.MediaItemMarshaller
import com.ctacorp.syndication.media.MediaItem
import grails.util.Holders
import grails.web.mime.MimeType

import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'campaigns', description = 'Information about campaigns')
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
    def mediaService

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/campaigns/{id}.json', operations = [
            @Operation(method = GET, description = "Information about a specific campaign", summary = "Get Campaign by ID", responses = [
                    @Response(code = 200, description = "Returns the Campaign identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfCampaigns', reference = '#/definitions/CampaignWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['campaigns']),
    ])
    def show(Campaign campaignInstance) {
        if(!campaignInstance){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified campaign could not be foround")
            return
        }
        respond ApiResponse.get200Response([campaignInstance]).autoFill(params), view:'index'
    }

    @Path(path = '/resources/campaigns.json', operations = [
            @Operation(method = GET, description = "Media Listings for a specific campaign", summary = "Get Campaigns", responses = [
                    @Response(code = 200, description = "Returns the list of Campaigns.", schema = @DataSchema(title = 'ArrayOfCampaigns', reference = '#/definitions/CampaignWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The offset of the records set to return for pagination', required = false),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = '* Set of fields to sort the records by.', required = false),
            ], tags = ['campaigns']),
    ])
    def list() {
        def campaignInstanceList = campaignsService.listCampaigns(params)
        params.total = campaignInstanceList.totalCount
        respond ApiResponse.get200Response(campaignInstanceList).autoFill(params), view:'index'
    }

    @Path(path = '/resources/campaigns/{id}/media.json', operations = [
            @Operation(method = GET, description = "Campaign Listings", summary = "Get MediaItems by Campaign ID", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems for the Campaign identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the campaign to find media items for', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The offset of the records set to return for pagination', required = false),
            ], tags = ['campaigns']),
    ])
    def listMediaForCampaign(Long id) {
        def mediaItemInstanceList = campaignsService.listMediaItemsForCampaign(id, params)
        if(!Campaign.get(id)){
            response.status = 400
            respond ApiResponse.get400ResponseCustomMessage("Specified campaign could not be found")
            return
        }
        params.total = mediaItemInstanceList.totalCount
        params.maxOverride = true
        def items = []
        mediaItemInstanceList.each {MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
    }

    @Path(path = '/resources/campaigns/{id}/syndicate.{format}', operations = [
            @Operation(method = GET, description = "MediaItem", summary = "Get MediaItems for Campaign", responses = [
                    @Response(code = 200, description = "Renders the list of MediaItems associated with the Tag identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', reference = '#/definitions/SyndicateMarshallerWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'displayMethod', type = ParameterType.STRING, description = 'Method used to render an html request. Accepts one: [mv, list, feed]', required = false),
            ], tags = ['campaigns']),
    ])
    def syndicate(Long id){
        String campaignName
        if(!id || !(campaignName = Campaign.get(id))){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }

        params.controllerContext = { model ->
            g.render template: "/media/mediaViewer", model:model
        }

        String content = mediaService.renderMediaForCampaign(id, params)

        response.withFormat {
            html{
                render text: content, contentType: MimeType.HTML.name
            }
            json{
                def resp = new Embedded(id:id, content:content ,name: campaignName, description: "Media associated with the Campaign: '${campaignName}'")
                respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
            }
        }
    }

    def embed(Long id){
        String campaignName
        if(!id || !(campaignName = Campaign.get(id))){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        String renderedResponse
        String url = Holders.config.API_SERVER_URL + "/resources/campaigns/${id}"
        CampaignHolder campaignHolder = new CampaignHolder([id:id, name:campaignName])
        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                renderedResponse = mediaService.renderMediaViewerSnippet(campaignHolder, params)
                break
            case "feed":
            case "list":
            default:
                if(params.flavor && params.flavor.toLowerCase() == "iframe") {
                    renderedResponse = mediaService.renderIframeSnippet(url, params)
                } else{
                    renderedResponse = mediaService.renderJSSnippet(url, campaignHolder, params)
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
