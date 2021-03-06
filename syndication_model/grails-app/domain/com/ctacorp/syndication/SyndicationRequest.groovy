/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem

class SyndicationRequest {
    String requestedUrl
    String contactEmail
    String requesterNote
    String adminNote
    String status = "Pending"
    Date dateCreated
    Date lastUpdated
    MediaItem mediaItem

    static constraints = {
        requestedUrl nullable: false, blank: false, maxSize: 5000
        contactEmail nullable: false, blank: false, maxSize: 255
        requesterNote nullable: true, blank: false, maxSize: 2000
        adminNote nullable: true, blank: false, maxSize: 2000
        status nullable: false, blank: false, maxSize: 255
        mediaItem nullable: true                      //If this is a new request, we don't have the media yet
        dateCreated()
        lastUpdated()
    }

    static namedQueries = {
        //Sort Order -------------------------------------------------------------------
        multiSort{ String sortQuery = "id" ->
            def parts = sortQuery.split(",")
            parts.each{ part ->
                if(part.startsWith("-")){
                    String field = part[1..-1]
                    order(field, 'desc')
                } else{
                    order(part, 'asc')
                }
            }
        }
    }

    String toString() { requestedUrl }
}
