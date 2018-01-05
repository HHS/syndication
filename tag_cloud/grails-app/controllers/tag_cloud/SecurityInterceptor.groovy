package tag_cloud

import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders


class SecurityInterceptor {

    def config = Holders.config
    def env = Environment.current

    SecurityInterceptor(){
        match(controller: 'tags|languages|tagTypes', action: 'save*|update*|delete*|set*|tag*|findOrSaveTag|deleteTag')
    }

    boolean before() {
        Boolean isDevEnvironment = (env == Environment.DEVELOPMENT)
        def skipAuthorization =  config.TAG_CLOUD_SKIPAUTHORIZATION

        if (isDevEnvironment && skipAuthorization != "false") {
            log.info("Skipping auth check because the environment is ${env} and flag is enabled")
        } else {
            if(request.getHeader("Authorization") != config.SYNDICATION_INTERNALAUTHHEADER){
                log.error("Request (${request.getHeader("date")}) was not not authorized")
                log.error("AuthHeader did not match")

                render text:([authorized:false] as JSON), contentType: "application/json", status: 403
                return false
            }
        }

        return true
    }

    boolean after() { Map model }

    void afterView() {
        Exception e
        // no-op
    }
}
