/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.rest
import com.ctacorp.syndication.manager.cms.search.PaginationOptions
import com.ctacorp.syndication.manager.cms.search.SearchOptions
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_RHYTHMYX_USER', 'ROLE_ADMIN'])
class MediaItemsController {

    def mediaItemsService
    def loggingService

    def loadMediaItems() {

        def response = [
            draw            : params.draw as int,
            recordsTotal    : 0,
            recordsFiltered : 0,
            data            : [],
            error           : ""
        ]

        try {
            def mediaItems = mediaItemsService.loadMediaItems(new SearchOptions(params), new PaginationOptions(params))
            def pagination = mediaItems?.meta?.pagination
            response.data = mediaItems?.results ?: []
            response.recordsFiltered = pagination?.total
        } catch (e) {
            response.error = loggingService.logError("an exception occured when trying to load media items using the following params:\n${params}", e)
            return renderJsonResponse(response, 500)
        }

        try {
            response.recordsTotal = mediaItemsService.getTotalMediaItemCount()
            renderJsonResponse(response, 200)
        } catch (e) {
            response.errorMessage = loggingService.logError("an exception occurred when trying to get the total media item count (HTML only)", e)
            renderJsonResponse(response, 500)
        }
    }

    def renderJsonResponse(response, status) {
        render status: status, text: (response as JSON).toString(), contentType: 'application/json', encoding: 'UTF-8'
    }
}
