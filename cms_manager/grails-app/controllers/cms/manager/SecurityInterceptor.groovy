package cms.manager

import com.ctacorp.syndication.manager.cms.rest.security.ApiKeyUtils
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResponseFormatter
import com.ctacorp.syndication.manager.cms.rest.security.AuthorizationResult
import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders

import javax.servlet.http.HttpServletRequest


class SecurityInterceptor {

    def filteredUri = Holders.config.apiKey.filteredUri
    def env = Environment.current
    def config=Holders.config
    def authorizationService
    def loggingService

    SecurityInterceptor(){
        match(uri: filteredUri).except(controller: 'authorization')
    }

    boolean before() {
        setSubscriberRequestAttribute(request)
        if (env == Environment.PRODUCTION || config.SYNDICATION_CMS_AUTH_ENABLED != "false") {
            try {
                AuthorizationResult authorizationResult = authorizationService.authorize(request)

                if (!authorizationResult.isAuthorized) {
                    def formattedResponse = AuthorizationResponseFormatter.formatResponse(authorizationResult)
                    render status: authorizationResult.httpStatus, contentType: 'application/json', text: formattedResponse
                    return false
                }

            } catch (e) {
                def errorMessage = loggingService.logError('Unexpected error occurred when trying to authorize request', e)
                def json = ([message: errorMessage] as JSON).toString(true)
                render status: 500, contentType: 'application/json', text: json, encoding: "UTF-8"
                return false
            }
        } else {
            log.info("Skipping auth check because the environment is ${env}")
        }

        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
    private static void setSubscriberRequestAttribute(HttpServletRequest request) {
        def rawAuthHeader = request.getHeader("Authorization")
        def authHeader = ApiKeyUtils.getAuthHeader(rawAuthHeader, System.currentTimeMillis())
        request.setAttribute("senderPublicKey", authHeader?.senderPublicKey)
    }
}
