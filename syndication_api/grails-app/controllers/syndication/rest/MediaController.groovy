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
import com.ctacorp.syndication.*
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.api.Embedded
import com.ctacorp.syndication.api.Message
import com.ctacorp.syndication.jobs.DelayedTaggingJob
import com.ctacorp.syndication.jobs.DelayedTinyUrlJob
import com.ctacorp.syndication.jobs.DelayedAlternateImageJob
import com.ctacorp.syndication.marshal.MediaItemMarshaller
import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.commons.util.*
import com.ctacorp.syndication.jobs.DelayedMetricAddJob
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.rest.render.RenderContext
import grails.util.Holders
import grails.web.mime.MimeType
import com.ctacorp.syndication.exception.*
import com.ctacorp.syndication.exception.UnauthorizedException
//import com.ctacorp.syndication.render.ApiResponseRenderer
import java.util.concurrent.Callable
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'media', description = 'Information about media')
class MediaController {
    static defaultAction = "list"
    static responseFormats = ['json']

    def mediaService
    def mediaValidationService
    def contentRetrievalService
    def config = Holders.config
    def likeService
    def youtubeService
    def resourcesService
    def guavaCacheService
    def assetResourceLocator
    def jsoupWrapperService

    RestBuilder rest = new RestBuilder()

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/media/{id}.json', operations = [
            @Operation(method = GET, description = "Information about a specific media item", summary = "Get MediaItem by ID", responses = [
                    @Response(code = 200, description = "Returns the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the record to look up', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['media']),
    ])
    def show(Long id){
        String key = Hash.md5("show/$id?" + params.sort().toString())
        ApiResponse renderedResponse
        renderedResponse = guavaCacheService.apiResponseCache.get(key, new Callable<ApiResponse>() {
            @Override
            public ApiResponse call() {
                def mediaItem = mediaService.getMediaItem(id)

                if(!mediaItem || !mediaItem.active){
                    response.status = 400
                    respond ApiResponse.get400NotFoundResponse().autoFill(params)
                    return
                }
                params.total = 1
//                ApiResponseRenderer renderer = new ApiResponseRenderer()
                StringWriter stringWriter = new StringWriter()
                //We need to hook into the ApiReponseRenderer here, so we can capture it's output in a string writer. I'm faking the RenderContext to hijack this
//                renderer.render(ApiResponse.get200Response([mediaItem]).autoFill(params), [getWriter:{stringWriter}, setContentType:{ it->it}] as RenderContext)
//                return stringWriter.toString()
                def items = [new MediaItemMarshaller(mediaItem)]
                ApiResponse.get200Response(items).autoFill(params) as ApiResponse
            }
        })
        respond renderedResponse, view:"/mediaItem/index"
    }

    @Path(path = '/resources/media/searchResults.json', operations = [
            @Operation(method = GET, description = "Full search", summary = "Get MediaItems by search query", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems matching the search query 'q'.<p>Please enter keyword or URL in search query 'q'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'q', type = ParameterType.STRING, description = 'The search query supplied by the user', required = true),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The offset of the records set to return for pagination.', required = false),
            ], tags = ['media']),
    ])
    def search() {

        params.controller = 'media/searchResults'

        String key = Hash.md5(params.sort().toString())
        guavaCacheService.flushAllCaches()

        ApiResponse apiResponse = guavaCacheService.apiResponseCache.get(key, new Callable<ApiResponse>() {
            @Override
            ApiResponse call(){

                def searchResults = resourcesService.mediaSearch(params)

                params.total = searchResults.total
                params.count = searchResults.list.size()

                def items = []

                searchResults.list.each { MediaItem item ->
                    items << new MediaItemMarshaller(item)
                }

                return ApiResponse.get200Response(items).autoFill(params) as ApiResponse
            }
        })

        respond apiResponse, view:'/mediaItem/index'
    }

    @Path(path = '/resources/media/{id}/embed.json', operations = [
            @Operation(method = GET, description = "Get the javascript or iframe embed code for this item (to embed it on a web page).", summary = "Get embed code for MediaItem", responses = [
                    @Response(code = 200, description = "Returns the javascript or iframe embed code for the MediaItem identified by 'id'.", schema = @DataSchema(title = 'EmbedCode', type = DataSchemaType.STRING)),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media to get embed code for.', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'flavor', type = ParameterType.STRING, description = "Currently supports 'iframe', defaults to 'javascript'.", required = false),
                    @Parameter(name = 'width', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The width of the generated iframe.', required = false),
                    @Parameter(name = 'height', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The height of the generated iframe.', required = false),
                    @Parameter(name = 'iframeName', type = ParameterType.STRING, description = "The name of the iframe element", required = false),
                    @Parameter(name = 'excludeJquery', type = ParameterType.BOOLEAN, description = "Should a reference to the JQuery Library be omitted?", required = false, defaultValue = "false"),
                    @Parameter(name = 'excludeDiv', type = ParameterType.BOOLEAN, description = "Should the div to insert content into be omitted?", required = false, defaultValue = "false"),
                    @Parameter(name = 'divId', type = ParameterType.STRING, description = "Should the div to insert content into have a specific name?", required = false),
                    @Parameter(name = 'displayMethod', type = ParameterType.STRING, description = "Method used to render an html request. Accepts one: [mv, list, feed]", required = false),
            ], tags = ['media']),
    ])
    def embed(long id){
        MediaItem mi = mediaService.getMediaItem(id)

        if(!mi || !mi.active){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        def paramsHaveErrors = mediaValidationService.embedValidation(params)

        if(paramsHaveErrors){
            Message message = new Message([errorCode:"400",errorMessage:"Bad Request",userMessage:paramsHaveErrors])
            response.status = 400
            render ApiResponse.get400ResponseCustomMessage(message) as JSON
            return
        }

        Closure getEmbedCode = {
            if (!mi.foreignSyndicationAPIUrl) {
                String url = config.API_SERVER_URL + "/resources/media/$mi.id"

                switch (params.displayMethod ? params.displayMethod.toLowerCase() : "feed") {
                    case "mv":
                        return mediaService.renderMediaViewerSnippet(mi, params)
                        break
                    case "feed":
                    case "list":
                    default:
                        if (params.flavor && params.flavor.toLowerCase() == "iframe") {
                            return mediaService.renderIframeSnippet(url, params)
                        } else {
                            return mediaService.renderJSSnippet(url, mi, params)
                        }
                }
            } else {
                def stripScripts = (params.int("stripScripts") == null || params.int("stripScripts") == 1)
                def stripStyles = (params.int("stripStyles") == null || params.int("stripStyles") == 1)
                def stripImages = (params.int("stripImages") == null || params.int("stripImages") == 0) ? false : true

                return rest.get(mi.foreignSyndicationAPIUrl + "/resources/media/${mi.externalGuid}/embed.json?stripscripts=${stripScripts}&stripstyles=${stripStyles}&stripImages=${stripImages}").json.results.encodeAsHTML()
            }
        }

        def keyParts = params.clone()
        keyParts.remove("_")
        keyParts.remove("callback")
        keyParts.remove("controllerContext")
        String renderedResponse = guavaCacheService.getEmbedCachesForId(id,"embed/${id}?" + keyParts.sort().toString(), getEmbedCode)

        withFormat {
            html {
                render renderedResponse
                return
            }
            json {
                response.contentType = 'application/json'
                respond ApiResponse.get200Response([[snippet:renderedResponse]]).autoFill(params), view: "/mediaItem/embed"
                return
            }
        }
    }

    @Path(path = '/resources/media/featured.json', operations = [
            @Operation(method = GET, description = "Get the list of featured content in the syndication system", summary = "Get the list of featured content in the syndication system", responses = [
                    @Response(code = 200, description = "Get the list of featured content in the syndication system", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItem')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
            ], tags = ['media']),
    ])
    def featured(){
        def featuredMediaItemIds = FeaturedMedia.list()*.mediaItem.id.join(",")
        if(!featuredMediaItemIds){
            respond ApiResponse.get200Response([]).autoFill(params), view:"/mediaItem/index"
            return
        }
        def limits = [max:params.max, offset:params.offset]
        params.restrictToSet = featuredMediaItemIds
        params['active'] = true
        def featuredMediaItems = MediaItem.facetedSearch(params).list(limits)
        def items = []
        featuredMediaItems.each {MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
    }

    @Path(path = '/resources/media/{id}/preview.jpg', operations = [
            @Operation(method = GET, description = "Get the jpg preview of the content item where applicable.", summary = "Get Tag by ID", responses = [
                    @Response(code = 200, description = "Returns the JPG preview, where applicable, for the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'Media preview', type = DataSchemaType.OBJECT)),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media to get a preview for.', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['media']),
    ])
    def preview(Long id){
        log.debug "preview request for media id: ${id}"
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi && id <= mediaService.getMaxId()) {
            log.debug "can't find that media item"
            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/missingFile.jpg").inputStream
            renderBytes(f.bytes)
            return
        }
        if(!mi || !mi.active){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }

        byte[] renderedResponse

        Closure getImage = {
            MediaPreview preview = MediaPreview.findByMediaItem(mi)
            if(preview?.customImageData) {
                return preview.customImageData
            }
            if (!preview?.imageData) {
                switch (mi) {
                    case Tweet:
                        InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/twitter.jpg").inputStream
                        return f.bytes
                    case QuestionAndAnswer:
                        InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream
                        return f.bytes
                    case FAQ:
                        InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream
                        return f.bytes
                    case Collection:
                        InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream
                        return f.bytes
                    case PDF:
                        InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream
                        return f.bytes
                    default:
                        InputStream f  = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/bad.jpg").inputStream
                        return f.bytes
                }
            }
            return preview.imageData
        }

        renderedResponse = guavaCacheService.getImageCachesForId(id,"preview/${id}?" + params.sort().toString(), getImage)

        renderBytes(renderedResponse)
    }

    @Path(path = '/resources/media/{id}/thumbnail.jpg', operations = [
            @Operation(method = GET, description = "Get the jpg thumbnail of the content item where applicable.", summary = "Get JPG thumbnail for MediaItem", responses = [
                    @Response(code = 200, description = "Returns the JPG thumbnail, where applicable, for the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'Media thumbnail bytes', type = DataSchemaType.OBJECT)),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media to get a thumbnail for.', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['media']),
    ])
    def thumbnail(Long id){
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi && id <= mediaService.getMaxId()) {
            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/missingFile.jpg").inputStream
            renderBytes(f.bytes)
            return
        }
        if(!mi || !mi.active){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        byte[] renderedResponse
            Closure getImage =  {
                MediaThumbnail thumbnail = MediaThumbnail.findByMediaItem(mi)
                if(thumbnail?.customImageData) {
                    return thumbnail.customImageData
                }
                if(!thumbnail || !thumbnail.imageData) {
                    switch(mi){
                        case Tweet:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/twitter.jpg").inputStream
                            return f.bytes
                        case Collection:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream
                            return f.bytes
                        case PDF:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream
                            return f.bytes
                        case QuestionAndAnswer:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream
                            return f.bytes
                        case FAQ:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream
                            return f.bytes
                        default:
                            InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/bad.jpg").inputStream
                            return f.bytes
                    }
                }
                return  thumbnail.imageData
            }
        renderedResponse = guavaCacheService.getImageCachesForId(id,"thumbnail/${id}?" + params.sort().toString(), getImage)

        renderBytes(renderedResponse)
    }

    @Path(path = '/resources/media/{id}/content', operations = [
            @Operation(method = GET, description = "The actual media content (html, image, etc...)", summary = "Get content for MediaItem", responses = [
                    @Response(code = 200, description = "Returns the raw content (html, image, etc...) for the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'MediaItem content', type = DataSchemaType.STRING)),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media to show content for.', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'calledByBuild', type = ParameterType.BOOLEAN, description = 'The method that called this method', required = false),
            ], tags = ['media']),
    ])
    def content(Long id){
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi && id <= mediaService.getMaxId()) {
            render template: "retractedItem", model:[storefrontUrl:config.STOREFRONT_SERVER_URL]
            return
        }
        if(!mi || mi.active == false){
            response.status = 400
            response.contentType = "application/json"
            render ApiResponse.get400NotFoundResponse() as JSON
            return
        }
        DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId:mi.id])
        try {
            switch (mi) {
                case Html:
                    Html html = mi as Html
                    def extractionResult = contentRetrievalService.extractSyndicatedContent(html.sourceUrl, params)
                    String extractedContent = extractionResult.extractedContent
                    renderHtml(extractedContent)
                    return
                case PDF:
                    PDF pdf = mi as PDF
                    InputStream inputStream = new URL(pdf.sourceUrl).newInputStream()
                    renderStream(pdf.sourceUrl, "application/pdf")
                    inputStream.close()
                    return
                case Image:
                    Image image = mi as Image
                    renderImage(image.sourceUrl)
                    return
                case Infographic:
                    Infographic infographic = mi as Infographic
                    renderImage(infographic.sourceUrl)
                    return
                default:
                    response.status = 400
                    response.contentType = "application/json"
                    render ApiResponse.get400ResponseCustomMessage("The requested media type does not support browser rendering/display.").autoFill(params) as JSON
            }
        }catch(ContentUnretrievableException e){
                response.status = 400
                response.contentType = "application/json"
                render ApiResponse.get400ContentUnretrievableResponse() as JSON
        }
    }

    @Path(path = '/resources/media/{id}/syndicate.{format}', operations = [
            @Operation(method = GET, description = "Get syndicated content.", summary = "Get syndicated content for MediaItem", responses = [
                    @Response(code = 200, description = "Returns the syndicated content for a given MediaItem in the specified 'format' (HTML or JSON).", schema = @DataSchema(title = 'SyndicatedMediaItem', reference = '#/definitions/SyndicateMarshallerWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media to show embed code for.', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'cssClass', type = ParameterType.STRING, description = 'The css class to target for extraction.', required = false, defaultValue = "syndicate"),
                    @Parameter(name = 'stripStyles', type = ParameterType.BOOLEAN, description = 'Remove in-line styles from content.', required = false, defaultValue = "false"),
                    @Parameter(name = 'stripScripts', type = ParameterType.BOOLEAN, description = 'Remove script tags from content.', required = false, defaultValue = "false"),
                    @Parameter(name = 'stripImages', type = ParameterType.BOOLEAN, description = 'Remove image tags from content.', required = false, defaultValue = "false"),
                    @Parameter(name = 'stripBreaks', type = ParameterType.BOOLEAN, description = 'Remove break tags from content.', required = false, defaultValue = "false"),
                    @Parameter(name = 'stripClasses', type = ParameterType.BOOLEAN, description = 'Remove class attributes from content (except \'syndicate\').', required = false, defaultValue = "false"),
                    @Parameter(name = 'font-size', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Set font size (in points) of p, div, and span tags.', required = false),
                    @Parameter(name = 'imageFloat', type = ParameterType.STRING, description = "Accepts valid CSS float options, such as 'left' or 'right'. Will inject a style into the content before rendering.", required = false),
                    @Parameter(name = 'imageMargin', type = ParameterType.STRING, description = "Accepts 4 CSV values representing pixel sizes of margin similar to CSS. Default format is 'north,east,south,west' - for example '0,10,10,0' would put a 10 pixel margin on the right and bottom sides of an image. Will inject a style into the content before rendering.", required = false),
                    @Parameter(name = 'autoplay', type = ParameterType.BOOLEAN, description = "If content is a video, the embeded video will auto play when loaded.", required = false, defaultValue="true"),
                    @Parameter(name = 'rel', type = ParameterType.BOOLEAN, description = "If content is a video, related items will be shown at the end of playback.", required = false, defaultValue="false"),
            ], tags = ['media']),
    ])
    def syndicate(Long id){
        MediaItem mi = mediaService.getMediaItem(id)

        def resp = new Embedded()
        if(!mi && id <= mediaService.getMaxId()) {
            response.withFormat {
                html{
                    render text:g.render(template:  "retractedItem", model:[storefrontUrl:config.STOREFRONT_SERVER_URL])
                }
                json{
                    resp.content = g.render(template:  "retractedItem", model:[storefrontUrl:config.STOREFRONT_SERVER_URL])
                    respond ApiResponse.get200Response([resp]).autoFill(params)
                }
            }

            return
        }
        if(!mi || mi.active == false){
            response.status = 400
            response.contentType = "application/json"
            respond ApiResponse.get400ResponseCustomMessage("Specified media could not be found")
            //render ApiResponse.get400NotFoundResponse().autoFill(params), view:"/mediaItem/index"
            return
        }

        DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: mi.id])

        resp = new Embedded( id: mi.id, name: mi.name, description: mi.description, sourceUrl: mi.sourceUrl)

        switch(mi){
            case com.ctacorp.syndication.media.Collection:
                params.controllerContext = { model ->
                    g.render template: "mediaViewer", model:model
                }
                String content = mediaService.renderCollection(mi as com.ctacorp.syndication.media.Collection, params)

                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Collection"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case FAQ:
                params.controllerContext = { model ->
                    g.render template: "mediaViewer", model:model
                }
                String content = mediaService.renderFAQ(mi as FAQ, params)

                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "FAQ"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case Html:
                String content = mediaService.renderHtml(mi as Html, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Html"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case Image:
                String content = mediaService.renderImage(mi as Image, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Image"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case Infographic:
                String content = mediaService.renderInfographic(mi as Infographic, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Infographic"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case PDF:
                String content = mediaService.renderPdf(mi as PDF, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "PDF"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case QuestionAndAnswer:
                String content = mediaService.renderQuestionAndAnswer(mi as QuestionAndAnswer, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "QuestionAndAnswer"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case Tweet:
                String content = mediaService.renderTweet(mi as Tweet, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Tweet"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            case Video:
                String content = mediaService.renderVideo(mi as Video, params)
                response.withFormat {
                    html{
                        render text: content, contentType: MimeType.HTML.name
                    }
                    json{
                        resp.mediaType = "Video"
                        resp.content = content
                        respond ApiResponse.get200Response([resp]).autoFill(params), view:"/mediaItem/syndicate"
                    }
                }
                break
            default:
                response.status = 400
                response.contentType = "application/json"
                render ApiResponse.get400ResponseCustomMessage("The requested mediaType cannot be returned as embed code").autoFill(params), view:"/mediaItem/index"
                return
        }
    }

    //TODO we need to add support for the new createdBy here, along with the new StructuredContentType
    @Path(path = '/resources/media.json', operations = [
            @Operation(method = GET, description = "Media Items Listings", summary = "Get MediaItems", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems matching the specified query parameters.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The offset of the records set to return for pagination.', required = false),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = '* Set of fields to sort the records by.', required = false),
                    @Parameter(name = 'order', type = ParameterType.STRING, description = '* The ascending or descending order.', required = false),
                    @Parameter(name = 'mediaTypes', type = ParameterType.STRING, description = 'Find all media items belonging to the specified media type[s].', required = false),
                    @Parameter(name = 'name', type = ParameterType.STRING, description = 'Find all media items containing the provided name, case insensitive.', required = false),
                    @Parameter(name = 'collectionId', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Restrict filtering to media items in a specific collection.', required = false),
                    @Parameter(name = 'nameContains', type = ParameterType.STRING, description = 'Find all media items containing the partial name, case insensitive.', required = false),
                    @Parameter(name = 'descriptionContains', type = ParameterType.STRING, description = 'Find all media items containing the provided partial description, case insensitive.', required = false),
                    @Parameter(name = 'sourceUrl', type = ParameterType.STRING, description = 'Find all media items which have the provided sourceUrl, case insensitive.', required = false),
                    @Parameter(name = 'sourceUrlContains', type = ParameterType.STRING, description = 'Find all media items which contain the provided partial sourceUrl, case insensitive.', required = false),
                    @Parameter(name = 'customThumbnailUrl', type = ParameterType.STRING, description = 'Find all media items which have the provided customThumbnailUrl, case insensitive.', required = false),
                    @Parameter(name = 'customThumbnailUrlContains', type = ParameterType.STRING, description = 'Find all media items which contain the provided partial customThumbnailUrl, case insensitive.', required = false),
                    @Parameter(name = 'dateContentAuthored', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items authored on the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'dateContentUpdated', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated on the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'dateContentPublished', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items published on the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'dateContentReviewed', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items reviewed on the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'dateSyndicationCaptured', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items syndicated on the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'dateSyndicationUpdated', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated through the syndication system on the provided day, (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentAuthoredSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items authored since the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentAuthoredBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items authored before the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentAuthoredInRange', type = ParameterType.STRING, description = 'Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'contentUpdatedSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated since the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentUpdatedBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated before the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentUpdatedInRange', type = ParameterType.STRING, description = 'Find all media items updated between the provided start and end days (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'contentPublishedSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated since the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentPublishedBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items published before the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentPublishedInRange', type = ParameterType.STRING, description = 'Find all media items published between the provided start and end days (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'contentReviewedSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items reviewed since the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentReviewedBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items reviewed before the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'contentReviewedInRange', type = ParameterType.STRING, description = 'Find all media items reviewed between the provided start and end days (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'syndicationCapturedSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items authored since the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationCapturedBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items authored before the provided day (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationCapturedInRange', type = ParameterType.STRING, description = 'Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'syndicationUpdatedSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated since the provided day, (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationUpdatedBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items updated before the provided day, (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationUpdatedInRange', type = ParameterType.STRING, description = 'Find all media items updated between the provided start and end days, (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'syndicationVisibleSinceDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items visible since the provided day, (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationVisibleBeforeDate', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items visible before the provided day, (RFC 3339, time ignored).', required = false),
                    @Parameter(name = 'syndicationVisibleInRange', type = ParameterType.STRING, format = ParameterFormat.DATE, description = 'Find all media items visible between the provided start and end days, (RFC 3339, comma separated, time ignored).', required = false),
                    @Parameter(name = 'languageId', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'Find all media items written in the language specified by Id.', required = false),
                    @Parameter(name = 'languageName', type = ParameterType.STRING, description = 'Find all media items written in the language specified by name, case insensitive.', required = false),
                    @Parameter(name = 'languageIsoCode', type = ParameterType.STRING, description = 'Find all media items written in the language specified by 639-2 isoCode , case insensitive.', required = false),
                    @Parameter(name = 'hash', type = ParameterType.STRING, description = 'Find all media items which match the provided hash, case insensitive.', required = false),
                    @Parameter(name = 'hashContains', type = ParameterType.STRING, description = 'Find all media items which match the provided partial hash, case insensitive.', required = false),
                    @Parameter(name = 'sourceId', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'Find all media items that belong to the source specified by Id.', required = false),
                    @Parameter(name = 'sourceName', type = ParameterType.STRING, description = 'Find all media items that belong to the source specified by name, case insensitive.', required = false),
                    @Parameter(name = 'sourceNameContains', type = ParameterType.STRING, description = 'Find all media items that belong to the source specified by partial name, case insensitive.', required = false),
                    @Parameter(name = 'sourceAcronym', type = ParameterType.STRING, description = 'Find all media items that belong to the source specified by acronym, case insensitive.', required = false),
                    @Parameter(name = 'sourceAcronymContains', type = ParameterType.STRING, description = 'Find all media items that belong to the source specified by partial acronym, case insensitive.', required = false),
                    @Parameter(name = 'tagIds', type = ParameterType.STRING, description = 'Find only media items tagged with the specified tag Ids.', required = false),
                    @Parameter(name = 'restrictToSet', type = ParameterType.STRING, description = 'Find only media from within the supplied list of Ids.', required = false),
                    @Parameter(name = 'createdBy', type = ParameterType.STRING, description = 'Find all media items containing the createdBy value.', required = false),
            ], tags = ['media']),
    ])
    def list() {
        String key = Hash.md5(params.sort().toString() + params.order.toString())
        ApiResponse renderedResponse = guavaCacheService.apiResponseCache.get(key, new Callable<ApiResponse>() {
            @Override
            public ApiResponse call(){
                try{
                    def mediaItems = mediaService.listMediaItems(params) ?: []
                    params.total = mediaItems.size() > 0 ? mediaItems.getTotalCount() : 0
                    def items = []
                    mediaItems?.each {MediaItem item ->
                        items << new MediaItemMarshaller(item)
                    }
                    return ApiResponse.get200Response(items).autoFill(params) as ApiResponse
                } catch(e){
                    response.status = 400
                    respond ApiResponse.get400NotFoundResponse().autoFill(params)
                    return
                }
            }
        });

//        String statusCode = ((renderedResponse =~ /"status":(\d{3})/)[0][1])
//        response.status =  statusCode ? statusCode as Integer : 200
//        render text: renderedResponse, contentType:"application/json"
        respond renderedResponse, view:"/mediaItem/index"
    }

    @Path(path = '/resources/media/mostPopularMedia.{format}', operations = [
            @Operation(method = GET, description = "Get the media with the highest ratings.", summary = "Get MediaItems by popularity", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems with the highest ratings.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The offset of the records set to return for pagination.', required = false),
            ], tags = ['media']),
    ])
    def mostPopularMedia(){
        def mostPopular = likeService.mostPopular(params)
        params.total = mostPopular.size()
        def items = []
        mostPopular?.each {MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
    }

    @Path(path = '/resources/media/{id}/relatedMedia.{format}', operations = [
            @Operation(method = GET, description = "Get the media related to the current media item.", summary = "Get related MediaItems by ID", responses = [
                    @Response(code = 200, description = "Returns the list of MediaItems related to the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaItems', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaItemWrapped')),
                    @Response(code = 400, description = 'Bad Request'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the media item to get related media for', required = true, whereIn = ParameterLocation.PATH),
                    @Parameter(name = 'max', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'The maximum number of records to return', required = false),
                    @Parameter(name = 'offset', type = ParameterType.INTEGER, format = ParameterFormat.INT_32, description = 'Return records starting at the offset index.', required = false),
                    @Parameter(name = 'sort', type = ParameterType.STRING, description = 'The name of the property to which sorting will be applied', required = false),
            ], tags = ['media']),
    ])
    def relatedMedia(Long id) {
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi || mi.active == false){
            response.status = 400
            respond ApiResponse.get400NotFoundResponse().autoFill(params)
            return
        }
        def result = mediaService.listRelatedMediaItems(params)
        params.total = result.getTotalCount()
        def items = []
        result?.each {MediaItem item ->
            items << new MediaItemMarshaller(item)
        }
        respond ApiResponse.get200Response(items).autoFill(params), view:"/mediaItem/index"
    }

    @Path(path = '/resources/media/{id}/youtubeMetaData.json', operations = [
            @Operation(method = GET, description = "Youtube meta-data for a video item.", summary = "Get Youtube metadata for MediaItem", responses = [
                    @Response(code = 200, description = "Returns the Youtube metadata, where applicable, for the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'YoutubeMetadata', type = DataSchemaType.OBJECT, reference = '#/definitions/YoutubeMetadataWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], parameters = [
                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = 'The id of the video to show meta data for.', required = true, whereIn = ParameterLocation.PATH),
            ], tags = ['media']),
    ])
    def youtubeMetaData(Long id){
        def mediaItem = mediaService.getMediaItem(id)
        if(!mediaItem || mediaItem.active == false){
            response.status = 400
            respond ApiResponse.get400Response()
            return
        }
        if(!(mediaItem instanceof Video)){
            response.status = 400
            respond ApiResponse.get400InvalidResourceForTypeErrorResponse()
            return
        }
        def code = youtubeService.getMetaDataForVideoUrl(mediaItem.sourceUrl)
        if(code?.error?.code == 404){
            respond ApiResponse.get400ResponseCustomMessage("Youtube reports that the specified video does not exist").autoFill(params)
            return
        }
        respond ApiResponse.get200Response([code]).autoFill(params)
    }

// ===================================================================================================
// |       ######     ###    ##     ## ########     ##     ## ######## ########  ####    ###         |
// |      ##    ##   ## ##   ##     ## ##           ###   ### ##       ##     ##  ##    ## ##        |
// |      ##        ##   ##  ##     ## ##           #### #### ##       ##     ##  ##   ##   ##       |
// |       ######  ##     ## ##     ## ######       ## ### ## ######   ##     ##  ##  ##     ##      |
// |            ## #########  ##   ##  ##           ##     ## ##       ##     ##  ##  #########      |
// |      ##    ## ##     ##   ## ##   ##           ##     ## ##       ##     ##  ##  ##     ##      |
// |       ######  ##     ##    ###    ########     ##     ## ######## ########  #### ##     ##      |
// ===================================================================================================

    def saveCollection(){
        Collection collectionInstance = new Collection(request.JSON)
        stripTail(collectionInstance)
        ApiResponse apiResponse
        log.info "Attempting to publish: ${collectionInstance} - ${collectionInstance.sourceUrl}"
        try{
            collectionInstance = mediaService.saveCollection(collectionInstance)
            if(collectionInstance.hasErrors()){
                apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(collectionInstance).autoFill(params)
            } else {
                apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(collectionInstance)]).autoFill(params)
            }

        } catch(UnauthorizedException e){
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }
        mediaPostSaveAndRespond(apiResponse, collectionInstance)
    }

    def saveFAQ(){
        FAQ faqInstance = new FAQ(request.JSON)
        stripTail(faqInstance)
        ApiResponse apiResponse
        log.info "Attempting to publish: ${faqInstance} - ${faqInstance.sourceUrl}"
        try{
            mediaService.saveFAQ(faqInstance)
            if(faqInstance.hasErrors()){
                apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(faqInstance).autoFill(params)
            } else {
                apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(faqInstance)]).autoFill(params)
            }
        } catch(UnauthorizedException e){
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }
        mediaPostSaveAndRespond(apiResponse, faqInstance)
    }

    def saveHtml(){
        Html htmlInstance = new Html(request.JSON)
        stripTail(htmlInstance)
        log.info "Attempting to publish: ${htmlInstance} - ${htmlInstance.sourceUrl}"
        ApiResponse apiResponse

        if (!htmlInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else{
            try{
                htmlInstance = mediaService.saveHtml(htmlInstance, params)
                if (htmlInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(htmlInstance)]).autoFill(params)
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${htmlInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(htmlInstance).autoFill(params)
                }
            } catch(ContentNotExtractableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(ContentUnretrievableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            }
        }
        mediaPostSaveAndRespond(apiResponse, htmlInstance)
    }

    def saveImage(){
        Image imageInstance = new Image(request.JSON)
        log.info "Attempting to publish: ${imageInstance} - ${imageInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!imageInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                imageInstance = mediaService.saveImage(imageInstance)
                if (imageInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(imageInstance)]).autoFill(params)
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${imageInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(imageInstance).autoFill(params)
                }
            } catch(ContentUnretrievableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            }
        }

        mediaPostSaveAndRespond(apiResponse, imageInstance)
    }

    def saveInfographic(){
        Infographic infographicInstance = new Infographic(request.JSON)
        log.info "Attempting to publish: ${infographicInstance} - ${infographicInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!infographicInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                infographicInstance = mediaService.saveInfographic(infographicInstance)
                if (infographicInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(infographicInstance)]).autoFill(params) as ApiResponse
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${infographicInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(infographicInstance).autoFill(params)
                }
            } catch(ContentUnretrievableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            }
        }
        mediaPostSaveAndRespond(apiResponse, infographicInstance)
    }

    def savePDF(){
        PDF PDFInstance = new PDF(request.JSON)
        log.info "Attempting to publish: ${PDFInstance} - ${PDFInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!PDFInstance) { // 400 -- no json body
            log.error "savePDF failed, no instance provided (maybe missing body?)"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                PDFInstance = mediaService.savePDF(PDFInstance)
                if (PDFInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(PDFInstance)]).autoFill(params)
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${PDFInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(PDFInstance).autoFill(params)
                }
            } catch(ContentUnretrievableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            }
        }
        mediaPostSaveAndRespond(apiResponse, PDFInstance)
    }

    def saveQuestionAndAnswer(){
        QuestionAndAnswer questionAndAnswerInstance = new QuestionAndAnswer(request.JSON)
        log.info "Attempting to publish: ${questionAndAnswerInstance} - ${questionAndAnswerInstance.name}"
        def requestJson = request.getJSON()

        questionAndAnswerInstance?.name = requestJson?.question ?: questionAndAnswerInstance.name

        ApiResponse apiResponse
        if (!questionAndAnswerInstance) { // 400 -- no json body
            log.error "saveQuestionAndAnswer failed, no instance provided (maybe missing body?)"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                questionAndAnswerInstance = mediaService.saveQuestionAndAnswer(questionAndAnswerInstance)
                if (questionAndAnswerInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(questionAndAnswerInstance)]).autoFill(params)
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${questionAndAnswerInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(questionAndAnswerInstance).autoFill(params)
                }
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            } catch(e){
                apiResponse = ApiResponse.get400ResponseCustomMessage(e.getMessage())
            }
        }
        mediaPostSaveAndRespond(apiResponse, questionAndAnswerInstance)
    }

    //TODO implement this
    def saveTweet(Tweet tweetInstance){

    }

    def saveVideo(){
        Video videoInstance = new Video(request.JSON)
        log.info "Attempting to publish: ${videoInstance.sourceUrl}"
        ApiResponse apiResponse

        if (!videoInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else{
            try{
                videoInstance = mediaService.saveVideo(videoInstance)
                if (videoInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([new MediaItemMarshaller(videoInstance)]).autoFill(params)
                } else { // it didn't save, error handle:
                    log.error("Couldn't save the instance: ${videoInstance.errors}")
                    apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(videoInstance).autoFill(params)
                }
            } catch(ContentUnretrievableException e){
                apiResponse = ApiResponse.get400ContentNotExtractableErrorResponse().autoFill(params)
            } catch(UnauthorizedException e){
                apiResponse = ApiResponse.get400NotAuthorizedResponse()
            }
        }
        mediaPostSaveAndRespond(apiResponse, videoInstance)
    }

// ======================================================
// |      ##       #### ##    ## ########  ######       |
// |      ##        ##  ##   ##  ##       ##    ##      |
// |      ##        ##  ##  ##   ##       ##            |
// |      ##        ##  #####    ######    ######       |
// |      ##        ##  ##  ##   ##             ##      |
// |      ##        ##  ##   ##  ##       ##    ##      |
// |      ######## #### ##    ## ########  ######       |
// ======================================================

//    @APIResource(path="/resources/media/{id}/ratings.json", description = "Get the 'like' count for the specified media item.", operations = [
//        @Operation(httpMethod="GET", notes="Get the Ratings (number of 'likes') for the MediaItem identified by the 'id'.", nickname="getMediaRatingsById", type = "Ratings", summary = "Get Ratings for MediaItem", responseMessages=[
//            @ResponseMessage(code = 400, description = "Invalid ID"),
//            @ResponseMessage(code = 500, description = "Internal Server Error")
//        ], parameters = [
//            @Parameter(name="id", type="integer", format="int64", description="The ID of the media item to 'like'.", required=true,  paramType = "path")
//        ])
//    ])
//    @Path(path = '/resources/media/{id}/ratings.json', operations = [
//            @Operation(method = GET, description = "Get the 'like' count for the specified media item.", summary = "Get Ratings for MediaItem", responses = [
//                    @Response(code = 200, description = "Get the Ratings (number of 'likes') for the MediaItem identified by the 'id'.", schema = @DataSchema(title = 'ArrayOfMediaRatings', type = DataSchemaType.ARRAY, reference = '#/definitions/Rating')),
//                    @Response(code = 400, description = 'Invalid ID'),
//                    @Response(code = 500, description = 'Internal Server Error'),
//            ], parameters = [
//                    @Parameter(name = 'id', type = ParameterType.INTEGER, format = ParameterFormat.INT_64, description = "The ID of the media item to 'like'.", required = true, whereIn = ParameterLocation.PATH),
//            ], tags = ['media']),
//    ])
    def likes(Long id){
        def exists = mediaService.getMediaItem(id)
        if(!exists || exists.active == false){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        respond ApiResponse.get200Response([[likes:likeService.likeCountForMedia(id)]]).autoFill(params)
    }

// =============================================================================
// |      ########  ########  #### ##     ##    ###    ######## ########       |
// |      ##     ## ##     ##  ##  ##     ##   ## ##      ##    ##             |
// |      ##     ## ##     ##  ##  ##     ##  ##   ##     ##    ##             |
// |      ########  ########   ##  ##     ## ##     ##    ##    ######         |
// |      ##        ##   ##    ##   ##   ##  #########    ##    ##             |
// |      ##        ##    ##   ##    ## ##   ##     ##    ##    ##             |
// |      ##        ##     ## ####    ###    ##     ##    ##    ########       |
// =============================================================================

    private void stripTail(MediaItem mediaItem){
        if(mediaItem?.sourceUrl?.endsWith("/")){
            mediaItem.sourceUrl = mediaItem.sourceUrl[0..-2]
        }
    }

    private void mediaPostSaveAndRespond(ApiResponse apiResponse, MediaItem instance){
        if(apiResponse.meta.status == 200){
            def requestJson = request.getJSON()
            //Tag media
            if(requestJson.tags) {
                DelayedTaggingJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, requestJson:requestJson.tags, methodName:"tagMedia"])
            }

            if(requestJson.tagNames){
                DelayedTaggingJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, requestJson:requestJson.tagNames, methodName:"tagMediaItemByNames"])
            }

            if(requestJson.tagDetails){
                DelayedTaggingJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, requestJson:requestJson.tagDetails, methodName:"tagMediaItemByNamesAndLanguageAndType"])
            }

            def metaTags = jsoupWrapperService.getMetaTags(instance.sourceUrl)
            if(!requestJson.tags && !requestJson.tagNames && !requestJson.tagDetails && metaTags){
                DelayedTaggingJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, requestJson:metaTags, methodName:"tagMediaItemByNames"])
            }

            if(requestJson.addAlternateImages) {
                DelayedAlternateImageJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, alternateImages:requestJson.addAlternateImages])
            }

            //Add URL Mapping
            DelayedTinyUrlJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, sourceUrl: instance.sourceUrl, externalGuid:instance.externalGuid])

            response.status = 200
        } else{
            log.info " - Didn't Save ${instance.sourceUrl}"
            response.status = 400
            respond apiResponse
            return
        }
        respond apiResponse, view:"/mediaItem/index"
    }

    private void renderHtml(String html){
        if(params.callback){
            render(
                text: "${params.callback}({'content':'${html.replace("'","’").replace("\n","").replace("\r","")}'});",
                contentType: 'application/json',
                encoding: "UTF-8"
            )
        } else{
            render html
        }
    }

    private void renderImage(String url){
        String contentType
        if (url.endsWith(".jpg") || url.endsWith(".jpeg")) {
            contentType = "image/jpeg"
        } else if (url.endsWith(".png")) {
            contentType =  "image/png"
        } else {
            log.error ("Tried to save an image that isn't a jpg or png: ${url}")
        }
        renderStream(url, contentType)
    }

    private renderBytes(byte[] bytes, String contentType = "image/jpeg"){
        response.contentType = contentType
        response.setHeader('Content-length', "${bytes.size()}")
        response.outputStream << bytes
        response.outputStream.flush()
    }

    private renderStream(String url, String contentType = "image/jpeg") {
        InputStream inputStream = new URL(url).newInputStream()
        response.contentType = contentType
        response.setHeader('Content-length', "${rest.head(url).getHeaders().'Content-Length'[0]}")
        response.outputStream << inputStream
        response.outputStream.flush()
        inputStream.close()
    }
}
