package com.ctacorp.syndication.manager.cms.controller

import com.ctacorp.syndication.manager.cms.KeyAgreement
import com.ctacorp.syndication.manager.cms.LoggingService
import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.SubscriberController
import com.ctacorp.syndication.manager.cms.SubscriberMailService
import com.ctacorp.syndication.manager.cms.SubscriberService
import com.ctacorp.syndication.manager.cms.SubscriptionService
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(SubscriberController)
@Build([Subscriber, KeyAgreement])
class SubscriberControllerSpec extends Specification {

    def subscriberMailService = Mock(SubscriberMailService)
    def loggingService = Mock(LoggingService)
    def subscriptionService = Mock(SubscriptionService)
    def subscriberService = Mock(SubscriberService)

    Subscriber subscriber
    Subscriber subscriberNotSaved

    def setup() {

        controller.subscriberMailService = subscriberMailService
        controller.loggingService = loggingService
        controller.subscriptionService = subscriptionService
        controller.subscriberService = subscriberService

        subscriber = Subscriber.build()
        subscriberNotSaved = Subscriber.buildWithoutSave()
    }

    void "show action handles a null subscriber instance"() {

        when: "the action is called with null instance"

        controller.show(null)

        then: "redirect to the index view"

        response.redirectUrl == '/subscriber/index'

        and: "a not found message"

        flash.errors == ['default.not.found.message']
    }

    void "show action retrieves a subscriber"() {

        when: "the action is called with a valid instance"

        controller.show(subscriber)

        then: "respond with the show view"

        view == 'show'

        and: "the subscriber"

        model.subscriberInstance ?: model.subscriber == subscriber
    }

    void "create action responds with a new instance"() {

        when: "calling the action"

        controller.create()

        then: "respond with the create view"

        view == 'create'

        and: "a new subscriber instance"

        model.subscriberInstance ?: model.subscriber != null
    }

    void "save action handles validation failures"() {

        given: "a subscriber with a null email"

        def subscriber = Subscriber.buildWithoutSave(email: null)

        when: "the action is called with an invalid instance"

        request.method = "POST"
        controller.save(subscriber)

        then: "respond with the create view with errors"

        view == "create"
        subscriber.errors.errorCount == 1
    }

    void "save action successfully creates a subscriber"() {

        when: "the action is called with a valid instance"

        request.method = "POST"
        controller.save(subscriberNotSaved)

        then: "create a new subscriber and key agreement"

        Subscriber.count == 2
        subscriberNotSaved.keyAgreement

        and: "redirect to the show view with a created message"

        response.redirectUrl == "/subscriber/show/2"
        flash.message == "default.created.message"
    }

    void "save action successfully creates a subscriber and emails the key agreement"() {

        given: "the 'sendKeyAgreement' param is true"

        params.sendKeyAgreement = true

        when: "the action is called with a valid instance"

        request.method = "POST"
        controller.save(subscriberNotSaved)

        then: "create a new subscriber and key agreement"

        Subscriber.count == 2
        subscriberNotSaved.keyAgreement

        and: "mail the key agreement to the subscriber's email"

        1 * subscriberMailService.sendSubscriberKeyAgreement(subscriberNotSaved)

        and: "redirect to the show view with a created message"

        response.redirectUrl == "/subscriber/show/2"
        flash.message == "default.created.message"
    }

    void "save action handles when the email service throws"() {

        given: "the 'sendKeyAgreement' param is true"

        params.sendKeyAgreement = true

        and: "the exception to throw"

        def exception = new Exception()

        when: "the action is called with a valid instance"

        request.method = "POST"
        controller.save(subscriberNotSaved)

        then: "create a new subscriber and key agreement"

        Subscriber.count == 2
        subscriberNotSaved.keyAgreement

        and: "mail the key agreement to the subscriber's email"

        1 * subscriberMailService.sendSubscriberKeyAgreement(subscriberNotSaved) >> {
            throw exception
        }

        and: "log the exception create a user friendly error message"

        1 * loggingService.logError('keyAgreement.email.send.error', exception) >> 'a friendly message'

        and: "redirect to the show view with an error message"

        response.redirectUrl == "/subscriber/show/2"
        flash.errors == ['a friendly message', 'keyAgreement.email.send.error']
    }

