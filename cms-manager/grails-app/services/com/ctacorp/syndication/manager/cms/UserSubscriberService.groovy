/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package com.ctacorp.syndication.manager.cms

import grails.transaction.Transactional

@Transactional
class UserSubscriberService {

    def sessionFactory
    def springSecurityService

    List<Subscriber> getSubscribers() {

        if(isAdmin()) {
            return Subscriber.list()
        }

        def user = springSecurityService.getCurrentUser() as User
        UserSubscriber.findAllByUser(user)*.subscriber.sort { it.name }
    }

    boolean hasAccess(RhythmyxSubscriber rhythmyxSubscriber) {

        if(!rhythmyxSubscriber) {
            return false
        }

        if(isAdmin()) {
            return true
        }

        def subscriberIds = getSubscribers()*.id
        if(subscriberIds.contains(rhythmyxSubscriber.subscriber.id)) {
            return true
        }

        false
    }

    List<RhythmyxSubscriber> listRhythmyxSubscribers() {

        def allRhythmyxSubscribers = RhythmyxSubscriber.list()

        if(isAdmin()) {
            return allRhythmyxSubscribers
        }

        List<RhythmyxSubscriber> rhythmyxSubscribers = []
        def subscriberIds = getSubscribers()*.id

        allRhythmyxSubscribers.each { rhythmyxSubscriber ->

            def id = rhythmyxSubscriber.subscriber.id

            if(subscriberIds.contains(id)) {
                rhythmyxSubscribers.add(rhythmyxSubscriber)
            }
        }

        rhythmyxSubscribers.sort { it.instanceName }
    }

    List<RhythmyxSubscription> listRhythmyxSubscriptions(Map params = [:]) {
        filterByUser(RhythmyxSubscription.list(params))
    }

    int rhythmyxSubscriptionCount() {
        filterByUser(RhythmyxSubscription.list()).size()
    }

    private List<RhythmyxSubscription> filterByUser(List<RhythmyxSubscription> allRhythmyxSubscriptions) {

        if (isAdmin()) {
            return allRhythmyxSubscriptions
        }

        List<RhythmyxSubscription> rhythmyxSubscriptions = []
        def rhythmyxSubscriberIds = listRhythmyxSubscribers()*.id

        allRhythmyxSubscriptions.each { rhythmyxSubscription ->

            if (rhythmyxSubscriberIds.contains(rhythmyxSubscription.rhythmyxSubscriber.id)) {
                rhythmyxSubscriptions.add(rhythmyxSubscription)
            }
        }

        rhythmyxSubscriptions
    }

    boolean isAdmin() {
        def user = springSecurityService.getCurrentUser() as User
        def authorities = UserRole.findAllByUser(user)*.role*.authority
        return 'ROLE_ADMIN' in authorities
    }

    void disassociateUserFromSubscribers(User user) {

        UserSubscriber.deleteAll(UserSubscriber.findAllByUser(user))
        flushSession()
    }

    void disassociateSubscriberFromUsers(Subscriber subscriber) {

        UserSubscriber.deleteAll(UserSubscriber.findAllBySubscriber(subscriber))
        flushSession()
    }

    UserSubscriber associateUserWithSubscriber(Subscriber subscriber, User user) {
        associateUserWithSubscribers([subscriber], user)?.get(0)
    }

    List<UserSubscriber> associateUserWithSubscribers(List<Subscriber> subscribers, User user) {

        subscribers.each { subscriber ->
            new UserSubscriber(user: user, subscriber: subscriber).save()
        }

        flushSession()
        UserSubscriber.findAllByUser(user)
    }

    void flushSession() {
        sessionFactory.getCurrentSession()?.flush()
    }
}
