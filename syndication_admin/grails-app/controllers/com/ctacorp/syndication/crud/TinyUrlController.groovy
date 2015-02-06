
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA) All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.crud

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class TinyUrlController {
    def tinyUrlService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def mappings = tinyUrlService.listTinyUrlMappings(params)
        render view:'index', model:[tinyUrlList:mappings]
    }

    def show(Long id) {
        def mapping = tinyUrlService.getMapping(id)
        render view:'show', model:[tinyUrl:mapping]
    }

    def create() {}

    def save(String targetUrl, Long syndicationId, String guid) {
        def resp = tinyUrlService.createMapping(targetUrl, syndicationId, guid)
        if(resp?.id){
            redirect action:"show", id:resp.id
        } else{
            render resp
        }
    }

    def edit(Long id) {
        def mapping = tinyUrlService.getMapping(id)
        render view:'edit', model:[tinyUrl:mapping]
    }

    def update(String targetUrl, Long syndicationId, Long mappingId,  String guid) {
        def mapping = tinyUrlService.getMapping(mappingId)
        if(mapping){
            def resp = tinyUrlService.updateMapping(targetUrl, syndicationId, guid, mapping.id)
            if(resp.id){
                redirect action:"show", id:resp.id
            } else{
                render resp
            }
        }
    }

    def delete(Long id) {
        def resp = tinyUrlService.deleteMapping(id)
        if(resp == 204){
            flash.message = "Mapping ${id} has been deleted."
            redirect action:"index"
        } else{
            render resp
        }
    }
}
