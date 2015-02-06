/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.utils.mq

import com.ctacorp.syndication.commons.mq.Message
import groovy.json.JsonException
import org.apache.commons.logging.LogFactory

class MqUtils {

    static log = LogFactory.getLog(this)

    static Long getSubscriptionId(message) {

        def subscriptionId = message.subscriptionId

        try {
            if (subscriptionId) {
                return subscriptionId as Long
            }
        } catch (ignored) {
            log.error("subscriptionId '${subscriptionId}' is not a long value")
        }

        return null
    }

    static void handleMessage(String message, String queueName, service) {

        log.info("Received the message \n${message} on the '${queueName}'")

        try {
            service.handleMessage(Message.messageFromJson(message), queueName)
        } catch (JsonException ignored) {
            log.warn("Received an malformed message '${message}' on the '${queueName}' queue")
        } catch (t) {
            log.error("Unexpected exception occurred when processing message: ${message}", t)
        }

        log.info("Message processed")
    }
}
