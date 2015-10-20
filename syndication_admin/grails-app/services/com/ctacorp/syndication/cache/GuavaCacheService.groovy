package com.ctacorp.syndication.cache

import com.google.common.cache.CacheBuilder
import com.google.common.cache.Cache
import grails.util.Holders
import twitter4j.Status

import java.util.concurrent.TimeUnit

class GuavaCacheService {

    static transactional = false
    private final int agenciesCacheSize = Holders.config.disableGuavaCache ? 0 : 10000
    private final int agenciesCacheTimeout = 1

    private final int generalTotalCacheSize = Holders.config.disableGuavaCache ? 0 : 200
    private final int generalTotalCacheTimeout = 30
//
    private final int topTenCacheSize = Holders.config.disableGuavaCache ? 0 : 500
    private final int topTenCacheTimeout = 30

    private final int tweetCacheSize = Holders.config.disableGuavaCache ? 0 : 1000
    private final int tweetCacheTimeout = 15

    private final int statusCacheSize = Holders.config.disableGuavaCache ? 0 : 1000
    private final int statusCacheTimeout = 15


    def caches = [
            agencies: CacheBuilder.newBuilder().
                    expireAfterWrite(agenciesCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(agenciesCacheSize).
                    build(),
            generalTotalCache:CacheBuilder.newBuilder().
                    expireAfterWrite(generalTotalCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(generalTotalCacheSize).
                    build(),
            topTenCache:CacheBuilder.newBuilder().
                    expireAfterWrite(topTenCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(topTenCacheSize).
                    build(),
            tweetCache:CacheBuilder.newBuilder().
                    expireAfterWrite(tweetCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(tweetCacheSize).
                    build(),
            statusCache: CacheBuilder.newBuilder().
                    expireAfterWrite(statusCacheTimeout, TimeUnit.MINUTES).
                    maximumSize(statusCacheSize).
                    build()
    ]


    Cache getAgencies() {
        caches.agencies
    }

    Cache getGeneralTotalCache() {
        caches.generalTotalCache
    }

    Cache getTotalTopTenCache() {
        caches.generalTotalCache
    }

    Cache getTweetCache() {
        caches.tweetCache
    }

    Void setTweetCache(String key, Status value) {
        caches.tweetCache.put(key, value)
    }

    Cache getStatusCache() {
        caches.statusCache
    }

}
