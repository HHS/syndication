package com.ctacorp.syndication.manager.cms

class UserSubscriber {

    User user
    Subscriber subscriber

    static constraints = {
        user nullable: false
        subscriber nullable: false
    }
}
