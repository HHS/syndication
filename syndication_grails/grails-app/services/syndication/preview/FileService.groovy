
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.preview

import com.ctacorp.syndication.commons.util.Hash
import groovyx.net.http.URIBuilder

class FileService {
    static transactional = false
    def grailsApplication

    File saveImage(String url) {
        File file
        URIBuilder uri = new URIBuilder(url)
        String path = uri.path
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            file = new File("${grailsApplication.config.syndication.scratch.root}/preview/full/${Hash.md5(url)}.jpg")
        } else if (path.endsWith(".png")) {
            file = new File("${grailsApplication.config.syndication.scratch.root}/preview/full/${Hash.md5(url)}.png")
        } else {
            log.error ("Tried to save an image that isn't a jpg or png: ${url}")
            return null
        }

        //If we already have the file
        if(file.exists()){
            //If it isn't stale yet
            if(new Date().time - file.lastModified() <= (grailsApplication.config.syndication.preview.imagettl * 1000)){
                return file
            }
        }

        if(loadCachedPreview(file)){
            return file
        }

        file.bytes = new URL(url).bytes
        verify(file)
    }

    private File loadCachedPreview(File file){
        if(verify(file)){
            if(fileCheck(file, grailsApplication.config.syndication.preview.imagettl)){
                return file
            }
        }
        null
    }

    File getImage(String url){
        saveImage(url)
    }

    File loadCachedThumbnail(String hash){
        File cached = new File("${grailsApplication.config.syndication.scratch.root}/preview/thumbnail/${hash}.jpg")
        if(loadCachedPreview(cached)){
            return cached
        }
        null
    }

    File loadCachedSmall(String hash){
        File cached = new File("${grailsApplication.config.syndication.scratch.root}/preview/small/${hash}.jpg")
        if(loadCachedPreview(cached)){
            return cached
        }
        null
    }

    File loadCachedMedium(String hash){
        File cached = new File("${grailsApplication.config.syndication.scratch.root}/preview/medium/${hash}.jpg")
        if(loadCachedPreview(cached)){
            return cached
        }
        null
    }

    File loadCachedLarge(String hash){
        File cached = new File("${grailsApplication.config.syndication.scratch.root}/preview/large/${hash}.jpg")
        if(loadCachedPreview(cached)){
            return cached
        }
        null
    }

    File loadCachedCustom(String hash){
        File cached = new File("${grailsApplication.config.syndication.scratch.root}/preview/custom/${hash}.jpg")
        if(loadCachedPreview(cached)){
            return cached
        }
        null
    }

    private verify(File f) {
        if (f.exists()) {
            return f
        }
        null
    }

    //ttl in seconds
    public boolean fileCheck(File file, int ttl){
        //If we already have the file
        if(file.exists()){
            //If it isn't stale yet
//            if(new Date().time - file.lastModified() <= (ttl * 1000)){
//                return true
//            }

            //ttl is problematic, probably replace it with totally diff mechanism in s3
            return true
        }
        false
    }
}
