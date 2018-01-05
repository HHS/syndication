
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms

import grails.converters.JSON

class KeyAgreement {

    String entity1
    String entity2
    String entity1PrivateKey
    String entity1PublicKey
    String entity2PrivateKey
    String entity2PublicKey
    String secret
    String prime
    String generator
    Date dateCreated

    static transients = ['keyAgreement', 'jsonExport']

    static constraints = {
        entity1 nullable:false
        entity2 nullable:false, unique:true
        entity1PublicKey nullable:false
        entity1PrivateKey nullable:false
        entity2PublicKey nullable:false, unique: true
        entity2PrivateKey nullable:false
        secret nullable:false
        prime nullable:false
        generator nullable:false
        jsonExport nullable:true, maxSize: 1000
    }

    String getJsonExport() {
        def json = ["Entity Name": entity2, "Public Key": entity2PublicKey, "Private Key": entity2PrivateKey, "Secret Key": secret] as JSON
        return json.toString(true)
    }

    String getKeyAgreement() {
        "${entity1}_${entity2}_keyAgreement"
    }
}
