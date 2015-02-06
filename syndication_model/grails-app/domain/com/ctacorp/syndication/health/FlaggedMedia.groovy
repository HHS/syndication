package com.ctacorp.syndication.health

import com.ctacorp.syndication.MediaItem

class FlaggedMedia {
    MediaItem mediaItem
    Date dateFlagged = new Date()
    String message
    Boolean ignored = false
    FailureType failureType

    static constraints = {
        mediaItem   nullable: false
        dateFlagged nullable: false
        message     nullable: false, blank:false, maxSize: 255
        ignored()
        failureType nullable: false
    }

    static enum FailureType {
        UNREACHABLE("Unreachable"), SERVER_ERROR("Server Error"), UNEXTRACTABLE("Unextractable"), NO_CONTENT("No Content"), SHORT_CONTENT("Short Content")

        String prettyName
        String description

        FailureType(String prettyName){
            this.prettyName = prettyName
        }
    }
}
