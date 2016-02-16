package com.ctacorp.syndication.social

import com.ctacorp.syndication.media.Collection

class TwitterStatusCollector {

    String hashTags
    Collection collection
    Date startDate
    Date endDate
    static hasMany=[twitterAccounts:TwitterAccount]

    static constraints = {
        hashTags        nullable: false, unique: true
        collection      nullable: false
        startDate       nullable: false
        endDate         nullable: true
        twitterAccounts()
    }
}