    void "update action handles a null subscriber instance"() {

        when: "the action is called with null instance"

        request.method = "PUT"
        controller.update(null)

        then: "redirect to the index view"

        response.redirectUrl == '/subscriber/index'

        and: "a not found message"

        flash.errors == ['default.not.found.message']
    }

    void "update action handles validation failures"() {

        given: "the subscriber instance has a null email"

        subscriber.email = null

        when: "the action is called with an invalid instance"

        request.method = "PUT"
        controller.update(subscriber)

        then: "respond with the edit view with errors"

        view == "edit"
        subscriber.errors.errorCount == 1
    }

    void "update action successfully updates a subscriber"() {

        when: "the action is called with a valid instance"

        request.method = "PUT"
        controller.update(subscriber)

        then: "redirect to the show view with a updated message"

        response.redirectUrl == "/subscriber/show/1"
        flash.message == "default.updated.message"
    }

    void "update action successfully updates a subscriber and emails the key agreement"() {

        given: "the 'sendKeyAgreement' param is true"

        params.sendKeyAgreement = true

        when: "the action is called with a valid instance"

        request.method = "PUT"
        controller.update(subscriber)

        then: "mail the key agreement to the subscriber's email"

        1 * subscriberMailService.sendSubscriberKeyAgreement(subscriber)

        and: "redirect to the show view with a updated message"

        response.redirectUrl == "/subscriber/show/1"
        flash.message == "default.updated.message"
    }

    void "update action handles when the email service throws"() {

        given: "the 'sendKeyAgreement' param is true"

        params.sendKeyAgreement = true

        and: "the subscriber's name has changed"

        subscriber.name = "something else"

        and: "the exception to throw"

        def exception = new Exception()

        when: "the action is called with a valid instance"

        request.method = "PUT"
        controller.update(subscriber)

        then: "update a new key agreement"

        Subscriber.findByName(subscriber.name)

        and: "mail the key agreement to the subscriber's email"

        1 * subscriberMailService.sendSubscriberKeyAgreement(subscriber) >> {
            throw exception
        }

        and: "log the exception create a user friendly error message"

        1 * loggingService.logError('keyAgreement.email.send.error', exception) >> 'a friendly message'

        and: "redirect to the show view with an error message"

        response.redirectUrl == "/subscriber/show/1"
        flash.errors == ['a friendly message', 'keyAgreement.email.send.error']
    }

    void "delete action handles a null subscriber instance"() {

        when: "the action is called with null instance"

        request.method = "DELETE"
        controller.delete(null)

        then: "redirect to the index view"

        response.redirectUrl == '/subscriber/index'

        and: "a not found message"

        flash.errors == ['default.not.found.message']
    }

    void "delete action deletes a subscriber"() {

        when: "the action is called with a valid instance"

        request.method = "DELETE"
        controller.delete(subscriber)

        then: "delegate the delete to the subscriber service"

        1 * subscriberService.deleteSubscriber(subscriber)

        and: "notify the subscriber's email owner that the subscriber has been removed"

        1 * subscriberMailService.sendSubscriberDelete(subscriber.email)

        and: "redirect to the index view"

        response.redirectUrl == '/subscriber/index'

        and: "a not found message"

        flash.message == 'default.deleted.message'
    }

    void "delete action handles when the mail service throws an exception"() {

        given: "the exception to throw"

        def exception = new Exception()

        when: "the action is called with a valid instance"

        request.method = "DELETE"
        controller.delete(subscriber)

        then: "delegate the delete to the subscriber service"

        1 * subscriberService.deleteSubscriber(subscriber)

        and: "notify the subscriber's email owner that the subscriber has been removed"

        1 * subscriberMailService.sendSubscriberDelete(subscriber.email) >> {
            throw exception
        }

        and: "log the exception create a user friendly error message"

        1 * loggingService.logError('subscriber.delete.email.send.error', exception) >> 'a friendly message'

        and: "redirect to the index view with an error message"

        response.redirectUrl == "/subscriber/index"
        flash.errors == ['a friendly message', 'subscriber.delete.email.send.error']

        and: "include a deleted message"

        flash.message == "default.deleted.message"
    }
}