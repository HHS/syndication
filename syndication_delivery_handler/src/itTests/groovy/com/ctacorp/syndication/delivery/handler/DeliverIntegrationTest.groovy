package com.ctacorp.syndication.delivery.handler

import com.percussion.rx.delivery.IPSDeliveryItem
import com.percussion.rx.delivery.IPSDeliveryResult
import com.percussion.utils.guid.IPSGuid
import spock.lang.Specification

class DeliverIntegrationTest extends Specification {

    def handler = new DeliveryHandler()
    def deliveryItem = Mock(IPSDeliveryItem)
    def guid = Mock(IPSGuid)

    String templateText

    def setup() {
        templateText = Thread.currentThread().getContextClassLoader().getResourceAsStream('derived_template.xml').text
    }

    def "publish to Syndication"() {

        when: "deliver is invoked"

        def deliveryResult = handler.deliver(deliveryItem)

        then: "get the jobId from the delivery item"

        deliveryItem.getJobId() >> 123456

        and: "get the referenceId from the delivery item"

        deliveryItem.getReferenceId() >> 23456

        and: "get the content id from the delivery id"

        deliveryItem.getId() >> guid

        and: "get the (long) value from the guid"

        guid.longValue() >> 56789

        and: "get the content stream"

        deliveryItem.getResultStream() >> {
            new ByteArrayInputStream(templateText.bytes)
        }

        and: 'get the delivery path from the delivery item'

        deliveryItem.getDeliveryPath() >> '/opa/about-opa-and-initiatives/index.html'

        expect: "the delivery result will be a success"

        IPSDeliveryResult.Outcome.DELIVERED == deliveryResult.outcome
    }
}
