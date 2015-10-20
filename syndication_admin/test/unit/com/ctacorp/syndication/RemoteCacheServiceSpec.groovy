package com.ctacorp.syndication

import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RemoteCacheService)
class RemoteCacheServiceSpec extends Specification {


    def authorizationService = Mock(AuthorizationService)
    def setup() {
        service.authorizationService = authorizationService
    }

    def cleanup() {
    }

    void "test flushRemoteCacheByNameAndKey"() {
        setup:""
            def cacheName = "cache name"
            def key = "key"
            String url = Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByNameAndKey?cacheName=${cacheName}&key=${key}"

        when:"flushRemoteCacheByNameAndKey is called"
            service.flushRemoteCacheByNameAndKey(cacheName, key)

        then:"the right url should be called"
            1 * service.authorizationService.getRest(url)
    }

    void "test flushRemoteCacheByName"() {
        setup:""
            def cacheName = "cache name"
            String url = Holders.config.syndication.serverUrl + "/cacheAccess/flushCacheByName?cacheName=${cacheName}"

        when:"flushRemoteCacheByName is called"
            service.flushRemoteCacheByName(cacheName)

        then:"the right url should be called"
            1 * service.authorizationService.getRest(url)
    }

    void "test flushRemoteCache"() {
        setup:""
        String url = Holders.config.syndication.serverUrl + "/cacheAccess/flushCache"

        when:"flushRemoteCacheByName is called"
        service.flushRemoteCache()

        then:"the right url should be called"
        1 * service.authorizationService.getRest(url)
    }
}
