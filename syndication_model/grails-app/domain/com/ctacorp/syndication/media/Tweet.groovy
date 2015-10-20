package com.ctacorp.syndication.media

import com.ctacorp.grails.swagger.annotations.ModelExtension
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.PropertyAttribute
import com.ctacorp.syndication.social.TwitterAccount

@ModelExtension(id = "Tweet", model = "MediaItem", addProperties = [
    @ModelProperty(propertyName = "account",      attributes = [@PropertyAttribute(type = "TwitterAccount",             required = true)]),
    @ModelProperty(propertyName = "tweetId",      attributes = [@PropertyAttribute(type = "integer",  format = "int64", required = true)]),
    @ModelProperty(propertyName = "messageText",  attributes = [@PropertyAttribute(type = "string",                     required = true)]),
    @ModelProperty(propertyName = "mediaUrl",     attributes = [@PropertyAttribute(type = "string",                     required = false)]),
    @ModelProperty(propertyName = "tweetDate",    attributes = [@PropertyAttribute(type = "string",   format = "date",  required = true)])
])
class Tweet extends MediaItem{
    TwitterAccount account
    Long tweetId
    String messageText
    String mediaUrl
    Date tweetDate

    static constraints = {
        account         nullable: false
        tweetId         nullable: false
        messageText     nullable: false,    maxSize: 255
        mediaUrl        nullable: true,     url: true
        tweetDate       nullable: false
    }
}
