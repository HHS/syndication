/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.rest

import grails.transaction.NotTransactional
import grails.transaction.Transactional
import com.ctacorp.syndication.media.*

@Transactional()
class ResourcesService {

    static transactional = false

    static final Map<String,String> TYPE_TO_GROUP_MAP = [
            Html.simpleName, 'htmls',
            Image.simpleName, 'images',
            Infographic.simpleName, 'infographics',
            Video.simpleName, 'videos',
            Tweet.simpleName, 'tweet',
            Collection.simpleName, 'collections'
    ].toSpreadMap()

    def elasticsearchService

    @Transactional(readOnly = true)
    def mediaSearch(Map params) {

        def response = [list:[], count: 0]
        def query = params.q?.trim() ?: '_all:*'
        params.max = params.max?.toInteger() ?: 20
        params.offset = params.offset?.toInteger() ?: 0

        setSortOrder(params)

        def results = findIds(query, [max: 10000, offset: 0])

        if(results.ids) {

            results.total = MediaItem.countByIdInList(results.ids)

            if(params.sort) {
                response.list = MediaItem.findAllByIdInList(results.ids, params)
            } else {

                def idsForQuery = results.ids.join(',')
                def foundIds = MediaItem.executeQuery("select m.id from MediaItem m where m.id in (${idsForQuery}) order by field (m.id,${idsForQuery})", [:], params)
                response.list = MediaItem.getAll(foundIds)
            }
        }

        response.total = results.total
        response.count = response.list.size()
        response
    }

    @Transactional(readOnly = true)
    def globalSearch(Map params) {

        def response = [total: 0, count:0]
        def query = params.q?.trim() ?: '_all:*'
        params.max = params.max?.toInteger() ?: 20
        params.offset = params.offset?.toInteger() ?: 0

        setSortOrder(params)

        def addMediaToGroup = {

            def type = it.class.simpleName
            addGroupedMedia(response, TYPE_TO_GROUP_MAP[type], [id: it.id, name: it.name], type)
        }

        def results = findIds(query, [max: 10000, offset: 0])

        if(results.ids) {

            results.total = MediaItem.countByIdInList(results.ids)

            if(params.sort) {
                response.count = MediaItem.findAllByIdInList(results.ids, params).collect { addMediaToGroup it }.sum()
            } else {

                def idsForQuery = results.ids.join(',')
                def foundIds = MediaItem.executeQuery("select m.id from MediaItem m where m.id in (${idsForQuery}) order by field (m.id,${idsForQuery})", [:], params)
                response.count = MediaItem.getAll(foundIds).collect { addMediaToGroup it }.sum()
            }
        }

        response.total = results.total
        response
    }

    private def findIds(String query, Map params) {

        try {

            elasticsearchService.luceneSearch(query, [
                    max   : params.sort ? 10000 : params.max,
                    offset: params.sort ? 0 : params.offset,
                    order : params.sort ? (params.sort?.startsWith('-') ? 'desc' : 'asc') : null
            ])

        } catch(Throwable t) {

            log.error('could not connect to elasticsearch', t)
            [ids: [], total: 0]
        }
    }

    @NotTransactional()
    private static addGroupedMedia(Map groups, String group, itemToAdd, String mediaType = null) {
        def count = 0
        if (!groups[group]) {
            groups[group] = [items: []]
            if (mediaType) {
                groups[group].mediaType = mediaType
            }
        }
        if (!groups[group].items.contains(itemToAdd)) {
            groups[group].items << itemToAdd
            count++
        }
        return count
    }

    private setSortOrder(params) {

        if(params.sort) {

            if(params.sort.startsWith('-')) {

                params.sort = params.sort.replace('-', '')
                params.order = 'desc'

            } else {
                params.order = 'asc'
            }
        }
    }
}
