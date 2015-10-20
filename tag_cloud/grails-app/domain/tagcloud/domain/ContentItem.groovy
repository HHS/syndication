/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package tagcloud.domain

class ContentItem{
    String url
    String externalUID
    Long syndicationId

    static hasMany = [tags: Tag]
    static belongsTo = Tag

    static constraints = {
        url             nullable: false, blank: false, url:true, maxSize: 2000
        externalUID     nullable: true,  blank: false
        syndicationId   nullable: true,  unique:true, min: 1L, max: Long.MAX_VALUE - 1
    }

    static marshalling = {
        contentList{
            shouldOutputIdentifier true
            shouldOutputClass false
            ignore "tags", "dateCreated", "lastUpdated"
        }

        showTag{
            shouldOutputIdentifier true
            shouldOutputClass false
            ignore "tags", "dateCreated", "lastUpdated"
        }

        showContentItem{
            shouldOutputIdentifier true
            shouldOutputClass true
        }

        syndicationIdList{
            shouldOutputIdentifier false
            shouldOutputClass false
            ignore "tags", "dateCreated", "lastUpdated", "url", "externalUID"
        }
    }

    String toString() { "(${id}) - ${url}" }

    static namedQueries = {
        //Sort Order -------------------------------------------------------------------
        multiSort { String sortQuery = "id" ->
            def parts = sortQuery.split(",")
            parts.each{ part ->
                if(part.startsWith("-")){
                    String field = part[1..-1]
                    order(field, 'desc')
                } else{
                    order(part, 'asc')
                }
            }
        }

        idIs { Long id ->
            idEq(id)
        }

        tagIdIs { Long tagId ->
            tags {
                idEq(tagId)
            }
        }

        syndicationIdIs { Long syndicationId ->
            eq 'syndicationId', syndicationId
        }

        urlIs { String url ->
            eq 'url', url, [ignoreCase: true]
        }

        urlContains { String url ->
            ilike 'url', "%${url}%"
        }

        facetedSearch { params ->
            and {
                if (params.id) {            idIs(params.long('id')) }
                if (params.syndicationId) { syndicationIdIs(params.long('syndicationId')) }
                if (params.tagId) {         tagIdIs((Long) params.long('tagId')) }
                if (params.url) {           urlIs((String) params.url) }
                if (params.urlContains) {   urlContains((String) params.urlContains) }
            }
            if (params.sort) {
                multiSort(params.sort)
            }
        }
    }
}
