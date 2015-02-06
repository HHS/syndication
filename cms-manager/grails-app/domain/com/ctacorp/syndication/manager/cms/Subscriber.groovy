
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms

import org.apache.commons.lang3.ObjectUtils

class Subscriber {

    String name
    String email
    KeyAgreement keyAgreement
    Date dateCreated
    Date lastUpdated
    boolean isPrivileged

    String subscriberId
    boolean sendKeyAgreement = true
    static transients = ['sendKeyAgreement']

    static constraints = {
        name nullable: false, blank: false, unique: true
        email nullable:false, email:true
        keyAgreement nullable: true, unique: true
        subscriberId nullable: true, blank: false
    }

    def beforeInsert() {
        subscriberId = generateSubscriberId(this)
    }

    static generateSubscriberId(Subscriber subscriber) {
        ObjectUtils.hashCodeMulti(subscriber.name).abs() as String
    }

    def afterUpdate(){
        keyAgreement?.entity2 = name
    }
}