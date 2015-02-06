package com.ctacorp.syndication.manager.cms.search

class SearchOptions {

    String searchString
    boolean searchByName
    boolean orderByName
    String orderByNameDirection

    SearchOptions(params) {

        searchString = params["search[value]"]
        searchByName = Boolean.valueOf(params['columns[2][searchable]'] as String)
        orderByName = Boolean.valueOf(params['columns[2][orderable]'] as String)
        orderByNameDirection = params['order[0][dir]']
    }
}

