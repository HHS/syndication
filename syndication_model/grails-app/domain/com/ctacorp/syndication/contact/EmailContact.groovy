package com.ctacorp.syndication.contact

class EmailContact {
    String name
    String email

    static constraints = {
        name    nullable: false, maxSize: 255
        email   nullable: false, maxSize: 255, email: true
    }
}
