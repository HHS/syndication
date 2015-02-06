
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

class HtmlPreviewService {
    static transactional = false
    def grailsApplication
    def nativeToolsService
    def fileService

    File getImageOfHtml(String url, int width) {
        String fileName = Hash.md5(url)
        File full = new File("${grailsApplication.config.syndication.scratch.root}/preview/full/${fileName}.png")

        if(fileService.fileCheck(full, grailsApplication.config.syndication.preview.pagettl as Integer)){
            return full
        }

        String command = ""
        switch(grailsApplication.config.syndication.htmlRenderingEngine){
            case "webkit2png": command = "${grailsApplication.config.webkit2png.location}/webkit2png -W ${width} -F -D ${grailsApplication.config.syndication.scratch.root}/preview/full -o ${fileName} \"${url}\""; break;
            case "cutycapt": command = "${grailsApplication.config.xvfb.location}/xvfb-run -a -n 1 --server-args=\"-screen 0, 1024x768x24\" ${grailsApplication.config.cutycapt.location}/cutycapt --url=\"${url}\" --min-width=${width} --out=${full.absolutePath}"; break;
            case "cutycaptMac": command = "${grailsApplication.config.cutycapt.location}/cutycapt --url=\"${url}\" --min-width=${width} --out=${full.absolutePath}"; break; //cutycapt running on mac only
            default: command = "Missing or invalid html rendering engine specified"
        }

        println command

        nativeToolsService.exe(command)
        moveFile(full)
    }

    private moveFile(full){
        File outFile
        switch(grailsApplication.config.syndication.htmlRenderingEngine){
            case "webkit2png":
                outFile = new File(full.getAbsolutePath()[0..-1 -4]+"-full.png")
                 if(outFile.exists()){
                    outFile.renameTo(full)
                    outFile = full
                }
                break;
            case "cutycapt": outFile = full; break;
            case "cutycaptMac": outFile = full; break;
            default: outFile = null
        }
        fileService.verify(outFile)
    }
}
