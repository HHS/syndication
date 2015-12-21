package com.ctacorp.syndication.microsite

class FlaggedMicrosite {

    MicroSite microsite
    Date dateFlagged = new Date()
    String message
    Boolean ignored = false

    static constraints = {
        microsite   nullable: false, unique: true
        dateFlagged nullable: false
        message     nullable: false, blank:false, maxSize: 255
        ignored()
    }
}
