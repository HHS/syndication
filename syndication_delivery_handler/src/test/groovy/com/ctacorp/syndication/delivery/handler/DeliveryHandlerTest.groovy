package com.ctacorp.syndication.delivery.handler

import com.percussion.rx.delivery.IPSDeliveryItem
import com.percussion.rx.delivery.IPSDeliveryResult
import com.percussion.utils.guid.IPSGuid
import groovyx.net.http.RESTClient
import spock.lang.Specification

class DeliveryHandlerTest extends Specification {

    def handler = new DeliveryHandler()
    def deliveryItem = Mock(IPSDeliveryItem)
    def restClient = Mock(RESTClient)
    def guid = Mock(IPSGuid)
    def authHeaders = Mock(AuthHeader)
    def restClientFactory = Mock(DeliveryHandler.RestClientFactory)

    def setup() {

        handler.authHeaders = authHeaders
        handler.restClientFactory = restClientFactory
    }

    def 'isTransational returns false'() {

        expect: 'isTransactional to always return false'

        !handler.isTransactional()
    }

    def 'deliver returns a 200'() throws Exception {

        setup: 'the headers for API key authentication'

        def headers = [:]

        and: 'the original and transformed content'

        def originalContent = '<![CDATA[{\"Hello\":\"$source_url\"}]]>'
        def transformedContent = '{"Hello":"http://localhost:9992/ham/sandwiches/for/life"}'

        when: 'deliver is invoked on the delivery handler'

        def deliveryResult = handler.deliver(deliveryItem)

        then: 'get the jobId of the delivery item'

        deliveryItem.getJobId() >> 123

        and: 'get the GUID from the delivery item'

        deliveryItem.getId() >> guid

        and: 'get the referenceId from the delivery item'

        deliveryItem.getReferenceId() >> 321

        and: 'get the transformedContent from the deivery item'

        deliveryItem.getResultStream() >> {
            new ByteArrayInputStream(originalContent.bytes)
        }

        and: 'get the delivery path from the delivery item'

        deliveryItem.getDeliveryPath() >> '/ham/sandwiches/for/life'

        and: "generate the required headers for API Key Authentication"

        authHeaders.create(transformedContent) >> headers

        and: "create a new rest client"

        restClientFactory.newRestClient() >> restClient

        and: 'publish the transformedContent to the REST publishing service'

        restClient.post([headers: headers, body: transformedContent, requestContentType: 'application/json']) >> {
            return [status: 200]
        }

        expect: 'the outcome to be a successful delivery'

        deliveryResult.outcome == IPSDeliveryResult.Outcome.DELIVERED
    }

    def 'deliver returns something other than a 200'() throws Exception {

        setup: 'the headers for API key authentication'

        def headers = [:]

        and: 'the original and transformed content'

        def originalContent = '<![CDATA[{\"Hello\":\"source_url\"}]]>'
        def transformedContent = '{"Hello":"http://localhost:9992/ham/sandwiches/for/life"}'

        when: 'deliver is invoked on the delivery handler'

        def deliveryResult = handler.deliver(deliveryItem)

        then: 'get the jobId of the delivery item'

        deliveryItem.getJobId() >> 123

        and: 'get the GUID from the delivery item'

        deliveryItem.getId() >> guid

        and: 'get the referenceId from the delivery item'

        deliveryItem.getReferenceId() >> 321

        and: 'get the content from the deivery item'

        deliveryItem.getResultStream() >> {
            new ByteArrayInputStream(originalContent.bytes)
        }

        and: 'get the delivery path from the delivery item'

        deliveryItem.getDeliveryPath() >> '/ham/sandwiches/for/life'

        and: "generate the required headers for API Key Authentication"

        authHeaders.create(transformedContent) >> headers

        and: "create a new rest client"

        restClientFactory.newRestClient() >> restClient

        and: 'publish the content to the REST publishing service'

        restClient.post([headers: headers, body: transformedContent, requestContentType: 'application/json']) >> {
            return [status: 401]
        }

        expect: 'the outcome to be a failed delivery'

        deliveryResult.outcome == IPSDeliveryResult.Outcome.FAILED
    }

    def 'deliver throws an exception '() throws Exception {

        setup: 'the headers for API key authentication'

        def headers = [:]

        and: 'the original and transformed content'

        def originalContent = '<![CDATA[{\"Hello\":\"$source_url\"}]]>'
        def transformedContent = '{"Hello":"http://localhost:9992/ham/sandwiches/for/life"}'

        when: 'deliver is invoked on the delivery handler'

        def deliveryResult = handler.deliver(deliveryItem)

        then: 'get the jobId of the delivery item'

        deliveryItem.getJobId() >> 123

        and: 'get the GUID from the delivery item'

        deliveryItem.getId() >> guid

        and: 'get the referenceId from the delivery item'

        deliveryItem.getReferenceId() >> 321

        and: 'get the content from the deivery item'

        deliveryItem.getResultStream() >> {
            new ByteArrayInputStream(originalContent.bytes)
        }

        and: 'get the delivery path from the delivery item'

        deliveryItem.getDeliveryPath() >> '/ham/sandwiches/for/life'

        and: "generate the required headers for API Key Authentication"

        authHeaders.create(transformedContent) >> headers

        and: "create a new rest client"

        restClientFactory.newRestClient() >> restClient

        and: 'throw an exception when publishing the content to the REST publishing service'

        restClient.post([headers: headers, body: transformedContent, requestContentType: 'application/json']) >> {
            throw new RuntimeException('What the hell man!')
        }

        expect: 'the outcome to be a failed delivery'

        deliveryResult.outcome == IPSDeliveryResult.Outcome.FAILED
    }

    def "remove is implemented but doesn't do anything"() {

        when: 'remove is invoked on the delivery handler'

        def deliveryResult = handler.remove(deliveryItem)

        then: 'get the jobId of the delivery item'

        deliveryItem.getJobId() >> 123

        and: 'get the GUID from the delivery item'

        deliveryItem.getId() >> guid

        and: 'get the referenceId from the delivery item'

        deliveryItem.getReferenceId() >> 321

        expect: 'the outcome to be a failed delivery'

        deliveryResult.outcome == IPSDeliveryResult.Outcome.DELIVERED
    }
}
