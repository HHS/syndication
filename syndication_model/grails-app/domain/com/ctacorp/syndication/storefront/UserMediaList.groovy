package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.authentication.User

class UserMediaList {
    String name
    String description
    Date dateCreated
    Date lastUpdated

    static hasMany = [mediaItems:MediaItem]
    static belongsTo = [user:User]

    static constraints = {
        name nullable:false, size:1..255
        description nullable:false, size:1..255
    }

    static namedQueries = {
        containsMediaItem{MediaItem mi ->
            mediaItems{
                eq "id", mi.id
            }
        }
    }
}