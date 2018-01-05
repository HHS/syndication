package com.ctacorp.syndication.manager.cms

import grails.transaction.Transactional

@Transactional
class SubscriptionService {

    void deleteSubscription(Subscription subscription) {

        if (!subscription) {
            return
        }

        RhythmyxSubscription.findAllBySubscription(subscription).each { rhythmyxSubscription ->
            rhythmyxSubscription.delete(flush: true)
        }

        EmailSubscription.findAllBySubscription(subscription).each { emailSubscription ->
            emailSubscription.delete(flush: true)
        }

        RestSubscription.findAllBySubscription(subscription).each { restSubscription ->
            restSubscription.delete(flush: true)
        }

        deleteSubscriptionIfChildless(subscription)
    }

    void deleteChildSubscription(childSubscription) {

        if (!childSubscription) {
            return
        }

        childSubscription.delete(flush: true)
        //noinspection GroovyAssignabilityCheck
        deleteSubscriptionIfChildless(childSubscription.subscription)
    }

    void deleteSubscriptionIfChildless(Subscription subscription) {

        if (!subscription) {
            return
        }

        def subscriptionsCount = {
            RestSubscription.findAllBySubscription(subscription).size() ?: 0 +
            RhythmyxSubscription.findAllBySubscription(subscription).size() ?: 0 +
            EmailSubscription.findAllBySubscription(subscription).size() ?: 0
        }()

        if (subscriptionsCount < 1) {

            try {
                subscription.delete(flush: true)
            } catch (e) {
                log.warn("Could not delete the subscription with id '${subscription.id}'. Perhaps it was deleted.", e)
            }
        }
    }
}