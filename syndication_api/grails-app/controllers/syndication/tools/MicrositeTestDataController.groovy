/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package syndication.tools

import com.ctacorp.syndication.media.MediaItem
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class MicrositeTestDataController {
    def tagsService

    def index() {

        [message:params.message]
    }

    def tagMedia(){
        def medias = [
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/1.jpg",
                    tags:["health","doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/2.jpg",
                    tags:["health"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/3.jpg",
                    tags:["health","food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/4.jpg",
                    tags:["doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/6.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/9.jpg",
                    tags:["doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/11.jpg",
                    tags:["doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/15.jpg",
                    tags:["doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/16.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/19.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/21.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/23.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/24.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/25.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/26.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/27.jpg",
                    tags:["doctor"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/29.jpg",
                    tags:["food"]],
                    [sourceUrl:"https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/30.jpg",
                    tags:["food"]]
        ]
            
        medias.each{ entry ->
                entry.tags.each { String tag ->
                        tagsService.tagMediaItemByName(MediaItem.findBySourceUrl(entry.sourceUrl).id, tag, 1L, 1L)
                }
        }

        redirect action:"index", params: [message:"done"]
    }
}
