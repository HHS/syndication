package com.ctacorp.syndication.manager.cms.utils

import com.ctacorp.syndication.manager.cms.utils.mq.MqUtils
import com.ctacorp.syndication.manager.cms.utils.mq.RabbitMqConsumerService
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.SimpleGrailsController
import spock.lang.Specification

@TestFor(SimpleGrailsController)
class MqUtilsSpec extends Specification {

    def service = Mock(RabbitMqConsumerService)
    def queueName = "someQueueName"


    void "handle message gracefully handles a bogus message"() {

        when: "handling a message"

        MqUtils.handleMessage( "a bogus message", queueName, service)

        then: "don't throw any exceptions"

        noExceptionThrown()
    }

    void "handle message gracefully handles when the service throws an exception"() {

        given: "the message to consume"

        def message = [message:"a bogus message", errorMessage:"null", hash:"null", mediaId:"null", messageType:"Import", meta:"null"]

        when: "handling a message"

        MqUtils.handleMessage(message, queueName, service)

        then: "the service throws an exception"

        1 * service.handleMessage((message as JSON).toString(), queueName) >> {
            throw new Exception("WTF!")
        }

        then: "no exceptions to be thrown"

        noExceptionThrown()
    }

    void "handle message successfully delegates to the service"() {

        given: "the message to consume"

        def message = [message:"a bogus message", errorMessage:"null", hash:"null", mediaId:"null", messageType:"DELETE", meta:"null"]

        when: "handling a message"

        MqUtils.handleMessage(message, queueName, service)

        then: "the service throws an exception"

        1 * service.handleMessage(message, queueName)
    }

}
