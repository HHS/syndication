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
import com.ctacorp.syndication.api.Embedded
import com.ctacorp.syndication.api.Message
import com.ctacorp.syndication.api.Meta
import com.ctacorp.syndication.api.Pagination
import com.ctacorp.syndication.jobs.DelayedTaggingJob
import com.ctacorp.syndication.jobs.DelayedTinyUrlJob
import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.commons.util.*
import com.ctacorp.syndication.jobs.DelayedMetricAddJob
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.preview.MediaPreview
import com.ctacorp.syndication.preview.MediaThumbnail
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.rest.render.RenderContext
import org.codehaus.groovy.grails.web.mime.MimeType
import com.ctacorp.syndication.exception.*
import com.ctacorp.syndication.exception.UnauthorizedException
import com.ctacorp.syndication.render.ApiResponseRenderer
import java.util.concurrent.Callable

@API(swaggerDataPath = "/media", description = "Information about media", modelExtensions = [
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
        @ModelProperty(propertyName = "id",                attributes = [@PropertyAttribute(type="integer", format = "int64")]),
        @ModelProperty(propertyName = "name",              attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "content",           attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "contentEncoding",   attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "description",       attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "mediaType",         attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "text",              attributes = [@PropertyAttribute(type="string")]),
        @ModelProperty(propertyName = "contentType",       attributes = [@PropertyAttribute(type="string")])
    ])
], modelRefs = [
        ApiResponse,
        MediaItem,
        Meta,
        Pagination,
        Message,
        Collection,
        AlternateImage,
        Html,
        Image,
        Infographic,
        PDF,
        Language,
        Source,
        Tweet,
        Video
])
class MediaController {
    static defaultAction = "list"
    static responseFormats = ['json']

    def mediaService
    def mediaValidationService
    def contentRetrievalService
    def grailsApplication
    def urlService
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

