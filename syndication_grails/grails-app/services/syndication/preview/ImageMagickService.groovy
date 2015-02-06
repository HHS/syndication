

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

class ImageMagickService {
    static transactional = false
    def grailsApplication
    def fileService
    def nativeToolsService

    // resize without names --------------------------------------------------------------------------------------------------------------

    File thumbnail(File original) { thumbnail(original, Hash.md5(original.bytes)) }

    File thumbnailCropped(File original) { thumbnailCropped(original, Hash.md5(original.bytes)) }

    File small(File original){ small(original, Hash.md5(original.bytes)) }

    File smallCropped(File original){ smallCropped(original, Hash.md5(original.bytes)) }

    File medium(File original) { medium(original, Hash.md5(original.bytes)) }

    File mediumCropped(File original) { mediumCropped(original, Hash.md5(original.bytes)) }

    File large(File original) { large(original, Hash.md5(original.bytes)) }

    File largeCropped(File original) { largeCropped(original, Hash.md5(original.bytes)) }

    File custom(File original, Integer width, Integer height) { custom(original, Hash.md5(original.bytes), width, height) }

    File customCropped(File original, Integer width, Integer height) { customCropped(original, Hash.md5(original.bytes), width, height) }

    // resize with names -----------------------------------------------------------------------------------------------------------------

    File thumbnail(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/thumbnail/${name}.jpg")
        resize(original, dest, grailsApplication.config.syndication.preview.thumbnail.width, grailsApplication.config.syndication.preview.thumbnail.height)
    }

    File thumbnailCropped(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/thumbnail/${name}.jpg")
        resizeAndCrop(original, dest, grailsApplication.config.syndication.preview.thumbnail.width, grailsApplication.config.syndication.preview.thumbnail.height)
    }

    File small(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/small/${name}.jpg")
        resize(original, dest, grailsApplication.config.syndication.preview.small.width, grailsApplication.config.syndication.preview.small.height)
    }

    File smallCropped(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/small/${name}.jpg")
        resizeAndCrop(original, dest, grailsApplication.config.syndication.preview.small.width, grailsApplication.config.syndication.preview.small.height)
    }

    File medium(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/medium/${name}.jpg")
        resize(original, dest, grailsApplication.config.syndication.preview.medium.width, grailsApplication.config.syndication.preview.medium.height)
    }

    File mediumCropped(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/medium/${name}.jpg")
        resizeAndCrop(original, dest, grailsApplication.config.syndication.preview.medium.width, grailsApplication.config.syndication.preview.medium.height)
    }

    File large(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/large/${name}.jpg")
        resize(original, dest, grailsApplication.config.syndication.preview.large.width, grailsApplication.config.syndication.preview.large.height)
    }

    File largeCropped(File original, String name){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/large/${name}.jpg")
        resizeAndCrop(original, dest, grailsApplication.config.syndication.preview.large.width, grailsApplication.config.syndication.preview.large.height)
    }

    File custom(File original, String name, int width, int height){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/custom/${name}.jpg")
        resize(original, dest, width, height)
    }

    File customCropped(File original, String name, int width=640, int height=480){
        File dest = new File("${grailsApplication.config.syndication.scratch.root}/preview/custom/${name}.jpg")
        resizeAndCrop(original, dest, width, height)
    }

    File resize(File original, File dest, int width=640, Integer height=480) {
        if(fileService.fileCheck(dest, grailsApplication.config.syndication.preview.pagettl)){ return dest }
        String command = "${grailsApplication.config.imageMagick.location}/convert ${original.absolutePath} -resize ${width}x${height}\\> ${dest.absolutePath}"

        nativeToolsService.exe(command)
        fileService.verify(dest)
    }

    File resizeAndCrop(File original, File dest, int width, int height) {
        if(fileService.fileCheck(dest, grailsApplication.config.syndication.preview.pagettl)){ return dest }
        String command = "${grailsApplication.config.imageMagick.location}/convert ${original.absolutePath} -resize ${width}x${height}\\^ -gravity north -extent ${width}x${height} ${dest.absolutePath}"

        nativeToolsService.exe(command)
        fileService.verify(dest)
    }
}
