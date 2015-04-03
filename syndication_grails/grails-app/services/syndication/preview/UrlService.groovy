/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.preview


class UrlService {
    static transactional = false

    def grailsApplication

    def getContentUrl(Long id) {
        "${grailsApplication.config.grails.serverURL}/api/v2/resources/media/$id/content"
    }

    def getThumbnailUrl(Long id){
        "${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/${id}/thumbnail.jpg"
    }

    def getPreviewUrl(Long id){
        "${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/${id}/preview.jpg"
    }

    def String aggregateSupportedCommands(Map params){
        String paramString = ""
        def supportedParams = grailsApplication.config.syndication.contentExtraction.supportedParams
        params.each{ key, value ->
            if(key in supportedParams){
                paramString += "${key}=${value}&"
            }
        }
        //Trim the last &
        if(paramString){
            paramString = paramString[0..-1 -1]
        }
        paramString
    }
}
