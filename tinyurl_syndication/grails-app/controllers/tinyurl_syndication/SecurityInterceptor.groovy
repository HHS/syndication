package tinyurl_syndication

import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders


class SecurityInterceptor {

    def config = Holders.config

    SecurityInterceptor() {
        match(controller:'mediaMappingRest', action:'save|update|delete')
    }
    boolean before() {
        if(request.getHeader("Authorization") != config.SYNDICATION_INTERNALAUTHHEADER){
            response.status = 400
            response.contentType = "application/json"
            render ([error:'not authorized'] as JSON)
            log.error "not authorized"
            return false
        }
        log.info("tinyurl post authorized")
        true
    }

    boolean after() { Map model  }

    void afterView() {
        Exception e
        // no-op
    }
}
