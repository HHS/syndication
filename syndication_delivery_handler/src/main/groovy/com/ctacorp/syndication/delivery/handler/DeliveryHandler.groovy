package com.ctacorp.syndication.delivery.handler

import com.percussion.rx.delivery.IPSDeliveryHandler
import com.percussion.rx.delivery.IPSDeliveryItem
import com.percussion.rx.delivery.IPSDeliveryResult
import com.percussion.rx.delivery.PSDeliveryException
import com.percussion.rx.delivery.data.PSDeliveryResult
import com.percussion.services.sitemgr.IPSSite
import com.percussion.utils.guid.IPSGuid
import groovy.util.logging.Log
import groovyx.net.http.RESTClient
import org.apache.commons.lang.StringEscapeUtils

@Log
class DeliveryHandler implements IPSDeliveryHandler {

    def authHeaders = new AuthHeader()
    def restClientFactory = new RestClientFactory()

    @Override
    void init(long jobId, IPSSite ipsSite) {
        log.info("'init' called with jobId ${jobId}")
    }

    @Override
    boolean isTransactional() {
        log.info("'isTransactional' called ... return false")
        return false
    }

    @Override
    IPSDeliveryResult deliver(IPSDeliveryItem deliveryItem) {

        def jobId = deliveryItem.getJobId()
        def referenceId = deliveryItem.getReferenceId()
        IPSGuid itemId = deliveryItem.getId()

        log.info("'deliver' called with ${[jobId: jobId, referenceId: referenceId, itemId: itemId.longValue()]}")

        def content = null

        try {

            content = getContent(deliveryItem)

            def headers = authHeaders.create(content)

            def response = restClientFactory.newRestClient().post(headers: headers, body: content, requestContentType: 'application/json')
            def status = response['status']

            if (status == 200) {
                log.info("Content published to syndication successfully")
                log.info("POST body was: \n${content}")
                return new PSDeliveryResult(IPSDeliveryResult.Outcome.DELIVERED, null, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
            }

            def message = response['data']
            log.severe("Publish failed: [status: ${status}, message: ${message}]")

            handleGeneralFailure(response, content, itemId, jobId, referenceId, deliveryItem.getDeliveryPath())

        } catch (ex) {
            handleException(ex, itemId, jobId, referenceId, content, deliveryItem.getDeliveryPath())
        }
    }

    static PSDeliveryResult handleException(Throwable ex, IPSGuid itemId, long jobId, long referenceId, String content, String deliveryPath) {

        def genericFailureMessage = "Exception occured when publishing to ${Config.PUBLISH_URL}"
        log.severe(genericFailureMessage)
        log.severe("Exception was: ${ex.message}\n\n${ex.stackTrace.join("\n\t")}\n")
        log.severe("Delivery path was '${deliveryPath}'")
        log.severe("POST body was was: \n'${content}'")
        return new PSDeliveryResult(IPSDeliveryResult.Outcome.FAILED, genericFailureMessage, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    static PSDeliveryResult handleGeneralFailure(response, String content, IPSGuid itemId, long jobId, long referenceId, String deliveryPath) {

        log.info("Error occured when publishing conent to syndication")
        log.severe("Delivery path was '${deliveryPath}'")
        log.severe("POST body was was: \n'${content}'")
        return new PSDeliveryResult(IPSDeliveryResult.Outcome.FAILED, "Repsonse was ${response.properties.toString()}", itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    static String getContent(IPSDeliveryItem deliveryItem) {

        def content = deliveryItem.getResultStream().text
        content = StringEscapeUtils.unescapeXml(content)

        def cDataStart = content.indexOf('<![CDATA[')
        def cDataEnd = content.indexOf(']]>')
        if (cDataStart > -1 && cDataEnd > -1) {
            content = content.substring(cDataStart + 9, cDataEnd)
        }

        def sourceUrl = Config.SERVER_BASE_URL + "/${deliveryItem.getDeliveryPath()}".replaceAll('//', '/')
        return content.replace('$source_url', sourceUrl)
    }

    @Override
    IPSDeliveryResult remove(IPSDeliveryItem deliveryItem) {

        def jobId = deliveryItem.getJobId()
        log.info("'remove' called with jobId ${jobId}")
        def itemId = deliveryItem.getId()
        def referenceId = deliveryItem.getReferenceId()
        return new PSDeliveryResult(IPSDeliveryResult.Outcome.DELIVERED, null, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    @Override
    Collection<IPSDeliveryResult> commit(long jobId) throws PSDeliveryException {
        log.info("'commit' called with jobId ${jobId} ... return new ArrayList<IPSDeliveryResult>()")
        new ArrayList<IPSDeliveryResult>()
    }

    @Override
    void rollback(long jobId) throws PSDeliveryException {
        log.info("'rollback' called with jobId ${jobId}")
    }

    static class RestClientFactory {

        @SuppressWarnings("GrMethodMayBeStatic")
        RESTClient newRestClient() {
            def client = new RESTClient(Config.PUBLISH_URL)
            def httpClient = client.client
            httpClient.params.setIntParameter('http.connection.timeout', Config.HTTP_CONNECTION_TIMEOUT)
            httpClient.params.setIntParameter('http.socket.timeout', Config.HTTP_SOCKET_TIMEOUT)
            client
        }
    }

}
