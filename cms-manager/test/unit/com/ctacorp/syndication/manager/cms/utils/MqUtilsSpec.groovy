package com.ctacorp.syndication.manager.cms.utils
import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumerService
import spock.lang.Specification

class MqUtilsSpec extends Specification {

    def service = Mock(RabbitMqConsumerService)
    def queueName = "someQueueName"

    void "handle message gracefully handles a bogus message"() {

        when: "handling a message"

        MqUtils.handleMessage("a bogus message", queueName, service)

        then: "don't throw any exceptions"

        noExceptionThrown()
    }

    void "handle message gracefully handles when the service throws an exception"() {

        given: "the message to consume"

        def message = new Message(messageType: MessageType.DELETE)

        when: "handling a message"

        MqUtils.handleMessage(message.toJsonString(), queueName, service)

        then: "the service throws an exception"

        1 * service.handleMessage(message, queueName) >> {
            throw new Exception("WTF!")
        }

        then: "no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle message successfully delegates to the service"() {

        given: "the message to consume"

        def message = new Message(messageType: MessageType.DELETE)

        when: "handling a message"

        MqUtils.handleMessage(message.toJsonString(), queueName, service)

        then: "the service throws an exception"

        1 * service.handleMessage(message, queueName)
    }
}