    @APIResource(path="/resources/media/{id}.json", description = "Information about a specific media item", operations = [
        @Operation(httpMethod="GET", notes="Returns the MediaItem identified by the 'id'.", nickname="getMediaById", type =  "MediaItems", summary = "Get MediaItem by ID", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the record to look up", required=true, paramType = "path")
        ])
    ])
    def show(Long id){
        String key = Hash.md5("show/$id?" + params.sort().toString())
        String renderedResponse
        renderedResponse = guavaCacheService.apiResponseCache.get(key, new Callable<String>() {
            @Override
            public String call(){
                def mediaItem = mediaService.getMediaItem(id)

                if(!mediaItem){
                    response.status = 400
                    respond ApiResponse.get400NotFoundResponse().autoFill(params)
                    return
                }
                params.total = 1
                ApiResponseRenderer renderer = new ApiResponseRenderer()
                StringWriter stringWriter = new StringWriter()
                //We need to hook into the ApiReponseRenderer here, so we can capture it's output in a string writer. I'm faking the RenderContext to hijack this
                renderer.render(ApiResponse.get200Response([mediaItem]).autoFill(params), [getWriter:{stringWriter}, setContentType:{it->it}] as RenderContext)
                return stringWriter.toString()
            }
        });
        render text: renderedResponse, contentType:"application/json"
    }

    @APIResource(path="/resources/media/searchResults.json", description = "Full search", operations = [
        @Operation(httpMethod="GET", notes="Returns the list of MediaItems matching the search query 'q'.<p>The search query 'q' is a Lucene query.<br>The syntax for a Lucene query can be found <a href=\"http://lucene.apache.org/core/2_9_4/queryparsersyntax.html\">here</a>", nickname="searchMedia", type = "MediaItems", summary = "Get MediaItems by search query", responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="q", type="string", description="The search query supplied by the user", required=true, paramType = "query"),
            @Parameter(name="max",     type="integer", format="int32",         description="The maximum number of records to return",                             required=false, paramType = "query"),
            @Parameter(name="offset",  type="integer", format="int32",         description="The offset of the records set to return for pagination.",             required=false, paramType = "query")
        ])
    ])
    def search() {
        String key = Hash.md5(params.sort().toString())
        String renderedResponse
        renderedResponse = guavaCacheService.apiResponseCache.get(key, new Callable<String>() {
            @Override
            public String call(){
                def searchResults = resourcesService.mediaSearch(params)

                if (!searchResults.list || searchResults.list.size() < 1) {
                    params.total = 0
                } else {
                    params.total = searchResults.listCount
                }
            ApiResponseRenderer renderer = new ApiResponseRenderer()
            StringWriter stringWriter = new StringWriter()
            renderer.render(ApiResponse.get200Response(searchResults.list).autoFill(params), [getWriter:{stringWriter}, setContentType:{it->it}] as RenderContext)
            return stringWriter.toString()
            }
        });
        render text: renderedResponse, contentType:"application/json"
    }

    @APIResource(path="/resources/media/{id}/embed.json", description = "Get the javascript or iframe embed code for this item (to embed it on a web page).", operations = [
        @Operation(httpMethod="GET", notes="Returns the javascript or iframe embed code for the MediaItem identified by 'id'.", nickname="getMediaEmbedById", type =  "EmbedCode", summary = "Get embed code for MediaItem", produces = ['text/html'], responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id",           type="integer", format="int64", description="The id of the media to get embed code for.",                   required=true,  paramType = "path"),
            @Parameter(name="flavor",       type="string",                  description="Currently supports 'iframe', defaults to 'javascript'.",       required=false, paramType = "query"),
            @Parameter(name="width",        type="integer", format="int32", description="The width of the generated iframe.",                           required=false, paramType = "query"),
            @Parameter(name="height",       type="integer", format="int32", description="The height of the generated iframe.",                          required=false, paramType = "query"),
            @Parameter(name="iframeName",   type="string",                  description="The name of the iframe element",                               required=false, paramType = "query"),
            @Parameter(name="excludeJquery",type="boolean",                 description="Should a reference to the JQuery Library be omitted?",         required=false, paramType = "query", defaultValue = "false"),
            @Parameter(name="excludeDiv",   type="boolean",                 description="Should the div to insert content into be omitted?",            required=false, paramType = "query", defaultValue = "false"),
            @Parameter(name="divId",        type="string",                  description="Should the div to insert content into have a specific name?",  required=false, paramType = "query"),
            @Parameter(name="displayMethod", type="string",                 description="Method used to render an html request. Accepts one: [mv, list, feed]", required=false, paramType = "query")
        ])
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

        String renderedResponse
        String url = grailsApplication.config.grails.serverURL + "/api/v2/resources/media/$mi.id"

        switch(params.displayMethod ? params.displayMethod.toLowerCase() : "feed"){
            case "mv":
                renderedResponse = mediaService.renderMediaViewerSnippet(mi, params)
                break
            case "feed":
            case "list":
            default:
                if(params.flavor && params.flavor.toLowerCase() == "iframe") {
                    renderedResponse = mediaService.renderIframeSnippet(url, params)
                } else{
                    renderedResponse = mediaService.renderJSSnippet(url, mi, params)
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

    @APIResource(path="/resources/media/featured.json", description = "Get the list of featured content in the syndication system", operations = [
        @Operation(httpMethod="GET", notes="Get the list of featured content in the syndication system", nickname="getFeaturedMedia", type = "string", summary = "Get the list of featured content in the syndication system", produces = ['application/json'], responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="sort",   type="string",                  required=false, paramType = "query",    description="* Set of fields to sort the records by."),
            @Parameter(name="max",    type="integer", format="int64", required=false, paramType = "query",    description="The maximum number of records to return."),
            @Parameter(name="offset", type="integer", format="int64", required=false, paramType = "query",    description="How many records to offset the query by.")
        ])
    ])
    def featured(){
        def featuredMediaItemIds = FeaturedMedia.list()*.mediaItem.id.join(",")
        if(!featuredMediaItemIds){
            respond ApiResponse.get200Response([]).autoFill(params)
            return
        }
        def limits = [max:params.max, offset:params.offset]
        params.restrictToSet = featuredMediaItemIds
        params['active'] = true
        def featuredMediaItems = MediaItem.facetedSearch(params).list(limits)

        respond ApiResponse.get200Response(featuredMediaItems).autoFill(params)
    }

    @APIResource(path="/resources/media/{id}/preview.jpg", description = "Get the jpg preview of the content item where applicable.", operations = [
        @Operation(httpMethod="GET", notes="Returns the JPG preview, where applicable, for the MediaItem identified by the 'id'.", nickname="getMediaPreviewById", type = "MediaItems", summary = "Get JPG preview for MediaItem", produces = ['image/jpeg'], responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the media to get a preview for.", required=true,  paramType = "path")
        ])
    ])
    def preview(Long id){
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi || !mi.active){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        //render special icons for collections and social media
        switch(mi){
            case QuestionAndAnswer:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream
                renderBytes(f.bytes)
                return
            case FAQ:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream
                renderBytes(f.bytes)
                return
            case Collection:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream
                renderBytes(f.bytes)
                return
            case PDF:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream
                renderBytes(f.bytes)
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

    @APIResource(path="/resources/media/{id}/thumbnail.jpg", description = "Get the jpg thumbnail of the content item where applicable.", operations = [
        @Operation(httpMethod="GET", notes="Returns the JPG thumbnail, where applicable, for the MediaItem identified by the 'id'.", nickname="getMediaThumbnailById", type = "string", summary = "Get JPG thumbnail for MediaItem", produces = ['image/jpeg'], responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the media to get a thumbnail for.", required=true, paramType = "path")
        ])
    ])
    def thumbnail(Long id){
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi || !mi.active){
            response.status = 400
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        //render special icons for collections and social media
        switch(mi){
            case Collection:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/collection.jpg").inputStream
                renderBytes(f.bytes)
                return
            case PDF:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/pdf.jpg").inputStream
                renderBytes(f.bytes)
                return
            case QuestionAndAnswer:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/questionAndAnswer.jpg").inputStream
                renderBytes(f.bytes)
                break
            case FAQ:
                InputStream f = assetResourceLocator.findAssetForURI("defaultIcons/thumbnail/faq.jpg").inputStream
                renderBytes(f.bytes)
                break
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

    @APIResource(path="/resources/media/{id}/content", description = "The actual media content (html, image, etc...)", operations = [
        @Operation(httpMethod="GET", notes="Returns the raw content (html, image, etc...) for the MediaItem identified by the 'id'.", nickname="getMediaContentById", type="string", produces=["application/json","text/html", "image/jpeg", "image/png"], summary = "Get content for MediaItem", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the media to show content for.", required=true, paramType = "path"),
            @Parameter(name="calledByBuild", type="Boolean", description="The method that called this method",required=false,paramType = "query")
        ])
    ])
    def content(Long id){
        MediaItem mi = mediaService.getMediaItem(id)

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

    @APIResource(path="/resources/media/{id}/syndicate.{format}", description = "Get syndicated content.", operations = [
        @Operation(httpMethod="GET", notes="Returns the syndicated content for a given MediaItem in the specified 'format' (HTML or JSON).", nickname="getMediaSyndicateById", type = "SyndicatedMediaItems", produces=["application/json","text/html"], summary = "Get syndicated content for MediaItem", responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id",           type="integer", format="int64", description="The id of the media to show embed code for.",                required=true, paramType = "path"),
            @Parameter(name="cssClass",     type="string",                  description="The css class to target for extraction.",                    required=false, defaultValue = "syndicate", paramType = "query"),
            @Parameter(name="stripStyles",  type="boolean",                 description="Remove in-line styles from content.",                        required=false, defaultValue = "false",     paramType = "query"),
            @Parameter(name="stripScripts", type="boolean",                 description="Remove script tags from content.",                           required=false, defaultValue = "false",     paramType = "query"),
            @Parameter(name="stripImages",  type="boolean",                 description="Remove image tags from content.",                            required=false, defaultValue = "false",     paramType = "query"),
            @Parameter(name="stripBreaks",  type="boolean",                 description="Remove break tags from content.",                            required=false, defaultValue = "false",     paramType = "query"),
            @Parameter(name="stripClasses", type="boolean",                 description="Remove class attributes from content (except 'syndicate').", required=false, defaultValue = "false",     paramType = "query"),
            @Parameter(name="font-size",    type="integer", format="int32", description="Set font size (in points) of p, div, and span tags.",        required=false, paramType = "query"),
            @Parameter(name="imageFloat",   type="string",                  description="Accepts valid CSS float options, such as 'left' or 'right'. Will inject a style into the content before rendering.", required=false, paramType = "query"),
            @Parameter(name="imageMargin",  type="string",                  description="Accepts 4 CSV values representing pixel sizes of margin similar to CSS. Default format is 'north,east,south,west' - for example '0,10,10,0' would put a 10 pixel margin on the right and bottom sides of an image. Will inject a style into the content before rendering.", required=false, paramType = "query"),
            @Parameter(name="autoplay",     type="boolean",                 description="If content is a video, the embeded video will auto play when loaded.",         required=false, paramType = "query", defaultValue="true"),
            @Parameter(name="rel",          type="boolean",                 description="If content is a video, related items will be shown at the end of playback.",   required=false, paramType = "query", defaultValue="false")
        ])
    ])
    def syndicate(Long id){
        MediaItem mi = mediaService.getMediaItem(id)
        if(!mi || mi.active == false){
            response.status = 400
            response.contentType = "application/json"
            render ApiResponse.get400NotFoundResponse().autoFill(params) as JSON
            return
        }

        DelayedMetricAddJob.schedule(new Date(System.currentTimeMillis() + 10000), [mediaId: mi.id])

        def resp = new Embedded( id: mi.id, name: mi.name, description: mi.description, sourceUrl: mi.sourceUrl)

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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
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
                        respond ApiResponse.get200Response([resp]).autoFill(params)
                    }
                }
                break
            default:
                response.status = 400
                response.contentType = "application/json"
                render ApiResponse.get400ResponseCustomMessage("The requested mediaType cannot be returned as embed code").autoFill(params) as JSON
                return
        }
    }

    //TODO we need to add support for the new createdBy here, along with the new StructuredContentType
    @APIResource(path="/resources/media.json", description="Media Items Listings", operations=[
        @Operation(httpMethod = "GET", notes="Returns the list of MediaItems matching the specified query parameters.", nickname="getMedia", type="MediaItems", summary="Get MediaItems", responseMessages=[
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="max",                          type="integer", format="int32", description="The maximum number of records to return",                                                                          required=false, paramType = "query"),
            @Parameter(name="offset",                       type="integer", format="int32", description="The offset of the records set to return for pagination.",                                                          required=false, paramType = "query"),
            @Parameter(name="sort",                         type="string",                  description="* Set of fields to sort the records by.",                                                                          required=false, paramType = "query"),
            @Parameter(name="order",                        type="string",                  description="* The ascending or descending order.",                                                                             required=false, paramType = "query"),
            @Parameter(name="mediaTypes",                   type="string",                  description="Find all media items belonging to the specified media type[s].",                                                   required=false, paramType = "query"),
            @Parameter(name="name",                         type="string",                  description="Find all media items containing the provided name, case insensitive.",                                             required=false, paramType = "query"),
            @Parameter(name="collectionId",                 type="integer", format="int32", description="Restrict filtering to media items in a specific collection.",                                                      required=false, paramType = "query"),
            @Parameter(name="nameContains",                 type="string",                  description="Find all media items containing the partial name, case insensitive.",                                              required=false, paramType = "query"),
            @Parameter(name="descriptionContains",          type="string",                  description="Find all media items containing the provided partial description, case insensitive.",                              required=false, paramType = "query"),
            @Parameter(name="sourceUrl",                    type="string",                  description="Find all media items which have the provided sourceUrl, case insensitive.",                                        required=false, paramType = "query"),
            @Parameter(name="sourceUrlContains",            type="string",                  description="Find all media items which contain the provided partial sourceUrl, case insensitive.",                             required=false, paramType = "query"),
            @Parameter(name="customThumbnailUrl",           type="string",                  description="Find all media items which have the provided customThumbnailUrl, case insensitive.",                               required=false, paramType = "query"),
            @Parameter(name="customThumbnailUrlContains",   type="string",                  description="Find all media items which contain the provided partial customThumbnailUrl, case insensitive.",                    required=false, paramType = "query"),
            @Parameter(name="dateContentAuthored",          type="string", format="date",   description="Find all media items authored on the provided day (RFC 3339, time ignored).",                                      required=false, paramType = "query"),
            @Parameter(name="dateContentUpdated",           type="string", format="date",   description="Find all media items updated on the provided day (RFC 3339, time ignored).",                                       required=false, paramType = "query"),
            @Parameter(name="dateContentPublished",         type="string", format="date",   description="Find all media items published on the provided day (RFC 3339, time ignored).",                                     required=false, paramType = "query"),
            @Parameter(name="dateContentReviewed",          type="string", format="date",   description="Find all media items reviewed on the provided day (RFC 3339, time ignored).",                                      required=false, paramType = "query"),
            @Parameter(name="dateSyndicationCaptured",      type="string", format="date",   description="Find all media items authored on the provided day (RFC 3339, time ignored).",                                      required=false, paramType = "query"),
            @Parameter(name="dateSyndicationUpdated",       type="string", format="date",   description="Find all media items updated on the provided day, (RFC 3339, time ignored).",                                      required=false, paramType = "query"),
            @Parameter(name="contentAuthoredSinceDate",     type="string", format="date",   description="Find all media items authored since the provided day (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="contentAuthoredBeforeDate",    type="string", format="date",   description="Find all media items authored before the provided day (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="contentAuthoredInRange",       type="string",                  description="Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).", required=false, paramType = "query"),
            @Parameter(name="contentUpdatedSinceDate",      type="string", format="date",   description="Find all media items updated since the provided day (RFC 3339, time ignored).",                                    required=false, paramType = "query"),
            @Parameter(name="contentUpdatedBeforeDate",     type="string", format="date",   description="Find all media items updated before the provided day (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="contentUpdatedInRange",        type="string",                  description="Find all media items updated between the provided start and end days (RFC 3339, comma separated, time ignored).",  required=false, paramType = "query"),
            @Parameter(name="contentPublishedSinceDate",    type="string", format="date",   description="Find all media items published since the provided day (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="contentPublishedBeforeDate",   type="string", format="date",   description="Find all media items published before the provided day (RFC 3339, time ignored).",                                 required=false, paramType = "query"),
            @Parameter(name="contentPublishedInRange",      type="string",                  description="Find all media items published between the provided start and end days (RFC 3339, comma separated, time ignored).",required=false, paramType = "query"),
            @Parameter(name="contentReviewedSinceDate",     type="string", format="date",   description="Find all media items reviewed since the provided day (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="contentReviewedBeforeDate",    type="string", format="date",   description="Find all media items reviewed before the provided day (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="contentReviewedInRange",       type="string",                  description="Find all media items reviewed between the provided start and end days (RFC 3339, comma separated, time ignored).", required=false, paramType = "query"),
            @Parameter(name="syndicationCapturedSinceDate", type="string", format="date",   description="Find all media items authored since the provided day (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="syndicationCapturedBeforeDate",type="string", format="date",   description="Find all media items authored before the provided day (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="syndicationCapturedInRange",   type="string",                  description="Find all media items authored between the provided start and end days (RFC 3339, comma separated, time ignored).", required=false, paramType = "query"),
            @Parameter(name="syndicationUpdatedSinceDate",  type="string", format="date",   description="Find all media items updated since the provided day, (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="syndicationUpdatedBeforeDate", type="string", format="date",   description="Find all media items updated before the provided day, (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="syndicationUpdatedInRange",    type="string",                  description="Find all media items updated between the provided start and end days, (RFC 3339, comma separated, time ignored).", required=false, paramType = "query"),
            @Parameter(name="syndicationVisibleSinceDate",  type="string", format="date",   description="Find all media items visible since the provided day, (RFC 3339, time ignored).",                                   required=false, paramType = "query"),
            @Parameter(name="syndicationVisibleBeforeDate", type="string", format="date",   description="Find all media items visible before the provided day, (RFC 3339, time ignored).",                                  required=false, paramType = "query"),
            @Parameter(name="syndicationVisibleInRange",    type="string",                  description="Find all media items visible between the provided start and end days, (RFC 3339, comma separated, time ignored).", required=false, paramType = "query"),
            @Parameter(name="languageId",                   type="integer", format="int64", description="Find all media items written in the language specified by Id.",                                                    required=false, paramType = "query"),
            @Parameter(name="languageName",                 type="string",                  description="Find all media items written in the language specified by name, case insensitive.",                                required=false, paramType = "query"),
            @Parameter(name="languageIsoCode",              type="string",                  description="Find all media items written in the language specified by 639-2 isoCode , case insensitive.",                      required=false, paramType = "query"),
            @Parameter(name="hash",                         type="string",                  description="Find all media items which match the provided hash, case insensitive.",                                            required=false, paramType = "query"),
            @Parameter(name="hashContains",                 type="string",                  description="Find all media items which match the provided partial hash, case insensitive.",                                    required=false, paramType = "query"),
            @Parameter(name="sourceId",                     type="integer", format="int64", description="Find all media items that belong to the source specified by Id.",                                                  required=false, paramType = "query"),
            @Parameter(name="sourceName",                   type="string",                  description="Find all media items that belong to the source specified by name, case insensitive.",                              required=false, paramType = "query"),
            @Parameter(name="sourceNameContains",           type="string",                  description="Find all media items that belong to the source specified by partial name, case insensitive.",                      required=false, paramType = "query"),
            @Parameter(name="sourceAcronym",                type="string",                  description="Find all media items that belong to the source specified by acronym, case insensitive.",                           required=false, paramType = "query"),
            @Parameter(name="sourceAcronymContains",        type="string",                  description="Find all media items that belong to the source specified by partial acronym, case insensitive.",                   required=false, paramType = "query"),
            @Parameter(name="tagIds",                       type="string",                  description="Find only media items tagged with the specified tag Ids.",                                                         required=false, paramType = "query"),
            @Parameter(name="restrictToSet",                type="string",                  description="Find only media from within the supplied list of Ids.",                                                            required=false, paramType = "query"),
            @Parameter(name="createdBy",                    type="string",                  description="Find all media items containing the createdBy value.",                                                             required=false, paramType = "query")
        ])
    ])
    def list() {
        String key = Hash.md5(params.sort().toString() + params.order.toString())
        String renderedResponse = guavaCacheService.apiResponseCache.get(key, new Callable<String>() {
            @Override
            public String call(){
                try{
                    def mediaItems = mediaService.listMediaItems(params) ?: []
                    params.total = mediaItems.size() > 0 ? mediaItems.getTotalCount() : 0

                    ApiResponseRenderer renderer = new ApiResponseRenderer()
                    StringWriter stringWriter = new StringWriter()
                    //We need to hook into the ApiReponseRenderer here, so we can capture it's output in a string writer. I'm faking the RenderContext to hijack this
                    renderer.render(ApiResponse.get200Response(mediaItems).autoFill(params), [getWriter:{stringWriter}, setContentType:{it->it}] as RenderContext)
                    return stringWriter.toString()
                } catch(e){
                    ApiResponseRenderer renderer = new ApiResponseRenderer()
                    StringWriter stringWriter = new StringWriter()
                    renderer.render(ApiResponse.get400InvalidField().autoFill(params), [getWriter:{stringWriter}, setContentType:{it->it}] as RenderContext)
                    response.status = 400
                    return stringWriter.toString()
                }
            }
        });

        String statusCode = ((renderedResponse =~ /"status":(\d{3})/)[0][1])
        response.status =  statusCode ? statusCode as Integer : 200
        render text: renderedResponse, contentType:"application/json"
    }

    @APIResource(path = "/resources/media/mostPopularMedia.{format}", description = "Get the media with the highest ratings.", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of MediaItems with the highest ratings.", nickname="getMostPopularMedia", type="MediaItems", summary = "Get MediaItems by popularity", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "max",    type="integer", format="int32", description="The maximum number of records to return",                      required = false, paramType = "query"),
            @Parameter(name = "offset", type="integer", format="int32", description="The offset of the records set to return for pagination.",      required = false, paramType = "query")
        ])
    ])
    def mostPopularMedia(){
        def mostPopular = likeService.mostPopular(params)
        params.total = mostPopular.size()
        respond ApiResponse.get200Response(mostPopular).autoFill(params)
    }

    @APIResource(path = "/resources/media/{id}/relatedMedia.{format}", description = "Get the media related to the current media item.", operations = [
        @Operation(httpMethod = "GET", notes="Returns the list of MediaItems related to the MediaItem identified by the 'id'.", nickname="getRelatedMediaById", type = "MediaItems", summary = "Get related MediaItems by ID", responseMessages = [
            @ResponseMessage(code = 400, description = "Bad Request"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name = "id",     type="integer", format="int64", description = "The id of the media item to get related media for",          required = true,  paramType = "path"),
            @Parameter(name = "max",    type="integer", format="int32", description="The maximum number of records to return",                    required = false, paramType = "query"),
            @Parameter(name = "offset", type="integer", format="int32", description="The offset of the records set to return for pagination.",    required = false, paramType = "query"),
            @Parameter(name = "sort",   type="string",                  description="Which field to sort the records by." ,                       required = false, paramType = "query")

        ])
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
        respond ApiResponse.get200Response(result).autoFill(params)
    }

    @APIResource(path="/resources/media/{id}/youtubeMetaData.json", description = "Youtube meta-data for a video item.", operations = [
        @Operation(httpMethod="GET", notes="Returns the Youtube metadata, where applicable, for the MediaItem identified by the 'id'.", nickname="getMediaYoutubeMetaDataById", type="YoutubeMetadata", summary = "Get Youtube metadata for MediaItem", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID or Invalid media type"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The id of the video to show meta data for.", required=true, paramType = "path")
        ])
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

    def saveCollection(Collection collectionInstance){
        stripTail(collectionInstance)
        ApiResponse apiResponse
        log.info "Attempting to publish: ${collectionInstance} - ${collectionInstance.sourceUrl}"
        try{
            mediaService.saveCollection(collectionInstance)
            if(collectionInstance.hasErrors()){
                apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(collectionInstance).autoFill(params)
            } else {
                apiResponse = ApiResponse.get200Response([collectionInstance]).autoFill(params)
            }

        } catch(UnauthorizedException e){
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }
        mediaPostSaveAndRespond(apiResponse, collectionInstance)
    }

    def saveFAQ(FAQ faqInstance){
        stripTail(faqInstance)
        ApiResponse apiResponse
        log.info "Attempting to publish: ${faqInstance} - ${faqInstance.sourceUrl}"
        try{
            mediaService.saveFAQ(faqInstance)
            if(faqInstance.hasErrors()){
                apiResponse = ApiResponse.get400InvalidInstanceErrorResponse(faqInstance).autoFill(params)
            } else {
                apiResponse = ApiResponse.get200Response([faqInstance]).autoFill(params)
            }
        } catch(UnauthorizedException e){
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }
        mediaPostSaveAndRespond(apiResponse, faqInstance)
    }

    def saveHtml(Html htmlInstance){
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
                    apiResponse = ApiResponse.get200Response([htmlInstance]).autoFill(params)
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

    def saveImage(Image imageInstance){
        log.info "Attempting to publish: ${imageInstance} - ${imageInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!imageInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                imageInstance = mediaService.saveImage(imageInstance)
                if (imageInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([imageInstance]).autoFill(params)
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

    def saveInfographic(Infographic infographicInstance){
        log.info "Attempting to publish: ${infographicInstance} - ${infographicInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!infographicInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                infographicInstance = mediaService.saveInfographic(infographicInstance)
                if (infographicInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([infographicInstance]).autoFill(params)
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

    def savePDF(PDF PDFInstance){
        log.info "Attempting to publish: ${PDFInstance} - ${PDFInstance.sourceUrl}"
        ApiResponse apiResponse
        if (!PDFInstance) { // 400 -- no json body
            log.error "savePDF failed, no instance provided (maybe missing body?)"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else {
            try{
                PDFInstance = mediaService.savePDF(PDFInstance)
                if (PDFInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([PDFInstance]).autoFill(params)
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

    def saveQuestionAndAnswer(QuestionAndAnswer questionAndAnswerInstance){
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
                    apiResponse = ApiResponse.get200Response([questionAndAnswerInstance]).autoFill(params)
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

    def saveVideo(Video videoInstance){
        log.info "Attempting to publish: ${videoInstance.sourceUrl}"
        ApiResponse apiResponse

        if (!videoInstance) { // 400 -- no json body
            log.error "Instance couldn't be created from publish"
            apiResponse = ApiResponse.get400ResponseCustomMessage("Could not create instance from request, did you provide the correct JSON payload in your post?").autoFill(params)
        } else{
            try{
                videoInstance = mediaService.saveVideo(videoInstance)
                if (videoInstance?.id) { // Media saved just fine
                    apiResponse = ApiResponse.get200Response([videoInstance]).autoFill(params)
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

    @APIResource(path="/resources/media/{id}/ratings.json", description = "Get the 'like' count for the specified media item.", operations = [
        @Operation(httpMethod="GET", notes="Get the Ratings (number of 'likes') for the MediaItem identified by the 'id'.", nickname="getMediaRatingsById", type = "Ratings", summary = "Get Ratings for MediaItem", responseMessages=[
            @ResponseMessage(code = 400, description = "Invalid ID"),
            @ResponseMessage(code = 500, description = "Internal Server Error")
        ], parameters = [
            @Parameter(name="id", type="integer", format="int64", description="The ID of the media item to 'like'.", required=true,  paramType = "path")
        ])
    ])
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
            if(metaTags){
                DelayedTaggingJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, requestJson:metaTags, methodName:"tagMediaItemByNames"])
            }

            //Add URL Mapping
            DelayedTinyUrlJob.schedule(new Date(System.currentTimeMillis() + 5000), [mediaId:instance.id, sourceUrl: instance.sourceUrl, externalGuid:instance.externalGuid])

            response.status = 200
        } else{
            log.info " - Didn't Save ${instance.sourceUrl}"
            response.status = 400
        }
        respond apiResponse
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
