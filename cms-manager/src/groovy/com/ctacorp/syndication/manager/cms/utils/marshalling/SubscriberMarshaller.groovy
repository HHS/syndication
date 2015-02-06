package com.ctacorp.syndication.manager.cms.utils.marshalling
import com.ctacorp.syndication.manager.cms.Subscriber
import grails.converters.JSON

class SubscriberMarshaller {

    def serverUrl

    SubscriberMarshaller(serverURL) {

        this.serverUrl = serverURL

        JSON.registerObjectMarshaller(Subscriber) { Subscriber subscriber ->

            def subscriberMap = [
                    id    : subscriber.subscriberId,
                    name            : subscriber.name,
                    email           : subscriber.email,
                    uri             : serverUrl + "/api/v1/subscriber.json?id=${subscriber.subscriberId}"
            ]

            if(subscriber.keyAgreement) {
                subscriberMap.keyAgreement = subscriber.keyAgreement
            }

            return subscriberMap
        }
    }
}
