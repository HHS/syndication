package com.ctacorp.syndication.commons.mq

class Test {
    
    public static void main(String[] args){

        Test test = new Test()
        testName()
    }

    static def testName(){

        Message m = new Message(
            messageType:MessageType.PUBLISH.prettyName(),
            hash:"This is the hash".hashCode(),
            userMessage: "This is some user message",
            developerMessage: "This is a developer message",
            errorMessage:"This is an error message",
            mediaId:"12345",
            subscriptionId:"23456",
            payload:([alpha:"beta", gamma:"theta"]).toString(),
            targetUrl:"http://www.apple.com",
            tinyUrl:"http://tiny.hhs.gov",
            meta: [metaItemOne:[something:"else"]]
        )

        println "Message as a JSON string:"
        println "--------------------------------------"
        println m.toJsonString()

        println "\nMessage deserialized from a JSON string"
        println "--------------------------------------"
        println Message.messageFromJson(m.toJsonString())
    }
}
