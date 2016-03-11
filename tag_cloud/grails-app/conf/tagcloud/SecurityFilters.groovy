package tagcloud

import grails.converters.JSON
import grails.util.Environment

class SecurityFilters {
    def env = Environment.current

    def filters = {
        all(controller: 'tags|languages|tagTypes', action: 'save*|update*|delete*|set*|tag*|findOrSaveTag') {
            before = {
//                log.info "Incoming tagcloud request to: ${request.forwardURI}"
                Boolean isDevEnvironment = (env == Environment.DEVELOPMENT)
                Boolean skipAuthorization =  grailsApplication.config.tagCloud.skipAuthorization

                if (isDevEnvironment && skipAuthorization) {
                    log.info("Skipping auth check because the environment is ${env} and flag is enabled")
                } else {
                    if(request.getHeader("Authorization") != grailsApplication.config.syndication.internalAuthHeader){
                        log.error("Request (${request.getHeader("date")}) was not not authorized")
                        log.error("AuthHeader did not match")

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
