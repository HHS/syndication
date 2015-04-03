package com.ctacorp.syndication.contentextraction

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FileService)
class FileServiceSpec extends Specification {
//    def config = Holders.config

    File testDir
    File testItem

    def setup() {
        testDir = new File("${grailsApplication.config.syndication.scratch.root}/test")
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

    void "downloading an image should work"(){
        when: "an image is requested for download"
        File downloadedImage = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")

        then: "the file should exist"
        downloadedImage.exists() == true

        and: "the fileCheck should see it"
        service.fileCheck(downloadedImage, 1000) == true

        cleanup: "remove the downloaded file"
        downloadedImage.delete()
    }

    void "asking to download an image that already exists and isn't stale should return the existing image"(){
        when: "an image already exists"
        File downloadedImage = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")
        Thread.sleep(2000)

        and: "the file is downloaded again"
        File downloadedImage2 = service.saveImage("http://upload.wikimedia.org/wikipedia/commons/2/29/Xiao_Liwu_im_San_Diego_Zoo_-_Foto_2.jpeg")

        then: "the new file should be at least 2 seconds old"
        System.currentTimeMillis() - downloadedImage2.lastModified() >= 2000
    }
}

