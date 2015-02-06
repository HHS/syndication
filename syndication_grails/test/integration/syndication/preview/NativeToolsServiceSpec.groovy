
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
@TestFor(NativeToolsService)
class NativeToolsServiceSpec extends Specification {
    def config = Holders.config

    File testDir

    def setup() {
        testDir = new File("${config.syndication.scratch.root}/test")
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
