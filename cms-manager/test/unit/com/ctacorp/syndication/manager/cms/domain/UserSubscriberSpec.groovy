package com.ctacorp.syndication.manager.cms.domain

import com.ctacorp.syndication.manager.cms.Subscriber
import com.ctacorp.syndication.manager.cms.User
import com.ctacorp.syndication.manager.cms.UserSubscriber
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserSubscriber)
@Build([UserSubscriber, User, Subscriber])
class UserSubscriberSpec extends Specification {

    Subscriber subscriber
    User user
    UserSubscriber userSubscriber

    def setup() {

        subscriber = Subscriber.build()
        user = User.build()
        userSubscriber = UserSubscriber.buildWithoutSave(user: user, subscriber: subscriber)
    }

    void "valid constraints"() {

        when: "the instance is saved"

        userSubscriber.save(flush: true)

        then: "it will not have errors"

        0 == userSubscriber.errors.errorCount
    }

    void "user can't be null constraint"() {

        given: "the user is null"

        userSubscriber.user = null

        when: "the instance is saved"

        userSubscriber.save(flush: true)

        then: "it will have errors"

        1 == userSubscriber.errors.errorCount
    }

    void "subscriber can't be null constraint"() {

        given: "the user is null"

        userSubscriber.subscriber = null

        when: "the instance is saved"

        userSubscriber.save(flush: true)

        then: "it will have errors"

        1 == userSubscriber.errors.errorCount
    }
}
