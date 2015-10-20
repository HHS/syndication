package com.ctacorp.syndication.delivery.handler

import com.percussion.rx.delivery.IPSDeliveryHandler
import com.percussion.rx.delivery.IPSDeliveryItem
import com.percussion.rx.delivery.IPSDeliveryResult
import com.percussion.rx.delivery.PSDeliveryException
import com.percussion.rx.delivery.data.PSDeliveryResult
import com.percussion.services.sitemgr.IPSSite
import com.percussion.utils.guid.IPSGuid
import groovy.util.logging.Log
import org.apache.commons.lang.StringEscapeUtils

@Log
class DeliveryHandler implements IPSDeliveryHandler {

    def authHeaders = new AuthHeader()
    def restClient = new RestClient()
    static logMessagePrefix = { long jobId -> "Syndication Publish - JOBID '${jobId}': " }

    @Override
    void init(long jobId, IPSSite ipsSite) {
        log.info("${logMessagePrefix(jobId)}'init' called")
    }

    @Override
    boolean isTransactional() {
        false
    }

    @Override
    IPSDeliveryResult deliver(IPSDeliveryItem deliveryItem) {

        def jobId = deliveryItem.getJobId()
        def referenceId = deliveryItem.getReferenceId()
        IPSGuid itemId = deliveryItem.getId()

        log.info("${logMessagePrefix(jobId)}'deliver' called with referenceId '${referenceId}, itemId '${itemId.longValue()}'")

        def content = null

        try {

            content = getContent(deliveryItem)
            def headers = authHeaders.create(content)

            def (status, response) = restClient.post(content, headers)
            def results = response?.results

            if (status == 200 && results && results[0].id) {

                log.info("${logMessagePrefix(jobId)}Content published to syndication successfully")
                postBodyWas(content, jobId)
                return new PSDeliveryResult(IPSDeliveryResult.Outcome.DELIVERED, null, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
            }

            log.severe("${logMessagePrefix(jobId)}Publish failed with statusCode '${status}', response '${response}'")
            handleGeneralFailure(response, content, itemId, jobId, referenceId, deliveryItem.getDeliveryPath())

        } catch (ex) {
            handleException(ex, itemId, jobId, referenceId, content, deliveryItem.getDeliveryPath())
        }
    }

    static PSDeliveryResult handleException(Throwable ex, IPSGuid itemId, long jobId, long referenceId, String content, String deliveryPath) {

        def genericFailureMessage = "${logMessagePrefix(jobId)}Exception was thrown when publishing to ${Config.PUBLISH_URL}: ${ex.message}\n\n${ex.stackTrace.join('\n\t')}\n"
        log.severe(genericFailureMessage)
        deliveryPathWas(deliveryPath, jobId)
        postBodyWas(content, jobId, true)

        new PSDeliveryResult(IPSDeliveryResult.Outcome.FAILED, genericFailureMessage, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    private static deliveryPathWas(String deliveryPath, long jobId) {
        log.severe("${logMessagePrefix(jobId)}Delivery path was '${deliveryPath}'")
    }

    private static postBodyWas(String content, long jobId, boolean logAsError = false) {

        def msg = "${logMessagePrefix(jobId)}POST body was '${content}'"

        if (logAsError) {
            log.severe(msg)
        } else {
            log.info(msg)
        }
    }

    static PSDeliveryResult handleGeneralFailure(response, String content, IPSGuid itemId, long jobId, long referenceId, String deliveryPath) {

        log.severe("${logMessagePrefix(jobId)}Unexpected response received from syndication when publishing content")
        deliveryPathWas(deliveryPath, jobId)
        postBodyWas(content, jobId, true)

        new PSDeliveryResult(IPSDeliveryResult.Outcome.FAILED, "Repsonse was ${response.properties.toString()}", itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    static String getContent(IPSDeliveryItem deliveryItem) {

        def content = deliveryItem.getResultStream().getText('UTF-8')
        content = StringEscapeUtils.unescapeXml(content)

        def cDataStart = content.indexOf('<![CDATA[')
        def cDataEnd = content.indexOf(']]>')
        if (cDataStart > -1 && cDataEnd > -1) {
            content = content.substring(cDataStart + 9, cDataEnd)
        }

        def sourceUrl = Config.SERVER_BASE_URL + "/${deliveryItem.getDeliveryPath()}".replaceAll('//', '/')
        content.replace('$source_url', sourceUrl)
    }

    @Override
    IPSDeliveryResult remove(IPSDeliveryItem deliveryItem) {

        def jobId = deliveryItem.getJobId()
        log.info("${logMessagePrefix(jobId)}'remove' called")
        def itemId = deliveryItem.getId()
        def referenceId = deliveryItem.getReferenceId()
        new PSDeliveryResult(IPSDeliveryResult.Outcome.DELIVERED, null, itemId, jobId, referenceId, Config.PUBLISH_URL.bytes)
    }

    @Override
    Collection<IPSDeliveryResult> commit(long jobId) throws PSDeliveryException {
        log.info("${logMessagePrefix(jobId)}'commit' called")
        new ArrayList<IPSDeliveryResult>()
    }

    @Override
    void rollback(long jobId) throws PSDeliveryException {
        log.info("${logMessagePrefix(jobId)}'rollback' called")
    }
}
