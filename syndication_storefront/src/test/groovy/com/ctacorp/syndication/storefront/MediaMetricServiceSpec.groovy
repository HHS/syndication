package com.ctacorp.syndication.storefront

import com.ctacorp.syndication.Language
import com.ctacorp.syndication.Source
import com.ctacorp.syndication.media.Html
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.metric.MediaMetric
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created by sgates on 3/26/15.
 */
@TestFor(MediaMetricService)
@Mock([MediaMetric, MediaItem, Html, Source, Language])
class MediaMetricServiceSpec extends Specification {

    def setup() {
        new Html(name:"Some Item", sourceUrl:"http://www.example.com", source:new Source(), language: new Language()).save(flush:true)
    }

    def "addStorefrontView should return null on null mediaId"() {
        expect:
            !service.addStorefrontView(null)
    }

    def "addStorefrontView should create media metrics for today if one doesn't exist for the current item"() {
        given: "a media item and no media medtrics"
            Html html = Html.read(1)
            MediaMetric.count() == 0
        when: "a storefront view is added for it"
            service.addStorefrontView(html.id)
        then: "There should now be a media metric"
            MediaMetric.count() == 1
        and: "The metric should be for this item"
            MediaMetric loaded = MediaMetric.get(1)
            loaded.media.id == html.id
        and: "The count should be 1"
            loaded.storefrontViewCount == 1
    }

    def "addStorefrontView should increment an existing mediaMetric's count"(){
        given: "an existing mediaMetric"
            service.addStorefrontView(1)
            MediaMetric.count() == 1
            MediaMetric.read(1).media.id == 1
            MediaMetric.read(1).storefrontViewCount == 1
        when: "A new view is added"
            service.addStorefrontView(1)
        then: "The number of mediaMetrics should be the same"
            MediaMetric.count() == 1
        and: "The storefront viewcount should be higher"
            MediaMetric.read(1).storefrontViewCount == 2
    }

    def "getToday should return today with no time"() {
        when: "getToday is called"
            Date today = service.getToday()
        then: "the date should be today"
            new Date() - today == 0
        and: "have no time"
            today.getHours() == 0
            today.getMinutes() == 0
            today.getSeconds() == 0
    }
}