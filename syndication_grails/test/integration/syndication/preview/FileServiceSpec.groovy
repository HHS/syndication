
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

@TestFor(FileService)
class FileServiceSpec extends Specification {
    def config = Holders.config

    File testDir
    File testItem
    
    def setup() {
        testDir = new File("${config.syndication.scratch.root}/test")
        testDir.mkdirs()
        testItem = new File("${testDir.absolutePath}/testItem.txt")
        if(testItem.exists()){
            testItem.delete()
        }
    }

    def cleanup() {
        testDir.deleteDir()
    }

    void "fileCheck should not give false positives"() {
        when: "a test item that doesn't exist is given"
            assert !testItem.exists()

        then: "the service should say the item doesn't exist"
            service.fileCheck(testItem, 1000) == false
    }

    void "fileCheck should find a file that really does exist"(){
        when: "a test item that does exist and has a valid ttl"
            testItem << "this is text for the test item"

        then: "the service should see that the file is there"
            service.fileCheck(testItem, 1000) == true
    }

    void "when the TTL expires, the fileCheck should be false"(){
        when: "a test item which has a short ttl has expired"
            testItem << "this is text for the test item"
            Thread.sleep 2000

        then: "when performing the check, the result should be false"
            service.fileCheck(testItem, 1) == false
    }

    void "downloading an image should work"(){
        when: "an image is requested for download"
            int imageTTL = config.syndication.preview.imagettl
            File downloadedImage = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")

        then: "the file should exist"
            downloadedImage.exists() == true

        and: "the fileCheck should see it"
            service.fileCheck(downloadedImage, imageTTL)

        cleanup: "remove the downloaded file"
            downloadedImage.delete()
    }

    void "asking to download an image that already exists and isn't stale should return the existing image"(){
        when: "an image already exists"
            File downloadedImage = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")
            Thread.sleep(2000)
            long lastMod = downloadedImage.lastModified()

        and: "the file is downloaded again"
            File downloadedImage2 = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")

        then: "the new file should be at least 2 seconds old"
            System.currentTimeMillis() - downloadedImage2.lastModified() >= 2000
    }
}
