
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.preview

import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.*

/**
 *
 */
@TestFor(ImageMagickService)
class ImageMagickServiceSpec extends Specification {
    File downloadedImage
    def fileService
    def grailsApplication = Holders.grailsApplication

    def setup() {
        fileService = new FileService()
        fileService.grailsApplication = grailsApplication
        service.fileService = fileService
        service.nativeToolsService = new NativeToolsService()
        service.grailsApplication = grailsApplication

        downloadedImage = fileService.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")
        assert downloadedImage.exists()
    }

    def cleanup() {
        if(downloadedImage){
            downloadedImage.delete()
        }
    }

    void "thumbnail() should create a thumbnail image"() {
        when: "the thumbnail method is called"
            File thumb = service.thumbnail(downloadedImage)

        then: "thumb shouldn't be null"
            thumb != null

        and: "the file should exist"
            thumb.exists() == true

        cleanup: "delete testing files"
            thumb.delete()
    }

    void "small() should create a small image"() {
        when: "the thumbnail method is called"
            File small = service.small(downloadedImage)

        then: "small shouldn't be null"
            small != null

        and: "the file should exist"
            small.exists() == true

        cleanup: "delete testing files"
            small.delete()
    }

    void "medium() should create a medium image"() {
        when: "the medium method is called"
            File medium = service.thumbnail(downloadedImage)

        then: "medium shouldn't be null"
            medium != null

        and: "the file should exist"
            medium.exists() == true

        cleanup: "delete testing files"
            medium.delete()
    }

    void "large() should create a large image"() {
        when: "the large method is called"
            File large = service.large(downloadedImage)

        then: "large shouldn't be null"
            large != null

        and: "the file should exist"
            large.exists() == true

        cleanup: "delete testing files"
            large.delete()
    }
}
