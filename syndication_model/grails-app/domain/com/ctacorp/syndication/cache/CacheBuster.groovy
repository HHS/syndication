package com.ctacorp.syndication.cache

class CacheBuster {

    String domainName

    static constraints = {
        domainName nullable: false, blank: false
    }
}
