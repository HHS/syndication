package tagcloud

import grails.converters.JSON
import grails.util.Environment

class SecurityFilters {
    def authorizationService
    def env = Environment.current

    def filters = {
        all(controller: 'tags|languages|tagTypes', action: 'save*|update*|delete*|set*|tag*|findOrSaveTag') {
            before = {
                Boolean isDevEnvironment = (env == Environment.DEVELOPMENT)
                Boolean skipAuthorization =  grailsApplication.config.tagCloud.skipAuthorization

                if (isDevEnvironment && skipAuthorization) {
                    log.info("Skipping auth check because the environment is ${env} and flag is enabled")
                } else {
                    def url = grailsApplication.config.grails.serverURL + request.forwardURI[request.contextPath.size()..-1]
                    def dateHeader = request.getHeader("date")
                    def authHeaders = [
                            authorizationHeader: request.getHeader("Authorization"),
                            dateHeader         : dateHeader,
                            contentTypeHeader  : request.getHeader("content-type"),
                            contentLengthHeader: request.getHeader("Content-Length"),
                            url                : url,
                            httpMethod         : request.getMethod(),
                            dataMd5            : authorizationService.hashBody(request.reader.text)
                    ]

                    boolean authorized = authorizationService.checkAuthorization(authHeaders)
                    if(!authorized){
                        log.error("Request (${dateHeader}) was not not authorized")
                        log.error("Computed authHeaders were: \n${(authHeaders as JSON).toString(true)}")

                        render text:([authorized:false] as JSON), contentType: "application/json", status: 403
                        return false
                    }
                }

                return true
            }
            after = { Map model ->
            }
            afterView = { Exception e ->
            }
        }
    }
}
