package syndication_api

import grails.converters.JSON
import grails.util.Environment


class AddHeaderInterceptor {

    int order = HIGHEST_PRECEDENCE

    AddHeaderInterceptor(){
        match(controller: '*', action: '*')
    }
    boolean before() {
        response.addHeader("Access-Control-Allow-Origin", "*")
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
