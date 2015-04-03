
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */

package com.ctacorp.syndication.manager.cms

class RhythmyxSubscriber {

    String instanceName
    String rhythmyxHost
    String rhythmyxPort
    String rhythmyxUser
    String rhythmyxPassword
    String rhythmyxCommunity
    Date lastUpdated
    Date dateCreated

    static belongsTo = [subscriber: Subscriber]

    RhythmyxWorkflow rhythmyxWorkflow
    static embedded = ['rhythmyxWorkflow']
    static transients = ['emailNotificationsEnabled']

    static constraints = {
        instanceName(nullable: false, blank: false, unique: true)
        rhythmyxHost(nullable: false, blank: false)
        rhythmyxPort(nullable: false, blank: false, matches:'[0-9]+')
        rhythmyxUser(nullable: false, blank: false)
        rhythmyxPassword(nullable: false, blank: false)
        rhythmyxCommunity(nullable: false, blank: false)
        rhythmyxWorkflow(nullable: true)
    }

    boolean getEmailNotificationsEnabled(sourceUrl) {
        def emailSubscribers = EmailSubscriber.findAllBySubscriber(this.subscriber)
        emailSubscribers.each { EmailSubscriber emailSubscriber ->
            if(EmailSubscription.findByEmailSubscriberAndSourceUrl(emailSubscriber, sourceUrl)) {
                return true
            }
        }
        false
    }
    
    def getEmailSubscribersWithSameSubscriber(){
        EmailSubscriber.findAllBySubscriber(this.subscriber)
    }
}
