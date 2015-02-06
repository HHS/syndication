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
import com.ctacorp.syndication.client.sdk.Pagination
import com.ctacorp.syndication.client.sdk.ResourcesApi
import com.ctacorp.syndication.manager.cms.MediaItemsService
import com.ctacorp.syndication.manager.cms.search.OptionsFactory
import com.ctacorp.syndication.manager.cms.search.PaginationOptions
import com.ctacorp.syndication.manager.cms.search.SearchOptions
import com.ctacorp.syndication.swagger.rest.client.model.MediaItem
import com.ctacorp.syndication.swagger.rest.client.model.MediaItems
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(MediaItemsService)
class MediaItemsServiceSpec extends Specification {

    int max = 1
    int offset = 0
    String sort = "name"
    String order = ""

    ResourcesApi resourcesApi = Mock(ResourcesApi)
    OptionsFactory optionsFactory = Mock(OptionsFactory)
    Pagination pagination = Mock(Pagination)
    GetMediaRequest getMediaRequest = Mock(GetMediaRequest)
    MediaItems mediaItems
    MediaItem mediaItem
    SearchOptions searchOptions = new SearchOptions([:])
    PaginationOptions paginationOptions = new PaginationOptions([:])

    def setup() {

        service.resourcesApi = resourcesApi
        service.optionsFactory = optionsFactory

        mediaItems = new MediaItems(meta: [pagination: [total: 2]])

        mediaItem = new MediaItem(name: "The Greatest Media Item", description: "This really is the greatest media item", sourceUrl: "http://thegreatestmediaitemintheworld.gov/it/really/is/dude", id: 1, mediaType: "Html")
        mediaItems.results = [mediaItem]
    }

    @SuppressWarnings("GrUnresolvedAccess")
    void "browse content returns a list of media items"() {

        when: "browsing syndicated media items"

        def results = service.loadMediaItems(searchOptions, paginationOptions)

        then: "create a new pagination object and request object"

        optionsFactory.newGetMediaRequest(searchOptions) >> getMediaRequest
        optionsFactory.newPagination(searchOptions, paginationOptions) >> pagination

        and: "get the media items from syndication"

        resourcesApi.getMedia(getMediaRequest, pagination) >> mediaItems

        and: "return a list of items with a shortened property list"

        results == mediaItems
    }
}
