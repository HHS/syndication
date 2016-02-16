/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.tag

import com.ctacorp.syndication.media.MediaItem
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import grails.util.Holders
import net.sf.ehcache.search.expression.Not

import javax.annotation.PostConstruct

@Transactional
class TagsService {
    String serverAddress
    RestBuilder rest = new RestBuilder()
    def authorizationService

    @PostConstruct
    void init() {
        rest.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
        serverAddress = Holders.config.tagCloud.serverAddress
    }

    @Transactional(readOnly = true)
    def getMediaForTagId(params) {
        params.tagId = params.id
        def response = restGet("${serverAddress}/content/getContentForTagId.json", params)
        if(!response) {
            return []
        }

        def ids = response*.syndicationId.join(",")
        boolean active = true
        def sort = params.sort
        params.sort = null
        MediaItem.facetedSearch([restrictToSet:ids,active:active,sort:sort]).list(params) ?: []
    }

    @NotTransactional
    def getMediaForTagIds(String tagIdCSV, params = [:]) {
        params['ids'] = tagIdCSV
        def response = restGet("${serverAddress}/resources/relatedMediaFromTagIds/${params.ids}.json", params) ?: []
        if (!response) {
            return []
        }
        return response
    }

    @NotTransactional
    def getTagsForMediaId(Long id) {
        restGet("${serverAddress}/tags/getTagsForSyndicationId.json", [syndicationId:id]) ?: []
    }

    @Transactional(readOnly = true)
    def tagMedia(Long mediaId, tagIds) {
        MediaItem instance = MediaItem.read(mediaId)
        String body = [syndicationId:instance.id,
                       url:instance.sourceUrl,
                       tagIds:tagIds] as JSON
        def resp = authorizationService.post(body.toString(), "${serverAddress}/tags/tagSyndicatedItemByTagIds")

        return resp
    }

    @NotTransactional
    def tagMediaItemByName(params) {
        tagMediaItemByName(params.long('mediaId'), params.tagName, params.long('typeId'), params.isoCode)
    }

    @NotTransactional
    def tagMediaItemByNames(Long mediaId, Collection tagDetails){
        tagDetails.each{ tag ->
            tagMediaItemByName(mediaId, tag, 1, 1)
        }
    }

    @NotTransactional
    def tagMediaItemByNamesAndLanguageAndType(Long mediaId, Collection tagDetails){
        tagDetails.each{ tag ->
            tagMediaItemByName(mediaId, tag.name, tag.typeId, tag.languageId)
        }
    }

    @Transactional(readOnly = true)
    def tagMediaItemByName(Long mediaId, String tagName, Long typeId = 1, Long languageId = 1) {
        MediaItem mi = MediaItem.read(mediaId)

        String body = [syndicationId:mi.id,
                       url:mi.sourceUrl,
                       tagName:tagName,
                       languageId:languageId,
                       typeId:typeId] as JSON
        def resp = authorizationService.post(body.toString(), "${serverAddress}/tags/tagSyndicatedItemByTagName")
        resp
    }

    @NotTransactional
    def getTagName(Long id){
        def result = restGet("${serverAddress}/tags/show/${id}.json")
        return result?.name
    }

    @NotTransactional
    def query(String q){
        restGet("${serverAddress}/tags/query.json?q=${q}")
    }

    @NotTransactional
    def show(Long id, params = null) {
        restGet("${serverAddress}/tags/show/${id}.json", params)
    }

    @NotTransactional
    def listTypes(params) {
        restGet("${serverAddress}/tagTypes/index.json", params) ?: []
    }

    @NotTransactional
    def listLanguages(params){
        restGet("${serverAddress}/languages/index.json", params) ?: []
    }

    @NotTransactional
    def listTags(params) {
        restGet("${serverAddress}/tags.json", params) ?: []
    }

    @NotTransactional
    def listRelatedTags(Long tagId, params = [:]) {
        params.tagId = tagId
        params.includePaginationFields = true
        restGet("${serverAddress}/tags/getTagsRelatedToTagId.json", params) ?: []
    }

    @NotTransactional
    def listRelatedMedia(params = []) {
        def id = params.id
        def media = restGet("${serverAddress}/resources/relatedMedia/${id}.json", params)
        media = media ?: []
        media
    }

    @NotTransactional
    def listRelatedMediaIds(params = []) {
        def id = params.id
        def media = restGet("${serverAddress}/resources/relatedMediaFromMedia/${id}.json", params)
        media = media ?: []
        return getMediaItemsFromRest(media,params) ?:[]
    }

    @NotTransactional
    def status() {
        try {
            def code = rest.get("${serverAddress}/statusCheck").status
            switch (code) {
                case 200: return true;
                default: return false;
            }
        } catch (e) {
            return false;
        }
    }

    def getMediaItemsFromRest(response, params) {
        String  idString = response.join(',')
        params['restrictToSet'] = idString
        params.id = params.long("id")

        def pag = [max:params.max, offset:params.offset]
        def results = MediaItem.facetedSearch(params).list(pag)

        return results
    }

    private restGet(String url, params = null) {
        try {
            return rest.get(url + aggregateParams(params)) { accept "application/json" }.json
        } catch (e) {
            log.warn "Tag Service could not be reached."
        }
    }


    private String aggregateParams(p) {
        if (!p) {
            return ""
        }
        String query = "?"
        p.each { key, value ->
            if (!(key == "controller" || key == "action" || key == "format")) {
                query += key.encodeAsURL() + "=" + value.encodeAsURL() + "&"
            }
        }
        if (query == "?") {
            return ""
        }

        query[0..-2] //remove trailing &
    }
}