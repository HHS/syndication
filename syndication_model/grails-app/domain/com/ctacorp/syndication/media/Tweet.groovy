package com.ctacorp.syndication.media

import com.ctacorp.grails.swagger.annotations.*
import com.ctacorp.syndication.social.TwitterAccount

@Definition
class Tweet extends MediaItem{
    @DefinitionProperty(type=DefinitionPropertyType.OBJECT, reference = 'TwitterAccount')
    TwitterAccount account
    @DefinitionProperty(type=DefinitionPropertyType.INTEGER, format = "int32")
    Long tweetId
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String messageText
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String mediaUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String videoVariantUrl
    @DefinitionProperty(type=DefinitionPropertyType.STRING, format = "date")
    Date tweetDate

    static constraints = {
        account         nullable: false
        tweetId         nullable: false
        messageText     nullable: false,    maxSize: 255
        mediaUrl        nullable: true,     url: true
        tweetDate       nullable: false
        videoVariantUrl nullable: true,     url: true
    }
}
