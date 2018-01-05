package syndication

import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders
import grails.web.servlet.mvc.GrailsParameterMap
import com.ctacorp.commons.multi.read.servlet.filter.MultiReadHttpServletRequest
import com.ctacorp.commons.multi.read.servlet.filter.MultiReadServletFilter


class SecurityInterceptor {

    def config = Holders.config
    def authorizationService

    SecurityInterceptor() {
        if (Environment.getCurrent() == Environment.PRODUCTION || config.SYNDICATION_CMS_AUTH_ENABLED == 'true') {
            match(controller: 'admin', action: '*').except(action: "swaggerUi")
            match(controller: 'cacheAccess', action: '*')
            match(controller: 'media', action: 'saveCollection|saveFAQ|saveHtml|saveImage|saveInfographic|savePDF|saveQuestionAndAnswer|saveTweet|saveVideo')
        }
    }


    boolean before() {
        handleAuthRequest(request)
    }

    boolean after() {
        Map model
        true
    }

    void afterView() {
        Exception e
        true
    }

    private boolean handleAuthRequest(request) {
        boolean authorized = checkAuthorization(request)

        if (!authorized) {

            def url = "Requested URL ------------------\n${config?.API_SERVER_URL}${request.forwardURI[request.contextPath.size()..-1]}"

            String requestHeaders = "Headers --------------------\n"

            request.getHeaderNames().each { name ->
                requestHeaders += " -> ${name}:${request.getHeader(name)}\n"
            }
            def body = "No Body"
            try {
                body = "Body ---------------------\n${request.reader.text}"
            } catch (e) {
                // body could not be read
            }
            log.info("Not Authorized: Request: \n${requestHeaders}\n${url}\n${body}")
            redirect(controller: "error", action: "unauthorized")

            return false

        }
        success(request)
        true

    }

    private boolean success(request) {
        try {
            log.info("Authentication succeeded for request: ${request.reader.text}")
        } catch (ignore) {
            log.info("Authentication succeeded for request: ${request.forwardURI}")
        }
    }

    private boolean checkAuthorization(request) {
        //Get the requested URL
        def url = config?.API_SERVER_URL + request.forwardURI[request.contextPath.size()..-1]
        log.debug "API: RequestURL: ${url}"
        def readerText = request.JSON

        def authHeaders = [
                authorizationHeader: request.getHeader("Authorization"),
                dateHeader         : request.getHeader("date"),
                contentTypeHeader  : request.getHeader("content-type"),
                contentLengthHeader: request.getHeader("Content-Length"),
                url                : url,
                httpMethod         : request.getMethod(),
                dataMd5            : authorizationService.hashBody(readerText.toString())
        ]

        log.debug "API: AuthHeaders as seen before authorization check: ${authHeaders}"
        def authorized = authorizationService.checkAuthorization(authHeaders)
        log.debug "API: Authorized?: ${authorized}"

        if (!authorized) {
            log.error("Request was not not authorized")
            log.error("Computed authHeaders were: \n${(authHeaders as JSON).toString(true)}")
        }

        authorized
    }

}
