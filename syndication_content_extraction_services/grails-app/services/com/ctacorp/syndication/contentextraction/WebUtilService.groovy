/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.cache.CachedContent
import com.ctacorp.syndication.exception.ContentUnretrievableException

class WebUtilService {
    static transactional = false

    def contentCacheService

    String getPage(String url, Boolean disableFailFast = false) {
        String content

        try{
            content = getContentFromUrl(url, disableFailFast)
        }catch (e){
            StringWriter sw = new StringWriter()
            PrintWriter pw = new PrintWriter(sw)
            log.error "There was an exception when trying to download content from the url ${url}. Falling back to saved version if possible"
            log.error "Exception message was: ${e.toString()}"
            CachedContent cached = contentCacheService.get(url)
            if(!cached){
                throw new ContentUnretrievableException("Could not load content located at ${url} from original location or cache.")
            }
            return cached.content
        }
        content
    }

    private String getContentFromUrl(String url, Boolean disableFailFast, int depth = 0){
        if(depth >= 10){
            log.error("Too many redirects, stopping on ${url}")
            return null
        }

        def cacheBuster = contentCacheService.getCacheBuster(url)

        HttpURLConnection con = (HttpURLConnection) (new URL(url+cacheBuster).openConnection());
        if(disableFailFast) {
            con.setConnectTimeout(1000 * 60 * 5) //5 min
            con.setReadTimeout(1000 * 60 * 5)
        } else{
            con.setConnectTimeout(5000)
            con.setReadTimeout(5000)
        }
        con.connect();
        int status = con.getResponseCode();
        log.info "Status: ${status} Getting content from ${url}"
        boolean redirected = false
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirected = true;
        }
        if(redirected){
            String newUrl = con.getHeaderField("Location");
            return getContentFromUrl(newUrl, disableFailFast, depth+1)
        }

        BufferedReader buff = new BufferedReader(new InputStreamReader(con.getInputStream(), 'UTF-8'))
        buff.text
    }
}