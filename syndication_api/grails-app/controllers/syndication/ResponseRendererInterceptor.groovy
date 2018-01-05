package syndication

import grails.converters.JSON
import grails.util.Environment


class ResponseRendererInterceptor {

    ResponseRendererInterceptor() {
            match(controller: 'media', action: '*')
            match(controller: 'tags', action: '*')
    }

    boolean before() {
        if(params.callback) {
            response << params.callback + "("
        }
        true
    }

    void afterView() {
        if(params.callback) {
            response << ");"
        }
        true
    }
}
