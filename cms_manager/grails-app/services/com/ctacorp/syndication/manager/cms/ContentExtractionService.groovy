/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import com.ctacorp.syndication.client.sdk.GetMediaRequest
import com.ctacorp.syndication.client.sdk.ResourcesApi
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.common.ApiException
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.util.Holders

import javax.annotation.PostConstruct

class ContentExtractionService {

    def apiBaseUrl
    ResourcesApi resourcesApi
    def getMediaRequestFactory

    @PostConstruct
    def init() {
        def config = Holders.config
        resourcesApi = new ResourcesApi()
        resourcesApi.basePath = config?.API_SERVER_URL + config?.SYNDICATION_APIPATH
        getMediaRequestFactory = new GetMediaRequestFactory()
    }

    MediaItem getMediaItemBySourceUrl(String sourceUrl) {

        log.info("Getting the media item for sourceUrl '${sourceUrl}'")

        def serviceException = { Exception e ->
            def message = "Error occured when fetching the media item for sourceUrl '${sourceUrl}'"
            log.error(message, e)
            throw new ServiceException(message)
        }

        def mediaItems = {

            GetMediaRequest request = getMediaRequestFactory.newGetMediaRequest(sourceUrl)

            try {
                return resourcesApi.getMedia(request)?.results
            } catch (ApiException e) {
                if (e.code == 400) {
                    return null
                } else {
                    serviceException(e)
                }
            } catch (e) {
                serviceException(e)
            }
        }()

        if (mediaItems) {
            return mediaItems.get(0)
        } else {
            log.error("No 'results' found in the response")
        }

        null
    }

    MediaItem getMediaItem(String mediaId) {

        log.info("Getting the media item for mediaId '${mediaId}'")

        def serviceException = { Exception e ->
            def message = "Error occured when fetching the media item for mediaId '${mediaId}'"
            log.error(message, e)
            throw new ServiceException(message);
        }

        def mediaItems = {

            try {
                return resourcesApi.getMediaById(mediaId as Long)?.results
            } catch (ApiException e) {
                if (e.code == 400) {
                    return null
                } else {
                    serviceException(e)
                }
            } catch (e) {
                serviceException(e)
            }
        }()

        if (mediaItems) {
            return mediaItems.get(0)
        } else {
            log.error("No 'results' found in the response")
        }

        return null
    }

    SyndicatedMediaItem getMediaSyndicate(String mediaId) {

        log.info("Getting the syndicated media for mediaId '${mediaId}'")

        def serviceException = { Exception e ->
            def message = "Error occured when getting the syndicated media for mediaId '${mediaId}'"
            log.error(message, e)
            throw new ServiceException(message)
        }

        try {
            return resourcesApi.getMediaSyndicateById(mediaId as Long)?.results?.get(0)
        } catch (ApiException e) {
            if (e.code == 400) {
                return null
            } else {
                serviceException(e)
            }
        } catch (e) {
            serviceException(e)
        }
    }

    static class GetMediaRequestFactory {

        @SuppressWarnings("GrMethodMayBeStatic")
        GetMediaRequest newGetMediaRequest(sourceUrl) {
            def request = new GetMediaRequest()
            request.sourceUrl = sourceUrl
            request.mediaTypes = "html"
            return request
        }
    }
}
