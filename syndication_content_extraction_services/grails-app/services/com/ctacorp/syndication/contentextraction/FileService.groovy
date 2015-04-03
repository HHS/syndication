
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.ctacorp.syndication.contentextraction

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

import com.ctacorp.syndication.commons.util.Hash
import groovyx.net.http.URIBuilder

import java.nio.file.Files
import java.nio.file.Paths

class FileService {
    static transactional = false
    def grailsApplication

    File saveImage(String url) {
        File file
        String fileName = "${Hash.md5(url)}-full"
        URIBuilder uri = new URIBuilder(url)
        String path = uri.path
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            file = new File("${grailsApplication.config.syndication.scratch.root}/preview/tmp/${fileName}.jpg")
        } else if (path.endsWith(".png")) {
            file = new File("${grailsApplication.config.syndication.scratch.root}/preview/tmp/${fileName}.png")
        } else {
            log.error ("Tried to save an image that isn't a jpg or png: ${url}")
            return null
        }

        if(loadCachedPreview(file)){
            return file
        }

        file.bytes = new URL(url).bytes
        verify(file)
    }

    private File loadCachedPreview(File file){
        if(verify(file)){
            if(fileCheck(file, 0)){
                return file
            }
        }
        null
    }

    File getImage(String url){
        saveImage(url)
    }


    File verify(File f) {
        if (f.exists()) {
            return f
        }
        null
    }

    File moveFile(File src, File dest){
        if(src.exists()){
            Files.move(Paths.get(src.toURI()), Paths.get(dest.toURI()), REPLACE_EXISTING);
        } else{
            log.error "Tried to move temp preview ${src} to ${dest}, but the temp file doesn't exist!"
            return null
        }
        return dest
    }

    //ttl in seconds
    public boolean fileCheck(File file, int ttl = 0){
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
