package com.ctacorp.syndication.contentextraction

import grails.test.mixin.TestFor
import grails.test.runtime.FreshRuntime
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@FreshRuntime
@TestFor(NativeToolsService)
class NativeToolsServiceSpec extends Specification {
//    def config = Holders.config

    File testDir

    def setup() {
        testDir = new File(grailsApplication.config.syndication.scratch.root + "/test")
        testDir.mkdirs()
    }

    def cleanup() {
        testDir.deleteDir()
    }

    void "running a native command should work"() {

        when: "there is no file present"
        File testFile = new File("${testDir.absolutePath}/nativeFileTest.txt")
        testFile.exists() == false

        then: "making a native call to the OS to make the file should work"
        service.exe("echo 'blah blah blah' > ${testFile.absolutePath}") == null
        testFile.exists() == true

        and: "the conent should be what we expect"
        testFile.text == "blah blah blah\n"

        cleanup: "Delete test files"
        testFile.delete()
    }

}

