
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
import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional
import grails.util.Holders
import com.ctacorp.syndication.marshal.MediaTypeHolder
import static com.ctacorp.grails.swagger.annotations.HTTPMethod.GET

@Tag(name = 'mediaTypes', description = 'Information about media types')
@Transactional(readOnly = true)
class MediaTypesController {
    def apiResponseBuilderService

    static responseFormats = ['json']

    static allowedMethods = [ list: 'GET']
    static defaultAction = "list"

    def beforeInterceptor = {
        response.characterEncoding = 'UTF-8' //workaround for https://jira.grails.org/browse/GRAILS-11830
    }

    @Path(path = '/resources/mediaTypes.{format}', operations = [
            @Operation(method = GET, description = "Information about media types", summary = "Get MediaTypes", responses = [
                    @Response(code = 200, description = "Returns the list of available MediaTypes.", schema = @DataSchema(title = 'ArrayOfMediaTypes', type = DataSchemaType.ARRAY, reference = '#/definitions/MediaTypeHolderWrapped')),
                    @Response(code = 400, description = 'Invalid ID'),
                    @Response(code = 500, description = 'Internal Server Error'),
            ], tags = ['mediaTypes']),
    ])
    def list() {
        def types = getMediaTypes().sort{ it.name }
        params.sort = "name"
        params.order = "ASC"
        params.max = types.size()
        respond ApiResponse.get200Response(types).autoFill(params), view:"index"
    }

    private getMediaTypes() {
        def mediaTypes = []

        Holders.grailsApplication.domainClasses.each {
            if (it.clazz.superclass.name == "com.ctacorp.syndication.media.MediaItem") {
                mediaTypes << new MediaTypeHolder(name:it.clazz.simpleName, description: g.message(code: "syndication.mediaType.${it.clazz.simpleName}.description"))
            }
        }

        def structuredTypes = MediaItem.StructuredContentType
                .enumConstants*.prettyName
                .collect{
                    def name = it.replace(" ", "")
                    new MediaTypeHolder(
                            name: name,
                            description: g.message(code: "syndication.mediaType.${name}.description"))
                }

        mediaTypes.addAll(structuredTypes)

        mediaTypes.sort()
    }
}
