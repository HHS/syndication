package com.ctacorp.syndication.manager.cms.utils.marshalling

import com.ctacorp.syndication.manager.cms.RestSubscription
import grails.converters.JSON

class RestSubscriptionMarshaller {

    RestSubscriptionMarshaller() {
        JSON.registerObjectMarshaller(RestSubscription) { RestSubscription restSubscription ->
            return [
                    subscriptionId  : restSubscription.id,
                    mediaId         : restSubscription.subscription.mediaId as Long,
                    title           : restSubscription.title,
                    sourceUrl       : restSubscription.sourceUrl,
                    notificationOnly: restSubscription.notificationOnly,
                    deliveryEndpoint: restSubscription.restSubscriber.deliveryEndpoint,
                    dateCreated     : restSubscription.dateCreated
            ]
        }
    }
}