/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

class RhythmyxSubscription {

    String sourceUrl
    String systemTitle
    Long targetFolder
    String contentType
    String contentId
    Date dateCreated
    Date lastUpdated
    String deliveryFailureLogId

    RhythmyxWorkflow rhythmyxWorkflow
    static embedded = ['rhythmyxWorkflow']

    boolean useAsDefaultWorkflow = true
    static transients = ['useAsDefaultWorkflow']

    static belongsTo = [subscription:Subscription, rhythmyxSubscriber:RhythmyxSubscriber]

    static constraints = {
        targetFolder(nullable: false)
        contentType(nullable: false, blank: false)
        systemTitle(nullable: true, blank: false)
        contentId(nullable: true, blank: false)
        sourceUrl(nullable: false, blank: false, url: true)
        subscription(nullable: true)
        rhythmyxWorkflow(nullable: true)
        deliveryFailureLogId(nullable: true, blank: false)
    }
}
