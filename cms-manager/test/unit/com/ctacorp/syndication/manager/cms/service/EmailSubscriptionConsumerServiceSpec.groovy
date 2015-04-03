package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(EmailSubscriptionConsumerService)
@Build([EmailSubscription, Subscription])
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

    void "handle message creates requeues individual update subscriptions"() {

        given: "an email subscription instance"

        def subscription = Subscription.build(mediaId: '123456')

        def emailSubscription1 = EmailSubscription.build(subscription: subscription)
        def emailSubscription2 = EmailSubscription.build(subscription: subscription)

        when: "sending a message to the consumer with an update the message type a media id"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, mediaId: '123456'), queueName)

        then: "requeue each individual subscription found"

        1 * queueService.sendToEmailUpdateQueue(MessageType.UPDATE, emailSubscription1.id)
        1 * queueService.sendToEmailUpdateQueue(MessageType.UPDATE, emailSubscription2.id)

        and: "expect no exceptions to be thrown"

        noExceptionThrown()
    }

    void "correctly handle an update message when the email service fails"() {

        given: "an email subscription instance"

        def emailSubscription = EmailSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, emailSubscription: emailSubscription.id, meta: [attempts: 2]), queueName)

        then: "when an update exception is thrown by the email service"

        emailSubscriptionUpdateService.updateEmailSubscription(emailSubscription) >> {
            throw new RuntimeException("boogers!")
        }

        then: "requeue the message on the error queue"

        queueService.sendToEmailErrorQueue(MessageType.UPDATE, emailSubscription.id, null, 2)

        and: "don't throw the exception"

        noExceptionThrown()
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

        then: "send the message to the email error queue"

        queueService.sendToEmailErrorQueue(MessageType.IMPORT, 123, null, 0)
    }

    void "correctly handle a second attempt failed import"() {

        when: "handling a valid import message"

        service.handleMessage(new Message(messageType: MessageType.IMPORT, subscriptionId: 123, attempts: 1), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.importEmailSubscription(123) >> false

        then: "send the message to the email error queue"

        queueService.sendToEmailErrorQueue(MessageType.IMPORT, 123, null, 1)
    }

    void "correctly handle a failed update"() {

        given: "an email subscription instance"

        def emailSubscription = EmailSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, subscriptionId: emailSubscription.id, meta: [attempts: 2]), queueName)

        then: "fail the update"

        1 * emailSubscriptionUpdateService.updateEmailSubscription(emailSubscription) >> false

        and: "requeue the update on the email error queue"

        queueService.sendToEmailErrorQueue(MessageType.UPDATE, emailSubscription.id, null, 2)
    }

    void "correctly handle an update message"() {

        given: "an email subscription instance"

        def emailSubscription = EmailSubscription.build()

        when: "handling a valid update message"

        service.handleMessage(new Message(messageType: MessageType.UPDATE, subscriptionId: emailSubscription.id, meta: [attempts: 2]), queueName)

        then: "an exception is thrown by the email service"

        1 * emailSubscriptionUpdateService.updateEmailSubscription(emailSubscription) >> true
    }
}