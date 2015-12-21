package com.ctacorp.syndication.microsite

import com.ctacorp.syndication.authentication.User

class MicrositeRegistration {

    String organization
    String description
    boolean verified = false

    static belongsTo = [user:User]

    static constraints = {

        organization blank:false, nullable: false
        description blank:false, nullable: false
        verified nullable:false
    }
}
