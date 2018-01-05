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
import com.ctacorp.syndication.marshal.MediaItemMarshaller
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.storefront.UserMediaList
import grails.util.Holders
import grails.web.mime.MimeType
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Embedded
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'userMediaLists', description = 'Get media from user generated lists.')
class UserMediaListsController {
    def mediaService
    static responseFormats = ['json']
    def config = Holders.config

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/userMediaLists/{id}.json', operations = [
            @Operation(method = GET, description = "Get a specific user media list.", summary = "Get UserMediaList by ID", responses = [
                    @Response(code = 200, description = "Get a specific user media list by 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'displayMethod', type = ParameterType.STRING, format = ParameterFormat.INT_64, description = 'Method used to render an html request. Accepts one: [mv, list, feed]', required = false),
            ], tags = ['userMediaLists']),
    ])
    def show(Long id){
        def userMediaList = UserMediaList.read(id)
        if(!userMediaList){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        def items = []
        userMediaList?.mediaItems?.each { MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
        return
    }

    def syndicate(Long id){
        def userMediaList = UserMediaList.get(id)
        if(!userMediaList){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        params.controllerContext = { model ->
            g.render template: "/media/mediaViewer", model:model
        }
        String content = mediaService.renderUserMediaList(userMediaList, params)
        def resp = new Embedded(id:id, name: userMediaList.name, description: userMediaList.description)
        response.withFormat {
            html{
                render text: content, contentType: MimeType.HTML.name
            }
            json{
                resp.mediaType = "UserMediaList"
                resp.content = content
                respond ApiResponse.get200Response([resp]).autoFill(params)
            }
        }
    }

    def embed(Long id){
        def userMediaList = UserMediaList.get(id)
        if(!userMediaList){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        String renderedResponse
        String url = config?.API_SERVER_URL + "/resources/userMediaLists/${id}"

        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                renderedResponse = mediaService.renderMediaViewerSnippet(userMediaList, params)
                break
            case "feed":
            case "list":
            default:
                if(params.flavor && params.flavor.toLowerCase() == "iframe") {
                    renderedResponse = mediaService.renderIframeSnippet(url, params)
                } else{
                    renderedResponse = mediaService.renderJSSnippet(url, userMediaList, params)
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
