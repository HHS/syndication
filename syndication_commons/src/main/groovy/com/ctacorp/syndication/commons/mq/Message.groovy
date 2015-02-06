package com.ctacorp.syndication.commons.mq

import groovy.json.JsonSlurper
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * The Message class is the Plain Old Groovy Object (POGO) representation of a
 * message to be transmitted or received via Message Queue. Instances of this
 * class can be serialized as JSON or vice-versa for easy messaging.
 **/
class Message{
    /**
     * Defines what type of message this is. Knowing the message type allows
     * the client to determine which fields to inspect upon receiving.
     */
    MessageType messageType

    /**
     * A content hash can be provided for convenience and to determine if the
     * content has changed from a previous delivery
     */
    String hash

    /**
     * A message destined for an end-user, such as "You have been subscribed successfully"
     */
    String userMessage

    /**
     * A message with detailed information intended for a developer. This
     * message might include debug information or information about the
     * circumstances surrounding a specific error.
     */
    String developerMessage

    /**
     * If there is a system error, details can be provided here along with stack trace if
     * needed.
     */
    String errorMessage

    /**
     * If the message is related to a specific piece of existing syndication media,
     * then the ID for that item can be included here.
     */
    String mediaId

    /**
     * If the message is related to an existing ssubscription in cms-manager,
     * then the ID for that item can be included here.
     */
    String subscriptionId

    /**
     * The actual content of the message. Contents should be comprised of JSON data with
     * any html/xml content correctly encoded for json embedding.
     */
    String payload

    /**
     * If the message relates to a specific target/source url, include it here
     */
    String targetUrl

    /**
     * If thee message related to a specific tiny/permanent url, include it here
     */
    String tinyUrl

    /**
     * A map containing an arbitrary collection of meta data. You can include any information
     * you want here, but don't duplicate any of the above fields.
     */
    Map meta

    Message(Map data){

        messageType         = getMessageType(data.messageType)  //what message type is this?
        hash                = data.hash                         //hash of content for content comparisons
        userMessage         = data.userMessage                  //message to present to an end user
        developerMessage    = data.developerMessage             //message for a developer
        errorMessage        = data.errorMessage                 //general error message
        mediaId             = data.mediaId                      //the unique id of an item in syndication
        subscriptionId      = data.subscriptionId               //the unique id of a subscription in cms-manager
        payload             = data.payload                      //body content (json data, html, whatever)
        targetUrl           = data.targetUrl                    //target URL for the content if it exists
        tinyUrl             = data.tinyUrl                      //tiny url for the content
        meta                = data.meta                         //any additional meta data to be included can be lumped here.
    }

    public static getMessageType(messageType){

        if(messageType instanceof MessageType){
            return messageType
        }
        if(messageType instanceof String){
            return MessageType."${messageType.toUpperCase()}"
        }
    }

    String toString(){

        "messageType: ${messageType.prettyName()}\n" +
        "hash: ${hash}\n" +
        "userMessage: ${userMessage}\n" +
        "developerMessage: ${developerMessage}\n" +
        "errorMessage: ${errorMessage}\n" +
        "mediaId: ${mediaId}\n" +
        "subscriptionId: ${subscriptionId}\n" +
        "payload: ${payload}\n" +
        "targetUrl: ${targetUrl}\n" +
        "tinyUrl: ${tinyUrl}\n" +
        "meta: ${meta}\n"
    }

    boolean equals(that) {
        EqualsBuilder.reflectionEquals(this, that)
    }

    int hashCode() {
        HashCodeBuilder.reflectionHashCode(this)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    public String toJsonString(){

        //noinspection UnnecessaryQualifiedReference
        def builder = new groovy.json.JsonBuilder()

        builder {
            messageType messageType.prettyName()
            hash hash
            userMessage userMessage
            developerMessage developerMessage
            errorMessage errorMessage
            mediaId mediaId
            subscriptionId subscriptionId
            payload payload
            targetUrl targetUrl
            tinyUrl tinyUrl
            meta meta
        }

        builder.toString()
    }

    @SuppressWarnings("GrUnresolvedAccess")
    public static Message messageFromJson(String jsonData){

        def slurper = new JsonSlurper()
        def json = slurper.parseText(jsonData)

        new Message(
            messageType : MessageType."${json.messageType.toUpperCase()}",
            hash : json.hash,
            userMessage : json.userMessage,
            developerMessage : json.developerMessage,
            errorMessage : json.errorMessage,
            mediaId : json.mediaId,
            subscriptionId : json.subscriptionId,
            payload : json.payload,
            targetUrl : json.targetUrl,
            tinyUrl : json.tinyUrl,
            meta : json.meta as Map
        )
    }
}
