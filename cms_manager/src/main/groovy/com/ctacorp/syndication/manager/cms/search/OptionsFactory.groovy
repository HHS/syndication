package com.ctacorp.syndication.manager.cms.search

import com.ctacorp.syndication.client.sdk.GetMediaRequest
import com.ctacorp.syndication.client.sdk.Pagination

@SuppressWarnings("GrMethodMayBeStatic")
class OptionsFactory {

    GetMediaRequest newGetMediaRequest(SearchOptions options) {

        def request = new GetMediaRequest(mediaTypes: "html")

        if (!options) {
            return request
        }

        def searchString = options.searchString

        if (options.searchByName) {
            request.nameContains = searchString
        }

        request
    }

    Pagination newPagination(SearchOptions searchOptions, PaginationOptions paginationOptions) {

        def pagination = new Pagination()

        def sortString = ""

        if (searchOptions.orderByName) {
            if (searchOptions.orderByNameDirection == "asc") {
                sortString = "name"
            } else if (searchOptions.orderByNameDirection == "desc") {
                sortString = "-name"
            }
        }

        if (sortString) {
            pagination.sort = sortString
        }

        if (paginationOptions.start > -1) {
            pagination.offset = paginationOptions.start
        }

        if (paginationOptions.max > -1) {
            pagination.max = paginationOptions.max
        }

        pagination
    }
}
