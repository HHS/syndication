
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */

package com.ctacorp.syndication.manager.cms.utils.rest

import com.ctacorp.syndication.manager.cms.utils.exception.ServiceException
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONElement
import org.codehaus.groovy.grails.web.json.JSONObject

class RestClientUtils {

    static def checkStatusCode(String url, response) {

        def statusCode = response.getStatusCode().value()
        if (statusCode != 200) {
            throw new ServiceException(url, statusCode as Integer)
        }
    }

    static def getProperty(String url, response, String property) {

        def jsonElement = response.json as JSONElement

        def properties = property.split(/[.]/) as List

        while (true) {

            if (jsonElement instanceof JSONArray) {
                jsonElement = getArrayValue(jsonElement)
            } else if (jsonElement instanceof JSONObject) {
                jsonElement = getPropertyValue(properties, jsonElement)
            } else {
                jsonElement = null
            }

            if (properties.isEmpty()) {
                return jsonElement?.toString() ?: null
            } else {
                if (!jsonElement) {
                    throw new ServiceException(url, property)
                }
            }
        }
    }

    private static Object getPropertyValue(List<String> properties, jsonElement) {
        def key = properties.remove(0)
        if (jsonElement.has(key)) {
            return jsonElement.get(key)
        }
        return null
    }

    private static Object getArrayValue(jsonElement) {
        if (jsonElement.size() > 0) {
            return jsonElement.get(0)
        }
        return []
    }

    static def getBody(String url, response) {
        def body = response.body
        if (!body) {
            throw new ServiceException(url)
        }
        return body
    }

    static def getBodyAsArray(String url, response) {
        def array = response.json
        if (!array || !(array instanceof JSONArray)) {
            throw new ServiceException(url)
        }
        return array
    }
}
