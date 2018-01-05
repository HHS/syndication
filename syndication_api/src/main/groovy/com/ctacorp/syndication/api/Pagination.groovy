

/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.api

import grails.util.Holders
import com.ctacorp.grails.swagger.annotations.*

@Definition
class Pagination {
    @DefinitionProperty(name='max', type = DefinitionPropertyType.INTEGER)
    int max                 = 1
    @DefinitionProperty(name='offset', type = DefinitionPropertyType.INTEGER)
    int offset              = 0
    @DefinitionProperty(name='count', type = DefinitionPropertyType.INTEGER)
    int count               = 0
    @DefinitionProperty(name='total', type = DefinitionPropertyType.INTEGER)
    int total               = 1
    @DefinitionProperty(name='pageNum', type = DefinitionPropertyType.INTEGER)
    int pageNum             = 1
    @DefinitionProperty(name='totalPages', type = DefinitionPropertyType.INTEGER)
    int totalPages          = 1
    @DefinitionProperty(name='sort', type = DefinitionPropertyType.STRING)
    String sort             = "id"
    @DefinitionProperty(name='currentUrl', type = DefinitionPropertyType.STRING)
    String currentUrl       = null
    @DefinitionProperty(name='nextUrl', type = DefinitionPropertyType.STRING)
    String nextUrl          = null
    @DefinitionProperty(name='previousUrl', type = DefinitionPropertyType.STRING)
    String previousUrl      = null

    def autoFill(params, int dataSize){
        String serverUrl = Holders.config.API_SERVER_URL
        params.max = params.max ?: 20
        if(params.maxOverride){
            max = params.int('max')
        } else{
            max = params.id ? 1 : Math.min(params.int("max"), 100)                          //if this is a query for a single item, max is always 1 - or -max, or 100 whichever is smaller, no max means 20
        }
        offset = Math.max(params.offset ? params.int("offset") : 0, 0)                  //offset unless negative, then offset = 0
        int nextOffset = offset + max
        int lastOffset = offset - max > 0 ? offset - max : 0
        if (params.countOverRide) {
            count = params.countOverRide
        } else {
            count = dataSize
        }
        total = params.int('total', 1)

        pageNum = (offset + max) / max
        if(pageNum == 0 || total == 0){ pageNum = 1 }
        totalPages = Math.ceil(params.float("totalPageOverride", total / max))

        sort = params.sort

        def resourcesPath = params.controller == 'resources' ? '' : 'resources/'
        def pathToResource = "${serverUrl}/api/v2/${resourcesPath}${params.controller}.json"

        //If data size is 1, then we don't need next or previous. If request is jsonp, append callback.
        previousUrl =   count > 1 ? pathToResource + buildQuery(params, lastOffset) : null
        currentUrl =    pathToResource + buildQuery(params, offset)
        nextUrl =       count > 1 ? pathToResource + buildQuery(params, nextOffset): null
    }

    //Build the default query portion of the string
    private String buildQuery(params, offset){
        "?offset=${offset}&max=${max}${sort ? '&sort=' + sort : ''}${params.callback ? "&callback=${params.callback}" : ""}${getAdditionalParams(params)}"
    }

    //Get a string of additional params (custom ones) supplied by the original request
    private String getAdditionalParams(params){
        String query = "&"
        params.each{ key, value ->
            if(!skip(key)){
                query += "${key}=${value}&"
            }
        }
        if(query.size() == 1){
            return ""
        }
        query[0..-1-1]
    }

    //Which params should be skipped when appending custom params to the end?
    private boolean skip(String param){
        def paramsToSkip = [
            "sort",
            "order",
            "max",
            "offset",
            "callback",
            "action",
            "controller",
            "total"
        ]
        param in paramsToSkip
    }

    def generatePaginationBlock() {
        [
                max:max,
                offset:offset,
                count:count,
                total:total,
                pageNum:pageNum,
                totalPages:totalPages,
                sort:sort,
                currentUrl:currentUrl,
                nextUrl:nextUrl,
                previousUrl:previousUrl,
        ]
    }

    String toString(){
        "      - max:${max}\n" +
        "      - offset:${offset}\n" +
        "      - count:${count}\n" +
        "      - total:${total}\n" +
        "      - pageNum:${pageNum}\n" +
        "      - totalPages:${totalPages}\n" +
        "      - sort:${sort}\n" +
        "      - currentUrl:${currentUrl}\n" +
        "      - nextUrl:${nextUrl}\n" +
        "      - previousUrl:${previousUrl}\n"
    }
}
