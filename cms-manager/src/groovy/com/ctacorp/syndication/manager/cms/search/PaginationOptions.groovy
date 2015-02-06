package com.ctacorp.syndication.manager.cms.search

class PaginationOptions {

    static final int PAGE_SIZE = 20

    int max = PAGE_SIZE
    int start = 0

    PaginationOptions(params) {
        if(params.start) {
            start = params.start as int
        }
    }
}
