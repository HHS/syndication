package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(EmailSubscriptionConsumerService)
@Build([EmailSubscription,Subscription])
class EmailSubscriptionConsumerServiceSpec extends Specification {

    def emailSubscriptionUpdateService = Mock(EmailSubscriptionUpdateService)
    def queueService = Mock(QueueService)
    def queueName = 'someQueue'

    def setup() {
        service.emailSubscriptionUpdateService = emailSubscriptionUpdateService
        service.queueService = queueService
    }

    void "correctly handle an uninteresting messageType"() {

        when: "handling a message with a missing messageType"

        service.handleMessage(new Message(messageType: MessageType.PUBLISH), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle an update message with a missing mediaId"() {

        when: "handling an update message with a missing mediaId"

        service.handleMessage(new Message(messageType: MessageType.UPDATE), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle an update message when the email service fails"() {

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: '123'), queueName)

        then: "when an update exception is thrown by the email service"

        emailSubscriptionUpdateService.updateEmailSubscriptions('123') >> {
            throw new RuntimeException("boogers!")
        }

        and: "don't catch the exception"

        thrown(RuntimeException)
    }

    void "correctly handle an import message with a missing subscriptionId"() {

        when: "handling an import message with a missing subscriptionId"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: null), queueName)

        then: "don't process the message"

        noExceptionThrown()
    }

    void "correctly handle an import message when the email service fails"() {

        when: "handling a valid import message"

        service.handleMessage([messageType: MessageType.IMPORT, subscriptionId: 123], queueName)

        then: "when an exception is thrown by the email service"

        emailSubscriptionUpdateService.importEmailSubscription(123) >> {
            throw new RuntimeException("boogers!")
        }

        and: "don't catch the exception"

        thrown(RuntimeException)
    }

    void "correctly handle an import message"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123), queueName)

        then: "when an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.importEmailSubscription(123)

        and: "don't catch the exception"

        noExceptionThrown()
    }

    void "correctly handle a failed import"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.importEmailSubscription(123) >> false

        then: "send the message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, 123, null, 0)
    }

    void "correctly handle a second attempt failed import"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123, attempts: 1), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.importEmailSubscription(123) >> false

        then: "send the message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.IMPORT, 123, null, 1)
    }

    void "correctly handle a failed update"() {

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: '1'), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.updateEmailSubscriptions('1') >> [[id:9],[id:10]]

        then: "send the first failed update message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.UPDATE, 9, '1', 0)

        and: "send the second failed update message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.UPDATE, 10, '1', 0)
    }

    void "correctly handle a second attempt failed update"() {

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: 1, attempts: 1), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.updateEmailSubscriptions('1') >> [[id:9]]

        then: "send the first failed update message to the rhythmyx error queue"

        queueService.sendToRhythmyxErrorQueue(MessageType.UPDATE, 9, '1', 1)
    }

    void "correctly handle an update message"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: '123'), queueName)

        then: "when an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.updateEmailSubscriptions('123')

        and: "don't catch the exception"

        noExceptionThrown()
    }
}