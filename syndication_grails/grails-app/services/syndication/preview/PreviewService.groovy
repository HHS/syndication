
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
import grails.transaction.Transactional

@Transactional
class PreviewService {
    def htmlPreviewService
    def imageMagickService
    def grailsApplication
    def fileService
    def urlService

    def previewHtml(String url, params) {
        int browserWindowSize
        String fileName = Hash.md5(url)
        browserWindowSize = params.browserWindowSize ? params.int("browserWindowSize") : grailsApplication.config.syndication.preview.browserWindowSize
        def imageLoader = { htmlPreviewService.getImageOfHtml(url, browserWindowSize) }

        if(!params.previewSize){params.crop = true}
        return getSizedPreview(fileName, params, imageLoader)
    }

    def previewImage(String url, params){
        String fileName = Hash.md5(url+params.width+params.height+params.crop)
        def imageLoader = {fileService.getImage(url)}

        if(!params.previewSize){params.crop = true}
        if(params.previewSize == "custom"){
            if(params.width && params.height && (!params.crop || params.boolean("crop") == false)){
                return imageMagickService.custom(imageLoader(), fileName, params.int("width"), params.int("height"))
            } else if(params.width && params.height && (params.boolean("crop") == true)){
                return imageMagickService.customCropped(imageLoader(), fileName, params.int("width"), params.int("height"))
            } else{
                params.previewSize = "medium"
            }
        }
        return getSizedPreview(fileName, params, imageLoader)
    }

    private File getSizedPreview(String fileName, params, imageLoader){
        def size = params.previewSize ?: "medium"
        File file = fileService."loadCached${size.capitalize()}"(fileName)
        if(file){ return file }
        String compressor = params.boolean('crop', true) ? "${size}Cropped" : size
        if(size == "custom"){
            return imageMagickService."${compressor}"(imageLoader(), fileName, params.int('width'), params.int('height'))
        } else{
            return imageMagickService."${compressor}"(imageLoader(), fileName)
        }
    }
}
