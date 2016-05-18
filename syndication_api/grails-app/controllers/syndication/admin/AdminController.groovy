/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.admin

import com.ctacorp.syndication.api.ApiResponse
import com.ctacorp.syndication.exception.UnauthorizedException
import com.ctacorp.syndication.media.MediaItem
import grails.converters.JSON

class AdminController {
    def mediaService

    static allowedMethods = [deleteMedia: 'DELETE', archiveMedia: "POST"]

    def deleteMedia(long id){
        ApiResponse apiResponse
        try{
            mediaService.deleteMediaItem(id)
            apiResponse = ApiResponse.get200ResponseCustomUserMessage("The record was deleted successfully")
        } catch(UnauthorizedException e){
            response.status = 400
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }

        render apiResponse as JSON
    }

    def archiveMedia(long id){
        ApiResponse apiResponse
        try{
            mediaService.archiveMedia(id)
            apiResponse = ApiResponse.get200ResponseCustomUserMessage("The record was archived successfully")
        } catch(UnauthorizedException e){
            response.status = 400
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }

        render apiResponse as JSON
    }

    def unarchiveMedia(long id){
        ApiResponse apiResponse
        try{
            MediaItem mi = mediaService.unarchiveMedia(id)
            if(mi && mi.active){
                apiResponse = ApiResponse.get200ResponseCustomUserMessage("The record was unarchived successfully", [mi])
                apiResponse.autoFill(params)
            } else{
                log.error "Either the record doesn't exist or it could not be activated: ${mi?.id} ${mi?.sourceUrl} ${mi?.active}"
                response.status = 400
                apiResponse = ApiResponse.get400ResponseCustomMessage("Either the record doesn't exist or it could not be activated")
            }

        } catch(UnauthorizedException e){
            response.status = 400
            apiResponse = ApiResponse.get400NotAuthorizedResponse()
        }

        render apiResponse as JSON
    }

}
