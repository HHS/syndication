/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms.service

import com.ctacorp.syndication.manager.cms.*
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.hibernate.SessionFactory
import spock.lang.Specification

@TestFor(UserSubscriberService)
@Build([User, Subscriber, RhythmyxSubscriber, UserSubscriber, UserRole, Role, Subscription, RhythmyxSubscription])
class UserSubscriberServiceSpec extends Specification {

    def sessionFactory = Mock(SessionFactory)
    def springSecurityService = Mock(SpringSecurityService)

    User user1
    User user2

    Subscriber subscriber1
    Subscriber subscriber2

    Subscription subscription
    RhythmyxSubscription rhythmyxSubscription1
    RhythmyxSubscription rhythmyxSubscription2

    RhythmyxSubscriber rhythmyxSubscriber1
    RhythmyxSubscriber rhythmyxSubscriber2

    def setup() {

        service.sessionFactory = sessionFactory

        user1 = User.build(username: 'user1')
        user2 = User.build(username: 'user2')

        subscriber1 = Subscriber.build(name: 'subscriber')
        subscriber2 = Subscriber.build(name: 'subscriber2')

        rhythmyxSubscriber1 = RhythmyxSubscriber.build(instanceName: 'rhythmyx.subscriber.1.jp', subscriber: subscriber1)
        rhythmyxSubscriber2 = RhythmyxSubscriber.build(instanceName: 'rhythmyx.subscriber.2.jp', subscriber: subscriber2)
        RhythmyxSubscriber.build(instanceName: 'rhythmyx.subscriber.3.jp', subscriber: subscriber1)

        UserSubscriber.build(user: user1, subscriber: subscriber1)
        UserSubscriber.build(user: user1, subscriber: subscriber2)

        subscription = Subscription.build()
        rhythmyxSubscription1 = RhythmyxSubscription.build(subscription: subscription, rhythmyxSubscriber: rhythmyxSubscriber1)
        rhythmyxSubscription2 = RhythmyxSubscription.build(subscription: subscription, rhythmyxSubscriber: rhythmyxSubscriber2)

        service.springSecurityService = springSecurityService
    }

    void "get all subscribers for user"() {

        when: "getting all the subscribers for a user"

        def subscribers = service.getSubscribers()

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user1

        and: "return the list of associated subscribers"

        subscribers.size() == 2
        subscribers[0].name == 'subscriber'
        subscribers[1].name == 'subscriber2'
    }

    void "get all subscribers for a user without any associated subscribers"() {

        when: "getting all the subscribers for a user"

        def subscribers = service.getSubscribers()

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user2

        and: "the returned subscriber list will be empty"

        subscribers.size() == 0
    }

    void "get all rhythmyx subscribers for a user"() {

        when: "getting all the rhythmyx subscribers for a user"

        def rhythmyxSubscribers = service.listRhythmyxSubscribers()

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user1

        and: "return the list of rhythmyx subscribers"

        rhythmyxSubscribers.size() == 3
        rhythmyxSubscribers[0].instanceName == 'rhythmyx.subscriber.1.jp'
        rhythmyxSubscribers[1].instanceName == 'rhythmyx.subscriber.2.jp'
        rhythmyxSubscribers[2].instanceName == 'rhythmyx.subscriber.3.jp'
    }

    void "get all rhythmyx subscribers for a user without any rhythmyx subscriptions"() {

        when: "getting all the rhythmyx subscribers for a user"

        def rhythmyxSubscribers = service.listRhythmyxSubscribers()

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user2

        and: "the returned rhythmyx subscriber list will be empty"

        rhythmyxSubscribers.size() == 0
    }

    void "associate a user with a subscriber"() {

        when: "associating a user with a subscriber"

        def userSubscriber = service.associateUserWithSubscriber(subscriber2, user2)

        then: "create the user subscriber domain object"

        userSubscriber.id
        userSubscriber.subscriber == subscriber2
        userSubscriber.user == user2
    }

    void "associate a user with multiple subscribers"() {

        when: "associating a user with one or more subscribers"

        def userSubscribers = service.associateUserWithSubscribers([subscriber1, subscriber2], user2)

        then: "create the user subscriber domain objects"

        userSubscribers.size() == 2
        userSubscribers[0].subscriber == subscriber1
        userSubscribers[1].subscriber == subscriber2
    }

    void "disassociate a user from a subscriber"() {

        when: "disassociating a user from a subscriber"

        service.disassociateUserFromSubscribers(user1)

        then: "delete all the user's subscriber associations"

        UserSubscriber.count == 0
    }

    void "disassociate a subscriber from one or more users"() {

        setup: "associate multiple users to the same subscriber"

        UserSubscriber.build(user: user1, subscriber: subscriber2)

        when: "disassociating a subscriber from users"

        service.disassociateSubscriberFromUsers(subscriber2)

        then: "delete all the subscriber's user associations"

        UserSubscriber.count == 1
        UserSubscriber.list()[0].subscriber == subscriber1
    }

    void "list rhythmyx subscriptions"() {

        when: "getting all the email subscriptions for a user"

        def rhythmyxSubscriptions = service.listRhythmyxSubscriptions()

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user1

        and: "return the list of email subscribers"

        rhythmyxSubscriptions.size() == 2
    }

    void "check if user can access a rhythmyx subscriber"() {

        when: "checking user access to a rhythmyx subscriber"

        boolean hasAccess = service.hasAccess(rhythmyxSubscriber1)

        then: "get the current logged in user"

        springSecurityService.getCurrentUser() >> user1

        and: "return the access decision"

        hasAccess
    }
}
