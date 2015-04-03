
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.client.sdk.GetMediaRequest
import com.ctacorp.syndication.client.sdk.ResourcesApi
import com.ctacorp.syndication.manager.cms.ContentExtractionService
import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import com.ctacorp.syndication.swagger.rest.client.common.ApiException
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import com.ctacorp.syndication.swagger.rest.client.model.MediaItems
import com.ctacorp.syndication.swagger.rest.client.model.SyndicatedMediaItem
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ContentExtractionService)
class ContentExtractionServiceSpec extends Specification {

    def sourceUrl = 'http://nerds4birds.net/lookup/by/nerd.json'
    def apiBaseUrl = 'http://blind-wombats.org/easier-to-catch/just-as-hard-to-eat/api'
    def resourcesApi = Mock(ResourcesApi)
    def getMediaRequestFactory = Mock(ContentExtractionService.GetMediaRequestFactory)

    def setup() {

        service.resourcesApi = resourcesApi
        service.getMediaRequestFactory = getMediaRequestFactory

        service.apiBaseUrl = apiBaseUrl
    }

    void "get media id throws a non api exception"() {

        given: 'a stubbed out get media request'

        def getMediaRequest = new GetMediaRequest()
        getMediaRequest.sourceUrl = sourceUrl

        when: 'getting the media id associated with a source url'

        service.getMediaItemBySourceUrl(sourceUrl)

        then: "create a new get media request"

        getMediaRequestFactory.newGetMediaRequest(sourceUrl) >> getMediaRequest

        and: 'throw an exception when making the rest call'

        resourcesApi.getMedia(getMediaRequest) >> {
            throw new RuntimeException('bacon')
        }

        and: "expect a thrown service exception"

        thrown(ServiceException)
    }

    void "get media id throws an api exception with a non 400 status code"() {

        given: 'a stubbed out get media request'

        def getMediaRequest = new GetMediaRequest()
        getMediaRequest.sourceUrl = sourceUrl

        and: "the exception to throw"

        def exception = new ApiException(code: 500)

        when: 'getting the media id associated with a source url'

        service.getMediaItemBySourceUrl(sourceUrl)

        then: "create a new get media request"

        getMediaRequestFactory.newGetMediaRequest(sourceUrl) >> getMediaRequest

        and: 'throw an exception when making the rest call'

        resourcesApi.getMedia(getMediaRequest) >> {
            throw exception
        }

        and: "expect a thrown service exception"

        thrown(ServiceException)
    }

    void "get media id throws an api exception with a 400 status code"() {

        given: 'a stubbed out get media request'

        def getMediaRequest = new GetMediaRequest()
        getMediaRequest.sourceUrl = sourceUrl

        and: "the exception to throw"

        def exception = new ApiException(code: 400)

        when: 'getting the media id associated with a source url'

        def mediaId = service.getMediaItemBySourceUrl(sourceUrl)

        then: "create a new get media request"

        getMediaRequestFactory.newGetMediaRequest(sourceUrl) >> getMediaRequest

        and: 'throw an exception when making the rest call'

        resourcesApi.getMedia(getMediaRequest) >> {
            throw exception
        }

        and: "return a null media id"

        !mediaId
    }

    void "get media id correctly handles when resources returns no results"() {

        setup: 'expected media items response and a stubbed out get media request'

        def getMediaRequest = new GetMediaRequest()
        getMediaRequest.sourceUrl = sourceUrl

        def items = new MediaItems()
        items.results = []

        when: 'getting the media item associated with a source url'

        def mediaId = service.getMediaItemBySourceUrl(sourceUrl)

        then: "create a new get media request"

        getMediaRequestFactory.newGetMediaRequest(sourceUrl) >> getMediaRequest

        and: 'return no results'

        resourcesApi.getMedia(getMediaRequest) >> items

        and: 'expect the returned media item to be null'

        mediaId == null
    }

    void "get media id returns a media item"() {

        setup: 'expected media items response and a stubbed out get media request'

        def getMediaRequest = new GetMediaRequest()
        getMediaRequest.sourceUrl = sourceUrl

        def item = new MediaItem()
        item.id = 1234

        def items = new MediaItems()
        items.results = [new MediaItem(id: 1234)]

        when: 'getting the media item associated with a source url'

        def mediaItem = service.getMediaItemBySourceUrl(sourceUrl)

        then: "create a new get media request"

        getMediaRequestFactory.newGetMediaRequest(sourceUrl) >> getMediaRequest

        and: 'return the media items response'

        resourcesApi.getMedia(getMediaRequest) >> items

        and: 'expect the returned media item have non empty properties'

        mediaItem.id == 1234l
    }

    void "get media item throws an api exception with a 400 status code"() {

        when: 'getting the media id associated with a source url'

        def mediaItem = service.getMediaItem("12345")

        then: 'expect the returned media item to be null'

        mediaItem == null
    }

    void "get media item successfully returns a media item"() {

        given: "the media item to return"

        def expectedMediaItem = new MediaItem()

        when: 'getting the media id associated with a source url'

        def mediaItem = service.getMediaItem("12345")

        then: 'return the associated media item'

        resourcesApi.getMediaById(12345) >> [results:[expectedMediaItem]]

        and: 'expect the returned media item to match the expected'

        mediaItem == expectedMediaItem
    }

    void "extract syndicated content throws a non api exception"() {

        when: "extracting the content for a given media id"

        service.getMediaSyndicate('1234')

        then: "throw an exception from the resources api"

        resourcesApi.getMediaSyndicateById(1234) >> {
            throw new RuntimeException('Hey man. No need to stand so close to me in line bro.')
        }

        and: "expect a thrown service exception"

        thrown(ServiceException)
    }

    void "extract syndicated content throws an api exception with a non 400 status code"() {

        given: "the exception to throw"

        def apiException = new ApiException(code: 500)

        when: "extracting the content for a given media id"

        service.getMediaSyndicate('1234')

        then: "throw an exception from the resources api"

        resourcesApi.getMediaSyndicateById(1234) >> {
            throw apiException
        }

        and: "expect a thrown service exception"

        thrown(ServiceException)
    }

    void "extract syndicated content throws an api exception with a 400 status code"() {

        given: "the exception to throw"

        def apiException = new ApiException(code: 400)

        when: "extracting the content for a given media id"

        def mediaItem = service.getMediaSyndicate('1234')

        then: "throw an exception from the resources api"

        resourcesApi.getMediaSyndicateById(1234) >> {
            throw apiException
        }

        and: "return a null media item"

        !mediaItem
    }

    void "extract content returns the extracted content"() {

        setup: "the content to return"

        def extractedContent = new SyndicatedMediaItem()

        when: "extracting the content for a given media id"

        def content = service.getMediaSyndicate('1234')

        then: "return the extracted content from the resources api"

        resourcesApi.getMediaSyndicateById(1234) >> [results:[extractedContent]]

        and: "expect the returned content to match"

        content == extractedContent
    }
}
